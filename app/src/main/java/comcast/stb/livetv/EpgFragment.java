package comcast.stb.livetv;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.Channel;
import comcast.stb.entity.ChannelCategory;
import comcast.stb.entity.DvrResponse;
import comcast.stb.entity.EventItem;

public class EpgFragment extends Fragment implements DateAdapter.OnDayClickListener, ChannelRecyclerAdapter.OnChannelListInteractionListener {
    private ArrayList<ChannelCategory> channelCategoryList;
    private int channelId;
    private static final String CHANNEL_LIST = "channel_list";
    private static final String CHANNEL_ID = "channel_id";
    private LinkedHashMap<String, ArrayList<EventItem>> epghashMap;
    private ArrayList<Calendar> calendarList;
    private MenuFragment.OnChannelClickedListener clickListener;

    public EpgFragment() {
    }

    private static EpgFragment instance;

    public static Fragment getInstance() {
        return instance = instance == null ? new EpgFragment() : instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            channelCategoryList = getArguments().getParcelableArrayList(CHANNEL_LIST);
            channelId = getArguments().getInt(CHANNEL_ID);
            populateChannelList(channelCategoryList);
        }
    }

    private void populateChannelList(ArrayList<ChannelCategory> channelCategoryList) {
        ArrayList<Channel> channels = new ArrayList<>();
        for (ChannelCategory category : channelCategoryList)
            channels.addAll(category.getChannels());

        ChannelRecyclerAdapter adapter = new ChannelRecyclerAdapter(getContext(), channels, EpgFragment.this);
        recChannels.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recChannels.setAdapter(adapter);
    }

    @BindView(R.id.recycler_channel_list)
    RecyclerView recChannels;
    @BindView(R.id.txt_on_air_prgm_time)
    TextView txtOnAirTime;
    @BindView(R.id.txt_on_air_prgm_name)
    TextView getTxtOnAirTPrgm;
    @BindView(R.id.date_recycler)
    RecyclerView recDate;
    @BindView(R.id.prgm_recycler)
    RecyclerView pgmRecyclerList;
    Channel currentChannel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View menuView = inflater.inflate(R.layout.fragment_epg_dvr, container, false);
        ButterKnife.bind(this, menuView);
        pgmRecyclerList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        if (getActivity() instanceof LiveTVActivity) {
            currentChannel = ((LiveTVActivity) getActivity()).getCurrentChannel();
        }
        return menuView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        userName.setText(username);

        populateEpg(channelCategoryList, currentChannel);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MenuFragment.OnChannelClickedListener) {
            clickListener = (MenuFragment.OnChannelClickedListener) context;
            if(getArguments()!=null){
                populateChannelList(getArguments().<ChannelCategory>getParcelableArrayList(CHANNEL_LIST));
            }
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEPGClickedListener");
        }
    }

    private void populateEpg(ArrayList<ChannelCategory> channelCategoryList, Channel currentChannel) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    DateAdapter dateAdapter;

    public void populateDayList(ArrayList<Calendar> calendarList, LinkedHashMap<String, ArrayList<EventItem>> epgChannelList) {
        epghashMap = epgChannelList;
        if (calendarList.size() > 3)
            this.calendarList = new ArrayList<>(calendarList.subList(0, 3));
        else
            this.calendarList = calendarList;
        RecyclerView dayRecyclerList = null;
        if (dateAdapter == null) {
            dateAdapter = new DateAdapter(getActivity(), this.calendarList, EpgFragment.this);
            dayRecyclerList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            dayRecyclerList.setAdapter(dateAdapter);
        } else {
            dateAdapter = new DateAdapter(getActivity(), this.calendarList, EpgFragment.this);
            dayRecyclerList.swapAdapter(dateAdapter, false);
        }
        onDayClicked(0);
    }

    @Override
    public void onDayClicked(int position) {
        ArrayList<EventItem> clickedDayEpgList = epghashMap.get((epghashMap.keySet().toArray())[position]);
        Log.d("onDayClicked: ", clickedDayEpgList.size() + "");
        ProgramRecyclerAdapter programRecyclerAdapter = new ProgramRecyclerAdapter(getActivity(), clickedDayEpgList);
        pgmRecyclerList.setAdapter(programRecyclerAdapter);
    }

    public void populateDvr(List<DvrResponse> dvrList, Channel channel) {

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            Bundle b = getArguments();
            //set Focus on channel
            LinkedHashMap<String, ArrayList<EventItem>> epgChannelList = b.getParcelable("epgChannelList");
            calendarList = getCalendarList(epgChannelList);
            //click on calendar
            //set epg
            populateDayList(calendarList, epgChannelList);

        }
        super.onHiddenChanged(hidden);
    }

    private ArrayList<Calendar> getCalendarList(LinkedHashMap<String, ArrayList<EventItem>> epgChannelList) {
        Log.d("hashmp", epgChannelList.size() + "");
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
        return calendarList;
    }


    @Override
    public void onChannelClickInteraction(Channel channel) {
        clickListener.OnEPGClicked(channel);
    }

    @Override
    public void onChannelSelected(Channel channel) {

    }

    @Override
    public void onChannelDeselected() {

    }
}
