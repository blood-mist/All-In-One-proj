package comcast.stb.packageInfoDialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comcast.stb.R;
import comcast.stb.entity.BuySpinnerItem;
import comcast.stb.entity.ChannelPckgItem;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.MoviePckgItem;
import comcast.stb.entity.PackagesInfo;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import io.realm.Realm;

import static comcast.stb.StringData.CHANNEL_PACKAGE;
import static comcast.stb.StringData.MOVIE_PACKAGE;
import static comcast.stb.StringData.PURCHASE_TYPE_BOUGHT;
import static comcast.stb.StringData.PURCHASE_TYPE_BUY;

public class PackageDialogFragment extends DialogFragment implements PackageApiInterface.PackagesView, LogoutApiInterface.LogoutView {
    private static final String PACKAGE_TYPE = "package_type";
    private static final String PACKAGE_ITEM = "package_id";


    private OnFragmentInteractionListener mListener;

    @BindView(R.id.txt_package_label)
    TextView listLabel;

    @BindView(R.id.recycler_pckgItms_list)
    RecyclerView packagItemsRecycler;

    @BindView(R.id.spinner_duration)
    Spinner packageDuration;

    @BindView(R.id.txt_package_price)
    TextView packagePrice;

    @BindView(R.id.package_description_label)
    TextView packageName;

    @BindView(R.id.btn_package_buy)
    Button btnBuy;

    @BindView(R.id.btn_packageCancel)
    Button btnCancel;
    private String packageType;
    private PackagesInfo packagesInfo;
    private PackagePresenterImpl packagePresenter;
    private LogoutPresImpl logoutPres;
    private Realm realm;
    private LoginData loginData;
    private int durationNumber;
    AVLoadingIndicatorView progressBar;


    public PackageDialogFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.btn_package_buy)
    public void onBuyClick() {
        mListener.onBuyClick(packageType, packagesInfo.getPackageId(), durationNumber);

    }

    @OnClick(R.id.btn_packageCancel)
    public void onCanceClicked(){
        getDialog().dismiss();
    }

    public static PackageDialogFragment newInstance(String packageType, PackagesInfo packagesInfo) {
        PackageDialogFragment fragment = new PackageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PACKAGE_ITEM, packagesInfo);
        bundle.putString(PACKAGE_TYPE, packageType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
        if (getArguments() != null) {
            packageType = getArguments().getString(PACKAGE_TYPE);
            packagesInfo = getArguments().getParcelable(PACKAGE_ITEM);
        }
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
        logoutPres = new LogoutPresImpl(this);
        packagePresenter = new PackagePresenterImpl(this, logoutPres);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dialogView = inflater.inflate(R.layout.fragment_package_dialog, container, false);
        ButterKnife.bind(this, dialogView);
        progressBar = getActivity().findViewById(R.id.progressbar_packageActivity);
        setValues();
        return dialogView;
    }

    private void setValues() {
        packageName.setText(packagesInfo.getPackageName());
        packagePrice.setText(packagesInfo.getPackagePrice());
        switch (packagesInfo.getSubscriptionStatus()){
            case PURCHASE_TYPE_BUY:
                btnBuy.setText("buy");
                break;
            case PURCHASE_TYPE_BOUGHT:
                if(packagesInfo.getExpiryFlag())
                    btnBuy.setText("upgrade");
                else
                    btnBuy.setText("buy");
                break;
        }

        String Months[] = {"1 month", "2 months", "3 months", "4 months",
                "5 months", "6 months", "7 months", "8 months ", "9 months",
                "10 months", "11 months", "12 months"};
        final ArrayList<BuySpinnerItem> durationList = new ArrayList<BuySpinnerItem>();
        for (int i = 0; i < Months.length; i++) {
            BuySpinnerItem item = new BuySpinnerItem();
            item.setDuration(Months[i]);
            durationList.add(item);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_default, Months) {
        };
        adapter.setDropDownViewResource(R.layout.spinner_row_new);
        packageDuration.setAdapter(adapter);
        packageDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                durationNumber = Integer.parseInt(durationList.get(position).getDuration()
                        .replaceAll("[^0-9?!\\.]", "")
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (packageType) {
            case CHANNEL_PACKAGE:
                packagePresenter.getChannlesInaPckg(packagesInfo.getPackageId(), loginData.getToken());
                break;
            case MOVIE_PACKAGE:
                packagePresenter.getMoviesInaPckg(packagesInfo.getPackageId(), loginData.getToken());
                break;
        }
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        packagItemsRecycler.setLayoutManager(manager);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserFragInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void setChannelsInaPckg(int packageId, List<ChannelPckgItem> channelsInaPckgList) {
        PackageRecyclerAdapter packageRecyclerAdapter = new PackageRecyclerAdapter(getActivity(), (ArrayList) channelsInaPckgList.get(0).getChannels(), CHANNEL_PACKAGE);
        packagItemsRecycler.setAdapter(packageRecyclerAdapter);
    }

    @Override
    public void setMoviesInaPckg(int packageId, List<MoviePckgItem> moviesInaPckgList) {
        PackageRecyclerAdapter packageRecyclerAdapter = new PackageRecyclerAdapter(getActivity(), (ArrayList) moviesInaPckgList.get(0).getMovies(), MOVIE_PACKAGE);
        packagItemsRecycler.setAdapter(packageRecyclerAdapter);
    }

    @Override
    public void onChannelInaPckgError(int packageId, String message) {
        getDialog().dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMoviesInaPckgError(int packageId, String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        progressBar.smoothToShow();
        progressBar.bringToFront();

    }

    @Override
    public void hideProgress() {
        progressBar.smoothToHide();

    }

    @Override
    public void successfulLogout() {
        logoutPres.logout();
    }


    public interface OnFragmentInteractionListener {

        void onBuyClick(String packageType, int packageId, int duration);
    }


}
