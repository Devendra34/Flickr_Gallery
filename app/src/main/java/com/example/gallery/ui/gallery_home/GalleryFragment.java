package com.example.gallery.ui.gallery_home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.adapter.PhotoAdapter;
import com.example.gallery.adapter.PhotoPageListAdapter;

public class GalleryFragment extends Fragment {

    private static final String TAG = "debug";
    private GalleryViewModel galleryViewModel;
    private PhotoAdapter adapter;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = root.findViewById(R.id.home_gallery_rv);

        observe();
        return root;
    }

    private void observe(){
        galleryViewModel.setPhotoRepository(getContext());
        final PhotoPageListAdapter photoPageListAdapter = new PhotoPageListAdapter();
        photoPageListAdapter.setContext(getContext());
        galleryViewModel.getPhotoesNew().observe(getViewLifecycleOwner(),photoPageListAdapter::submitList);
        galleryViewModel.getNetworkState().observe(getViewLifecycleOwner(), networkState -> {
            photoPageListAdapter.setNetworkState(networkState);
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(photoPageListAdapter);
    }
}