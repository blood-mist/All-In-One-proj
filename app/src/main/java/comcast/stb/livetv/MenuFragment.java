package comcast.stb.livetv;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import comcast.stb.R;
import comcast.stb.entity.Channel;
import comcast.stb.entity.ChannelCategory;
import comcast.stb.entity.EventItem;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static comcast.stb.StringData.PURCHASE_TYPE_BUY;
import static comcast.stb.StringData.USER_NAME;


public class MenuFragment extends Fragment implements CategoryRecyclerAdapter.OnCategoryListInteractionListener, ChannelRecyclerAdapter.OnChannelListInteractionListener, DateAdapter.OnDayClickListener {
    private Unbinder unbinder;
    DividerItemDecoration mDividerItemDecoration;
    @BindView(R.id.recycler_category_list)
    RecyclerView categoryRecyclerView;

    @BindView(R.id.recycler_channel_list)
    RecyclerView channelRecyclerView;

    @BindView(R.id.current_category)
    TextView selectedCategory;

    @BindView(R.id.category_container)
    LinearLayout categoryLayout;

    @BindView(R.id.description_container)
    LinearLayout descriptionLayout;

    @BindView(R.id.txt_channel_description)
    TextView channelDescription;

    @BindView(R.id.txt_menu_price)
    TextView channelPrice;

    @BindView(R.id.txt_tv_description)
    TextView descriptionTitle;

    @BindView(R.id.txt_tv_username)
    TextView userName;

    @BindView(R.id.img_tv_logout)
    ImageButton logout;

    @BindView(R.id.buy_option_container)
    LinearLayout buylayout;

    @BindView(R.id.channel_list_layout)
    LinearLayout channelListContainer;

    @BindView(R.id.txt_errorEpgText)
    TextView errorEpgTxt;

    @BindView(R.id.img_desc)
    ImageView imgDescription;

    @BindView(R.id.epg_container)
    RelativeLayout epgContainer;

    @BindView(R.id.day_recycler)
    RecyclerView dayRecyclerList;

    @BindView(R.id.pgm_guide_recycler)
    RecyclerView pgmRecyclerList;

    @BindView(R.id.description_category)
    TextView descriptionCategory;

    @BindView(R.id.epg_list_layout)
    LinearLayout epgListlayouut;
    private Channel currentChannel;
    private LinkedHashMap<String, ArrayList<EventItem>> epghashMap;

    private static final String CHANNEL_LIST = "channel_list";

    private ArrayList<ChannelCategory> channelCategoryList;
    private ArrayList<Calendar> calendarList;
    private ArrayList<EventItem> programList;

    private ArrayList<Channel> channelList;
    private String username;

    private OnChannelClickedListener clickListener;
    CategoryRecyclerAdapter categoryRecyclerAdapter;
    ChannelRecyclerAdapter channelRecyclerAdapter;
    ProgramRecyclerAdapter programRecyclerAdapter;
    DateAdapter dateAdapter;

