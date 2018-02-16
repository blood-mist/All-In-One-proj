package comcast.stb.launcher;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import comcast.stb.R;
import comcast.stb.entity.PackagesInfo;

/**
 * Created by blood-mist on 2/2/18.
 */

public class MainPckgRecyclerAdapter extends RecyclerView.Adapter<MainPckgRecyclerAdapter.ViewHolder> {
    private ArrayList<PackagesInfo> packageList;
    private OnPackageListInteraction packageInteractionListener;
    Context mContext;
    private int focusedItem = 0;
    int tryFocusItem;
    String packageType;

    public MainPckgRecyclerAdapter(Context context, ArrayList<PackagesInfo> packageList, String packageType) {
        this.packageList = packageList;
        this.mContext = context;
        this.packageInteractionListener = (OnPackageListInteraction) context;
        this.packageType = packageType;

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
        final PackagesInfo packagesInfo = packageList.get(position);
        holder.packageName.setText(packagesInfo.getPackageName());
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView packageName;

        public ViewHolder(final View itemView) {
            super(itemView);
            packageName = itemView.findViewById(R.id.txt_cat_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    packageInteractionListener.onPackageInfoClicked(packageList.get(getAdapterPosition()), packageType);
                }
            });


        }
    }

    public interface OnPackageListInteraction {
        void onPackageInfoClicked(PackagesInfo packagesInfo, String packageType);

        void onPackageBuyClicked(PackagesInfo packagesInfo, String packageType);
    }
}
