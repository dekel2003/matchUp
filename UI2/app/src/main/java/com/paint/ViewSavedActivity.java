package com.paint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.peek.matchup.ui2.R;

import java.io.File;

public class ViewSavedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_saved);

        String path = get_img_path();
        File imgFile = new File(path);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imgView = (ImageView)findViewById(R.id.imageView);
            imgView.setImageBitmap(myBitmap);
        } else {
            Log.d("Ciaren", "File doesn't exist");
        }
    }

    String get_img_path() {
        String root = Environment.getExternalStorageDirectory().toString();
        return root+"/MatchUp/"+"myimg"+".jpg";
    }
}

