package com.dromescu.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dromescu.popularmovies.data.MoviesContract;
import com.dromescu.popularmovies.models.IMDB;

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
            MoviesContract.MovieEntry.COLUMN_VOTE_COUNT
    };

    private ImageView poster;
    private ImageView backdrop;
    private TextView title;
    private TextView year;
    private TextView duration;
    private TextView rating;
    private TextView overview;
    private TextView votecount;


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

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        this.poster = (ImageView) rootView.findViewById(R.id.details_movie_poster);
        this.title = (TextView) rootView.findViewById(R.id.details_movie_title);
        this.year = (TextView) rootView.findViewById(R.id.details_movie_year);
        this.duration = (TextView) rootView.findViewById(R.id.details_movie_duration);
        this.rating = (TextView) rootView.findViewById(R.id.details_movie_rating);
        this.overview = (TextView) rootView.findViewById(R.id.details_movie_overview);
        this.backdrop = (ImageView) rootView.findViewById(R.id.details_movie_backdrop);

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

        String imgUrl = Utility.getArtUrlForMovie(getActivity(), IMDB.SIZE_W185) + cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH));

        Glide.with(getActivity())
                .load(imgUrl)
                .error(Utility.getDefaultImageForMovie(getActivity()))
                .crossFade()
                .into(this.poster);

        String title = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE));
        this.title.setText(title);

        String year = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE));
        this.year.setText(year);

        double averageRating = cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE));
        this.rating.setText(String.format(getString(R.string.details_rating), averageRating));

        String overview = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW));
        this.overview.setText(overview);

        imgUrl = Utility.getArtUrlForMovie(getActivity(), IMDB.SIZE_W342) + cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH));

        Glide.with(getActivity())
                .load(imgUrl)
                .error(Utility.getDefaultImageForMovie(getActivity()))
                .crossFade()
                .into(this.backdrop);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
