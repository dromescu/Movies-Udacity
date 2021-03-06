package com.dromescu.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by dromescu on 10.07.15.
 */
public class MoviesSyncService extends Service {
    private static final String LOG_TAG = MoviesSyncService.class.getSimpleName();

    public static final Object sSyncAdapterLock = new Object();
    private static MoviesSyncAdapter sMoviesSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate - MoviesSyncService");
        synchronized (sSyncAdapterLock) {
            if (sMoviesSyncAdapter == null) {
                sMoviesSyncAdapter = new MoviesSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sMoviesSyncAdapter.getSyncAdapterBinder();
    }
}
