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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import comcast.stb.R;
import comcast.stb.entity.Channel;
import comcast.stb.entity.ChannelCategory;
import timber.log.Timber;

import static comcast.stb.StringData.PURCHASE_TYPE_BUY;
import static comcast.stb.StringData.USER_NAME;


public class MenuFragment extends Fragment implements CategoryRecyclerAdapter.OnCategoryListInteractionListener, ChannelRecyclerAdapter.OnChannelListInteractionListener {
    private Unbinder unbinder;
    @BindView(R.id.recycler_category_list)
    RecyclerView categoryRecyclerView;

    @BindView(R.id.recycler_channel_list)
    RecyclerView channelRecyclerView;

    @BindView(R.id.current_category)
    TextView selectedCategory;

    @BindView(R.id.txt_channel_description)
    TextView channelDescription;

    @BindView(R.id.txt_menu_price)
    TextView channelPrice;

    @BindView(R.id.txt_tv_username)
    TextView userName;

    @BindView(R.id.img_tv_logout)
    ImageView logout;

    @BindView(R.id.buy_option_container)
    LinearLayout buylayout;

    @BindView(R.id.channel_list_layout)
    LinearLayout channelListContainer;

    @BindView(R.id.txt_tv_description)
    TextView descTitle;

    @BindView(R.id.img_desc)
    ImageView imgDescription;
    private Channel currentChannel;

    private static final String CHANNEL_LIST = "channel_list";

    private ArrayList<ChannelCategory> channelCategoryList;

    private ArrayList<Channel> channelList;
    private String username;

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

        // Inflate the layout for this fragment
        return menuView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userName.setText(username);
        populateChannelWithCategory(channelCategoryList);


    }

    private void populateChannelWithCategory(ArrayList<ChannelCategory> channelCategoryList) {
        this.channelCategoryList = channelCategoryList;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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
            onCategoryListClickInteraction(currentCategory.getCategoryTitle(),(ArrayList) currentCategory.getChannels());
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
    public void onCategoryListClickInteraction(String categoryName,ArrayList channelList) {
        this.channelList = channelList;
        selectedCategory.setText(categoryName);
        channelRecyclerAdapter = new ChannelRecyclerAdapter(getActivity(), this.channelList, MenuFragment.this);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        channelRecyclerView.setLayoutManager(manager);
        channelRecyclerView.setAdapter(channelRecyclerAdapter);
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
        channelListContainer.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white_selection));
        updateChannelDescriptionUI(channel);

    }

    @Override
    public void onChannelDeselected() {
        channelListContainer.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white_no_selection));
    }


    private void updateChannelDescriptionUI(Channel channel) {
        channelDescription.setText(channel.getChannelName() + channel.getChannelId() + channel.getExpiry());
         Picasso.with(getActivity())
                .load(channel.getChannelLogo())
                .into(imgDescription);
        switch (channel.getSubscriptionStatus()) {
            case PURCHASE_TYPE_BUY:
                buylayout.setVisibility(View.VISIBLE);
                break;
            default:
                if (channel.isExpiryFlag()) {
                    buylayout.setVisibility(View.VISIBLE);
                } else {
                    buylayout.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void updateCategoryDescriptionUI(ChannelCategory category) {
        descTitle.setText("Category Description");
        buylayout.setVisibility(View.GONE);
        /*Picasso.with(getActivity())
                .load(category.getCategoryImage())
                .into(imgDescription);*/

    }


public interface OnChannelClickedListener {
    void onChannelClicked(Channel channel);
}
}
