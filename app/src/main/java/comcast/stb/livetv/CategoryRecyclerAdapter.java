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
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_channel_category, parent,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
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
//        if (position == getSelectedPos()) {
//            holder.itemLayout.setSelected(true);
//        } else {
//            holder.itemLayout.setSelected(false);
//        }

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

            itemLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        setSelectedPos(getAdapterPosition());

                        onCategoryClicked(channelCategoryList.get(getAdapterPosition()));
                    }
                }
            });

        }
    }

    private void onCategoryClicked(ChannelCategory category) {
        mListener.onCategoryListClickInteraction(category);
    }

    public interface OnCategoryListInteractionListener {
        void onCategoryListClickInteraction(ChannelCategory category);
    }
}
