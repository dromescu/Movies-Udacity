package com.dromescu.popularmovies.async;

import com.dromescu.popularmovies.models.TrailersParcel;

/**
 * Created by dromescu on 7/20/15.
 */
public interface OnTrailer {
    void OnSuccess(TrailersParcel trailersParcel);
    void OnError();
}
