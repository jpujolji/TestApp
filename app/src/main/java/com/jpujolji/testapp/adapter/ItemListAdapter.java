package com.jpujolji.testapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpujolji.testapp.R;
import com.jpujolji.testapp.model.ItemList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.MyViewHolder> {

    private List<ItemList> mItems;
    private Context mContext;
    private ItemInterface mItemInterface;

    public ItemListAdapter(List<ItemList> items, Context context, ItemInterface itemInterface) {
        mItems = items;
        mContext = context;
        mItemInterface = itemInterface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItemList itemList = mItems.get(position);

        if (itemList.title != null && !itemList.title.isEmpty()) {
            holder.tvTitle.setText(itemList.title.replaceAll("(/r/|r/|/u/)", ""));
        } else {
            holder.tvTitle.setText("");
        }

        if (itemList.public_description != null && !itemList.public_description.isEmpty()) {
            holder.tvDescription.setText(itemList.public_description.replaceAll("(/r/|r/|/u/)", ""));
        } else {
            holder.tvDescription.setText("");
        }

        if (itemList.header_title != null && !itemList.header_title.isEmpty()) {
            holder.tvHeader.setText(itemList.header_title.replaceAll("(/r/|r/|/u/)", ""));
        } else {
            holder.tvHeader.setText("");
        }

        if (itemList.header_img != null && !itemList.header_img.isEmpty()) {
            Picasso.with(mContext).load(itemList.header_img).into(holder.ivHeader);
        } else {
            holder.ivHeader.setImageDrawable(null);
        }

        if (itemList.icon_img != null && !itemList.icon_img.isEmpty()) {
            Picasso.with(mContext).load(itemList.icon_img).into(holder.ivIcon);
        } else {
            holder.ivIcon.setImageDrawable(null);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private String getId(int position) {
        return mItems.get(position).id;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvHeader, tvTitle, tvDescription;
        ImageView ivHeader, ivIcon;

        MyViewHolder(View itemView) {
            super(itemView);

            tvHeader = (TextView) itemView.findViewById(R.id.tvHeader);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            ivHeader = (ImageView) itemView.findViewById(R.id.ivHeader);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mItemInterface.onItemClick(itemView, getId(getAdapterPosition()));
        }
    }

    public interface ItemInterface {
        void onItemClick(View view, String position);
    }
}
