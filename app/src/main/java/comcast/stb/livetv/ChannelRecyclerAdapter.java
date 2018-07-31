package comcast.stb.livetv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import comcast.stb.R;
import comcast.stb.entity.Channel;

/**
 * Created by blood-mist on 1/10/18.
 */

public class ChannelRecyclerAdapter extends RecyclerView.Adapter<ChannelRecyclerAdapter.ViewHolder> {
    private ArrayList<Channel> channelList;
    private OnChannelListInteractionListener mListener;

    Context mContext;
    private int focusedItem = 0;
    int tryFocusItem;

    public int getSelectedChannel() {
        return selectedChannel;
    }

    public void setSelectedChannel(int selectedChannelPos) {
        this.selectedChannel = selectedChannelPos;
    }

    private int selectedChannel=-1;

    public ChannelRecyclerAdapter(Context context, ArrayList<Channel> channelList, ChannelRecyclerAdapter.OnChannelListInteractionListener mListener) {
        this.channelList = channelList;
        this.mContext = context;
        this.mListener = mListener;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_modified, parent,false);


        return new ViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }



    @Override
    public void onBindViewHolder(ChannelRecyclerAdapter.ViewHolder holder, final int position) {

//        holder.itemView.setSelected(tryFocusItem==position);

        Channel channel = channelList.get(position);
        Picasso.with(mContext)
                .load(channel.getChannelLogo())
                .resize(150,150)
                .placeholder(R.drawable.placeholder)
                .into(holder.channelImage);
        if (position == getSelectedChannel()) {
            holder.itemLayout.setSelected(true);
        } else {
            holder.itemLayout.setSelected(false);
        }

    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout itemLayout;
        private ImageView channelImage;
        public ViewHolder(final View itemView) {
            super(itemView);
            channelImage=itemView.findViewById(R.id.img_channel);
            itemLayout = itemView.findViewById(R.id.channel_item_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onChannelClicked(channelList.get(getAdapterPosition()));

                }
            });

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        onChannelSelected(channelList.get(getAdapterPosition()));
                    }
                }
            });

        }
    }


    private void onChannelSelected(Channel channel) {
        mListener.onChannelSelected(channel);
    }

    private void onChannelClicked(Channel channel) {
        mListener.onChannelClickInteraction(channel);
    }

    public interface OnChannelListInteractionListener {
        void onChannelClickInteraction(Channel channel);
        void onChannelSelected(Channel channel);
    }
}
