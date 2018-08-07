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

import java.io.Serializable;
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

import static comcast.stb.StringData.CHANNEL_CATEGORY;
import static comcast.stb.StringData.CURRENT_CHANNEL;

public class EpgFragment extends Fragment implements DateAdapter.OnDayClickListener, ChannelRecyclerAdapter.OnChannelListInteractionListener {
    private ArrayList<ChannelCategory> channelCategoryList;
    private int channelId;
    private static final String CHANNEL_LIST = "channel_list";
    private static final String CHANNEL_ID = "channel_id";
    public static final String ERROR_MESSAGE = "error_message";
    private static final String CHANNEL_CATEGORY = "channelCategory";
    private static final String CHANNEL = "channel";
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
            channelId = getArguments().getInt(CHANNEL_ID);
//            populateChannelList(channelCategoryList);
        }
    }

    private void populateChannelList(List<ChannelCategory> channelCategoryList) {
        ArrayList<Channel> channels = new ArrayList<>();
        for (ChannelCategory category : channelCategoryList)
            channels.addAll(category.getChannels());

        ChannelRecyclerAdapter adapter = new ChannelRecyclerAdapter(getContext(), channels, EpgFragment.this);
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
        recChannels.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        pgmRecyclerList.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        channelCategoryList = getArguments().getParcelableArrayList(CHANNEL_CATEGORY);
        populateChannelList(channelCategoryList);
        if (getActivity() instanceof LiveTVActivity) {
            currentChannel = getArguments().getParcelable(CURRENT_CHANNEL);
            updateDatas(getArguments());
//            populateEpg(currentChannel);
        }
        Log.d("EPG FRAG CREATE VIEW", "complete: ");
        return menuView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        userName.setText(username);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MenuFragment.OnChannelClickedListener) {
            clickListener = (MenuFragment.OnChannelClickedListener) context;
            if (getArguments() != null) {
                channelCategoryList = getArguments().getParcelableArrayList(CHANNEL_CATEGORY);
//                populateChannelList(channelCategories);
            }
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEPGClickedListener");
        }
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
        if (dateAdapter == null) {
            dateAdapter = new DateAdapter(getActivity(), this.calendarList, EpgFragment.this);
            recDate.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recDate.setAdapter(dateAdapter);
        } else {
            dateAdapter = new DateAdapter(getActivity(), this.calendarList, EpgFragment.this);
            recDate.swapAdapter(dateAdapter, false);
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            Bundle b = getArguments();
            updateDatas(b);
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


    public void updateDatas(Bundle bundle) {
        LinkedHashMap<String, ArrayList<EventItem>> epgChannelList = (LinkedHashMap<String, ArrayList<EventItem>>) bundle.getSerializable("epgChannelList");
        calendarList = getCalendarList(epgChannelList);
        //click on calendar
        //set epg
        populateDayList(calendarList, epgChannelList);
    }
}


