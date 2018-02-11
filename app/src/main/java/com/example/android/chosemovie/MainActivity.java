package com.example.android.chosemovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.Preference;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.chosemovie.adapter.PicRecAdapter;
import com.example.android.chosemovie.common.MovieClickHandle;
import com.example.android.chosemovie.data.BaseDataInfo;
import com.example.android.chosemovie.data.SQLBaseInfo;
import com.example.android.chosemovie.db.MovieInfoContract;
import com.example.android.chosemovie.sync.MovieSyncUtil;
import com.example.android.chosemovie.utility.MovieSyncDataTask;
import com.example.android.chosemovie.utility.OpenMovieInfoJson;

public class MainActivity extends AppCompatActivity implements MovieClickHandle, LoaderManager.LoaderCallbacks<Cursor> {

    private boolean DBG = true;
    private String TAG = "ChoseMovie-MainActivity";
    private RecyclerView recyclerView;
    private String MAIN_UI_STAUS = "main_ui_staus";

    private OpenMovieInfoJson openMovieInfoJson;

    private final int spanCount = 3;
    private final int POPULAR_MODE = BaseDataInfo.POPULAR_MODE;
    private final int RATE_DATA_MODE = BaseDataInfo.RATE_DATE_MODE;
    private final int FAVORITE_MODE = BaseDataInfo.FAVORITE_MODE;
    private PicRecAdapter imageAdapter;
    private ProgressBar progressBar;
    private int initMode;
    private int mPosition = RecyclerView.NO_POSITION;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().setElevation(0f);

        sharedPreferences = getSharedPreferences(BaseDataInfo.MOVIE_PREFERENCE_MAIN,MODE_PRIVATE);
        recyclerView = findViewById(R.id.recycle_show);
        progressBar = findViewById(R.id.show_progress);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        Log.e(TAG,"onCreate + savedInstanceState :"+savedInstanceState);
        Log.d(TAG,"onCreate + init mode :"+initMode);
        if(savedInstanceState != null && savedInstanceState.containsKey(MAIN_UI_STAUS)){
            initMode = savedInstanceState.getInt(MAIN_UI_STAUS);
          //  Toast.makeText(this,initMode,Toast.LENGTH_SHORT).show();
        }else{
            initMode = sharedPreferences.getInt(MAIN_UI_STAUS,POPULAR_MODE);
        }
        imageAdapter = new PicRecAdapter(this);
        recyclerView.setAdapter(imageAdapter);
        Log.d(TAG, "start Task");
        MovieSyncDataTask movieSyncDataTask = new MovieSyncDataTask(openMovieInfoJson,this);
        getSupportLoaderManager().initLoader(initMode, null, this);
        MovieSyncUtil.initialize(this,initMode);
    }

    /**
     * start a new AsyncTask to refresh the activity by new sort
     * <p>
     * choseMode : 0 -> popular
     * 1 -> rate data
     *
     * @param choseMode sort mode
     */
    public void refreshMode(int choseMode) {
        switch (choseMode) {
            case POPULAR_MODE: {
                initMode = POPULAR_MODE;
                getSupportLoaderManager().restartLoader(POPULAR_MODE, null, this);
                MovieSyncUtil.initialize(this,POPULAR_MODE);
            }
            break;
            case RATE_DATA_MODE: {
                initMode = RATE_DATA_MODE;
                Log.d("test1","RATE--MAIN");
                //getSupportLoaderManager().restartLoader()
                getSupportLoaderManager().restartLoader(RATE_DATA_MODE, null, this);
                MovieSyncUtil.initialize(this,RATE_DATA_MODE);
            }
            break;
            case FAVORITE_MODE: {
                initMode = FAVORITE_MODE;
                getSupportLoaderManager().restartLoader(FAVORITE_MODE,null,this);
                MovieSyncUtil.initialize(this,FAVORITE_MODE);
            }
            break;
        }
    }


    @Override
    public void onClick(String index) {
        Uri uri = MovieInfoContract.buildContentForMovieDetail(index);
        Class desClass = ChildActivity.class;
        Intent intent = new Intent(this, desClass);
        intent.setData(uri);
        intent.putExtra(Intent.EXTRA_TEXT,initMode);
        startActivity(intent);
        //Toast.makeText(this,index+"",Toast.LENGTH_SHORT).show();
        if (DBG) Log.d(TAG,uri.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case (R.id.tv_sort_by_popular): {
                refreshMode(POPULAR_MODE);
            }
            break;
            case (R.id.tv_sort_by_date): {
                refreshMode(RATE_DATA_MODE);
            }
            break;
            case (R.id.tv_sort_by_favorite):{
                refreshMode(FAVORITE_MODE);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri movieUri = MovieInfoContract.MovieInfos.CONTENT_URI;
        String sOrder = MovieInfoContract.MovieInfos.COLUMN_MOVIE_VOTE
                + SQLBaseInfo.SORT_AES;
        switch (id) {
            case POPULAR_MODE: {
                String select = MovieInfoContract.getSelect(POPULAR_MODE);
                return new CursorLoader(this, movieUri, MovieInfoContract.MAIN_MOVIE_UI,
                        select, null, sOrder);
            }
            case RATE_DATA_MODE: {
                Log.d("test1","RATE--OncreateLoader");
                String select = MovieInfoContract.getSelect(RATE_DATA_MODE);
                return new CursorLoader(this, movieUri, MovieInfoContract.MAIN_MOVIE_UI,
                        select, null, sOrder);
            }
            case FAVORITE_MODE:{
                String select = MovieInfoContract.getSelect(FAVORITE_MODE);
                Log.d(TAG,"onCreateLoader FAVORITE_MODE : " + select);
                return new CursorLoader(this, movieUri, MovieInfoContract.MAIN_MOVIE_UI,
                        select, null, sOrder);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        imageAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) {
            mPosition = 0;
        }
        recyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) {

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        imageAdapter.swapCursor(null);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG,"save state " + initMode);
        savedInstanceState.putInt(MAIN_UI_STAUS,initMode);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MAIN_UI_STAUS,initMode);
        editor.commit();
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.e(TAG,"Restore state");
        super.onRestoreInstanceState(savedInstanceState);
        initMode = savedInstanceState.getInt(MAIN_UI_STAUS);
    }
}
