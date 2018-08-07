package comcast.stb.fm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import android.support.v7.widget.LinearLayoutManager;


import butterknife.BindView;
import butterknife.ButterKnife;
import comcast.stb.R;
import comcast.stb.entity.FmCategory;
import comcast.stb.entity.FmsItem;
import comcast.stb.entity.MovieCategory;
import comcast.stb.entity.MoviesItem;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class FmComplexRecycler extends RecyclerView.Adapter<FmComplexRecycler.ViewHolder> {

    private Context mContext;
    private ArrayList<FmCategory> categoryArrayList;

    private OnItemClickListener mItemClickListener;

    private OnItemSelectListener mItemSelectListener;

    public FmComplexRecycler(Context context, ArrayList<FmCategory> categoryArrayList) {
        this.mContext = context;
        this.categoryArrayList = categoryArrayList;
    }

    public void updateList(ArrayList<FmCategory> modelList) {
        this.categoryArrayList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_google_play, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //Here you can fill your row view

        final FmCategory model = getItem(position);


        ArrayList<FmsItem> singleSectionItems = (ArrayList<FmsItem>) model.getFms();
        if(singleSectionItems.size()>0) {

            holder.itemTxtTitle.setText(model.getCategoryName());

            FmItemListAdapter itemListDataAdapter = new FmItemListAdapter(mContext, singleSectionItems);

            holder.recyclerViewHorizontalList.setHasFixedSize(true);
            holder.recyclerViewHorizontalList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerViewHorizontalList.setAdapter(itemListDataAdapter);


            holder.recyclerViewHorizontalList.setNestedScrollingEnabled(false);


            itemListDataAdapter.SetOnItemClickListener(new FmItemListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int itemPosition, FmsItem model) {

                    mItemClickListener.onItemClick(view, position, itemPosition, model);

                }
            });

            itemListDataAdapter.SetOnItemFocusedListener(new FmItemListAdapter.OnItemSelectListener() {
                @Override
                public void onItemSelected(View view, int position) {
                    mItemSelectListener.onItemSelect(view, holder.getAdapterPosition());
                }
            });
        }else{
            remove(model);
        }


    }
    public void remove(FmCategory category) {
        int position = categoryArrayList.indexOf(category);
        categoryArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,categoryArrayList.size());
    }
    @Override
    public int getItemCount() {

        return categoryArrayList.size();
    }


    private FmCategory getItem(int position) {
        return categoryArrayList.get(position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnItemSelectListener(final OnItemSelectListener mItemSelectListener){
        this.mItemSelectListener=mItemSelectListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int absolutePosition, int relativePosition, FmsItem model);
    }

    public interface OnItemSelectListener{
        void onItemSelect(View view,int absolutePosition);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_view_horizontal_list)
        protected RecyclerView recyclerViewHorizontalList;
        @BindView(R.id.item_txt_title)
        TextView itemTxtTitle;


        public ViewHolder(final View itemView) {
            super(itemView);

             ButterKnife.bind(this, itemView);



        }
    }


}

