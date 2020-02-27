package com.example.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.OpenedImage;
import com.example.gallery.R;
import com.example.gallery.model.Photo;
import com.example.gallery.model.NetworkState;
import com.example.gallery.view_holders.ImageViewHolder;
import com.example.gallery.view_holders.NetworkStateItemViewHolder;

public class PhotoPageListAdapter extends PagedListAdapter<Photo, RecyclerView.ViewHolder> {

    private Context context;
    private NetworkState networkState;
    private static final String TAG = "PhotoPageListAdapter";
    public PhotoPageListAdapter() {
        super(Photo.DIFF_CALLBACK);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() -1) {
            Log.d(TAG, "getItemViewType: Loading view");
            return R.layout.network_state_item;
        } else {
            return R.layout.img_cell;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == R.layout.img_cell){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_cell,parent,false);
            return new ImageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.network_state_item,parent,false);
            Log.d(TAG, "onCreateViewHolder: loading createview");
            return new NetworkStateItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == R.layout.img_cell) {
            ImageViewHolder imageViewHolder = (ImageViewHolder)holder;
            Photo photo = getItem(position);
            Glide.with(context).load(photo.getUrlS()).centerCrop().into(imageViewHolder.imageView);
            imageViewHolder.titleTV.setText(photo.getTitle());
            imageViewHolder.linearLayout.setOnClickListener(view -> {
                Intent intent = new Intent(context, OpenedImage.class);
                intent.putExtra("title",photo.getTitle());
                intent.putExtra("url",photo.getUrlS());
                context.startActivity(intent);
            });
        } else {
            NetworkStateItemViewHolder viewHolder = (NetworkStateItemViewHolder)holder;
            Log.d(TAG, "onBindViewHolder: loading bindview" + networkState.getStatus().toString());
            if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
            } else {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
            if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
                viewHolder.textView.setText(networkState.getMsg());
                viewHolder.textView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.textView.setVisibility(View.GONE);
            }
        }
    }

    private boolean hasExtraRow () {
        return  (networkState != null && networkState != NetworkState.LOADED);
    }

    public void setNetworkState(NetworkState newNetworkState) {
        Log.d(TAG, "setNetworkState: "+newNetworkState.getStatus().toString());
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        notifyDataSetChanged();
        if ((previousExtraRow != newExtraRow)) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }
}
