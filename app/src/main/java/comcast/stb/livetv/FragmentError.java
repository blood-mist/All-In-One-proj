package comcast.stb.livetv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import comcast.stb.R;

import static comcast.stb.livetv.EpgFragment.ERROR_MESSAGE;

public class FragmentError extends Fragment {


    TextView txtErrorMessage, txtErrorCode,error;
    String strErrorCode, strErrorMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_error, container, false);
        findViewByIds(view);
        final Bundle bundle = getArguments();
        setErrorOnUI(bundle);

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void setErrorOnUI(Bundle bundle) {
        try {

            strErrorCode = "ER_EPG";
            strErrorMessage = bundle.getString(ERROR_MESSAGE,"Sorry,EPG for the requested channel is not available");

        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            if (strErrorCode != null) {
                txtErrorCode.setVisibility(View.VISIBLE);
                txtErrorCode.setText("Error Code: " + strErrorCode);
            }
            txtErrorMessage.setText(strErrorMessage);
        }
    }

    private void findViewByIds(View view) {

        txtErrorMessage = view.findViewById(R.id.txt_error_msg);
        txtErrorCode = view.findViewById(R.id.txt_error_code);
        error = view.findViewById(R.id.error);
        txtErrorCode.setVisibility(View.GONE);


    }
}