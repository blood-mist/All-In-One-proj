package comcast.stb.movielist;

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
import comcast.stb.entity.MoviesItem;

import static comcast.stb.StringData.ERROR_TYPE;
import static comcast.stb.StringData.MOVIE_ID;

public class MovieDialogFragment extends DialogFragment {
    private static final String CAN_RETRY = "can_retry";
    private static final String INFO_HEADER = "info_header";
    private static final String INFO_BODY = "info_body";
    private boolean canRetry;
    private String infoHeader;
    private String infoBody;
    private MoviesItem movie;
    private String errorType;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.txt_infoHeader)
    TextView header;

    @BindView(R.id.txt_infoBody)
    TextView body;

    @BindView(R.id.btn_retry)
    Button retry;

    @BindView(R.id.btn_dismiss)
    Button dismiss;


    public MovieDialogFragment() {
        // Required empty public constructor
    }


    public static MovieDialogFragment newInstance(String infoHeader, String infoBody, MoviesItem movie, String errorType, boolean canRetry) {
        MovieDialogFragment fragment = new MovieDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(CAN_RETRY, canRetry);
        bundle.putString(INFO_HEADER, infoHeader);
        bundle.putString(INFO_BODY, infoBody);
        bundle.putString(ERROR_TYPE,errorType);
        bundle.putParcelable(MOVIE_ID,movie);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            canRetry = getArguments().getBoolean(CAN_RETRY);
            infoHeader = getArguments().getString(INFO_HEADER);
            infoBody = getArguments().getString(INFO_BODY);
            movie =getArguments().getParcelable(MOVIE_ID);
            errorType=getArguments().getString(ERROR_TYPE);
        }
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dialogView = inflater.inflate(R.layout.fragment_error_dialog, container, false);
        ButterKnife.bind(this, dialogView);
        if (canRetry) {
            retry.setVisibility(View.VISIBLE);
        } else {
            retry.setVisibility(View.GONE);
        }
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


    private void updateUI() {
        header.setText(infoHeader);
        body.setText(infoBody);
    }


    @OnClick(R.id.btn_retry)
    public void onRetryClick() {
        mListener.onRetryBtnInteraction(errorType, movie);
    }

    @OnClick(R.id.btn_dismiss)
    public void onDismissClick() {
        mListener.onDismissBtnInteraction();
    }


    public interface OnFragmentInteractionListener {
        void onRetryBtnInteraction(String errorType, MoviesItem movie);

        void onDismissBtnInteraction();
    }
}
