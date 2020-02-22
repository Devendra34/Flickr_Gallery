package com.example.gallery.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.adapter.ImageAdapter;
import com.example.gallery.model.Photo;
import com.example.gallery.model.Photos;

import java.util.List;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private ImageAdapter adapter;
    private RecyclerView recyclerView;
    private boolean isInitState = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = root.findViewById(R.id.search_recycler_view);
        SearchView searchView = root.findViewById(R.id.search_view);
        recyclerView.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchViewModel.makeQuery(s);
                return true;
            }
        });

        searchViewModel.getPhotoLiveData().observe(getViewLifecycleOwner(), new Observer<Photos>() {
            @Override
            public void onChanged(Photos photos) {
                if(!isInitState) {
                    initRecyclerView();
                } else {
                    adapter.setPhotos(photos.getPhoto());
                }
            }
        });

        return root;
    }

    private void initRecyclerView() {
        adapter = new ImageAdapter(getContext(),searchViewModel.getPhotoLiveData().getValue().getPhoto());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        isInitState = true;
    }
}