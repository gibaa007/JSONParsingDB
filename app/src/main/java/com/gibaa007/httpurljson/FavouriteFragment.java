package com.gibaa007.httpurljson;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gibaa007 on 15/3/16.
 */
public class FavouriteFragment extends android.support.v4.app.Fragment {
    private CoordinatorLayout coordinatorLayout;
    private List<Movie> movieList = new ArrayList<Movie>();
    private RecyclerView listView;
    private CustomListAdapter adapter;
    private SearchView search;
    private SimpleDraweeView imageView;
    public Boolean fav = false;
    DatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        db = new DatabaseHandler(getActivity());
        listView = (RecyclerView) rootView.findViewById(R.id.list);
        imageView = (SimpleDraweeView) rootView.findViewById(R.id.image_view);
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id
                .coordinatorLayout);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        movieList = db.getAllLikedContacts();
        adapter = new CustomListAdapter(getActivity(), movieList, coordinatorLayout,imageView);
        listView.setAdapter(adapter);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(db!=null)
            {
                movieList = db.getAllLikedContacts();
                adapter = new CustomListAdapter(getActivity(), movieList, coordinatorLayout,imageView);
                listView.setAdapter(adapter);
            }


        }
    }
}
