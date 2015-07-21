package com.dromescu.popularmovies.async;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dromescu.popularmovies.R;
import com.dromescu.popularmovies.models.TrailersParcel;

/**
 * Created by dromescu on 7/20/15.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private TrailersParcel mTrailersParcel;
    private Context mContext;

    public TrailerAdapter(Context context, TrailersParcel trailers) {
        mContext = context;
        mTrailersParcel = trailers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.name.setTag(viewHolder);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        String name = mTrailersParcel.results.get(position).name;
        viewHolder.name.setText(name);
    }


    @Override
    public int getItemCount() {
        if (mTrailersParcel == null) {
            return 0;
        }
        return mTrailersParcel.results.size();
    }

    public void addItems(TrailersParcel parcel) {
        mTrailersParcel = parcel;
        notifyDataSetChanged();
    }

    public void removeItems() {
        mTrailersParcel.results.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public LinearLayout title_base;
        public LinearLayout base;

        public ViewHolder(View x) {
            super(x);
            name = (TextView) x.findViewById(R.id.trailer_name);
            title_base = (LinearLayout) x.findViewById(R.id.title_base);
          //  poster = (ImageView) x.findViewById(R.id.poster);
            base = (LinearLayout) x.findViewById(R.id.movie_base);
            base.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
         /*   Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("popular_movie", mMovies.results.get(getAdapterPosition()));
            v.getContext().startActivity(intent);*/
        }
    }
}
