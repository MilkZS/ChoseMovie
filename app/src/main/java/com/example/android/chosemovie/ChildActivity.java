package com.example.android.chosemovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.chosemovie.base.MovieInfo;
import com.example.android.chosemovie.data.BaseDataInfo;
import com.example.android.chosemovie.utility.MovieDetailSearchTask;
import com.example.android.chosemovie.utility.OpenMovieInfoJson;
import com.squareup.picasso.Picasso;

public class ChildActivity extends AppCompatActivity {

    ImageView showSinglePic;
    TextView nameText;
    MovieInfo movieInfo;
    MovieDetailSearchTask movieDetailSearchTask;
    private OpenMovieInfoJson openMovieInfoJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        openMovieInfoJson = OpenMovieInfoJson.getInstance();
        showSinglePic = findViewById(R.id.show_single_picture);
        nameText = findViewById(R.id.show_name_picture);

        Intent intent = getIntent();
        if(intent.hasExtra(BaseDataInfo.CLASS_PASS)){
            movieInfo = (MovieInfo) intent.getSerializableExtra(BaseDataInfo.CLASS_PASS);
        }


        String sPopularPath = movieInfo.getPath_back();
        Picasso.with(this).load(sPopularPath).into(showSinglePic);
        String showText = "\n\n\t" + getResources().getString(R.string.movie_name)
                + " :\t" + movieInfo.getTitle() + "\n\n\n"
                + "\t" + getResources().getString(R.string.movie_over_view)
                + " \n\n "+ movieInfo.getOverView() + "\n\n\n"
                + "\t"+ getResources().getString(R.string.movie_vote)
                + " :\t" + movieInfo.getVoteAver() + "\n\n\n"
                + "\t" + getResources().getString(R.string.movie_publish_date)
                + " :\t" + movieInfo.getPubDate() + "\n\n\n";
        nameText.append(showText);

    }

    public void test(View view){
        Toast.makeText(this,"click",Toast.LENGTH_SHORT).show();
       // if (movieDetailSearchTask == null ){
            movieDetailSearchTask = new MovieDetailSearchTask(openMovieInfoJson);
            movieDetailSearchTask.execute(movieInfo.getId());
      //  }


    }
}
