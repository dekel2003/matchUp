package com.paint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chat.ChatMessage;
import com.google.gson.Gson;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static com.helperClasses.SendNotifications.SendJSONByParseId;

/**
 * Created by Omer on 05/01/2016.
 */
public class MyTouchEventView extends View {
    private Paint paint = new Paint();
    public SerPath path = new SerPath();
    //private Paint circlePaint = new Paint();
    //private Path circlePath = new Path();


//    public ViewGroup.LayoutParams params;
    public Bitmap bitmap;



    public MyTouchEventView(Context context) {
        super(context);

        bitmap = null;

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(10f);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        //canvas.drawColor(Color.GREEN);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
        canvas.drawPath(path, paint);
        //canvas.drawPath(circlePath, circlePaint);
    }

    protected Bitmap createBitmap(){
        Bitmap bitmap;

        View parentLinearLayout = this;

        parentLinearLayout.setDrawingCacheEnabled(true);
//        layout(0, 0, 100, 100);
        parentLinearLayout.buildDrawingCache();
        Toast.makeText(getContext(), "Saving Image to chat", Toast.LENGTH_SHORT).show();
//        String root = Environment.getExternalStorageDirectory().toString();
//            File imgDir = new File(root+"/MatchUp/");
//            String imgName;
        bitmap = Bitmap.createBitmap(parentLinearLayout.getDrawingCache());
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();

//        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        return bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //super.onTouchEvent(event);

        float pointX = event.getX();
        float pointY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                //circlePath.reset();

                // circlePath.addCircle(pointX, pointY, 30, Path.Direction.CW);
                break;
            case MotionEvent.ACTION_UP:
                // circlePath.reset();
                try {
                    getDrawnMessage();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // super.onLayout(changed, left, top, right, bottom);
    }

    public void getDrawnMessage() throws FileNotFoundException {
        getDrawnMessage(true);
    }

    public void getDrawnMessage(final boolean sendPush) throws FileNotFoundException {
        //parentLinearLayout.setVisibility(View.INVISIBLE);
        //Bitmap bitmap;
        //View v = getRootView();
        //v.setDrawingCacheEnabled(true);
        //layout(0, 0, 100, 100);
        //v.buildDrawingCache();
        //Toast.makeText(getContext(), "Toasting", Toast.LENGTH_SHORT).show();
//        String root = Environment.getExternalStorageDirectory().toString();
        //      File imgDir = new File(root+"/MatchUp/");
        //    String imgName;
        //bitmap = Bitmap.createBitmap(v.getDrawingCache());
        //bitmap = Bitmap.createBitmap(bitmap, 0, 20, bitmap.getWidth(), bitmap.getHeight()-20);
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);


        //byte[] data = stream.toByteArray();
        invalidate();
        byte[] data = serialize(path.actions);
        final ParseFile image_file = new ParseFile("myimg", data);
        image_file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Intent MyIntent = ((Activity) getContext()).getIntent();
                final String chatId = MyIntent.getStringExtra("chatId");
                final String senderId = MyIntent.getStringExtra("senderId");
                final String id1 = MyIntent.getStringExtra("id1");
                final String id2 = MyIntent.getStringExtra("id2");
                ParseQuery<ParseObject> query = ParseQuery.getQuery("imageChat")
                        .whereEqualTo("chatId", chatId)
                        .whereEqualTo("senderId", senderId);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (object == null)
                            object = new ParseObject("imageChat");
                        object.put("chatId", chatId);
                        object.put("senderId", senderId);
                        object.put("applicantResumeFile", image_file);
                        final ParseObject finalObject = object;
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (!sendPush)
                                    return;
                                PaintIdMessage paintIdMessaget = new PaintIdMessage();
                                String imgId = finalObject.getObjectId();
                                paintIdMessaget.setId(imgId);
                                Gson gson = new Gson();
                                JSONObject request = null;
                                try {
                                    JSONObject msgObj = new JSONObject(gson.toJson(paintIdMessaget));
                                    request = new JSONObject();
                                    request.putOpt("value", msgObj);
                                    request.put("intention", "updateImage");
                                } catch (JSONException ee) {
                                    ee.printStackTrace();
                                }

                                if (senderId.equals(id2))
                                    SendJSONByParseId(id1, request);
                                else
                                    SendJSONByParseId(id2, request);
                            }
                        });
                    }
                });

//
//                final ParseObject jobApplication = new ParseObject("imageChat");
//
//                jobApplication.put("chatId", chatId);
//                jobApplication.put("senderId", senderId);
//
//                jobApplication.put("applicantResumeFile", image_file);
//
//                jobApplication.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        PaintIdMessage paintIdMessaget = new PaintIdMessage();
//                        String imgId = jobApplication.getObjectId();
//                        paintIdMessaget.setId(imgId);
//                        Gson gson = new Gson();
//                        JSONObject request = null;
//                        try {
//                            JSONObject msgObj = new JSONObject(gson.toJson(paintIdMessaget));
//                            request = new JSONObject();
//                            request.putOpt("value", msgObj);
//                            request.put("intention", "updateImage");
//                        } catch (JSONException ee) {
//                            ee.printStackTrace();
//                        }
//
//                        Log.i("Paint:", request.toString());
//
//                        if (senderId.equals(id2))
//                            SendJSONByParseId(id1, request);
//                        else
//                            SendJSONByParseId(id2, request);
//                    }
//                });
//            }
//        });
//
//        invalidate();

            }
        });
    }

    //for (int i = 0; i < 10; ++i) for (int j = 0; j < 10; ++j)
    //bitmap.setPixel(i,j,Color.GREEN);

    //imgDir.mkdirs();
    //imgName = "myimg"+".jpg";
    //File file = new File(imgDir,imgName);
    //if(file.exists()) file.delete();
    //FileOutputStream outImg = new FileOutputStream(file);
    // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outImg);
    // v.setDrawingCacheEnabled(false);
    //parentLinearLayout.setVisibility(View.VISIBLE);


    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return is.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}