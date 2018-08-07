package comcast.stb.packageInfoDialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comcast.stb.R;
import comcast.stb.entity.PackagesInfo;

import static comcast.stb.StringData.PURCHASE_TYPE_BOUGHT;
import static comcast.stb.StringData.PURCHASE_TYPE_BUY;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PackageInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PackageInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PackageInfo  extends DialogFragment {
    private static final String PACKAGE_ITEM = "packageItem";
    private static final String PACKAGE_TYPE = "package_type";

    private PackagesInfo packagesInfo;
    private String packageType;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.txt_packageName)
    TextView packageName;
    @BindView(R.id.txt_packagePrice)
    TextView packagePrice;
    @BindView(R.id.txt_expiryStatus)
    TextView expiryStatus;
    @BindView(R.id.txt_expiryDate)
    TextView expiryDate;

    public PackageInfo() {
        // Required empty public constructor
    }

    public static PackageInfo newInstance(PackagesInfo packagesInfo, String packageType) {
        PackageInfo fragment = new PackageInfo();
        Bundle args = new Bundle();
        args.putParcelable(PACKAGE_ITEM, packagesInfo);
        args.putString(PACKAGE_TYPE,packageType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE,0);
        if (getArguments() != null) {
            this.packagesInfo = getArguments().getParcelable(PACKAGE_ITEM);
            this.packageType=getArguments().getString(PACKAGE_TYPE);
        }
    }

    @OnClick(R.id.btn_packageCancel)
    public void onCancelClicked(){
        getDialog().dismiss();
    }

    @OnClick(R.id.btn_packagePurchase)
    public void onPurchaseClicked(){
        if (mListener != null) {
            mListener.onPurchaseInteraction(packagesInfo,packageType);
        }
        getDialog().dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_package_info2, null, false);
        ButterKnife.bind(this,v);
        packageName.setText(packagesInfo.getPackageName());
        packagePrice.setText(packagesInfo.getPackagePrice());
        expiryStatus.setText(setPackageStatus());
        expiryDate.setText(setExpiryDate());
        return v;
    }

    private String setExpiryDate() {
        if(packagesInfo.getExpiry()==null || packagesInfo.getExpiry().equals("")){
            return "N/A";
        }else{
            return packagesInfo.getExpiry();
        }
    }

    private String setPackageStatus() {
        String returnString="";
        switch (packagesInfo.getSubscriptionStatus()){
            case PURCHASE_TYPE_BUY:
                returnString= "BUY";
                break;
            case PURCHASE_TYPE_BOUGHT:
                if(packagesInfo.getExpiryFlag())
                    returnString= "UPGRADE";
                else
                    returnString= "BOUGHT";
                break;

        }
        return returnString;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPurchaseInteraction(PackagesInfo packagesInfo,String packageType);
    }
}
