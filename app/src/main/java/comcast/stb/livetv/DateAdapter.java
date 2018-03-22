package comcast.stb.livetv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import comcast.stb.R;
import comcast.stb.entity.Channel;

/**
 * Created by ACER on 3/22/2018.
 */

class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
private ArrayList<Calendar> calendarArrayList;

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

public DateAdapter(Context context, ArrayList<Calendar> calendarArrayList) {
        this.calendarArrayList = calendarArrayList;
        this.mContext = context;
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
public void onBindViewHolder(DateAdapter.ViewHolder holder, final int position) {

//        holder.itemView.setSelected(tryFocusItem==position);

        Calendar calendar = calendarArrayList.get(position);
        holder.channelTitle.setText(channel.getChannelName());
        if (position == getSelectedChannel()) {
        holder.itemLayout.setSelected(true);
        } else {
        holder.itemLayout.setSelected(false);
        }

        }

@Override
public int getItemCount() {
        return calendarArrayList.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {

    private TextView channelTitle;
    private LinearLayout itemLayout;
    private View shadeView;
    private ImageView channelImage;
    public ViewHolder(final View itemView) {
        super(itemView);
        shadeView=itemView.findViewById(R.id.shade_view);
        channelTitle = itemView.findViewById(R.id.txt_channelname);
        channelImage=itemView.findViewById(R.id.img_channel);
        itemLayout = itemView.findViewById(R.id.channel_item_layout);
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChannelClicked(calendarArrayList.get(getAdapterPosition()));

            }
        });

        itemLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    onChannelSelected(calendarArrayList.get(getAdapterPosition()));
                    shadeView.setVisibility(View.GONE);
                    itemView.setScaleX(1.05f);
                    itemView.setScaleY(1.05f);
                } else {
                    shadeView.setVisibility(View.VISIBLE);
                    itemView.setScaleX(1.0f);
                    itemView.setScaleY(1.0f);
                    mListener.onChannelDeselected();

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
