package com.example.android.chosemovie;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.android.chosemovie.data.SQLBaseInfo;
import com.example.android.chosemovie.db.MovieInfoContract;
import com.example.android.chosemovie.sync.MovieSyncUtil;
import com.example.android.chosemovie.utility.MovieSearchTask;
import com.example.android.chosemovie.utility.OpenMovieInfoJson;

public class MainActivity extends AppCompatActivity implements MovieClickHandle, LoaderManager.LoaderCallbacks<Cursor> {

    private String TAG = "ChoseMovie-MainActivity";
    private RecyclerView recyclerView;

    private final int spanCount = 3;
    private OpenMovieInfoJson openMovieInfoJson;
    private final int POPULAR_MODE = PicRecAdapter.POPULAR_MODE;
    private final int RATE_DATA_MODE = PicRecAdapter.RATE_DATE_MODE;
    private final int FAVORITE_MODE = PicRecAdapter.FAVORITE_MODE;
    private PicRecAdapter imageAdapter;
    private ProgressBar progressBar;
    private MovieSearchTask movieSearchTask;

    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openMovieInfoJson = OpenMovieInfoJson.getInstance();
        recyclerView = findViewById(R.id.recycle_show);
        progressBar = findViewById(R.id.show_progress);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        imageAdapter = new PicRecAdapter(this);
        recyclerView.setAdapter(imageAdapter);
        Log.d(TAG, "start Task");
        refreshMode(POPULAR_MODE);
        //getSupportLoaderManager().initLoader(POPULAR_MODE, null, this);


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
                getSupportLoaderManager().restartLoader(POPULAR_MODE, null, this);
                MovieSyncUtil.initialize(this,POPULAR_MODE);
            }
            break;
            case RATE_DATA_MODE: {
                Log.d("test1","RATE--MAIN");
                //getSupportLoaderManager().restartLoader()
                getSupportLoaderManager().restartLoader(RATE_DATA_MODE, null, this);
                MovieSyncUtil.initialize(this,RATE_DATA_MODE);
            }
            break;
            case FAVORITE_MODE: {
                if (movieSearchTask != null) {
                    movieSearchTask.cancel(true);
                }
            }
            break;
        }
    }


    @Override
    public void onClick(long index) {
       // Class desClass = ChildActivity.class;
       // Intent intent = new Intent(this, desClass);
        //intent.putExtra(BaseDataInfo.CLASS_PASS, movieInfo);
        //startActivity(intent);
        Toast.makeText(this,index+"",Toast.LENGTH_SHORT).show();
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case POPULAR_MODE: {
                Uri movieUri = MovieInfoContract.MovieInfos.CONTENT_URI;
                String sOrder = MovieInfoContract.MovieInfos.COLUMN_MOVIE_VOTE
                        + SQLBaseInfo.SORT_AES;
                String select = MovieInfoContract.getSelect(POPULAR_MODE);
                return new CursorLoader(this, movieUri, MovieInfoContract.MAIN_MOVIE_UI,
                        select, null, sOrder);
            }
            case RATE_DATA_MODE: {
                Log.d("test1","RATE--OncreateLoader");
                Uri movieUri = MovieInfoContract.MovieInfos.CONTENT_URI;
                String sOrder = MovieInfoContract.MovieInfos.COLUMN_MOVIE_VOTE
                        + SQLBaseInfo.SORT_AES;
                Log.d("test1",movieUri.toString());
                String select = MovieInfoContract.getSelect(RATE_DATA_MODE);
                Log.d("test1",select);
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
}
