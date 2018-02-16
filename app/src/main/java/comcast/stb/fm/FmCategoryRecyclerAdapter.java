package comcast.stb.fm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import comcast.stb.R;
import comcast.stb.entity.FmCategory;
import comcast.stb.entity.FmsItem;


public class FmCategoryRecyclerAdapter extends RecyclerView.Adapter<FmCategoryRecyclerAdapter.ViewHolder> {
    private ArrayList<FmCategory> fmCategoryArrayList;
    private OnCategoryListInteractionListener mListener;

    Context mContext;

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    private int selectedPos = -1;

    public FmCategoryRecyclerAdapter(Context context, ArrayList<FmCategory> fmCategoryArrayList, FmCategoryRecyclerAdapter.OnCategoryListInteractionListener mListener) {
        this.fmCategoryArrayList = fmCategoryArrayList;
        this.mContext = context;

        this.mListener = mListener;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_category, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public void onBindViewHolder(FmCategoryRecyclerAdapter.ViewHolder holder, final int position) {

//        holder.itemView.setSelected(tryFocusItem==position);

        FmCategory fmCategory = fmCategoryArrayList.get(position);
        holder.categoryOrChannelName.setText(fmCategory.getCategoryName());
        if (position == getSelectedPos()) {
            holder.itemLayout.setSelected(true);
        } else {
            holder.itemLayout.setSelected(false);
        }

    }

    @Override
    public int getItemCount() {
        return fmCategoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryOrChannelName;
        private LinearLayout itemLayout;

        public ViewHolder(final View itemView) {
            super(itemView);
            categoryOrChannelName = itemView.findViewById(R.id.txt_cat_title);
            itemLayout = itemView.findViewById(R.id.channel_category_layout);

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        Log.d("current_position",getAdapterPosition()+"");
                        setSelectedPos(getAdapterPosition());
                        List<FmsItem> fmsItemArrayList=  fmCategoryArrayList.get(getAdapterPosition()).getFms();
                        onCategoryClicked(fmsItemArrayList);
                    }
                }
            });

        }
    }

    private void onCategoryClicked(List<FmsItem> fms) {
        mListener.onCategoryListClickInteraction(fms);
    }

    public interface OnCategoryListInteractionListener {
        void onCategoryListClickInteraction(List<FmsItem> fmsList);
    }
}
