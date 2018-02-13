package com.example.android.chosemovie;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.chosemovie.adapter.MovieReviewsAdapter;
import com.example.android.chosemovie.adapter.MovieTrailersAdapter;
import com.example.android.chosemovie.base.MovieReviews;
import com.example.android.chosemovie.data.BaseDataInfo;
import com.example.android.chosemovie.db.MovieInfoContract;
import com.example.android.chosemovie.db.MovieInfoDBHelper;
import com.example.android.chosemovie.utility.MovieSyncDataTask;
import com.example.android.chosemovie.utility.OpenMovieInfoJson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChildActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String TAG = "ChildActivity";
    private boolean DBG = true;

    private OpenMovieInfoJson openMovieInfoJson;
    private MovieReviewsAdapter movieReviewsAdapter;
    private RecyclerView recyclerViewReview;
    private MovieTrailersAdapter movieTrailersAdapter;
    private RecyclerView recyclerViewTrailer;
    private ProgressBar progressBar;

    private Uri mUri;
    private int ifFavorite;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private ImageView imageView;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView voteTextView;
    private TextView userViewTextView;
    private Button buttonFavorite;
private ContentResolver contentResolver;
    private String Movie_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        contentResolver = getContentResolver();
        sharedPreferences = getSharedPreferences(BaseDataInfo.MOVIE_PREFERENCE, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (DBG)Log.e(TAG,"onCreate");

        progressBar = findViewById(R.id.show_progress_child);

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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        movieTrailersAdapter = new MovieTrailersAdapter();
        recyclerViewTrailer = findViewById(R.id.show_trailer);
        recyclerViewTrailer.setLayoutManager(gridLayoutManager);
        recyclerViewTrailer.setHasFixedSize(true);
        recyclerViewTrailer.setAdapter(movieTrailersAdapter);

        progressBar = findViewById(R.id.show_progress_child);

        Intent intent = getIntent();
        mUri = intent.getData();
        if (DBG) Log.e(TAG,"mUri " + mUri);
        if (mUri == null) {
            throw new NullPointerException("URI for DetailActivity cannot be null");
        }

        //if (savedInstanceState == null){
        getSupportLoaderManager().initLoader(BaseDataInfo.ID_MOVIE, null, this);
        //}else {
        //    getSupportLoaderManager().restartLoader(BaseDataInfo.ID_MOVIE,null,this);
       // if(intent.hasExtra(Intent.EXTRA_TEXT)){
//            Log.d(TAG,"initMode"+initMode+"");
//        new MovieSyncDataTask(openMovieInfoJson,this).execute();
       // }
    }


    private Cursor data;
    //private MovieInfoDBHelper movieInfoDBHelper = new MovieInfoDBHelper(this);

    /**
     * when button is clicked , change the preference
     *
     * @param view button view
     */
    public void markMovie(View view) {
        Log.d(TAG,"markMovie");
        String buttonFavoriteText = (String) buttonFavorite.getText();
        ContentValues contentValues = new ContentValues();
        //SQLiteDatabase db = movieInfoDBHelper.getWritableDatabase();
        int newFavorite = 0;
        if (buttonFavoriteText.equals(getResources().getString(R.string.movie_favorite))) {
            buttonFavorite.setText(getResources().getString(R.string.movie_favorite_fix));
            editor.putInt(Movie_Id, BaseDataInfo.FAVORITE_MODE);
            Log.d(TAG,"ifFavorite -- 收藏 : " + ifFavorite);
            newFavorite = ifFavorite + BaseDataInfo.FAVORITE_MODE;
            Log.d(TAG,"newFavorite -- 收藏 : " + newFavorite);
        } else if (buttonFavoriteText.equals(getResources().getString(R.string.movie_favorite_fix))) {
            buttonFavorite.setText(getResources().getString(R.string.movie_favorite));
            editor.putInt(Movie_Id, BaseDataInfo.UN_FAVORITE);
            Log.d(TAG,"ifFavorite -- 取消收藏 : " + ifFavorite);
            newFavorite = ifFavorite - BaseDataInfo.FAVORITE_MODE;
            Log.d(TAG,"newFavorite -- 取消收藏 : " + newFavorite);
        }
        editor.commit();
        contentValues.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_SORT,newFavorite);

        contentResolver.update(mUri,contentValues, MovieInfoContract.MovieInfos.COLUMN_MOVIE_ID + "=?",new String[]{Movie_Id});
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case BaseDataInfo.ID_MOVIE: {
                return new CursorLoader(this, mUri, MovieInfoContract.CHILD_MOVIE_UI,
                        null, null, null);
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {

        if (data != null && data.moveToFirst()) {
            getFavorite(data);
            setInfoIntoUI(data);
            setTrailers(data);
            setReviews(data);
        }
    }

    private void getFavorite(Cursor cursor){
        Log.d(TAG,"getFavorite -- ifFavorite" + cursor.getString(
                cursor.getColumnIndex(MovieInfoContract.MovieInfos.COLUMN_MOVIE_SORT)));
        ifFavorite = Integer.parseInt(cursor.getString(
                cursor.getColumnIndex(MovieInfoContract.MovieInfos.COLUMN_MOVIE_SORT)));
        Log.d(TAG,"getFavorite -- ifFavorite : " + ifFavorite);
    }


    /**
     * Set info into the UI
     *
     * @param dataCursor cursor include messages
     */
    private void setInfoIntoUI(Cursor dataCursor) {
        Log.e(TAG,"setInfo");
        // detail movie image
        String sImagePath = dataCursor.getString(
                dataCursor.getColumnIndex(MovieInfoContract.MovieInfos.COLUMN_MOVIE_BACK_IMAGE));
        Picasso.with(this).load(sImagePath).into(imageView);

        //detail movie name
        String sTitle = dataCursor.getString(
                dataCursor.getColumnIndex(MovieInfoContract.MovieInfos.COLUMN_MOVIE_NAME));
        titleTextView.setText(sTitle);

        //detail movie public date
        String sDate = dataCursor.getString(
                dataCursor.getColumnIndex(MovieInfoContract.MovieInfos.COLUMN_MOVIE_DATE));
        dateTextView.setText(sDate);

        //detail movie over view
        String sOverView = dataCursor.getString(
                dataCursor.getColumnIndex(MovieInfoContract.MovieInfos.COLUMN_MOVIE_OVER_VIEW));
        userViewTextView.setText(sOverView);

        //detail movie vote
        String sVote = dataCursor.getString(
                dataCursor.getColumnIndex(MovieInfoContract.MovieInfos.COLUMN_MOVIE_VOTE));
        sVote = getResources().getString(R.string.movie_vote) + "\t" + sVote;
        voteTextView.setText(sVote);

        //detail movie favorite find by movie id sort by button
        Movie_Id = dataCursor.getString(
                dataCursor.getColumnIndex(MovieInfoContract.MovieInfos.COLUMN_MOVIE_ID));
        int sFavorite = sharedPreferences.getInt(
                Movie_Id, BaseDataInfo.UN_FAVORITE);
        switch (sFavorite) {
            case BaseDataInfo.FAVORITE_MODE: {
                if (DBG) Log.d(TAG, "sFavorite : " + sFavorite);
                buttonFavorite.setText(getResources().getString(R.string.movie_favorite_fix));
            }
            break;
            case BaseDataInfo.UN_FAVORITE: {
                if (DBG) Log.d(TAG, "sFavorite : " + sFavorite);
                buttonFavorite.setText(getResources().getString(R.string.movie_favorite));
            }
            break;
        }
    }

    /**
     * Get reviews from cursor and set them to the UI
     *
     * @param dataCursor row cursor
     */
    private void setReviews(Cursor dataCursor){
        String reviews = dataCursor.getString(
                dataCursor.getColumnIndex(MovieInfoContract.MovieInfos.COLUMN_MOVIE_REVIEW));
        if(reviews == null || reviews.equals("")){
            Log.d(TAG,"Reviews is null");
            return;
        }
        String[] reviewsArr = reviews.split(";");
        MovieReviews[] movieReviews = new MovieReviews[reviewsArr.length - 1];
        for (int i=1;i<reviewsArr.length;i++){
            String[] s = reviewsArr[i].split(",");
            movieReviews[i-1] = new MovieReviews(s[0],s[1]);
        }
        movieReviewsAdapter.deliverData(movieReviews);
    }

    /**
     * Get Uri arrayList from cursor and set it to the UI
     *
     * @param dataCursor row cursor
     */
    private void setTrailers(Cursor dataCursor){
        String sTrailers = dataCursor.getString(
                dataCursor.getColumnIndex(MovieInfoContract.MovieInfos.COLUMN_MOVIE_TRAILER));
        if(sTrailers == null || sTrailers.equals("")){
            Log.d(TAG,"Trailers is null");
            return;
        }
        Log.d(TAG,"Trailers is not null");
        String[] sTrailersArr = sTrailers.split(";");
        ArrayList<Uri> uriArrayList = new ArrayList<>();
        for (int i=1;i<sTrailersArr.length;i++){
            Uri uri = Uri.parse(sTrailersArr[i]).buildUpon().build();
            Log.d(TAG,uri.toString());
            uriArrayList.add(uri);
        }
        movieTrailersAdapter.deliverTrailers(uriArrayList);
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
