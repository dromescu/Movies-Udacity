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
            long movieId = getIntent().getLongExtra(KEY_MOVIE_ID, 0);
            DetailsFragment detailFragment = DetailsFragment.newInstance(movieId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container, detailFragment)
                    .commit();
        }
    }

}
