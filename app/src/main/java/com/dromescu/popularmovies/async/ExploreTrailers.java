package com.dromescu.popularmovies.async;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.dromescu.popularmovies.models.TrailersParcel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Created by dromescu on 20.07.15.
 */

public class ExploreTrailers extends AsyncTask<String, Void, TrailersParcel> {

    public OnTrailer mCallback;

    public ExploreTrailers(Context context) {
        mCallback = (OnTrailer) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected TrailersParcel doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        Gson gson;
        GsonBuilder gsonBuilder;

        String movieId = "135397";

        try {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").authority("api.themoviedb.org").appendPath("3")
                    .appendPath("movie").appendPath(movieId).appendPath("videos")
                    .appendQueryParameter("api_key", "7fa82d7aa48d5cc30a64b80870546857");

            URL url = new URL(builder.build().toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int status = urlConnection.getResponseCode();
            switch(status) {
                case 200:
                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }
                    if (buffer.length() == 0) {
                        return null;
                    }

                    gsonBuilder = new GsonBuilder();
                    gson = gsonBuilder.create();

                    TrailersParcel trailers = gson.fromJson(buffer.toString(), TrailersParcel.class);

                    return trailers;

                case 500:
                    Log.e("Explore Movies", "There was a 500 error server-side");
                    return null;
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    Log.e("MainFragment", "Error closing stream", e);
                }
            }

        }
        return null;
    }

    @Override
    protected void onPostExecute(TrailersParcel trailers) {
        super.onPostExecute(trailers);

        if (trailers != null) {
            mCallback.OnSuccess(trailers);
        } else {
            mCallback.OnError();
        }
    }
}
