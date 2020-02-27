package com.example.gallery.view_holders;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;

public class ImageViewHolder extends RecyclerView.ViewHolder{

    private static final String TAG = "ImageViewHolder";
    public ImageView imageView;
    public TextView titleTV;
    public LinearLayout linearLayout;
    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.img);
        titleTV = itemView.findViewById(R.id.img_title);
        linearLayout = itemView.findViewById(R.id.root);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
            }
        });
    }
}
