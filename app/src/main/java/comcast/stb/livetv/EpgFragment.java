package comcast.stb.livetv;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.Channel;
import comcast.stb.entity.ChannelCategory;
import comcast.stb.entity.EpgResponse;
import comcast.stb.entity.EventItem;
import comcast.stb.entity.LoginData;
import comcast.stb.utils.ApiManager;
import comcast.stb.utils.DurationUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;
import static comcast.stb.StringData.LANGUAGE_ENGLISH;
import static comcast.stb.StringData.LIVE_CATEGORY_ERROR;
import static comcast.stb.StringData.LIVE_EPG_ERROR;
import static comcast.stb.StringData.PREF_LANG;


public class EpgFragment extends Fragment implements DateAdapter.OnDayClickListener, EpgChannelAdapter.OnChannelListInteractionListener {
    private static final String CHANNEL_CATEGORY = "channelCategory";
    private static final String CHANNEL = "channel";
    public static final String ERROR_MESSAGE = "error_message";


    @BindView(R.id.channelList)
    RecyclerView channelReycler;


    @BindView(R.id.txt_channel_name)
    TextView channelName;

    @BindView(R.id.txt_day)
    TextView onAirDay;

    @BindView(R.id.txt_date)
    TextView onAirDate;

    @BindView(R.id.txt_on_air_prgm_time)
    TextView epgOnAirTime;

    @BindView(R.id.txt_on_air_prgm_name)
    TextView epgonAirPgm;

    @BindView(R.id.layout_date_epg)
    LinearLayout layoutDateEpg;


    @BindView(R.id.gv_date)
    RecyclerView dateRecycler;

    @BindView(R.id.gv_prgms)
    RecyclerView pgmRecycler;

    // TODO: Rename and change types of parameters
    private ChannelCategory channelCategory;
    private Channel currentEpgChannel;
    private ArrayList<Calendar> calendars;
    private Realm realm;
    private LoginData loginData;
    private LinkedHashMap<String, ArrayList<EventItem>> epgHash;
    ProgramRecyclerAdapter programRecyclerAdapter;
    DateAdapter dateAdapter;

    private String preferedLang;

    private OnFragmentInteractionListener mListener;
    private ArrayList<EventItem> programList;

    public EpgFragment() {
    }


