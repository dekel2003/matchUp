package com.paint;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.List;

import static com.paint.MyTouchEventView.deserialize;
import static com.paint.MyTouchEventView.serialize;

public class Paint_chat extends Activity {

    private MyTouchEventView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.paint_chat_layout);
        tv = new MyTouchEventView(this);

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("imageChat")
            .whereEqualTo("chatId", getIntent().getStringExtra("chatId"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject o : objects){
                    ParseFile img = (ParseFile) o.get("applicantResumeFile");
                    img.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            tv.path.addActions((LinkedHashSet<SerPath.PathAction>) deserialize(data));
                            tv.invalidate();
                        }
                    });
                }
            }
        });


//        query.getFirstInBackground(new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                if (object != null) {
//                    ParseFile applicantResume = (ParseFile) object.get("applicantResumeFile");
//                    applicantResume.getDataInBackground(new GetDataCallback() {
//                        @Override
//                        public void done(byte[] data, ParseException e) {
//                            tv.path = ((SerPath) MyTouchEventView.deserialize(data));
//                            tv.invalidate();
//                        }
//                    });
//                } else {  //  image history is not exist in table
//                    byte[] data = serialize(tv.path);
//                    final ParseFile image_file = new ParseFile("myimg.jpeg", data);
//                    image_file.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            final ParseObject jobApplication = new ParseObject("imageChat");
//                            jobApplication.put("chatId", getIntent().getStringExtra("chatId"));
//                            jobApplication.put("senderId", getIntent().getStringExtra("senderId"));
//                            jobApplication.put("applicantResumeFile", image_file);
//                            try {
//                                jobApplication.save();
//                            } catch (ParseException e1) {
//                                e1.printStackTrace();
//                            }
//                        }
//                    });
//
//                }
//            }
//        });

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




//        try {
//            tv.getDrawnMessage();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
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
                if (json.has("clear")){
                    tv.path.reset();
                    tv.invalidate();
                    try {
                        tv.getDrawnMessage(false);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return;
                }

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


                                //     tmpPath.addPath((SerPath) MyTouchEventView.deserialize(data));
                           //     tv.path = tmpPath;
                                tv.path.addActions((LinkedHashSet<SerPath.PathAction>) deserialize(data));

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

