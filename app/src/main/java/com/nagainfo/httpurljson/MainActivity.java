package com.nagainfo.httpurljson;

import android.app.SearchManager;
import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSuggestionsAdapter(new SearchSuggestionsAdapter(this));
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                Toast.makeText(MainActivity.this, "" + mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }
        });
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onPrepareOptionsMenu(menu) | true;
    }

    // You must implements your logic to get data using OrmLite


    public static class SearchSuggestionsAdapter extends SimpleCursorAdapter {
        private static final String[] mFields = {"_id", "result"};
        private static final String[] mVisible = {"result"};
        private static final int[] mViewIds = {android.R.id.text1};
        DatabaseHandler db = new DatabaseHandler(mContext);
        List<Movie> movieList = new ArrayList<Movie>();

        public SearchSuggestionsAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1, null, mVisible, mViewIds, 0);
        }

        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
            movieList = db.getAllContacts();
            return new SuggestionsCursor(movieList, constraint);
        }

        private static class SuggestionsCursor extends AbstractCursor {

            private ArrayList<String> mResults;

            public SuggestionsCursor(List<Movie> movieList, CharSequence constraint) {
                mResults = new ArrayList<String>(movieList.size());
                for (int i = 0; i < movieList.size(); i++) {
                    mResults.add(movieList.get(i).getTitle());
                }

                if (!TextUtils.isEmpty(constraint)) {
                    String constraintString = constraint.toString().toLowerCase(Locale.ROOT);
                    Iterator<String> iter = mResults.iterator();
                    while (iter.hasNext()) {
                        if (!iter.next().toLowerCase(Locale.ROOT).startsWith(constraintString)) {
                            iter.remove();
                        }
                    }
                }
            }

            @Override
            public int getCount() {
                return mResults.size();
            }

            @Override
            public String[] getColumnNames() {
                return mFields;
            }

            @Override
            public long getLong(int column) {
                if (column == 0) {
                    return mPos;
                }
                throw new UnsupportedOperationException("unimplemented");
            }

            @Override
            public String getString(int column) {
                if (column == 1) {
                    return mResults.get(mPos);
                }
                throw new UnsupportedOperationException("unimplemented");
            }

            @Override
            public short getShort(int column) {
                throw new UnsupportedOperationException("unimplemented");
            }

            @Override
            public int getInt(int column) {
                throw new UnsupportedOperationException("unimplemented");
            }

            @Override
            public float getFloat(int column) {
                throw new UnsupportedOperationException("unimplemented");
            }

            @Override
            public double getDouble(int column) {
                throw new UnsupportedOperationException("unimplemented");
            }

            @Override
            public boolean isNull(int column) {
                return false;
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), "MUSIC");
        adapter.addFragment(new FavouriteFragment(), "FAVOURITES");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}