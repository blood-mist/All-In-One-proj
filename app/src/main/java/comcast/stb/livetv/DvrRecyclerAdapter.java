package comcast.stb.livetv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import comcast.stb.R;
import comcast.stb.entity.DvrResponse;
import comcast.stb.entity.EventItem;

/**
 * Created by blood-mist on 3/24/18.
 */

class DvrRecyclerAdapter extends RecyclerView.Adapter<DvrRecyclerAdapter.ViewHolder> {
    private final DvrItemClickListener dvrItemClickListener;
    private List<DvrResponse> programList;

    Context mContext;

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    private int selectedPos = -1;
    private int focusedItem = 0;
    int tryFocusItem;

    public DvrRecyclerAdapter(Context context, List<DvrResponse> programList,DvrItemClickListener dvrItemClickListener) {
        this.programList = programList;
        this.mContext = context;
        this.dvrItemClickListener = dvrItemClickListener;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_epg_programs, parent,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public void onBindViewHolder(DvrRecyclerAdapter.ViewHolder holder, final int position) {

//        holder.itemView.setSelected(tryFocusItem==position);

        DvrResponse eventItem = programList.get(position);
        holder.programName.setText(eventItem.getUrl());
        holder.duration.setText(eventItem.getStartDate()+"-"+eventItem.getStartTime());

    }

    @Override
    public int getItemCount() {
        return programList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView programName;
        private TextView duration;

        public ViewHolder(final View itemView) {
            super(itemView);
            programName = itemView.findViewById(R.id.txt_cat_title);
            duration=itemView.findViewById(R.id.txt_timeDuration);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dvrItemClickListener.onDvrItemClick(programList.get(getAdapterPosition()));

                }
            });

        }
    }
    interface DvrItemClickListener{
        void onDvrItemClick(DvrResponse dvrResponse);
    }
}
