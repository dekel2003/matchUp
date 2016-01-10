package com.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.com.fragments.Setting;
import com.google.gson.Gson;
import com.helperClasses.SendNotifications;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.peek.matchup.ui2.R;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import static com.helperClasses.SendNotifications.SendJSONByParseId;


public class ChatActivity extends ActionBarActivity {
    public static boolean active = false;

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initControls();

    }

    //register your activity onResume()
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter("updateChat"));
        Log.i("Dekel Chat Register:", "registered");
    }

    //Must unregister onPause()
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
        Log.i("Dekel Chat Register:", "unregistered");
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
                Log.i("Dekel Chat Recieve: ", json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                Log.i("Dekel Chat Recieve: ", "null pointer exception..");
            }


            Gson gson = new Gson();
            ChatMessage chatMessage = null;
            if (json != null) {
                chatMessage = gson.fromJson(json.toString(), ChatMessage.class);
                chatMessage.setMe(false);
                displayMessage(chatMessage);
            }else{
                Log.e("Dekel Chat", "json is null");
            }




        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);

        TextView meLabel = (TextView) findViewById(R.id.meLbl);
        TextView companionLabel1 = (TextView) findViewById(R.id.friendLabel1);
        TextView companionLabel2 = (TextView) findViewById(R.id.friendLabel2);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);


        Log.d("Chat Activity: ", getIntent().getStringExtra("id"));
        Log.d("Chat Activity: ", getIntent().getStringExtra("id1"));
        Log.d("Chat Activity: ", getIntent().getStringExtra("id2"));

        final String id = getIntent().getStringExtra("id");
        final String id1 = getIntent().getStringExtra("id1");
        final String id2 = getIntent().getStringExtra("id2");

//          SendNotifications.notifyByParseId(id);

        meLabel.setText(getUserNameByParseID(id));

        companionLabel1.setText(getUserNameByParseID(id1));
        companionLabel2.setText(getUserNameByParseID(id2));

        loadDummyHistory();
        loadHistory(id);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }
                ParseObject message = new ParseObject("ChatMessages");
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(id);
                message.put("senderId", id);
                String chatId = getIntent().getStringExtra("chatId");
                message.put("chatId", chatId);
                chatMessage.setMessage(messageText);
                message.put("content", messageText);
                chatMessage.setDate(format.format(new Date()));
                message.put("date", format.parse(chatMessage.getDate(), new ParsePosition(0)));
                chatMessage.setMe(true);

                messageET.setText("");

                Gson gson = new Gson();
                JSONObject request = null;
                try {
                    JSONObject msgObj = new JSONObject(gson.toJson(chatMessage));
                    request = new JSONObject();
                    request.putOpt("value", msgObj);
                    request.put("intention", "updateChat");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SendJSONByParseId(id1, request);
                SendJSONByParseId(id2, request);

                displayMessage(chatMessage);
                message.saveInBackground();


            }
        });


    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    static private String getUserNameByParseID(String id){
        ParseQuery<ParseUser> query_user = ParseUser.getQuery();
        query_user.whereEqualTo("objectId", id);
        String name = null;
        try {
            name = query_user.find().get(0).get("name").toString();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Chat Activity: ", "Error load Name by id: " + id);
        }
        Log.d("Chat Activity: ", "Name0 : " + name);
        return name;
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }


    private void loadHistory(String id) {
//        final String id = getIntent().getStringExtra("id");
        String chatId = getIntent().getStringExtra("chatId");
        if (chatId.isEmpty()) {
            Log.d("Chat: ", "Error - chat ID is invalid");
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChatMessages");
        query.whereEqualTo("chatId", chatId);
        List<ParseObject> messages = null;
        try {
            messages = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert messages != null;
        for (ParseObject message : messages) {
            ChatMessage msg = new ChatMessage();
            msg.setId(message.getString("senderId"));
            msg.setMe(true);
            if (!msg.getId().equals(id))
                msg.setMe(false);
            msg.setDate(format.format(message.getDate("date")));
            msg.setMessage(message.getString("content"));
            displayMessage(msg);
        }
    }


    private void loadDummyHistory(){

        chatHistory = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId("1");
        msg.setMe(false);
        msg.setMessage("Hi");
        msg.setDate(format.format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId("2");
        msg1.setMe(false);
        msg1.setMessage("How r u doing???");
        msg1.setDate(format.format(new Date()));
        chatHistory.add(msg1);

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

                for(int i=0; i<chatHistory.size(); i++) {
                    ChatMessage message = chatHistory.get(i);
                    displayMessage(message);
                }

    }

}
