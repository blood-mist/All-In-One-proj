package comcast.stb.valueAddedPackages;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


import comcast.stb.R;
import comcast.stb.entity.PackagesInfo;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class PackageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<PackagesInfo> modelList;

    private OnItemClickListener mItemClickListener;


    public PackageListAdapter(Context context, ArrayList<PackagesInfo> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<PackagesInfo> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_packages, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final PackagesInfo model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.itemTxtTitle.setText(model.getPackageName());
            genericViewHolder.itemPrice.setText("$"+model.getPackagePrice());

        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private PackagesInfo getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, PackagesInfo model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTxtTitle;
        private TextView itemPrice;
        private RelativeLayout itemLayout;


        // @BindView(R.id.img_user)
        // ImageView imgUser;
        // @BindView(R.id.item_txt_title)
        // TextView itemTxtTitle;
        // @BindView(R.id.item_txt_message)
        // TextView itemPrice;
        // @BindView(R.id.radio_list)
        // RadioButton itemPrice;
        // @BindView(R.id.check_list)
        // CheckBox itemCheckList;
        public ViewHolder(final View itemView) {
            super(itemView);

            // ButterKnife.bind(this, itemView);

            this.itemTxtTitle = itemView.findViewById(R.id.txt_package_name);
            this.itemPrice = itemView.findViewById(R.id.txt_package_price);
            this.itemLayout=itemView.findViewById(R.id.layout_package);

            itemLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b){
                        itemTxtTitle.setSelected(true);
                    }else{
                        itemTxtTitle.setSelected(false);
                    }
                }
            });


            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), getItem(getAdapterPosition()));


                }
            });

        }
    }

}

