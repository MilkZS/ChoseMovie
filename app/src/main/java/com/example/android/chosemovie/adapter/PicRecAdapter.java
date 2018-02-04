package com.example.android.chosemovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.chosemovie.R;
import com.example.android.chosemovie.base.MovieInfo;
import com.example.android.chosemovie.common.MovieClickHandle;
import com.squareup.picasso.Picasso;

/**
 * Created by milkdz on 2018/1/7.
 */

public class PicRecAdapter extends RecyclerView.Adapter<PicRecAdapter.PictureOnViewHolder>{

    private String TAG = "PicRecAdapter";
    private MovieInfo[] mData;
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
        holder.bind(position,context);
    }

    @Override
    public int getItemCount() {
        if(mData == null){
            return 0;
        }
        return mData.length;
    }

    class PictureOnViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView listItem;
        MovieInfo movieInfo;
        public PictureOnViewHolder(View itemView) {
            super(itemView);
            listItem = itemView.findViewById(R.id.image_show_movie);
            listItem.setOnClickListener(this);
        }

        void bind( int position,Context context){
            String sPath = mData[position].getPath();
            Log.d(TAG,"Path := " + sPath);
            Picasso.with(context).load(sPath).into(listItem);
        }

        @Override
        public void onClick(View v) {
            int adapterLocation = getAdapterPosition();
            movieInfo = mData[adapterLocation];
            movieClickHandle.onClick(movieInfo);
        }
    }

    /**
     * use interface to receive data
     *
     * @param mData data array
     */
    public void setData(MovieInfo[] mData){
        this.mData = mData;
        notifyDataSetChanged();
    }
}
