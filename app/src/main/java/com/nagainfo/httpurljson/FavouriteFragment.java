package com.nagainfo.httpurljson;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nagainfo on 15/3/16.
 */
public class FavouriteFragment extends android.support.v4.app.Fragment {
    private CoordinatorLayout coordinatorLayout;
    private List<Movie> movieList = new ArrayList<Movie>();
    private RecyclerView listView;
    private CustomListAdapter adapter;
    private SearchView search;
    public Boolean fav = false;
    DatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        db = new DatabaseHandler(getActivity());
        listView = (RecyclerView) rootView.findViewById(R.id.list);
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id
                .coordinatorLayout);
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        movieList = db.getAllLikedContacts();
        adapter = new CustomListAdapter(getActivity(), movieList, coordinatorLayout);
        listView.setAdapter(adapter);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(db!=null)
            {
                movieList = db.getAllLikedContacts();
                adapter = new CustomListAdapter(getActivity(), movieList, coordinatorLayout);
                listView.setAdapter(adapter);
            }


        }
    }
}
