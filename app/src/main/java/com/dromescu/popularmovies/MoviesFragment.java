package com.dromescu.popularmovies;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.dromescu.popularmovies.data.MoviesContract;

/**
 * Created by dromescu on 15.07.15.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = MoviesFragment.class.getSimpleName();

    public static final String KEY_MOVIE_ID = "movie_id";
    private int mChoiceMode;
    private boolean mHoldForTransition, mAutoSelectView;


    public interface MovieSelectedCallback {
        void onItemSelected(long id, MoviesAdapter.MoviesAdapterViewHolder vh);
    }

    private static final int MOVIES_LOADER = 0;

    private static final String[] MOVIES_COLUMNS = {
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry._ID,
            MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieEntry.COLUMN_MOVIEDB_ID
    };

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_IMAGE_PATH = 2;
    public static final int COL_MOVIEDB_ID = 3;

    private MoviesAdapter mMoviesAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    /*
        @Override
        public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
            super.onInflate(activity, attrs, savedInstanceState);
            TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.MoviesFragment,
                    0, 0);
            mChoiceMode = a.getInt(R.styleable.MoviesFragment_android_choiceMode, AbsListView.CHOICE_MODE_NONE);
            mAutoSelectView = a.getBoolean(R.styleable.MoviesFragment_autoSelectView, false);
            mHoldForTransition = a.getBoolean(R.styleable.MoviesFragment_sharedElementTransitions, false);
            a.recycle();
        }
    */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_all_movies, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_movies);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        View emptyView = rootView.findViewById(R.id.recyclerview_movie_empty);
        mRecyclerView.setHasFixedSize(true);

        // The ForecastAdapter will take data from a source and
        // use it to populate the RecyclerView it's attached to.
        mMoviesAdapter = new MoviesAdapter(getActivity(), new MoviesAdapter.MoviesAdapterOnClickHandler() {
            @Override
            public void onClick(long aLong, MoviesAdapter.MoviesAdapterViewHolder vh) {
                Cursor cursor = mMoviesAdapter.getCursor();
                ((MovieSelectedCallback) getActivity())
                        .onItemSelected(cursor.getLong(COL_MOVIEDB_ID),
                                vh);

                mPosition = vh.getAdapterPosition();
                cursor.moveToPosition(mPosition);
            }
        }, emptyView, mChoiceMode);

        mRecyclerView.setAdapter(mMoviesAdapter);

        /*
        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null && cursor.moveToPosition(position)) {
                    ((MovieSelectedCallback) getActivity()).onItemSelected(cursor.getLong(COL_MOVIEDB_ID));
                }
            }
        });*/

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        final String sortOrder;

        String sortCriteria = Utility.getPrefferedMoviesList(getActivity());
        if (sortCriteria.equals(getString(R.string.pref_sort_option_default))) {
            sortOrder = MoviesContract.MovieEntry.COLUMN_POPULARITY + " DESC";
        } else {
            sortOrder = MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE + " DESC";
        }

        return new CursorLoader(getActivity(),
                MoviesContract.MovieEntry.CONTENT_URI,
                MOVIES_COLUMNS,
                null,
                null,
                sortOrder);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviesAdapter.swapCursor(data);
        if (mPosition != RecyclerView.NO_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mRecyclerView.smoothScrollToPosition(mPosition);
        }
       // updateEmptyView();
        if (data.getCount() == 0) {
            getActivity().supportStartPostponedEnterTransition();
        } else {
            mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    // Since we know we're going to get items, we keep the listener around until
                    // we see Children.
                    if (mRecyclerView.getChildCount() > 0) {
                        mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                        int itemPosition = mMoviesAdapter.getSelectedItemPosition();
                        if (RecyclerView.NO_POSITION == itemPosition) itemPosition = 0;
                        RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForAdapterPosition(itemPosition);
                        if (null != vh && mAutoSelectView) {
                            mMoviesAdapter.selectView(vh);
                        }
                        if (mHoldForTransition) {
                            getActivity().supportStartPostponedEnterTransition();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        this.mMoviesAdapter.swapCursor(null);
    }

    /*
       Updates the empty list view with contextually relevant information that the user can
       use to determine why they aren't seeing weather.
    */
    private void updateEmptyView() {
        if (mMoviesAdapter.getItemCount() == 0) {
            TextView tv = (TextView) getView().findViewById(R.id.recyclerview_movie_empty);
            if (null != tv) {
                // if cursor is empty, why? do we have an invalid location
                int message = R.string.empty_movie_list;

                if (!Utility.isNetworkAvailable(getActivity())) {
                    message = R.string.empty_movie_list_no_network;
                }

                tv.setText(message);
            }
        }
    }
}
