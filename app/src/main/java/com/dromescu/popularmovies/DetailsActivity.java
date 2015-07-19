package com.dromescu.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dromescu on 15.07.15.
 */
public class DetailsActivity extends AppCompatActivity {

    public static final String KEY_MOVIE_ID = "movie_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {

            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            long movieId = getIntent().getLongExtra(KEY_MOVIE_ID, 1);


            arguments.putLong(DetailsFragment.KEY_MOVIE_ID, movieId);

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_details_container, fragment)
                    .commit();

        }
    }

}
