package comcast.stb.livetv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import comcast.stb.R;

/**
 * Created by ACER on 3/22/2018.
 */

class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    private ArrayList<Calendar> calendarArrayList;
    private OnDayClickListener listener;
    private Context mContext;
    private int focusedItem = 0;
    int tryFocusItem;

    public int getSelectedChannel() {
        return selectedChannel;
    }

    public void setSelectedChannel(int selectedChannelPos) {
        this.selectedChannel = selectedChannelPos;
    }

    private int selectedChannel = -1;

    DateAdapter(Context context, ArrayList<Calendar> calendarArrayList, OnDayClickListener listener) {
        this.calendarArrayList = calendarArrayList;
        this.mContext = context;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_text, parent,false);


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
        holder.days.setText(new SimpleDateFormat("E, MMM d", Locale.US).format(calendar.getTime()));

    }

    @Override
    public int getItemCount() {
        return calendarArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView days;
        private LinearLayout parentLayout;

        public ViewHolder(final View itemView) {
            super(itemView);
            days = itemView.findViewById(R.id.txt_day_name);
            parentLayout=itemView.findViewById(R.id.layout_epg);
            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDayClicked(getAdapterPosition());
                }
            });
        }
    }

    public interface OnDayClickListener {
        void onDayClicked(int position);
    }
}