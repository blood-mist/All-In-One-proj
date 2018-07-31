package comcast.stb.livetv;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import comcast.stb.entity.events.ChannelSwitch;
import timber.log.Timber;

import static comcast.stb.StringData.PURCHASE_TYPE_BOUGHT;
import static comcast.stb.StringData.PURCHASE_TYPE_BUY;
import static comcast.stb.StringData.USER_NAME;


public class MenuFragment extends Fragment implements CategoryRecyclerAdapter.OnCategoryListInteractionListener, ChannelRecyclerAdapter.OnChannelListInteractionListener {
    private Unbinder unbinder;
    DividerItemDecoration mDividerItemDecoration;
    @BindView(R.id.recycler_category_list)
    RecyclerView categoryRecyclerView;

    @BindView(R.id.recycler_channel_list)
    RecyclerView channelRecyclerView;

    @BindView(R.id.txt_epg)
    TextView pgmEpg;


    @BindView(R.id.txt_channel_description)
    TextView channelDescription;

    @BindView(R.id.txt_menu_price)
    TextView channelPrice;

    @BindView(R.id.txt_tv_description)
    TextView descriptionTitle;


    @BindView(R.id.channel_list_layout)
    LinearLayout channelListContainer;

    @BindView(R.id.txt_category_label)
    TextView txtCurrentCategory;


    @BindView(R.id.img_desc)
    ImageView imgDescription;

    @BindView(R.id.default_view)
    View dividerView;


    @BindView(R.id.description_category)
    TextView descriptionCategory;

    private Channel currentChannel;
    private Channel selectedChannel;

    private static final String CHANNEL_LIST = "channel_list";

    private ArrayList<ChannelCategory> channelCategoryList;

    private ArrayList<Channel> channelList;
    private ArrayList<Channel> playableChannelList;
    private ChannelCategory currentChannelCategory;

    private OnChannelClickedListener clickListener;
    CategoryRecyclerAdapter categoryRecyclerAdapter;
    ChannelRecyclerAdapter channelRecyclerAdapter;

    public MenuFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param channelCategoryList Parameter 1.
     * @return A new instance of fragment MenuFragment.
     */
    public static MenuFragment newInstance(List<ChannelCategory> channelCategoryList) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(CHANNEL_LIST, (ArrayList<? extends Parcelable>) channelCategoryList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            channelCategoryList = getArguments().getParcelableArrayList(CHANNEL_LIST);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View menuView = inflater.inflate(R.layout.fragment_tv_menu, container, false);
        ButterKnife.bind(this, menuView);
        if (getActivity() instanceof LiveTVActivity) {
            currentChannel = ((LiveTVActivity) getActivity()).getCurrentChannel();
        }
        pgmEpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onEpgRequest(selectedChannel, currentChannelCategory);
            }
        });

        dividerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    pgmEpg.requestFocus();
                }
            }
        });


        // Inflate the layout for this fragment
        return menuView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateChannelWithCategory(channelCategoryList);


    }

    private void populateChannelWithCategory(ArrayList<ChannelCategory> channelCategoryList) {
        this.channelCategoryList = channelCategoryList;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        if (categoryRecyclerAdapter == null)
            categoryRecyclerAdapter = new CategoryRecyclerAdapter(getActivity(), this.channelCategoryList, MenuFragment.this);
        categoryRecyclerView.setAdapter(categoryRecyclerAdapter);
        categoryRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.HORIZONTAL));
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
            selectedChannel = currentChannel;
            currentChannelCategory = currentCategory;
            updateChannelDescriptionUI(selectedChannel);
            txtCurrentCategory.setText(currentCategory.getCategoryTitle());
            assert currentCategory != null;
            onCategoryListClickInteraction(currentCategory);
            Timber.d("category:" + channelCategoryList.indexOf(currentCategory));
        } else {
            onCategoryListClickInteraction(channelCategoryList.get(0));
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
    public void onCategoryListClickInteraction(ChannelCategory channelCategory) {
        this.channelList = (ArrayList<Channel>) channelCategory.getChannels();
        playableChannelList = new ArrayList<>();
        for (Channel isPlayableCh : channelList) {
            if (!isPlayableCh.getSubscriptionStatus().equals(PURCHASE_TYPE_BUY) && !isPlayableCh.isExpiryFlag()) {
                playableChannelList.add(isPlayableCh);
            }
        }
        txtCurrentCategory.setText(channelCategory.getCategoryTitle());
        currentChannelCategory = channelCategory;
        if (channelRecyclerAdapter == null) {
            channelRecyclerAdapter = new ChannelRecyclerAdapter(getActivity(), this.channelList, MenuFragment.this);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            channelRecyclerView.setLayoutManager(manager);
            channelRecyclerView.setAdapter(channelRecyclerAdapter);
            channelRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                    DividerItemDecoration.VERTICAL));
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
        updateChannelDescriptionUI(channel);


    }


    private void updateChannelDescriptionUI(Channel channel) {
        this.selectedChannel = channel;
        descriptionTitle.setText(channel.getChannelName());
        descriptionCategory.setText("Channel Category: " + currentChannelCategory.getCategoryTitle());
        channelPrice.setText("$" + channel.getChannelPrice());
        channelDescription.setText(channel.getExpiry());
        Picasso.with(getActivity())
                .load(channel.getChannelLogo())
                .resize(150, 150)
                .placeholder(R.drawable.placeholder)
                .into(imgDescription);
        switch (channel.getSubscriptionStatus()) {
            case PURCHASE_TYPE_BUY:

                break;
            default:
                if (channel.isExpiryFlag()) {

                } else {

                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChannelSwitch event) {
        Channel toChangeChannel = null;
        int currentChannelPos = 0;
        for (int i = 0; i < playableChannelList.size(); i++) {
            if (currentChannel.getChannelId() == playableChannelList.get(i).getChannelId()) {
                currentChannelPos = i;
            }

        }
        if (event.isUpChannel()) {
            if (currentChannelPos < playableChannelList.size() - 1) {
                currentChannelPos++;
            } else {
                currentChannelPos = 0;
            }

        } else {
            if (currentChannelPos > 0) {
                currentChannelPos--;
            } else {
                currentChannelPos = currentChannelCategory.getChannels().size() - 1;
            }
        }
        toChangeChannel = playableChannelList.get(currentChannelPos);
        onChannelClickInteraction(toChangeChannel);
    }

    ;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public interface OnChannelClickedListener {
        void onChannelClicked(Channel channel);

        void onEpgRequest(Channel channel, ChannelCategory channelCategory);

        void onLogoutClicked();
    }
}
