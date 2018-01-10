package com.example.android.chosemovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.chosemovie.base.MovieInfo;
import com.example.android.chosemovie.data.BaseDataInfo;
import com.squareup.picasso.Picasso;

public class ChildActivity extends AppCompatActivity {

    ImageView showSinglePic;
    TextView nameText;
    MovieInfo movieInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        showSinglePic = findViewById(R.id.show_single_picture);
        nameText = findViewById(R.id.show_name_picture);

        Intent intent = getIntent();
        if(intent.hasExtra(BaseDataInfo.CLASS_PASS)){
            movieInfo = (MovieInfo) intent.getSerializableExtra(BaseDataInfo.CLASS_PASS);
        }

        String sBackPath = movieInfo.getPath_back();
        Picasso.with(this).load(sBackPath).into(showSinglePic);
        String showText = "\n\n\t" + getResources().getString(R.string.movie_name)
                + " :\t" + movieInfo.getTitle() + "\n\n\n"
                + "\t" + getResources().getString(R.string.movie_over_view)
                + " \n\n "+ movieInfo.getOverView() + "\n\n\n"
                + "\t"+ getResources().getString(R.string.movie_vote)
                + " :\t" + movieInfo.getVoteAver() + "\n\n\n"
                + "\t" + getResources().getString(R.string.movie_publish_date)
                + " :\t" + movieInfo.getPubDate();
        nameText.append(showText);

    }
}
