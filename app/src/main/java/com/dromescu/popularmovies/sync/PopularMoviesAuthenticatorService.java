package com.dromescu.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by dromescu on 15.07.15.
 */
public class PopularMoviesAuthenticatorService extends Service {
    private PopularMoviesAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        this.mAuthenticator = new PopularMoviesAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.mAuthenticator.getIBinder();
    }
}
