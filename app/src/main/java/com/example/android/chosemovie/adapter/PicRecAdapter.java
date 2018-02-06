package com.example.android.chosemovie.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.http.SslCertificate;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.chosemovie.R;
import com.example.android.chosemovie.base.MovieInfo;
import com.example.android.chosemovie.common.MovieClickHandle;
import com.example.android.chosemovie.db.MovieInfoContract;
import com.squareup.picasso.Picasso;

/**
 * Created by milkdz on 2018/1/7.
 */

public class PicRecAdapter extends RecyclerView.Adapter<PicRecAdapter.PictureOnViewHolder>{

    private String TAG = "PicRecAdapter";
    private boolean DBG = true;
    private Cursor mCursor;
    private Context context;
    /* 0:sort by popular  1:sort by rate data  the default sort is by popular */
    public final static int POPULAR_MODE = 0;
    public final static int RATE_DATE_MODE = 1;
    public final static int ID_VIDEO = 2;
    public final static int ID_REVIEWS = 3;
    public final static int FAVORITE_MODE = 4;

    MovieClickHandle movieClickHandle;

    public PicRecAdapter(MovieClickHandle movieClickHandle){
        this.movieClickHandle = movieClickHandle;
    }

    @Override
    public PictureOnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutListLayout = R.layout.image_show;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutListLayout,parent,false);
        return new PictureOnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PictureOnViewHolder holder, int position) {
        mCursor.moveToPosition(position);

       String sPath = mCursor.getString(
               mCursor.getColumnIndex(MovieInfoContract.MovieInfos.COLUMN_MOVIE_POSTER_IMAGE));
        if (DBG) Log.d(TAG,"Path := " + sPath);
        Picasso.with(context).load(sPath).into(holder.listItem);


    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    class PictureOnViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView listItem;

        public PictureOnViewHolder(View itemView) {
            super(itemView);
            listItem = itemView.findViewById(R.id.image_show_movie);
            listItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterLocation = getAdapterPosition();
            mCursor.moveToPosition(adapterLocation);
            long listItemNum = mCursor.getColumnIndex(
                    MovieInfoContract.MovieInfos.COLUMN_MOVIE_POSTER_IMAGE);
            movieClickHandle.onClick(listItemNum);
        }
    }
}
