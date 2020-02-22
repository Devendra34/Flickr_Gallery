package com.example.gallery.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;

public class ImageViewHolder extends RecyclerView.ViewHolder{

    public ImageView imageView;
    public TextView titleTV;
    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.img);
        titleTV = itemView.findViewById(R.id.img_title);
    }
}
