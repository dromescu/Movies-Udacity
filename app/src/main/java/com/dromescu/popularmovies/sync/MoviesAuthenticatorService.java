package com.dromescu.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by dromescu on 15.07.15.
 */
public class MoviesAuthenticatorService extends Service {
    private MoviesAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        this.mAuthenticator = new MoviesAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.mAuthenticator.getIBinder();
    }
}
