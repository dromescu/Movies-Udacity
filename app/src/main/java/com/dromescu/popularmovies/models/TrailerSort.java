package com.dromescu.popularmovies.models;

import java.util.Comparator;

/**
 * Created by dromescu on 7/20/15.
 */
public class TrailerSort  implements Comparator<TrailerParcel> {

    //Compare the Trailers Quality
    @Override
    public int compare(TrailerParcel left, TrailerParcel right) {
        if (left.size < right.size) return -1;
        if (left.size > right.size) return 1;

        return 0;
    }
}

