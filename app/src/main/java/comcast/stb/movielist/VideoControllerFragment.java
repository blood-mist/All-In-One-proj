package comcast.stb.movielist;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Formatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.MoviesItem;


public class VideoControllerFragment extends Fragment {
    private static final String MOVIE_DATA = "movieData";
    private static final String MEDIA_INSTANCE = "mediaInstance";
    private static final int FORWARD_REWIND_DURATION = 15000;

    private MoviesItem movie;
    private MediaPlayer mediaPlayer;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private boolean mShowing;
    private boolean mDragging;

    @BindView(R.id.movie_title)
    TextView movieTitle;
    @BindView(R.id.movie_image)
    ImageView movieImage;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.txt_startTime)
    TextView mCurrentTime;
    @BindView(R.id.txt_endTime)
    TextView endTime;
    @BindView(R.id.btn_play)
    ImageButton btnPlay;
    @BindView(R.id.btn_forward)
    ImageButton btnForward;
    @BindView(R.id.btn_rewind)
    ImageButton btnRewind;
    public VideoControllerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie Parameter 1.
     * @param mediaPlayer Parameter 2.
     * @return A new instance of fragment VideoControllerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoControllerFragment newInstance(MoviesItem movie,MediaPlayer mediaPlayer) {
        VideoControllerFragment fragment = new VideoControllerFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_DATA, movie);
        args.putParcelable(MEDIA_INSTANCE, (Parcelable) mediaPlayer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable(MOVIE_DATA);
            mediaPlayer = getArguments().getParcelable(MEDIA_INSTANCE);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_video_controller, container, false);
        ButterKnife.bind(this,v);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                if (mediaPlayer == null) {
                    return;
                }

                if (!fromUser) {
                    // We're not interested in programmatically generated changes to
                    // the progress bar's position.
                    return;
                }

                long duration = mediaPlayer.getDuration();
                long newposition = (duration * value) / 1000L;
                mediaPlayer.seekTo((int) newposition);
                if (mCurrentTime != null)
                    mCurrentTime.setText(stringForTime((int) newposition));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                mDragging = true;

                // By removing these pending progress messages we make sure
                // that a) we won't update the progress while the user adjusts
                // the seekbar and b) once the user is done dragging the thumb
                // we will post one of these messages to the queue again and
                // this ensures that there will be exactly one message queued up.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mDragging = false;
                setProgress();
                updatePausePlay();
            }
        });
        seekbar.setProgress(0);
        seekbar.setMax(1000);
        return v;
    }

    private void updatePausePlay() {
             if (mediaPlayer.isPlaying()) {
                btnPlay.setImageResource(R.drawable.ic_pause_black_24dp);
            } else {
                btnPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            }

    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
    private int setProgress() {
        if (mediaPlayer == null || mDragging) {
            return 0;
        }

        int position = mediaPlayer.getCurrentPosition();
        int duration = mediaPlayer.getDuration();
        if (seekbar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                seekbar.setProgress((int) pos);
            }
//            int percent = mPlayer.getBufferPercentage();
//            mProgress.setSecondaryProgress(percent * 10);
        }

        if (endTime != null)
            endTime.setText(stringForTime(duration));
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        return position;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
