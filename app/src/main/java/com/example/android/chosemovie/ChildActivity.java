package com.example.android.chosemovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private MovieDetailSearchTask movieDetailSearchTask;
    private OpenMovieInfoJson openMovieInfoJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        openMovieInfoJson = OpenMovieInfoJson.getInstance();
        showSinglePic = findViewById(R.id.show_single_picture);
        nameText = findViewById(R.id.show_name_picture);

        Intent intent = getIntent();
        if (intent.hasExtra(BaseDataInfo.CLASS_PASS)) {
            movieInfo = (MovieInfo) intent.getSerializableExtra(BaseDataInfo.CLASS_PASS);
        }

        LinearLayout[] linearLayouts = new LinearLayout[3];
        linearLayouts[0] = findViewById(R.id.show_but_first);
        linearLayouts[1] = findViewById(R.id.show_but_second);
        linearLayouts[2] = findViewById(R.id.show_but_third);

        movieDetailSearchTask = new MovieDetailSearchTask(openMovieInfoJson,linearLayouts , this);
        movieDetailSearchTask.execute(movieInfo.getId());



/*
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
*/
    }
}
