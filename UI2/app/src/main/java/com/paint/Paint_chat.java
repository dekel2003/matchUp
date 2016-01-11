package com.paint;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chat.ChatMessage;
import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peek.matchup.ui2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Paint_chat extends Activity {

    private MyTouchEventView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.paint_chat_layout);
        tv = new MyTouchEventView(this);
        //ViewGroup layout = (ViewGroup) findViewById(R.id.mylayout);
        //tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //layout.addView(tv);

        setContentView(tv);
        addContentView(tv.parentLinearLayout, tv.params);

    }


    //register your activity onResume()
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter("updateImage"));
        Log.i("Dekel Image Register:", "registered");
    }

    //Must unregister onPause()
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
        Log.i("Dekel Image Register:", "unregistered");
        finish();
    }

    //This is the handler that will manager to process the broadcast intent
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Dekel Chat Recieve:", "start");
            // Extract data included in the Intent
            JSONObject json = null;
            try {
                json = new JSONObject(intent.getStringExtra("message"));
//                Date date = format.parse(json.getString("dateTime"));
//                json.put("dateTime",date);
                Log.i("Dekel Image Recieve: ", json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                Log.i("Dekel Image Recieve: ", "null pointer exception..");
            }


            Gson gson = new Gson();
            PaintIdMessage ImageId = null;
            if (json != null) {
                ImageId = gson.fromJson(json.toString(), PaintIdMessage.class);
                Log.i("Image:", ImageId.getId());
            }else{
                Log.e("Dekel Image", "json is null");
            }

//            ParseObject obj = Pa
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("imageChat");
            query.whereEqualTo("objectId", ImageId.getId());

            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    ParseFile applicantResume = (ParseFile) object.get("applicantResumeFile");
                    applicantResume.getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                // data has the bytes for the resume
                              //  tv.bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                              //  tv.path.reset();
                                tv.path=(SerPath) tv.deserialize(data);
                                tv.invalidate();

                            } else {
                                e.printStackTrace();
                                // something went wrong
                            }
                        }
                    });
                }
            });


        }
    };
}

