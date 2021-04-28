package com.example.comp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FavoriteAdapter favoriteAdapter;


   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recycle_view_favorite);
        layoutManager = new GridLayoutManager(getContext(), 2);

       recyclerView.setLayoutManager(layoutManager);



        recyclerView.setAdapter(favoriteAdapter);
        recyclerView.setHasFixedSize(true);

        return view;
    }
}