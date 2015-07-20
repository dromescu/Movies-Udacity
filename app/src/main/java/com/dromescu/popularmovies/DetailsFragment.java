package com.dromescu.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dromescu.popularmovies.data.MoviesContract;
import com.dromescu.popularmovies.models.IMDB;


import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Created by dromescu on 15.07.15.
 */
public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static String LOG_TAG = DetailsFragment.class.getSimpleName();
    private static final int DETAILS_LOADER = 0;
    static final String DETAIL_URI = "URI";
    private Uri mUri;
    public static final String KEY_MOVIE_ID = "movie_id";

    private long mMovieId;
    // final private Context mContext;

    private static final String[] DETAILS_COLUMNS = {
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry._ID,
            MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MoviesContract.MovieEntry.COLUMN_OVERVIEW,
            MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MoviesContract.MovieEntry.COLUMN_VOTE_COUNT,
            MoviesContract.MovieEntry.COLUMN_POPULARITY
    };

    View rootView;
    @Bind(R.id.details_movie_poster)
    ImageView poster;
    @Bind(R.id.details_movie_year)
    TextView release;
    @Bind(R.id.details_movie_rating)
    TextView rating;
    @Bind(R.id.details_movie_overview)
    TextView overview;
    @Bind(R.id.details_backdrop)
    ImageView backdrop;
    @Bind(R.id.popularity)
    TextView popularity;



    public static DetailsFragment newInstance(long movieId) {
        DetailsFragment detailFragment = new DetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putLong(DetailsFragment.KEY_MOVIE_ID, movieId);
        detailFragment.setArguments(bundle);

        return detailFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();

        if (arguments != null) {
            mMovieId = arguments.getLong(this.KEY_MOVIE_ID, 1);
            mUri = MoviesContract.MovieEntry.buildMoviesUri(this.mMovieId);
        }

        rootView = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this,rootView);

        return rootView;
    }
/*
    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(DetailsFragment.KEY_MOVIE_ID)
                && mMovieId != 0) {
            getLoaderManager().restartLoader(DETAILS_LOADER, null, this);
        }
    }
*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAILS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAILS_COLUMNS,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished");

        if (cursor == null || !cursor.moveToFirst()) {
            return;
        }

        String imgUrl = Utility.getArtUrlForMovie(getActivity(), IMDB.SIZE_W342) +
                cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH));

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        Toolbar toolbarView = (Toolbar) getView().findViewById(R.id.details_toolbar);


        activity.setSupportActionBar(toolbarView);
        assert activity.getSupportActionBar() != null;
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Glide.with(activity)
                .load(imgUrl)
                .error(Utility.getDefaultImageForMovie(getActivity()))
                .crossFade()
                .into(this.backdrop);

        // Title
        String title = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE));
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) activity.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(title);

        // Overview
        this.overview.setText(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW)));

        // Rating
        this.rating.setText(String.format("%.2f",
                cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE)))
                + " / 10 - " + cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_COUNT)) + " votes");

        // popularity
        this.popularity.setText(String.format("%.1f", cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POPULARITY))));

        imgUrl = Utility.getArtUrlForMovie(getActivity(), IMDB.SIZE_W185) + cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH));

        Glide.with(getActivity())
                .load(imgUrl)
                .error(Utility.getDefaultImageForMovie(getActivity()))
                .crossFade()
                .into(this.poster);

        // Release Date
        String year = cursor.getString((cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE)));
        this.release.setText(year);




   /*
        // We need to start the enter transition after the data has loaded
        if ( mTransitionAnimation ) {
            activity.supportStartPostponedEnterTransition();

            if ( null != toolbarView ) {
                activity.setSupportActionBar(toolbarView);

                activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        } else {
            if ( null != toolbarView ) {
                Menu menu = toolbarView.getMenu();
                if ( null != menu ) menu.clear();
                toolbarView.inflateMenu(R.menu.detailfragment);
                finishCreatingMenu(toolbarView.getMenu());
            }
        }
*/


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
