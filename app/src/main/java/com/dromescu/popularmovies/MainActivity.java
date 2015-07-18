package com.dromescu.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dromescu.popularmovies.sync.PopularMoviesSyncAdapter;


public class MainActivity extends AppCompatActivity implements MoviesFragment.MovieSelectedCallback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_details_container) != null) {
            this.mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_details_container, new DetailsFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            this.mTwoPane = false;
        }

        PopularMoviesSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(long id, MoviesAdapter.MoviesAdapterViewHolder vh) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putLong(DetailsFragment.KEY_MOVIE_ID, id);

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class).
                    putExtra(DetailsFragment.KEY_MOVIE_ID, id);

            ActivityOptionsCompat activityOptions =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                            new Pair<View, String>(vh.image, getString(R.string.detail_icon_transition_name)));
            ActivityCompat.startActivity(this, intent, activityOptions.toBundle());

        }
    }
}
