package comcast.stb.movielist;

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
import comcast.stb.entity.MovieCategory;
import comcast.stb.entity.MoviesItem;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<ComplexRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MovieCategory> categoryArrayList;

    private OnItemClickListener mItemClickListener;

    private OnItemSelectListener mItemSelectListener;

    public ComplexRecyclerViewAdapter(Context context, ArrayList<MovieCategory> categoryArrayList) {
        this.mContext = context;
        this.categoryArrayList = categoryArrayList;
    }

    public void updateList(ArrayList<MovieCategory> modelList) {
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

        final MovieCategory model = getItem(position);


        ArrayList<MoviesItem> singleSectionItems = (ArrayList<MoviesItem>) model.getMovies();

        holder.itemTxtTitle.setText(model.getCategoryTitle());

        SingleItemListAdapter itemListDataAdapter = new SingleItemListAdapter(mContext, singleSectionItems);

        holder.recyclerViewHorizontalList.setHasFixedSize(true);
        holder.recyclerViewHorizontalList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerViewHorizontalList.setAdapter(itemListDataAdapter);


        holder.recyclerViewHorizontalList.setNestedScrollingEnabled(false);


        itemListDataAdapter.SetOnItemClickListener(new SingleItemListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int itemPosition, MoviesItem model) {

                mItemClickListener.onItemClick(view, position, itemPosition, model);

            }
        });

        itemListDataAdapter.SetOnItemFocusedListener(new SingleItemListAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelected(View view, int position) {
                mItemSelectListener.onItemSelect(view,holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {

        return categoryArrayList.size();
    }


    private MovieCategory getItem(int position) {
        return categoryArrayList.get(position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnItemSelectListener(final OnItemSelectListener mItemSelectListener){
        this.mItemSelectListener=mItemSelectListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int absolutePosition, int relativePosition, MoviesItem model);
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

