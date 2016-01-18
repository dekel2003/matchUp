package com.paint;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chat.ChatMessage;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.List;

import static com.helperClasses.SendNotifications.SendJSONByParseId;
import static com.paint.MyTouchEventView.deserialize;
import static com.paint.MyTouchEventView.serialize;

public class Paint_chat extends Activity {

    private MyTouchEventView tv;
    public Button btnReset, btnSave, btnGotoSaved;
    LinearLayout parentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.paint_chat_layout);

        RelativeLayout relativeLayout = new RelativeLayout(this);
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


//        parentLinearLayout = new LinearLayout(context);

        btnReset = new Button(this);
        btnReset.setText("Clear Screen");
        btnSave = new Button(this);
        btnSave.setText("Save");

        parentLinearLayout = new LinearLayout(this);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.weight = 1.0f;
//        params.gravity = Gravity.RIGHT;
//        btnReset.setLayoutParams(params);

        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params2.addRule(RelativeLayout.RIGHT_OF, btnSave.getId());

        btnReset.setLayoutParams(params2); //causes layout update


        RelativeLayout.LayoutParams tv_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tv_params.addRule(RelativeLayout.ALIGN_BOTTOM, btnSave.getId());
        tv_params.addRule(RelativeLayout.ALIGN_BOTTOM, btnReset.getId());

        tv_params.addRule(RelativeLayout.BELOW, btnSave.getId());
        tv_params.addRule(RelativeLayout.BELOW, btnReset.getId());
        tv_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

//        tv_params.setMargins(0, 0, 0, 0);

        parentLinearLayout.setLayoutParams(tv_params); //causes layout update


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ParseQuery<ParseObject> query = ParseQuery.getQuery("imageChat")
                        .whereEqualTo("chatId", getIntent().getStringExtra("chatId"));
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        for (ParseObject o : objects){
                            o.deleteInBackground();
                        }
                    }
                });

                Gson gson = new Gson();
                JSONObject request = null;
                try {
                    JSONObject msgObj = new JSONObject();
                    msgObj.put("clear", true);
                    request = new JSONObject();
                    request.putOpt("value", msgObj);
                    request.put("intention", "updateImage");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent MyIntent = getIntent();
                final String senderId = MyIntent.getStringExtra("senderId");
                final String id1 = MyIntent.getStringExtra("id1");
                final String id2 = MyIntent.getStringExtra("id2");
                if (senderId.equals(id2))
                    SendJSONByParseId(id1, request);
                else
                    SendJSONByParseId(id2, request);


//                String chatId = MyIntent.getStringExtra("chatId");
//                ParseQuery queryReset = ParseQuery.getQuery("imageChat");
//                queryReset.whereEqualTo("chatId",chatId);
//                queryReset.


                tv.path.reset();
                try {
                    tv.getDrawnMessage();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                tv.postInvalidate();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = tv.createBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                byte[] data = stream.toByteArray();

                final ParseFile image_file = new ParseFile("myimg", data);
                image_file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent MyIntent = getIntent();
                        final String chatId = MyIntent.getStringExtra("chatId");
                        final String senderId = MyIntent.getStringExtra("senderId");
                        final String id1 = MyIntent.getStringExtra("id1");
                        final String id2 = MyIntent.getStringExtra("id2");
                        final ParseObject object = new ParseObject("ChatMessages");
                        object.put("chatId", chatId);
                        object.put("senderId", senderId);
                        object.put("attachment", image_file);
                        object.put("type", "img");
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                String chatMsgId = object.getObjectId();
//                                JSONObject msgObj = new JSONObject();
                                Gson gson = new Gson();
                                JSONObject request = null;
                                ChatMessage msg = new ChatMessage();
                                msg.setId(senderId);
                                msg.setMessage(chatMsgId);
                                msg.setSpecialType("AddPaint");
                                try {
//                                    msgObj.put("msgId", chatMsgId);
//                                    msgObj.put("type", "paint");

                                    JSONObject msgObj = new JSONObject(gson.toJson(msg));
                                    request = new JSONObject();
                                    request.putOpt("value", msgObj);
                                    request.put("intention", "updateChat");
                                } catch (JSONException ee) {
                                    ee.printStackTrace();
                                }

                                Log.i("Paint:", request.toString());

                                if (senderId.equals(id2))
                                    SendJSONByParseId(id1, request);
                                else
                                    SendJSONByParseId(id2, request);
                            }
                        });
                    }
                });
            }
        });

//        btnGotoSaved.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(this, ViewSavedActivity.class);
//                getContext().startActivity(myIntent);
//            }
//        });





        relativeLayout.addView(parentLinearLayout);
        relativeLayout.addView(btnReset);
        relativeLayout.addView(btnSave);
        btnSave.setVisibility(View.INVISIBLE);
        parentLinearLayout.addView(tv);



        setContentView(relativeLayout);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        relativeLayout.setLayoutParams(params);
//        addContentView(tv.parentLinearLayout, tv.params);

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
                    if (e == null) {
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
                    } else {
                    e.printStackTrace();
                    // something went wrong
                }
                }
            });


        }
    };
}