    public MenuFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param channelCategoryList Parameter 1.
     * @return A new instance of fragment MenuFragment.
     */
    public static MenuFragment newInstance(List<ChannelCategory> channelCategoryList, String username) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(CHANNEL_LIST, (ArrayList<? extends Parcelable>) channelCategoryList);
        args.putString(USER_NAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            channelCategoryList = getArguments().getParcelableArrayList(CHANNEL_LIST);
            username = getArguments().getString(USER_NAME);

        }
    }

    public static void tintButton(@NonNull ImageButton button) {
        ColorStateList colours = button.getResources()
                .getColorStateList(R.color.colorBlue);
        Drawable d = DrawableCompat.wrap(button.getDrawable());
        DrawableCompat.setTintList(d, colours);
        button.setImageDrawable(d);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View menuView = inflater.inflate(R.layout.fragment_tv_menu, container, false);
        ButterKnife.bind(this, menuView);
        if (getActivity() instanceof LiveTVActivity) {
            currentChannel = ((LiveTVActivity) getActivity()).getCurrentChannel();
        }
        categoryLayout.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if (categoryLayout.getFocusedChild() == null) {
                    categoryLayout.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.menu_left_bg_unselected));

                } else {
                    categoryLayout.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.menu_left_bg_selected));
                }

            }
        });
        descriptionLayout.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if (descriptionLayout.getFocusedChild() == null) {
                    descriptionLayout.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.menu_right_bg_unselected));

                } else {
                    descriptionLayout.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.menu_right_bg_selected));
                }

            }
        });
        categoryRecyclerView.setNextFocusRightId(epgListlayouut.getVisibility() == VISIBLE ? dayRecyclerList.getId() : channelRecyclerView.getId());
        channelRecyclerView.setNextFocusLeftId(epgListlayouut.getVisibility() == VISIBLE ? dayRecyclerList.getId() : categoryRecyclerView.getId());
        // Inflate the layout for this fragment
        return menuView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userName.setText(username);
        populateChannelWithCategory(channelCategoryList);


    }
    @OnClick(R.id.img_tv_logout)
    public void logout(){
        clickListener.onLogoutClicked();
    }

    private void populateChannelWithCategory(ArrayList<ChannelCategory> channelCategoryList) {
        this.channelCategoryList = channelCategoryList;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        if (categoryRecyclerAdapter == null)
            categoryRecyclerAdapter = new CategoryRecyclerAdapter(getActivity(), this.channelCategoryList, MenuFragment.this);
        categoryRecyclerView.setAdapter(categoryRecyclerAdapter);
        categoryRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        categoryRecyclerView.requestFocus();
        if (currentChannel != null) {
            ChannelCategory currentCategory = null;
            int categoryId = currentChannel.getChannelCategoryId();
            for (ChannelCategory channelCategory : channelCategoryList) {
                if (categoryId == channelCategory.getCategoryId()) {
                    currentCategory = channelCategory;
                    break;
                }
            }
            categoryRecyclerAdapter.setSelectedPos(channelCategoryList.indexOf(currentCategory));
            assert currentCategory != null;
            onCategoryListClickInteraction(currentCategory.getCategoryTitle(), (ArrayList) currentCategory.getChannels());
            Timber.d("category:" + channelCategoryList.indexOf(currentCategory));
        }

        categoryRecyclerAdapter.notifyDataSetChanged();


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChannelClickedListener) {
            clickListener = (OnChannelClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChannelClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clickListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCategoryListClickInteraction(String categoryName, ArrayList channelList) {
        this.channelList = channelList;
        selectedCategory.setText(categoryName);
        if (channelRecyclerAdapter == null) {
            channelRecyclerAdapter = new ChannelRecyclerAdapter(getActivity(), this.channelList, MenuFragment.this);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            channelRecyclerView.setLayoutManager(manager);
            channelRecyclerView.setAdapter(channelRecyclerAdapter);
        } else {
            channelRecyclerView.swapAdapter(new ChannelRecyclerAdapter(getActivity(), this.channelList, MenuFragment.this), true);
        }
        if (currentChannel != null) {
            channelRecyclerAdapter.setSelectedChannel(channelList.indexOf(currentChannel));
        }
    }

    @Override
    public void onChannelClickInteraction(Channel channel) {
        clickListener.onChannelClicked(channel);
    }

    @Override
    public void onChannelSelected(Channel channel) {
        channelListContainer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white_selection));
        updateChannelDescriptionUI(channel);
        clickListener.onChannelSelected(channel);


    }

    @Override
    public void onChannelDeselected() {
        channelListContainer.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white_no_selection));
    }


    private void updateChannelDescriptionUI(Channel channel) {
        descriptionTitle.setText(channel.getChannelName());
        descriptionCategory.setText("Category Type: "+selectedCategory.getText().toString());
        Picasso.with(getActivity())
                .load(channel.getChannelLogo())
                .into(imgDescription);
        switch (channel.getSubscriptionStatus()) {
            case PURCHASE_TYPE_BUY:
                buylayout.setVisibility(View.VISIBLE);
                epgContainer.setVisibility(GONE);
                break;
            default:
                if (channel.isExpiryFlag()) {
                    buylayout.setVisibility(View.VISIBLE);
                    epgContainer.setVisibility(GONE);
                } else {
                    buylayout.setVisibility(GONE);
                    epgContainer.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void populateDayList(ArrayList<Calendar> calendarList, LinkedHashMap<String, ArrayList<EventItem>> epgChannelList) {
        epghashMap = epgChannelList;
        if (calendarList.size() > 3)
            this.calendarList = new ArrayList<>(calendarList.subList(0, 3));
        else
            this.calendarList = calendarList;
        if (dateAdapter == null) {
            dateAdapter = new DateAdapter(getActivity(), this.calendarList, MenuFragment.this);
            dayRecyclerList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            dayRecyclerList.setAdapter(dateAdapter);
        } else {
            dateAdapter = new DateAdapter(getActivity(), this.calendarList, MenuFragment.this);
            dayRecyclerList.swapAdapter(dateAdapter, false);
        }
        onDayClicked(0);
        epgListlayouut.setVisibility(View.VISIBLE);
        errorEpgTxt.setVisibility(GONE);

    }

    @Override
    public void onDayClicked(int position) {
        ArrayList<EventItem> clickedDayEpgList = epghashMap.get((epghashMap.keySet().toArray())[position]);
        Log.d("onDayClicked: ", clickedDayEpgList.size() + "");
        if (programRecyclerAdapter == null) {
            programList = clickedDayEpgList;
            programRecyclerAdapter = new ProgramRecyclerAdapter(getActivity(), programList);
            pgmRecyclerList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            pgmRecyclerList.setAdapter(programRecyclerAdapter);
        } else {
            programList = clickedDayEpgList;
            programRecyclerAdapter = new ProgramRecyclerAdapter(getActivity(), programList);
            pgmRecyclerList.swapAdapter(programRecyclerAdapter, false);
        }
    }

    public void hideEpgMenu() {
        epgListlayouut.setVisibility(GONE);
        errorEpgTxt.setVisibility(View.VISIBLE);
    }


    public interface OnChannelClickedListener {
        void onChannelClicked(Channel channel);

        void onChannelSelected(Channel channel);

        void onLogoutClicked();
    }
}
