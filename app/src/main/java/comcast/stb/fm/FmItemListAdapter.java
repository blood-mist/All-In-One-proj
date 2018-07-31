package comcast.stb.fm;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import comcast.stb.R;
import comcast.stb.entity.FmsItem;
import comcast.stb.entity.MoviesItem;


public class FmItemListAdapter extends RecyclerView.Adapter<FmItemListAdapter.SingleItemRowHolder> {

    private ArrayList<FmsItem> itemsList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private OnItemSelectListener mItemSelectedListener;

    public FmItemListAdapter(Context context, ArrayList<FmsItem> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_item_google_play, viewGroup, false);

        return new SingleItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        FmsItem singleItem = itemsList.get(i);

        holder.itemCardTxtTitle.setText(singleItem.getName());
        Picasso.with(mContext)
                .load(singleItem.getImage())
                .resize(150,200)
                .placeholder(R.drawable.placeholder)
                .into(holder.itemCardImg);

       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnItemFocusedListener(final OnItemSelectListener mItemSelectedListener){
        this.mItemSelectedListener=mItemSelectedListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int itemPosition, FmsItem model);


    }
    public interface OnItemSelectListener{
        void onItemSelected(View view, int position);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemCardTxtTitle;

        protected ImageView itemCardImg;
        private RelativeLayout itemLayout;


        public SingleItemRowHolder(View view) {
            super(view);

            this.itemCardTxtTitle = view.findViewById(R.id.item_card_txt_title);
            this.itemCardImg = view.findViewById(R.id.item_card_img);
            itemLayout=view.findViewById(R.id.google_item_layout);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int devicewidth = displaymetrics.widthPixels /8;
            int deviceheight = displaymetrics.heightPixels / 3;
            view.getLayoutParams().width=devicewidth;
            itemLayout.getLayoutParams().height=deviceheight;


            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    mItemClickListener.onItemClick(v, getAdapterPosition(), itemsList.get(getAdapterPosition()));

                }
            });

            itemLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b){
                        itemCardTxtTitle.setSelected(true);
                        itemLayout.setScaleX(1.01f);
                        itemLayout.setScaleY(1.02f);
                        mItemSelectedListener.onItemSelected(view,getAdapterPosition());
                    }else{
                        itemCardTxtTitle.setSelected(false);
                        itemLayout.setScaleX(1.0f);
                        itemLayout.setScaleY(1.0f);
                    }
                }
            });


        }

    }

}
