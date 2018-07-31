package comcast.stb.livetv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import comcast.stb.R;
import comcast.stb.entity.Channel;
import comcast.stb.entity.ChannelCategory;


public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {
    private ArrayList<ChannelCategory> channelCategoryList;
    private OnCategoryListInteractionListener mListener;

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

    public CategoryRecyclerAdapter(Context context, ArrayList<ChannelCategory> channelCategoryList, CategoryRecyclerAdapter.OnCategoryListInteractionListener mListener) {
        this.channelCategoryList = channelCategoryList;
        this.mContext = context;

        this.mListener = mListener;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_channel_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public void onBindViewHolder(CategoryRecyclerAdapter.ViewHolder holder, final int position) {

//        holder.itemView.setSelected(tryFocusItem==position);

        ChannelCategory channelCategory = channelCategoryList.get(position);
        holder.categoryOrChannelName.setText(channelCategory.getCategoryTitle());
        if (position == getSelectedPos()) {
            holder.itemLayout.setSelected(true);
        } else {
            holder.itemLayout.setSelected(false);
        }

    }

    @Override
    public int getItemCount() {
        return channelCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryOrChannelName;
        private LinearLayout itemLayout;

        public ViewHolder(final View itemView) {
            super(itemView);
            categoryOrChannelName = itemView.findViewById(R.id.txt_cat_title);
            itemLayout = itemView.findViewById(R.id.channel_category_layout);

            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCategoryClicked(channelCategoryList.get(getAdapterPosition()));
                }
            });

            itemLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b){
                        categoryOrChannelName.setSelected(true);
                        itemLayout.setScaleY(1.09f);
                        itemLayout.setScaleX(1.09f);
                    }else{
                        categoryOrChannelName.setSelected(false);
                        itemLayout.setScaleX(1.0f);
                        itemLayout.setScaleY(1.0f);
                    }
                }
            });

        }
    }

    private void onCategoryClicked(ChannelCategory channelCategory) {
        mListener.onCategoryListClickInteraction(channelCategory);
    }

    public interface OnCategoryListInteractionListener {
        void onCategoryListClickInteraction(ChannelCategory channelCategory);
    }
}
