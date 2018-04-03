package comcast.stb.movielist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import comcast.stb.R;
import comcast.stb.entity.MovieCategory;


public class MovieCategoryRecyclerAdapter extends  RecyclerView.Adapter<MovieCategoryRecyclerAdapter.ViewHolder> {
    private ArrayList<MovieCategory> categoryArrayList;
    private OnMovieCategoryInteraction movieCategoryInteraction;
    Context mContext;
    private int focusedItem = 0;
    int tryFocusItem;
    private int selectedPos = -1;
    Random random=new Random();

    public MovieCategoryRecyclerAdapter(Context context, ArrayList<MovieCategory> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
        this.mContext=context;
        this.movieCategoryInteraction=(OnMovieCategoryInteraction)context;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_channel_category, parent,false);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        //bind MovieItemView here
        final MovieCategory movieCategory = categoryArrayList.get(position);
        holder.categoryName.setText(movieCategory.getCategoryTitle());
        if (position == getSelectedPos()) {
            holder.itemLayout.setSelected(true);
        } else {
            holder.itemLayout.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        private TextView categoryName;
        private LinearLayout itemLayout;

        public ViewHolder(final View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.txt_cat_title);
            itemLayout = itemView.findViewById(R.id.channel_category_layout);

            itemLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        setSelectedPos(getAdapterPosition());
                        movieCategoryInteraction.onCategoryClicked(categoryArrayList.get(getAdapterPosition()));
                    }
                }
            });

        }
    }
    public interface OnMovieCategoryInteraction{
        void onCategoryClicked(MovieCategory movieCategory);
    }
}
