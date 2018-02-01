package com.example.android.chosemovie;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by milkdz on 2018/2/1.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private TextView titleTextView;
    private TextView contentTextView;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{

        public ReviewViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.review_title);
            titleTextView = itemView.findViewById(R.id.review_content);

        }



    }
}
