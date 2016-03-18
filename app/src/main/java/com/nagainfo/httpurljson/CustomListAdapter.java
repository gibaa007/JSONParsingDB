package com.nagainfo.httpurljson;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Random;

/**
 * Created by nagainfo on 11/3/16.
 */
public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.CustomViewHolder> {
    private final CoordinatorLayout coordinatorLayout;
    private final SimpleDraweeView listView;
    private Activity activity;
    private List<Movie> movieItems;
    private DatabaseHandler db;

    public CustomListAdapter(Activity activity, List<Movie> movieItems, CoordinatorLayout coordinatorLayout, SimpleDraweeView listView) {
        this.activity = activity;
        this.movieItems = movieItems;
        this.coordinatorLayout = coordinatorLayout;
        this.listView = listView;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, null);
        db = new DatabaseHandler(activity);
        listView.setImageURI(Uri.parse(movieItems.get(0).getThumbnailUrl()));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {

        Movie feedItem = movieItems.get(i);
        customViewHolder.title.setText(feedItem.getTitle());
        customViewHolder.rating.setText("Rating: " + String.valueOf(feedItem.getRating()));
        Uri uri = Uri.parse(feedItem.getThumbnailUrl());
        customViewHolder.image.setImageURI(uri);
        if (feedItem.isFav() == true)
            customViewHolder.favourite.setImageResource(R.drawable.ic_favorite);
        else
            customViewHolder.favourite.setImageResource(R.drawable.ic_non_favorite);
        customViewHolder.releaseYear.setText(String.valueOf(feedItem.getYear()));
        customViewHolder.image.setTag(customViewHolder);
        customViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                final Movie feedItem = movieItems.get(holder.getPosition());
                Intent image = new Intent(activity, ImageViewActivity.class);
                image.putExtra("image", feedItem.getThumbnailUrl());
                activity.startActivity(image);


            }
        });
        customViewHolder.card_view.setTag(customViewHolder);
        customViewHolder.card_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                final Movie feedItem = movieItems.get(holder.getPosition());
                new AlertDialog.Builder(activity)
                        .setTitle("Delete Movie")
                        .setMessage("Are you sure you want to delete this Movie?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteContact(feedItem);
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Movie Deleted", Snackbar.LENGTH_LONG);
                                setMovieItems(db.getAllContacts());
                                notifyDataSetChanged();
                                // Changing message text color
                                snackbar.setActionTextColor(Color.RED);
                                // Changing action button text color
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(Color.GREEN);
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.BLUE);

                                snackbar.show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .show();
                return false;
            }
        });
        customViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                final Movie feedItem = movieItems.get(holder.getPosition());
                listView.setImageURI(Uri.parse(feedItem.getThumbnailUrl()));
            }
        });

        customViewHolder.favourite.setTag(customViewHolder);
        customViewHolder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                final Movie feedItem = movieItems.get(holder.getPosition());
                if (!feedItem.isFav()) {
                    feedItem.setFav(true);
                    customViewHolder.favourite.setImageResource(R.drawable.ic_favorite);
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Added Favourite", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    customViewHolder.favourite.setImageResource(R.drawable.ic_non_favorite);
                                    feedItem.setFav(false);
                                    db.updateContact(feedItem);
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
                } else {
                    feedItem.setFav(false);
                    customViewHolder.favourite.setImageResource(R.drawable.ic_non_favorite);
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Favourite Removed", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    customViewHolder.favourite.setImageResource(R.drawable.ic_favorite);
                                    feedItem.setFav(true);
                                    db.updateContact(feedItem);
                                }
                            });

                    // Changing message text color
                    snackbar.setActionTextColor(Color.BLUE);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.YELLOW);
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.RED);

                    snackbar.show();
                }
                db.updateContact(feedItem);
                setMovieItems(db.getAllContacts());


            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != movieItems ? movieItems.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected SimpleDraweeView image;
        protected CardView card_view;
        protected ImageView favourite;
        protected TextView title, rating, releaseYear;

        public CustomViewHolder(View view) {
            super(view);
            this.image = (SimpleDraweeView) view.findViewById(R.id.thumbnail);
            this.card_view = (CardView) view.findViewById(R.id.card_view);
            this.favourite = (ImageView) view.findViewById(R.id.fav);
            this.title = (TextView) view.findViewById(R.id.title);
            this.rating = (TextView) view.findViewById(R.id.rating);
            this.releaseYear = (TextView) view.findViewById(R.id.releaseYear);
        }
    }

    public void setMovieItems(List<Movie> movieItems) {
        this.movieItems = movieItems;
    }
//    @Override
//    public int getCount() {
//        return movieItems.size();
//    }
//
//    @Override
//    public Object getItem(int location) {
//        return movieItems.get(location);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        if (inflater == null)
//            inflater = (LayoutInflater) activity
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if (convertView == null)
//            convertView = inflater.inflate(R.layout.item, null);
//
//        TextView title = (TextView) convertView.findViewById(R.id.title);
//        SimpleDraweeView image = (SimpleDraweeView) convertView.findViewById(R.id.thumbnail);
//        TextView rating = (TextView) convertView.findViewById(R.id.rating);
//        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
//
//        // getting movie data for the row
//        Movie m = movieItems.get(position);
//        // title
//        title.setText(m.getTitle());
//
//        // rating
//        rating.setText("Rating: " + String.valueOf(m.getRating()));
//
//        Uri uri = Uri.parse(m.getThumbnailUrl());
//        image.setImageURI(uri);
//        // release year
//        year.setText(String.valueOf(m.getYear()));
//
//        return convertView;
//    }

}
