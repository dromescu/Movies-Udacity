package com.dromescu.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by dromescu on 15.07.15.
 */
public class MoviesAdapter extends CursorAdapter {

    final private Context mContext;

    public MoviesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.gridview_poster_item, parent, false);
        PosterItemViewHolder viewHolder = new PosterItemViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final PosterItemViewHolder holder = (PosterItemViewHolder)view.getTag();

        final String movieTitle = cursor.getString(MoviesFragment.COL_TITLE);
        holder.title.setText(movieTitle);

        String imageUrl = Utility.getArtUrlForMovie(mContext) + cursor.getString(MoviesFragment.COL_IMAGE_PATH);

        Glide.with(mContext)
                .load(imageUrl)
                .error(Utility.getDefaultImageForMovie(mContext))
                .crossFade()
                .into(holder.image);


    }

    public static class PosterItemViewHolder {
        public final ImageView image;
        public final TextView title;

        public PosterItemViewHolder(View v) {
            this.image = (ImageView)v.findViewById(R.id.poster_img);
            this.title = (TextView)v.findViewById(R.id.poster_title);
        }
    }
}
