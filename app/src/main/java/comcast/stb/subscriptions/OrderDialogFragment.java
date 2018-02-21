package comcast.stb.subscriptions;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comcast.stb.R;

public class OrderDialogFragment extends DialogFragment {
    private static final String ORDER_NAME = "order_name";
    private static final String ORDER_ID = "order_id";
    private static final String SUBSCRIBED_ID = "subscribed_at";
    private static final String UPDATED_AT = "updated_at";
    private static final String EXPIRY_AT = "expiry_at";
    private static final String TOTAL_PRICE = "total_price";
    private String orderName;
    private int orderId;
    private String subscribedId;
    private String updatedAt;
    private String expiryAt;
    private String totalPrice;


    @BindView(R.id.txt_orderName)
    TextView txtOrderName;

    @BindView(R.id.txt_orderId)
    TextView txtOrderId;

    @BindView(R.id.txt_subsId)
    TextView txtSubsId;
    @BindView(R.id.txt_updatedAt)
    TextView txtUpdatedAt;
    @BindView(R.id.txt_expiryAt)
    TextView txtExpiryAt;
    @BindView(R.id.txt_totalPrice)
    TextView txtTotalPrice;
    @BindView(R.id.btn_dismiss)
    Button dismiss;


    public OrderDialogFragment() {
        // Required empty public constructor
    }


    public static OrderDialogFragment newInstance(String orderName, int orderId, String subscribedId, String updatedAt, String expiryAt, String totalPrice) {
        OrderDialogFragment fragment = new OrderDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_NAME, orderName);
        bundle.putInt(ORDER_ID, orderId);
        bundle.putString(SUBSCRIBED_ID, subscribedId);
        bundle.putString(UPDATED_AT, updatedAt);
        bundle.putString(EXPIRY_AT, expiryAt);
        bundle.putString(TOTAL_PRICE, totalPrice);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderName = getArguments().getString(ORDER_NAME);
            orderId = getArguments().getInt(ORDER_ID);
            subscribedId = getArguments().getString(SUBSCRIBED_ID);
            updatedAt = getArguments().getString(UPDATED_AT);
            expiryAt = getArguments().getString(EXPIRY_AT);
            totalPrice = getArguments().getString(TOTAL_PRICE);
        }
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dialogView = inflater.inflate(R.layout.fragment_order_dialog, container, false);
        ButterKnife.bind(this, dialogView);
        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUI();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void updateUI() {
        txtOrderName.setText(orderName);
        txtOrderId.setText(orderId);
        txtSubsId.setText(subscribedId);
        txtUpdatedAt.setText(updatedAt);
        txtExpiryAt.setText(expiryAt);
        txtTotalPrice.setText(totalPrice);
    }


    @OnClick(R.id.btn_dismiss)
    public void onDismissClick() {
        getDialog().dismiss();
    }


}
