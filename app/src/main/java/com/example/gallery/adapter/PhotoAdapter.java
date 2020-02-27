package com.example.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.gallery.OpenedImage;
import com.example.gallery.R;
import com.example.gallery.model.Photo;
import com.example.gallery.view_holders.ImageViewHolder;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private Context context;
    private List<Photo> photos;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.img_cell;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(context).inflate(viewType,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final String url = photos.get(position).getUrlS();
        final String title = photos.get(position).getTitle();
        holder.titleTV.setText(title);
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OpenedImage.class);
                intent.putExtra("url",url);
                intent.putExtra("title",title);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }
}
