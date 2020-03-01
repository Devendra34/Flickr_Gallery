package com.example.gallery.view_holders;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;

public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;
    public ProgressBar progressBar;
    public NetworkStateItemViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.error_msg);
        progressBar = itemView.findViewById(R.id.progress_bar);
    }
}
