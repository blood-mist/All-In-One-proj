package comcast.stb.subscriptions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import comcast.stb.R;
import comcast.stb.entity.OrderItem;

/**
 * Created by blood-mist on 2/2/18.
 */

public class MainOrderRecyclerAdapter extends RecyclerView.Adapter<MainOrderRecyclerAdapter.ViewHolder> {
    private ArrayList<OrderItem> orderItemArrayList;
    Context mContext;
    private int focusedItem = 0;
    int tryFocusItem;

    public MainOrderRecyclerAdapter(Context context, ArrayList<OrderItem> orderItemArrayList) {
        this.orderItemArrayList = orderItemArrayList;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_subscription, parent,false);


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
        final OrderItem orderItem = orderItemArrayList.get(position);
        holder.orderName.setText(orderItem.getName());
        holder.orderPrice.setText("Price:"+orderItem.getPrice());
        holder.orderPurchase.setText("Purchased Date:"+getCreatedDate(orderItem));
        holder.orderExpiry.setText("Expiry:"+getExpiryDate(orderItem));
    }

    private String getExpiryDate(OrderItem orderItem) {
        if(orderItem.getExpiry()==null||orderItem.getExpiry().equals("")){
            return "N/A";
        }else{
            return  orderItem.getExpiry();
        }
    }

    private String getCreatedDate(OrderItem orderItem) {
        if(orderItem.getCreatedAt()==null||orderItem.getCreatedAt().equals("")){
            return "N/A";
        }else{
            return  orderItem.getCreatedAt();
        }
    }

    @Override
    public int getItemCount() {
        return orderItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderName;
        private TextView orderPrice;
        private TextView orderExpiry;
        private TextView orderPurchase;

        public ViewHolder(final View itemView) {
            super(itemView);
            orderName = itemView.findViewById(R.id.txt_subs_name);
            orderPrice=itemView.findViewById(R.id.txt_subs_price);
            orderPurchase=itemView.findViewById(R.id.txt_purchaseDate);
            orderExpiry=itemView.findViewById( R.id.txt_expiryDate);


        }
    }

}
