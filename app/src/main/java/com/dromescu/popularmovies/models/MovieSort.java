package com.dromescu.popularmovies.models;

import java.util.Comparator;

/**
 * Created by dromescu on 7/18/15.
 */
public class MovieSort implements Comparator<MovieParcel> {

    @Override
    public int compare(MovieParcel left, MovieParcel right) {
        if (left.vote_average < right.vote_average) return -1;
        if (left.vote_average > right.vote_average) return 1;

        return 0;
    }
}


