package com.dromescu.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dromescu.popularmovies.data.MoviesContract;
import com.dromescu.popularmovies.models.IMDB;

/**
 * Created by dromescu on 15.07.15.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    final private Context mContext;
    private Cursor mCursor;
    final private MoviesAdapterOnClickHandler mClickHandler;
    final private ItemChoiceManager mICM;
    final private View mEmptyView;


    public MoviesAdapter(Context context, MoviesAdapterOnClickHandler madh,
                         View emptyView, int choiceMode) {

        mContext = context;
        mClickHandler = madh;
        mEmptyView = emptyView;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewGroup instanceof RecyclerView) {
            int layoutId = -1;

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gridview_poster_item, viewGroup, false);
            view.setFocusable(true);
            return new MoviesAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }




    }

    public static interface MoviesAdapterOnClickHandler {
        void onClick(long aLong, MoviesAdapterViewHolder vh);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        final String movieTitle = mCursor.getString(MoviesFragment.COL_TITLE);
        holder.title.setText(movieTitle);

        String imageUrl = Utility.getArtUrlForMovie(mContext, IMDB.SIZE_W185) + mCursor.getString(MoviesFragment.COL_IMAGE_PATH);

        Glide.with(mContext)
                .load(imageUrl)
                .error(Utility.getDefaultImageForMovie(mContext))
                .crossFade()
                .into(holder.image);

        mICM.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }



    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        int itemCount = getItemCount();
        mEmptyView.setVisibility(itemCount == 0 ? View.VISIBLE : View.GONE);
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void selectView(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof MoviesAdapterViewHolder) {
            MoviesAdapterViewHolder vfh = (MoviesAdapterViewHolder) viewHolder;
            vfh.onClick(vfh.itemView);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mICM.onRestoreInstanceState(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        mICM.onSaveInstanceState(outState);
    }


    public int getSelectedItemPosition() {
        return mICM.getSelectedItemPosition();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final ImageView image;
        public final TextView title;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.poster_img);
            this.title = (TextView) view.findViewById(R.id.poster_title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int dateColumnIndex = mCursor.getColumnIndex(MoviesContract.MovieEntry._ID);
            mClickHandler.onClick(mCursor.getLong(dateColumnIndex), this);//mCursor.getLong(dateColumnIndex)
            mICM.onClick(this);
        }
    }

}

