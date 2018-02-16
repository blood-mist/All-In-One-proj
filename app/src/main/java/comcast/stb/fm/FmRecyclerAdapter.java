package comcast.stb.fm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import comcast.stb.R;
import comcast.stb.entity.FmsItem;

/**
 * Created by blood-mist on 1/10/18.
 */

public class FmRecyclerAdapter extends RecyclerView.Adapter<FmRecyclerAdapter.ViewHolder> {
    private ArrayList<FmsItem> fmList;
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

    private int selectedChannel = -1;

    public FmRecyclerAdapter(Context context, ArrayList<FmsItem> fmList, FmRecyclerAdapter.OnChannelListInteractionListener mListener) {
        this.fmList = fmList;
        this.mContext = context;
        this.mListener = mListener;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel, null);


        return new ViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }


    @Override
    public void onBindViewHolder(FmRecyclerAdapter.ViewHolder holder, final int position) {

//        holder.itemView.setSelected(tryFocusItem==position);

        FmsItem fm = fmList.get(position);
        holder.channelTitle.setText(fm.getName());
        Picasso.with(mContext)
                .load(fm.getImage())
                .resize(200, 200)
                .into(holder.channelImage);
        if (position == getSelectedChannel()) {
            holder.itemLayout.setSelected(true);
        } else {
            holder.itemLayout.setSelected(false);
        }

    }

    @Override
    public int getItemCount() {
        return fmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView channelTitle;
        private LinearLayout itemLayout;
        private View shadeView;
        private ImageView channelImage;

        public ViewHolder(final View itemView) {
            super(itemView);
            shadeView = itemView.findViewById(R.id.shade_view);
            channelTitle = itemView.findViewById(R.id.txt_channelname);
            channelImage = itemView.findViewById(R.id.img_channel);
            itemLayout = itemView.findViewById(R.id.channel_item_layout);
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFMClicked(fmList.get(getAdapterPosition()));
                    notifyDataSetChanged();

                }
            });

            itemLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        onFMSelected(fmList.get(getAdapterPosition()));
                        shadeView.setVisibility(View.GONE);
                        itemView.setScaleX(1.05f);
                        itemView.setScaleY(1.05f);
                    } else {
                        shadeView.setVisibility(View.VISIBLE);
                        itemView.setScaleX(1.0f);
                        itemView.setScaleY(1.0f);
                    }
                }
            });

        }
    }

    private void onFMSelected(FmsItem fmsItem) {
        mListener.onChannelSelected(fmsItem);
    }

    private void onFMClicked(FmsItem fmsItem) {
        mListener.onChannelClickInteraction(fmsItem);
    }

    public interface OnChannelListInteractionListener {
        void onChannelClickInteraction(FmsItem fmsItem);

        void onChannelSelected(FmsItem fmsItem);
    }
}
