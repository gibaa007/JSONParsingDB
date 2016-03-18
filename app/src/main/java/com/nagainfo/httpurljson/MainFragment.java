package com.nagainfo.httpurljson;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

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
public class MainFragment extends android.support.v4.app.Fragment {
    private CoordinatorLayout coordinatorLayout;
    private List<Movie> movieList = new ArrayList<Movie>();
    private RecyclerView listView;
    private CustomListAdapter adapter;
    LinearLayout linlaHeaderProgress;
    private SearchView search;
    public Boolean fav = false;
    DatabaseHandler db;
    private SimpleDraweeView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        db = new DatabaseHandler(getActivity());
        listView = (RecyclerView) rootView.findViewById(R.id.list);
        imageView = (SimpleDraweeView) rootView.findViewById(R.id.image_view);
        linlaHeaderProgress = (LinearLayout) rootView.findViewById(R.id.linlaHeaderProgress);
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id
                .coordinatorLayout);
//        search = (SearchView) findViewById(R.id.searchView1);
//        search.setQueryHint("SearchView");
//
//        //*** setOnQueryTextFocusChangeListener ***
//        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//
//                Toast.makeText(getBaseContext(), String.valueOf(hasFocus),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //*** setOnQueryTextListener ***
//        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // TODO Auto-generated method stub
//
//                Toast.makeText(getBaseContext(), query,
//                        Toast.LENGTH_SHORT).show();
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // TODO Auto-generated method stub
//
//                Toast.makeText(getBaseContext(), newText,
//                        Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        AsyncTaskRunner runner = new AsyncTaskRunner();
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String url = "http://api.androidhive.info/json/movies.json";
        if (db.getContactsCount() == 0) {
            if (GlobalMethods.isNetworkAvailable(getActivity()))
                runner.execute(url);
            else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onResume();
                            }
                        });

                // Changing message text color
                snackbar.setActionTextColor(Color.RED);
                // Changing action button text color
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.GREEN);
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.BLUE);

                snackbar.show();
            }
        } else
            movieList = db.getAllContacts();
        adapter = new CustomListAdapter(getActivity(), movieList, coordinatorLayout,imageView);
        listView.setAdapter(adapter);

    }


    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING FEEDS
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            URL url = null;
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream inputStream = null;
            try {
                inputStream = connection.getInputStream();
            } catch (IOException exception) {
                inputStream = connection.getErrorStream();
            }
            StringBuilder result = new StringBuilder("");
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            try {
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject ob = jsonArray.getJSONObject(i);

//                Movie movie = new Movie();
//                movie.setTitle(ob.getString("title"));
//                movie.setThumbnailUrl(ob.getString("image"));
//                movie.setRating(((Number) ob.get("rating"))
//                        .doubleValue());
//                movie.setYear(ob.getInt("releaseYear"));
//
//                // adding movie to movies array
//                movieList.add(movie);
                    db.addContact(new Movie(ob.getString("title"), ob.getString("image"), ob.getInt("releaseYear"), ((Number) ob.get("rating")).doubleValue(), fav));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.setMovieItems(db.getAllContacts());
            adapter.notifyDataSetChanged();
            linlaHeaderProgress.setVisibility(View.GONE);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (db != null) {
                movieList = db.getAllContacts();
                adapter = new CustomListAdapter(getActivity(), movieList, coordinatorLayout,imageView);
                listView.setAdapter(adapter);
            }


        }
    }
}
