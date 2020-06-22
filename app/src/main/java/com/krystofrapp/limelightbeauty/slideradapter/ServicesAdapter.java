package com.krystofrapp.limelightbeauty.slideradapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krystofrapp.limelightbeauty.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ServicesAdapter extends RecyclerView.ViewHolder {
    View mView;
    private ServicesAdapter.ClickListener mClickListener;

    public ServicesAdapter(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        //Item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        //Item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });
    }

    //Set details to recyclerview row
    public void setDetails(Context mContext, String title, String price, String image) {
        TextView mTitle = mView.findViewById(R.id.item_title);
        TextView mPrice = mView.findViewById(R.id.item_price);
        CircleImageView mImageView = mView.findViewById(R.id.item_image);
        //Set data to views
        mTitle.setText(title);
        mPrice.setText(price);
        Picasso.get().load(image).into(mImageView);
    }

    public void setOnClickListener(ServicesAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    //Interface to send callbacks
    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
