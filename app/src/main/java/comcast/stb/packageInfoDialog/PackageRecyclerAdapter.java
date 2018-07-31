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
import comcast.stb.entity.ChannelsItem;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_packages, parent,false);
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

        private LinearLayout itemLayout;
        private ImageView channelImage;
        public ViewHolder(final View itemView) {
            super(itemView);
            channelImage = itemView.findViewById(R.id.img_channel);
            itemLayout = itemView.findViewById(R.id.channel_item_layout);

        }


        public void populateItemsForChannel() {
            final ChannelsItem pckgItem= (ChannelsItem) pckgList.get(getAdapterPosition());
            Picasso.with(mContext)
                    .load(pckgItem.getChannelLogo())
                    .resize(60,60)
                    .placeholder(R.drawable.placeholder)
                    .into(channelImage);
        }
        public void populateItemsForMovies() {
            final MoviesItem pckgItem= (MoviesItem) pckgList.get(getAdapterPosition());
            Picasso.with(mContext)
                    .load(pckgItem.getMoviePicture())
                    .resize(60,60)
                    .placeholder(R.drawable.placeholder)
                    .into(channelImage);
        }
    }
}