    public static EpgFragment newInstance(ChannelCategory channelCategory, Channel channel) {
        EpgFragment fragment = new EpgFragment();
        Bundle args = new Bundle();
        args.putParcelable(CHANNEL_CATEGORY, channelCategory);
        args.putParcelable(CHANNEL, channel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            channelCategory = getArguments().getParcelable(CHANNEL_CATEGORY);
            currentEpgChannel = getArguments().getParcelable(CHANNEL);
        }
        realm = Realm.getDefaultInstance();
        loginData = realm.where(LoginData.class).findFirst();
        preferedLang= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(PREF_LANG,LANGUAGE_ENGLISH);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_epg, container, false);
        ButterKnife.bind(this, v);
        String currentDay = new SimpleDateFormat("EEEE", Locale.US).format(Calendar.getInstance().getTime());
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Calendar.getInstance().getTime());
        onAirDay.setText(currentDay);
        onAirDate.setText(currentDate);
        EpgChannelAdapter epgChannelAdapter = new EpgChannelAdapter(getActivity(), (ArrayList<Channel>) channelCategory.getChannels(), EpgFragment.this);
        channelReycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        channelReycler.setAdapter(epgChannelAdapter);
        requestEpg(currentEpgChannel.getChannelId(), loginData.getToken());
        return v;
    }

    private void requestEpg(int channelId, String token) {
        Retrofit retrofit = ApiManager.getAdapter();
        final LiveTVApiInterface channelApiInterface = retrofit.create(LiveTVApiInterface.class);


        Observable<Response<EpgResponse>> observable = channelApiInterface.getEpg(channelId, token,preferedLang);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<EpgResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<EpgResponse> value) {
                        int responseCode = value.code();
                        if (responseCode == 200) {
                            setEpg(getFilteredEpg(value.body()));
//                            channelWithCategoryListener.takeEpgList(value.body());
                        } else {
                            onErrorOccured(value.message()); //value.message()
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException || e instanceof ConnectException) {
                            onErrorOccured("No Internet Connection");
                        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                            onErrorOccured("Couldn't connect to server");
                        } else {
                            onErrorOccured("EPG of the requested channel couldn't be fetched");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    private void onErrorOccured(String message) {
        layoutDateEpg.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putString(ERROR_MESSAGE, message);
        Fragment fragment = new FragmentError();
        fragment.setArguments(bundle);
        try {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container_epg_dvr, fragment, "error")
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private LinkedHashMap<String, ArrayList<EventItem>> getFilteredEpg(EpgResponse epgResponse) {
        String previousDate = "";
        List<EventItem> datewiseEpgList = null;
        LinkedHashMap<String, ArrayList<EventItem>> epgHash = new LinkedHashMap<>();
        List<EventItem> allEpgFrmServer = epgResponse.getEvents();
        Calendar currentCal = Calendar.getInstance();
        Calendar epgStartCal = Calendar.getInstance();
        Calendar epgEndCal = Calendar.getInstance();
        for (EventItem epgItem : allEpgFrmServer) {
            String startDate = epgItem.getBeginTime();
            String duration = epgItem.getDuration();
            DurationUtil durationUtil = new DurationUtil(duration);
            SimpleDateFormat epgDurationFormat = new SimpleDateFormat("HH:mm", Locale.US);
            SimpleDateFormat epgParseDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            try {
                Date epgStartDate = epgParseDateFormat.parse(startDate);
                epgStartCal.setTime(epgStartDate);
                epgEndCal.setTime(new Date(epgStartDate.getTime()));
                epgEndCal.add(Calendar.HOUR_OF_DAY, durationUtil.getDurationHour());
                epgEndCal.add(Calendar.MINUTE, durationUtil.getDurationMinute());
                epgEndCal.add(Calendar.SECOND, durationUtil.getDurationSecond());
                Log.d("endDateTime", startDate + "-" + epgParseDateFormat.format(epgEndCal.getTime()));
                if (epgEndCal.after(currentCal)) {
                    epgItem.setStartHour(epgDurationFormat.format(epgStartCal.getTime()));
                    epgItem.setEndHour(epgDurationFormat.format(epgEndCal.getTime()));
                    Log.d("filteredEndDateTime", startDate + "-" + epgParseDateFormat.format(epgEndCal.getTime()));
                    SimpleDateFormat dateDayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    String dayString = dateDayFormat.format(epgStartDate);
                    if (dayString.equals(previousDate)) {
                        //if contains key
                        assert datewiseEpgList != null;
                        datewiseEpgList.add(epgItem);
                        if (allEpgFrmServer.indexOf(epgItem) == allEpgFrmServer.size() - 1) {
                            epgHash.put(dayString, (ArrayList<EventItem>) datewiseEpgList);
                        }
                    } else {
                        if (datewiseEpgList != null)
                            epgHash.put(previousDate, (ArrayList<EventItem>) datewiseEpgList);

                        datewiseEpgList = new ArrayList<>();
                        datewiseEpgList.add(epgItem);
                        previousDate = dayString;
                        if (allEpgFrmServer.indexOf(epgItem) == allEpgFrmServer.size() - 1)
                            epgHash.put(dayString, (ArrayList<EventItem>) datewiseEpgList);
                    }

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return epgHash;
    }

    public void setEpg(LinkedHashMap<String, ArrayList<EventItem>> epgChannelList) {
        Log.d("hashmp", epgChannelList.size() + "");
        if(!epgChannelList.isEmpty()) {
            final ArrayList<Calendar> calendarList = new ArrayList<>();
            ArrayList<String> keys = new ArrayList<>(epgChannelList.keySet());
            for (int i = 0; i < keys.size(); i++) {
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(keys.get(i)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendarList.add(calendar);
            }
            showEpgMenu(calendarList, epgChannelList);
        }else{
            onErrorOccured("Sorry, EPG for the requested channel is not available");
        }
    }

    private void showEpgMenu(ArrayList<Calendar> calendarList, LinkedHashMap<String, ArrayList<EventItem>> epgChannelList) {
        populateDayList(calendarList, epgChannelList);
    }

    public void populateDayList(ArrayList<Calendar> calendarList, LinkedHashMap<String, ArrayList<EventItem>> epgChannelList) {
        epgHash = epgChannelList;
        this.calendars = calendarList;
        if (dateAdapter == null) {
            dateAdapter = new DateAdapter(getActivity(), this.calendars, EpgFragment.this);
            dateRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            dateRecycler.setAdapter(dateAdapter);
        } else {
            dateAdapter = new DateAdapter(getActivity(), this.calendars, EpgFragment.this);
            dateRecycler.swapAdapter(dateAdapter, false);
        }
        onDayClicked(0);

    }

    @Override
    public void onDayClicked(int position) {
        ArrayList<EventItem> clickedDayEpgList = epgHash.get((epgHash.keySet().toArray())[position]);
        Log.d("onDayClicked: ", clickedDayEpgList.size() + "");
        if (programRecyclerAdapter == null) {
            programList = clickedDayEpgList;
            programRecyclerAdapter = new ProgramRecyclerAdapter(getActivity(), programList);
            pgmRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
            pgmRecycler.setAdapter(programRecyclerAdapter);
        } else {
            programList = clickedDayEpgList;
            programRecyclerAdapter = new ProgramRecyclerAdapter(getActivity(), programList);
            pgmRecycler.swapAdapter(programRecyclerAdapter, false);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onChannelClickInteraction(Channel channel) {
        this.currentEpgChannel = channel;
        Fragment child = getChildFragmentManager().findFragmentByTag("error");
        if (child != null)
            getChildFragmentManager().beginTransaction().remove(child).commit();

        layoutDateEpg.setVisibility(View.VISIBLE);
        requestEpg(this.currentEpgChannel.getChannelId(), loginData.getToken());
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
        void onFragmentInteraction(Uri uri);
    }
}
