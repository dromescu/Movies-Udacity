package com.dromescu.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by dromescu on 7/20/15.
 */
public class TrailersParcel implements Parcelable {
    public ArrayList<TrailerParcel> results = new ArrayList<>();

    public TrailersParcel(Parcel in) {
        in.readTypedList(results, TrailerParcel.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
    }

    public static final Parcelable.Creator<TrailersParcel> CREATOR =
            new Parcelable.Creator<TrailersParcel>() {
                @Override
                public TrailersParcel createFromParcel(Parcel source) {
                    return new TrailersParcel(source);
                }

                @Override
                public TrailersParcel[] newArray(int size) {
                    return new TrailersParcel[size];
                }
            };
}