package com.example.gallery.ui.gallery_home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.adapter.PhotoPageListAdapter;
import com.example.gallery.model.NetworkState;
import com.example.gallery.model.QueryLiveData;
import com.google.android.material.snackbar.Snackbar;

public class GalleryFragment extends Fragment {

    private static final String TAG = "debug";
    private GalleryViewModel galleryViewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Snackbar snackbar;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = root.findViewById(R.id.home_gallery_rv);
        progressBar = root.findViewById(R.id.gallery_fragment_progressBar);

        observe();
        return root;
    }

    private void observe(){
        QueryLiveData.liveData.setValue("cat");
        galleryViewModel.setPhotoRepository(getContext(), QueryLiveData.liveData);
        final PhotoPageListAdapter photoPageListAdapter = new PhotoPageListAdapter();
        photoPageListAdapter.setContext(getContext());
        galleryViewModel.getPhotoesNew().observe(getViewLifecycleOwner(),photoPageListAdapter::submitList);
        galleryViewModel.getNetworkState().observe(getViewLifecycleOwner(), networkState -> {
            if (networkState.getStatus() == NetworkState.Status.RUNNING) {
                progressBar.setVisibility(View.VISIBLE);
            } else if (networkState.getStatus() == NetworkState.Status.FAILED){
                progressBar.setVisibility(View.GONE);
                makeSnackBar("Failed to Load");
            } else if (networkState.getStatus() == NetworkState.Status.SUCCESS){
                progressBar.setVisibility(View.GONE);
                if(snackbar != null) {
                    snackbar.dismiss();
                }
            }
            photoPageListAdapter.setNetworkState(networkState);
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(photoPageListAdapter);
    }

    private void makeSnackBar(String message) {
        snackbar = Snackbar.make(root, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", view -> {
                    observe();
                    Toast.makeText(getContext(), "Retrying ...", Toast.LENGTH_SHORT).show();
                });
        snackbar.show();
    }
}