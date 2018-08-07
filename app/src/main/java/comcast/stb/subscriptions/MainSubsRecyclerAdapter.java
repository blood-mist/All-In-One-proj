package comcast.stb.subscriptions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import comcast.stb.R;
import comcast.stb.entity.SubsItem;

/**
 * Created by blood-mist on 2/2/18.
 */

public class MainSubsRecyclerAdapter extends RecyclerView.Adapter<MainSubsRecyclerAdapter.ViewHolder> {
    private ArrayList<SubsItem> subsArrayList;
    Context mContext;
    private int focusedItem = 0;
    int tryFocusItem;

    public MainSubsRecyclerAdapter(Context context, ArrayList<SubsItem> subsArrayList) {
        this.subsArrayList = subsArrayList;
        this.mContext = context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_subscription, parent, false);


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
        final SubsItem subsItem = subsArrayList.get(position);
        holder.subsName.setText(subsItem.getSubscribedId() + " " + subsItem.getSubscribedType());
        holder.subsPrice.setText("Recent:" + getRecentUpdatedDate(subsItem));
        holder.purchaseDate.setText("Purchased Date:" + getCreatedDate(subsItem));
        holder.expiryDate.setText("Expiry Date:"+getExpiryDate(subsItem));
    }

    private String getExpiryDate(SubsItem subsItem) {
        if (subsItem.getExpiry() == null || subsItem.getExpiry().equals("")) {
            return "N/A";
        } else {
            return subsItem.getExpiry();
        }
    }

    private String getCreatedDate(SubsItem subsItem) {
        if (subsItem.getCreatedAt() == null || subsItem.getCreatedAt().equals("")) {
            return "N/A";
        } else {
            return subsItem.getCreatedAt();
        }
    }

    private String getRecentUpdatedDate(SubsItem subsItem) {
        if (subsItem.getUpdatedAt() == null || subsItem.getUpdatedAt().equals("")) {
            return "N/A";
        } else {
            return subsItem.getUpdatedAt();
        }
    }


    @Override
    public int getItemCount() {
        return subsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView subsName;
        private TextView subsPrice;
        private TextView purchaseDate;
        private TextView expiryDate;

        public ViewHolder(final View itemView) {
            super(itemView);
            subsName = itemView.findViewById(R.id.txt_subs_name);
            subsPrice = itemView.findViewById(R.id.txt_subs_price);
            purchaseDate = itemView.findViewById(R.id.txt_purchaseDate);
            expiryDate = itemView.findViewById(R.id.txt_expiryDate);
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        subsName.setSelected(true);
                    } else {
                        subsName.setSelected(false);
                    }
                }
            });


        }
    }

}
