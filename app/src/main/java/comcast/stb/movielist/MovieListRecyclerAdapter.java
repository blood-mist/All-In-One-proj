package comcast.stb.movielist;

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
import comcast.stb.entity.MoviesItem;

/**
 * Created by blood-mist on 2/2/18.
 */

public class MovieListRecyclerAdapter  extends  RecyclerView.Adapter<MovieListRecyclerAdapter.ViewHolder> {
    private ArrayList<MoviesItem> moviesArrayList;
    private OnMovieListInteraction movieInteraction;
    Context mContext;
    private int focusedItem = 0;
    int tryFocusItem;
    public MovieListRecyclerAdapter(Context context, ArrayList<MoviesItem> moviesArrayList) {
        this.moviesArrayList = moviesArrayList;
        this.mContext=context;
        this.movieInteraction =(OnMovieListInteraction)context;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel, parent,false);


        return new ViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        // Handle key up and key down and attempt to move selection
       /* recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                recyclerView.requestFocus();
                Toast.makeText(mContext, "onclicked", Toast.LENGTH_SHORT).show();
                // Return false if scrolled to the bounds and allow focus to move off the list

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_U) {
                        return tryMoveSelection(lm, 1);
                    } else if (keyCode == KeyEvent.KEYCODE_D) {
                        return tryMoveSelection(lm, -1);
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                        Toast.makeText(mContext, "Inside", Toast.LENGTH_SHORT).show();
                    }

                }


                return false;
            }
        });*/
    }


    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
        tryFocusItem = focusedItem + direction;


        // If still within valid bounds, move the selection, notify to redraw, and scroll
        if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
            notifyItemChanged(focusedItem);
            focusedItem = tryFocusItem;
            notifyItemChanged(focusedItem);
            lm.scrollToPosition(focusedItem);

            return true;
        }

        return false;
    }







    @Override
    public void onBindViewHolder(ViewHolder holder,  int position) {
        //bind MovieItemView here
        final MoviesItem movie= moviesArrayList.get(position);
        holder.channelTitle.setText(movie.getMovieName());
        Picasso.with(mContext)
                .load(movie.getMoviePicture())
                .resize(250,250)
                .placeholder(R.drawable.placeholder)
                .into(holder.channelImage);
    }

    @Override
    public int getItemCount() {
        return moviesArrayList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        private TextView channelTitle;
        private LinearLayout itemLayout;
        private ImageView channelImage;
        public ViewHolder(final View itemView) {
            super(itemView);
            channelTitle = itemView.findViewById(R.id.txt_channelname);
            channelImage=itemView.findViewById(R.id.img_channel);
            itemLayout = itemView.findViewById(R.id.channel_item_layout);
            itemLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b){
                        movieInteraction.onMovieSelected(moviesArrayList.get(getAdapterPosition()));
                    }else{
                        movieInteraction.onMovieDeselected();
                    }
                }
            });
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                            movieInteraction.onMovieClicked( moviesArrayList.get(getAdapterPosition()));

                        }

                }
            );



        }
    }
    public interface OnMovieListInteraction{
        void onMovieClicked(MoviesItem movie);
        void onMovieSelected(MoviesItem movie);
        void onMovieDeselected();
    }
}
