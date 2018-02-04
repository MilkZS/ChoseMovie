package com.example.android.chosemovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.chosemovie.adapter.MovieReviewsAdapter;
import com.example.android.chosemovie.adapter.MovieTrailersAdapter;
import com.example.android.chosemovie.base.MovieInfo;
import com.example.android.chosemovie.data.BaseDataInfo;
import com.example.android.chosemovie.utility.MovieDetailSearchTask;
import com.example.android.chosemovie.utility.OpenMovieInfoJson;
import com.squareup.picasso.Picasso;

public class ChildActivity extends AppCompatActivity {

    private String TAG = "ChildActivity";
    private boolean DBG = true;
    private MovieInfo movieInfo;
    private MovieDetailSearchTask movieDetailSearchTask;
    private OpenMovieInfoJson openMovieInfoJson;
    private MovieReviewsAdapter movieReviewsAdapter;
    private RecyclerView recyclerViewReview;
    private MovieTrailersAdapter movieTrailersAdapter;
    private RecyclerView recyclerViewTrailer;
    private ProgressBar progressBar;

    private ImageView imageView;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView voteTextView;
    private TextView userViewTextView;
    private Button buttonFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        imageView = findViewById(R.id.movie_detail_image);
        titleTextView = findViewById(R.id.movie_detail_title);
        dateTextView = findViewById(R.id.movie_detail_date);
        voteTextView = findViewById(R.id.movie_detail_vote);
        userViewTextView = findViewById(R.id.movie_detail_user_view);
        buttonFavorite = findViewById(R.id.movie_favorite);
        openMovieInfoJson = OpenMovieInfoJson.getInstance();

        recyclerViewReview = findViewById(R.id.show_review);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        movieReviewsAdapter = new MovieReviewsAdapter();
        recyclerViewReview.setLayoutManager(linearLayoutManager);
        recyclerViewReview.setHasFixedSize(true);
        recyclerViewReview.setAdapter(movieReviewsAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        movieTrailersAdapter = new MovieTrailersAdapter();
        recyclerViewTrailer = findViewById(R.id.show_trailer);
        recyclerViewTrailer.setLayoutManager(gridLayoutManager);
        recyclerViewTrailer.setHasFixedSize(true);
        recyclerViewTrailer.setAdapter(movieTrailersAdapter);

        progressBar = findViewById(R.id.show_progress_child);
        movieDetailSearchTask = new MovieDetailSearchTask(openMovieInfoJson,progressBar,
                movieReviewsAdapter, movieTrailersAdapter);


        Intent intent = getIntent();
        if (intent.hasExtra(BaseDataInfo.CLASS_PASS)) {
            movieInfo = (MovieInfo) intent.getSerializableExtra(BaseDataInfo.CLASS_PASS);
        }
        movieDetailSearchTask.execute(movieInfo.getId());
        String sImagePath = movieInfo.getPath_back();
        Picasso.with(this).load(sImagePath).into(imageView);

        titleTextView.setText(movieInfo.getTitle());
        dateTextView.setText(movieInfo.getPubDate());
        userViewTextView.setText(movieInfo.getOverView());

        String sVote = movieInfo.getVoteAver();
        sVote = getResources().getString(R.string.movie_vote) + "\t" + sVote;
        voteTextView.setText(sVote);
    }

    public void markMovie(View view){

    }

}
