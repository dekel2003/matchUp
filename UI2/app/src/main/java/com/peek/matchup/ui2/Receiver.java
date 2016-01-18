package com.peek.matchup.ui2;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class Receiver extends ParsePushBroadcastReceiver {

    public Receiver(){super();}

    @Override
    public void onPushReceive(Context context, Intent intent) {
//        Toast.makeText(context, "Push received!!!!.", Toast.LENGTH_LONG).show();

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Log.d("-Reciever", "raw JSONE object recieved: " + json.toString());
            Intent intentChatInformation = new Intent(json.getString("intention"));
            json = json.getJSONObject("value");
            json.put("makeAlert", true);
            Log.d("-Reciever", "JSONE object: " + json.toString());

            intentChatInformation.putExtra("message", json.toString());
//            if (intentChatInformation.getAction().equals("updateChat")) {
//                intentChatInformation.putExtra("message", json.toString());
//            }else if (intentChatInformation.getAction().equals("updateImage")) {
//                intentChatInformation.putExtra("message", json.toString());
//            }

            context.sendBroadcast(intentChatInformation);

//            if (ChatActivity.active)

        } catch (JSONException e) {
            Log.d("-Reciever", "JSONException: " + e.getMessage());
            e.printStackTrace();
        }
        Log.i("-Reciever", "Push Received");
        Log.d("-Reciever", "received " + intent.getAction());

//        notification.flags |= Notification.FLAG_AUTO_CANCEL;

//        getNotification(context, intent).flags |= Notification.FLAG_AUTO_CANCEL;
//        getNotification(context, intent).contentIntent;
        super.onPushReceive(context, intent);

    }


}
