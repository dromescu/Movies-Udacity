package com.dromescu.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by dromescu on 7/18/15.
 */
public class MoviesParcel implements Parcelable {
    public ArrayList<MovieParcel> results = new ArrayList<>();

    public MoviesParcel(Parcel in) {
        in.readTypedList(results, MovieParcel.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
    }

    public static final Parcelable.Creator<MoviesParcel> CREATOR =
            new Parcelable.Creator<MoviesParcel>() {
        @Override
        public MoviesParcel createFromParcel(Parcel source) {
            return new MoviesParcel(source);
        }

        @Override
        public MoviesParcel[] newArray(int size) {
            return new MoviesParcel[size];
        }
    };
}