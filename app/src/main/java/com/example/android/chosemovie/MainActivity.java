package com.example.android.chosemovie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.chosemovie.base.MovieInfo;
import com.example.android.chosemovie.common.MovieClickHandle;
import com.example.android.chosemovie.data.BaseDataInfo;
import com.example.android.chosemovie.utility.NetWorkUtils;
import com.example.android.chosemovie.utility.OpenMovieInfoJson;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieClickHandle{

    private String TAG = "MainActivity";
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
     * start a new AsycTask to refresh the activity by new sort
     *
     * @param choseMode sort mode
     */
    public void refreshMode(int choseMode){
        if(choseMode == POPULAR_MODE){
            if(movieSearchTask != null){
                movieSearchTask.cancel(true);
            }
            movieSearchTask = new MovieSearchTask();
            movieSearchTask.execute(POPULAR_MODE);
        }else if(choseMode == RATE_DATA_MODE){
            if(movieSearchTask != null){
                movieSearchTask.cancel(true);
            }
            movieSearchTask = new MovieSearchTask();
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

    class MovieSearchTask extends AsyncTask<Integer,Void ,MovieInfo[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieInfo[] doInBackground(Integer... choseMode) {

            if(choseMode.length == 0){
                return null;
            }

            URL url = NetWorkUtils.buildUrlForPopular(choseMode[0]);
            String sJsonData ;
            MovieInfo[] movieData = null;
            try {
                sJsonData = NetWorkUtils.getResponseFromHttpUrl(url);
                movieData = openMovieInfoJson.getDataFromMovieJson(sJsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieData;
        }

        @Override
        protected void onPostExecute(MovieInfo[] movieData) {
            if(movieData != null){
                progressBar.setVisibility(View.INVISIBLE);
                imageAdapter.setData(movieData);
            }
        }
    }

    class PrevueMovieTask extends AsyncTask<Integer,Void ,String>{


        String sId;

        public PrevueMovieTask(String sId){
            this.sId = sId;
        }

        @Override
        protected String doInBackground(Integer... integers) {
            if (integers.length == 0) {
                return null;
            }

            URL url = NetWorkUtils.buildUrlForPopular(integers[0],sId);
            String sJsonData;
            String s = null;
            try {
                sJsonData = NetWorkUtils.getResponseFromHttpUrl(url);
                s = openMovieInfoJson.getPrevueDataFromMovieJson(sJsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null){

                Log.d(TAG,"path id ---->" + s);
            }
        }
    }

   public void showPrevueMovie(String sId) throws IOException {
       new PrevueMovieTask(sId).execute(PicRecAdapter.ID_MODE);
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
