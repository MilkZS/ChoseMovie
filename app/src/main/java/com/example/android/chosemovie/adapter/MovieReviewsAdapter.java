package com.example.android.chosemovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.chosemovie.R;
import com.example.android.chosemovie.base.MovieReviews;

/**
 * Created by milkdz on 2018/2/1.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ReviewViewHolder> {

    private MovieReviews[] movieReviewsArr;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutList = R.layout.show_recivew;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutList,parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        if (movieReviewsArr == null) {
            return 0;
        }
        return movieReviewsArr.length;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView contentTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.review_title);
            contentTextView = itemView.findViewById(R.id.review_content);
        }

        public void bindData(int position) {
            MovieReviews movieReviews = movieReviewsArr[position];
            titleTextView.setText(movieReviews.getAuthor());
            contentTextView.setText(movieReviews.getContent());
        }
    }

    /**
     * Deliver data between task and adapter
     *
     * @param movieReviewsArr array of MovieReviews
     */
    public void deliverData(MovieReviews[] movieReviewsArr) {
        if (movieReviewsArr != null) {
            this.movieReviewsArr = movieReviewsArr;
            notifyDataSetChanged();
        }
    }
}
