package comcast.stb.packageInfoDialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comcast.stb.R;
import comcast.stb.entity.ChannelPckgItem;
import comcast.stb.entity.LoginData;
import comcast.stb.entity.MoviePckgItem;
import comcast.stb.logout.LogoutApiInterface;
import comcast.stb.logout.LogoutPresImpl;
import io.realm.Realm;

import static comcast.stb.StringData.CHANNEL_PACKAGE;
import static comcast.stb.StringData.MOVIE_PACKAGE;

public class PackageDialogFragment extends DialogFragment implements PackageApiInterface.PackagesView, LogoutApiInterface.LogoutView {
    private static final String PACKAGE_TYPE = "package_type";
    private static final String PACKAGE_ID = "package_id";


    private OnFragmentInteractionListener mListener;

    @BindView(R.id.txt_package_label)
    TextView listLabel;

    @BindView(R.id.recycler_pckgItms_list)
    RecyclerView packagItemsRecycler;

    @BindView(R.id.txt_package_description)
    TextView description;

    @BindView(R.id.txt_package_price)
    TextView packagePrice;

    @BindView(R.id.btn_package_buy)
    Button btnBuy;
    private String packageType;
    private int packageId;
    private PackagePresenterImpl packagePresenter;
    private LogoutPresImpl logoutPres;
    private Realm realm;
    private LoginData loginData;


    public PackageDialogFragment() {
        // Required empty public constructor
    }


    public static PackageDialogFragment newInstance(String packageType, int packageId) {
        PackageDialogFragment fragment = new PackageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PACKAGE_ID, packageId);
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
            packageId = getArguments().getInt(PACKAGE_ID);
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
        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (packageType) {
            case CHANNEL_PACKAGE:
                packagePresenter.getChannlesInaPckg(packageId, loginData.getToken());
                break;
            case MOVIE_PACKAGE:
                packagePresenter.getMoviesInaPckg(packageId, loginData.getToken());
                break;
        }
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        packagItemsRecycler.setLayoutManager(manager);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);


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
        PackageRecyclerAdapter packageRecyclerAdapter=new PackageRecyclerAdapter(getActivity(), (ArrayList) channelsInaPckgList,CHANNEL_PACKAGE);
        packagItemsRecycler.setAdapter(packageRecyclerAdapter);
    }

    @Override
    public void setMoviesInaPckg(int packageId, List<MoviePckgItem> moviesInaPckgList) {
        PackageRecyclerAdapter packageRecyclerAdapter=new PackageRecyclerAdapter(getActivity(), (ArrayList) moviesInaPckgList,MOVIE_PACKAGE);
        packagItemsRecycler.setAdapter(packageRecyclerAdapter);
    }

    @Override
    public void onChannelInaPckgError(int packageId, String message) {

    }

    @Override
    public void onMoviesInaPckgError(int packageId, String message) {

    }

    @Override
    public void successfulLogout() {
        logoutPres.logout();
    }


    public interface OnFragmentInteractionListener {

        void onBuyClick();
    }


}
