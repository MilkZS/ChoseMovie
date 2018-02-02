package com.example.android.chosemovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.android.chosemovie.R;

import java.util.ArrayList;

/**
 * Created by milkdz on 2018/2/2.
 */

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailersHolder> {

    private ArrayList<Uri> uriArrayList;
    private Context context;
    private final String TRAILER = "Trailer";

    @Override
    public MovieTrailersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutList =  R.layout.show_trailers;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutList,parent,false);
        return new MovieTrailersHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailersHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        if (uriArrayList == null) {
            return 0;
        }
        return uriArrayList.size();
    }

    class MovieTrailersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Button button;
        private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        public MovieTrailersHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button_trailers);
            button.setLayoutParams(layoutParams);
        }

        public void bindData(int position) {
            String sVideo = TRAILER + Integer.toString(position + 1);
            button.setText(sVideo);
            button.setTag(position);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            if (tag < 0) {
                return;
            }
            Uri uri = uriArrayList.get(tag);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            /*
             * This is a check we perform with every implicit Intent that we launch. In some cases,
             * the device where this code is running might not have an Activity to perform the action
             * with the data we've specified. Without this check, in those cases your app would crash.
             */
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }
    }

    public void deliverTrailers(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
        notifyDataSetChanged();
    }
}
