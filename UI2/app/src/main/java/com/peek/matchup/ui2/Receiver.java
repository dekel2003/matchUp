package com.peek.matchup.ui2;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.chat.ChatActivity;
import com.parse.ParseAnalytics;
import com.parse.ParseBroadcastReceiver;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Receiver extends ParsePushBroadcastReceiver {

    public Receiver(){super();}

    @Override
    public void onPushReceive(Context context, Intent intent) {
//        Toast.makeText(context, "Push received!!!!.", Toast.LENGTH_LONG).show();

        super.onPushReceive(context, intent);

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Intent intentChatInformation = new Intent(json.getString("intention"));
            json = json.getJSONObject("value");
            intentChatInformation.putExtra("message", json.toString());
            context.sendBroadcast(intentChatInformation);

            Log.d("Dekel", "JSONE object: " + json.toString());
//            if (ChatActivity.active)

        } catch (JSONException e) {
            Log.d("Dekel", "JSONException: " + e.getMessage());
            e.printStackTrace();
        }
        Log.i("Dekel", "Push Received");
        Log.d("Dekel", "received " + intent.getAction());
    }
}
