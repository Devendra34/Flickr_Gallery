package com.example.gallery.ui.gallery_home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.adapter.ImageAdapter;
import com.example.gallery.model.Photos;

public class GalleryFragment extends Fragment {

    private static final String TAG = "debug";
    private GalleryViewModel galleryViewModel;
    private ImageAdapter adapter;
    private RecyclerView recyclerView;
    private boolean isRecInit = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = root.findViewById(R.id.home_gallery_rv);
        recyclerView.setVisibility(View.GONE);

        galleryViewModel.getPhotos().observe(getViewLifecycleOwner(), new Observer<Photos>() {
            @Override
            public void onChanged(Photos photos) {
                if (!isRecInit) {
                    initRecyclerView();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return root;
    }

    private void initRecyclerView() {
        adapter = new ImageAdapter(getContext(),galleryViewModel.getPhotos().getValue().getPhoto());
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(adapter);
        isRecInit = true;
    }
}