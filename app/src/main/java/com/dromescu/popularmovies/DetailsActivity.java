package com.dromescu.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.dromescu.popularmovies.async.ExploreTrailers;
import com.dromescu.popularmovies.async.OnTrailer;
import com.dromescu.popularmovies.async.TrailerAdapter;
import com.dromescu.popularmovies.models.TrailersParcel;

/**
 * Created by dromescu on 15.07.15.
 */
public class DetailsActivity extends AppCompatActivity
        implements OnTrailer
{

    public static final String KEY_MOVIE_ID = "movie_id";
    public static final String OBJ_KEY = "trailers";
    private TrailersParcel mTrailersParcel;
    private TrailerAdapter mTrailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        if (savedInstanceState == null || !savedInstanceState.containsKey(OBJ_KEY)) {

      //      new ExploreTrailers(this).execute();
            Bundle arguments = new Bundle();
            createFragments(arguments);


        } else {
     //       mTrailersParcel = savedInstanceState.getParcelable(OBJ_KEY);

        }

  //      mTrailerAdapter = new TrailerAdapter(DetailsActivity.this, mTrailersParcel);


    }

    public void createFragments(Bundle arguments){

        long movieId = getIntent().getLongExtra(KEY_MOVIE_ID, 1);


        arguments.putLong(DetailsFragment.KEY_MOVIE_ID, movieId);
        DetailsFragment fragmentDetails = new DetailsFragment();
        fragmentDetails.setArguments(arguments);

   /*     arguments.putParcelable(OBJ_KEY, mTrailersParcel);
        TrailersFragment fragmentTrailers = new TrailersFragment();
        fragmentTrailers.setArguments(arguments);*/


        getSupportFragmentManager().beginTransaction()
                .add(R.id.movie_details_container, fragmentDetails)
         //       .add(R.id.movie_details_container, fragmentTrailers)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(OBJ_KEY, mTrailersParcel);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            new ExploreTrailers(this).execute();
        }
    }

    @Override
    public void OnSuccess(TrailersParcel trailersParcel) {
        mTrailersParcel = trailersParcel;
        mTrailerAdapter.addItems(mTrailersParcel);
        Bundle arguments = new Bundle();
        createFragments(arguments);
    }

    @Override
    public void OnError() {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_message), Snackbar.LENGTH_LONG).show();
    }


}
