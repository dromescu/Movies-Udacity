package com.dromescu.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;

/**
 * Created by dromescu on 15.07.15.
 */
public class Utility {
    public static String getPrefferedMoviesList(final Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_option_default));
    }

    /**
     * Helper method to provide the art urls according to the themoviedb call.
     *
     * @param context Context to use for retrieving the URL format
     * @param
     * @return url for the corresponding movie artwork. default artwork if no relation is found.
     */
    public static String getArtUrlForMovie(Context context) {

         String imageUrl = context.getString(R.string.pref_url_movies_picture);

        return imageUrl;
    }

    public static Drawable getDefaultImageForMovie(Context context) {

        Uri path = Uri.parse("android.resource://com.dromescu.popularmovies/" + R.mipmap.ic_launcher);
        String pathName = path.toString();
        Drawable defaultMovieImage = Drawable.createFromPath(pathName);

        return defaultMovieImage;
    }

    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

}
