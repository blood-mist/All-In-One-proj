package comcast.stb.packageInfoDialog;

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
import comcast.stb.entity.Channel;
import comcast.stb.entity.MoviesItem;

import static comcast.stb.StringData.CHANNEL_PACKAGE;
import static comcast.stb.StringData.MOVIE_PACKAGE;

/**
 * Created by blood-mist on 2/8/18.
 */

public class PackageRecyclerAdapter extends  RecyclerView.Adapter<PackageRecyclerAdapter.ViewHolder> {
    Context mContext;
    private ArrayList pckgList;
    String packageType;
    public PackageRecyclerAdapter(Context context, ArrayList pckgList,String packageType) {
        this.pckgList = pckgList;
        this.mContext=context;
        this.packageType=packageType;

    }
    @Override
    public PackageRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_packages, null);
        return new PackageRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,  int position) {
        switch(packageType){
            case CHANNEL_PACKAGE:
                holder. populateItemsForChannel();
                break;
            case MOVIE_PACKAGE:
                holder.populateItemsForMovies();
        }
        //bind MovieItemView here
    }


    @Override
    public int getItemCount() {
        return pckgList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

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

            itemLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        shadeView.setVisibility(View.GONE);
                        itemView.setScaleX(1.05f);
                        itemView.setScaleY(1.05f);
                        channelTitle.setSelected(true);
                    } else {
                        shadeView.setVisibility(View.VISIBLE);
                        itemView.setScaleX(1.0f);
                        itemView.setScaleY(1.0f);
                        channelTitle.setSelected(false);
                    }
                }
            });
        }


        public void populateItemsForChannel() {
            final Channel pckgItem= (Channel) pckgList.get(getAdapterPosition());
            channelTitle.setText(pckgItem.getChannelName());
            Picasso.with(mContext)
                    .load(pckgItem.getChannelLogo())
                    .resize(70,70)
                    .placeholder(R.drawable.placeholder)
                    .into(channelImage);
        }
        public void populateItemsForMovies() {
            final MoviesItem pckgItem= (MoviesItem) pckgList.get(getAdapterPosition());
            channelTitle.setText(pckgItem.getMovieName());
            Picasso.with(mContext)
                    .load(pckgItem.getMoviePicture())
                    .resize(70,70)
                    .placeholder(R.drawable.placeholder)
                    .into(channelImage);
        }
    }
}