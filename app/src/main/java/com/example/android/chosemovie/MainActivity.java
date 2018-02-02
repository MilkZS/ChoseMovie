package com.example.android.chosemovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.example.android.chosemovie.adapter.PicRecAdapter;
import com.example.android.chosemovie.base.MovieInfo;
import com.example.android.chosemovie.common.MovieClickHandle;
import com.example.android.chosemovie.data.BaseDataInfo;
import com.example.android.chosemovie.utility.MovieSearchTask;
import com.example.android.chosemovie.utility.OpenMovieInfoJson;

public class MainActivity extends AppCompatActivity implements MovieClickHandle{

    private String TAG = "ChoseMovie-MainActivity";
    private RecyclerView recyclerView;

    private final int spanCount = 3;
    private OpenMovieInfoJson openMovieInfoJson;
    private int POPULAR_MODE = PicRecAdapter.POPULAR_MODE;
    private int RATE_DATA_MODE = PicRecAdapter.RATE_DATE_MODE;
    private PicRecAdapter imageAdapter;
    private ProgressBar progressBar;
    private MovieSearchTask movieSearchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openMovieInfoJson = OpenMovieInfoJson.getInstance();
        recyclerView = findViewById(R.id.recycle_show);
        progressBar = findViewById(R.id.show_progress);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,spanCount);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        imageAdapter = new PicRecAdapter(this);
        recyclerView.setAdapter(imageAdapter);
        Log.d(TAG,"start Task");
        refreshMode(POPULAR_MODE);
    }

    /**
     * start a new AsyncTask to refresh the activity by new sort
     *
     * choseMode : 0 -> popular
     *             1 -> rate data
     *
     * @param choseMode sort mode
     */
    public void refreshMode(int choseMode){
        if(choseMode == POPULAR_MODE){
            if(movieSearchTask != null){
                movieSearchTask.cancel(true);
            }
            movieSearchTask = new MovieSearchTask(progressBar,imageAdapter,openMovieInfoJson);
            movieSearchTask.execute(POPULAR_MODE);
        }else if(choseMode == RATE_DATA_MODE){
            if(movieSearchTask != null){
                movieSearchTask.cancel(true);
            }
            movieSearchTask = new MovieSearchTask(progressBar,imageAdapter,openMovieInfoJson);
            movieSearchTask.execute(RATE_DATA_MODE);
        }
    }

    @Override
    public void onClick(MovieInfo movieInfo) {
        Class desClass = ChildActivity.class;
        Intent intent = new Intent(this,desClass);
        intent.putExtra(BaseDataInfo.CLASS_PASS,movieInfo);
        startActivity(intent);
        // Toast.makeText(this,sId,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_sort,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case (R.id.tv_sort_by_popular):{
                refreshMode(POPULAR_MODE);
            }break;
            case (R.id.tv_sort_by_date):{
                refreshMode(RATE_DATA_MODE);
            }break;
        }
        return super.onOptionsItemSelected(item);
    }
}
