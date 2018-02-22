package comcast.stb.packageInfoDialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;

public class PackageActivity extends AppCompatActivity {


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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_package_dialog);
        ButterKnife.bind(this);
    }



}
