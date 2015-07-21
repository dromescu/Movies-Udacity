package com.dromescu.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dromescu.popularmovies.async.TrailerAdapter;
import com.dromescu.popularmovies.models.TrailersParcel;

/**
 * Created by dromescu on 7/20/15.
 */
public class TrailersFragment extends Fragment  {

    private final static String LOG_TAG = DetailsFragment.class.getSimpleName();

 //   private static final String OBJ_KEY = "trailers";
    private TrailersParcel mTrailersParcel;
    private TrailerAdapter mTrailerAdapter;

    public Context mContext;

    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    View rootView;
    private String moviesId;


    @Override
    public void onAttach(Activity activity) {
        mContext = activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_trailers, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mTrailersParcel = arguments.getParcelable(DetailsActivity.OBJ_KEY);
        }

        //  ButterKnife.bind(this, rootView);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_trailers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(getActivity(), mTrailersParcel);
        mRecyclerView.setAdapter(mTrailerAdapter);


        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
