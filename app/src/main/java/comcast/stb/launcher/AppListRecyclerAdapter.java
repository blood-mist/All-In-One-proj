package comcast.stb.launcher;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import comcast.stb.R;
import comcast.stb.entity.AppData;
import comcast.stb.fm.FmListActivity;
import comcast.stb.livetv.LiveTVActivity;
import comcast.stb.movielist.MovieNewActivity;
import comcast.stb.valueAddedPackages.PackageActivity;

import static comcast.stb.utils.StringData.LIVE_TV;
import static comcast.stb.utils.StringData.MOVIE;
import static comcast.stb.utils.StringData.PACKAGES;
import static comcast.stb.utils.StringData.RADIO_SERVICE;


/**
 * Created by Mitab on 11/10/2016.
 */

public class AppListRecyclerAdapter extends RecyclerView.Adapter<AppListRecyclerAdapter.ViewHolder> {
    private ArrayList<AppData> appList;
    Context mContext;
    private int focusedItem = 0;
    int tryFocusItem;
    RecyclerView appRecyclerView;

    public AppListRecyclerAdapter(Context context, ArrayList<AppData> appList, RecyclerView appRecyclerView) {
        this.appList = appList;
        this.mContext=context;
        this.appRecyclerView=appRecyclerView;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item_layout, null);


        return new ViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        // Handle key up and key down and attempt to move selection
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
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
        });
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
    public void onBindViewHolder(ViewHolder holder, final int position) {

//        holder.itemView.setSelected(tryFocusItem==position);

        AppData appData = appList.get(position);
        holder.apptitle.setText(appData.getAppName());
        holder.appImage.setImageDrawable(appData.getAppImage());
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView appImage;
        private TextView apptitle;
        private LinearLayout itemLayout;
        public ViewHolder(final View itemView) {
            super(itemView);
            appImage=itemView.findViewById(R.id.img_appicon);
            apptitle =  itemView.findViewById( R.id.txt_appName);
            itemLayout=itemView.findViewById(R.id.item_layout);
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppData clickedAppData=appList.get(getAdapterPosition());
                    switch(clickedAppData.getAppName()){
                        case LIVE_TV:
                            Intent liveIntent=new Intent(mContext,LiveTVActivity.class);
                            mContext.startActivity(liveIntent);
                            break;
                        case MOVIE:
                            Intent movieIntent=new Intent(mContext, MovieNewActivity.class);
                            mContext.startActivity(movieIntent);
                            break;
                        case RADIO_SERVICE:
                            Intent radioIntent=new Intent(mContext, FmListActivity.class);
                            mContext.startActivity(radioIntent);
                            break;
                        case "settings":
                            ((LauncherModifiedActivity) mContext).startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                            break;

                        case PACKAGES:
                            Intent packageIntent=new Intent(mContext, PackageActivity.class);
                            mContext.startActivity(packageIntent);
                            break;
                    }
                }
            });


        }
    }
}
