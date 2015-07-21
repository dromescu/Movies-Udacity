package com.dromescu.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dromescu on 7/20/15.
 */
public class TrailerParcel implements Parcelable {
    
    public String id;
    public String iso_639_1;
    public String key;
    public String name;
    public String site;
    public int size;//For Trailer Quality
    public String type;

    public TrailerParcel(Parcel in) {

        this.id = in.readString();
        this.iso_639_1 = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }


    public static final Parcelable.Creator<TrailerParcel> CREATOR = new Parcelable.Creator<TrailerParcel>() {
        @Override
        public TrailerParcel createFromParcel(Parcel source) {
            return new TrailerParcel(source);
        }

        @Override
        public TrailerParcel[] newArray(int size) {
            return new TrailerParcel[size];
        }
    };
}

