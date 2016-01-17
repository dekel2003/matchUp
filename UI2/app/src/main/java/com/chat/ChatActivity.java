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

import com.google.gson.Gson;
import com.paint.Paint_chat;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.peek.matchup.ui2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.helperClasses.ParseErrorHandler.handleParseError;
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
    private final String id = ParseUser.getCurrentUser().getObjectId();
    private String match_name = "";
    private String chatId;

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
        loadHistory(id);
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
            ChatMessage chatMessage;
            if (json != null) {
                chatMessage = gson.fromJson(json.toString(), ChatMessage.class);
                if (!chatMessage.getchatId().equals(chatId))
                    return;
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
        int itemId = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (itemId == R.id.action_paint) {

            Gson gson = new Gson();
            JSONObject request = null;
            try {
//                JSONObject msgObj = new JSONObject(gson.toJson(chatMessage));
                request = new JSONObject();
                request.putOpt("value", "activate");
                request.put("intention", "paint");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final String id1 = getIntent().getStringExtra("id1");
            final String id2 = getIntent().getStringExtra("id2");
            final String match_id = (id.equals(id1)) ? id2 : id1;
            SendJSONByParseId(match_id, request);

            Intent intent = new Intent(ChatActivity.this,Paint_chat.class);
            intent.putExtra("chatId",chatId);
            intent.putExtra("id1",getIntent().getStringExtra("id1"));
            intent.putExtra("id2",getIntent().getStringExtra("id2"));
            intent.putExtra("senderId", ParseUser.getCurrentUser().getObjectId());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

//        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

//        final String id = getIntent().getStringExtra("id");
        final String id1 = getIntent().getStringExtra("id1");
        final String id2 = getIntent().getStringExtra("id2");

        final String match_id = (id.equals(id1)) ? id2 : id1;
        chatId = getIntent().getStringExtra("chatId");

//        updateUserNameByParseID(match_id);

//          SendNotifications.notifyByParseId(id);

//        meLabel.setText(getUserNameByParseID(id));



//        loadDummyHistory();



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }
                ParseObject message = new ParseObject("ChatMessages");
                message.put("type", "msg");
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(id);
                message.put("senderId", id);
//                String chatId = getIntent().getStringExtra("chatId");
                chatMessage.setchatId(chatId);
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
                    request.put("alert", "You got a new message from " + ParseUser.getCurrentUser().getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                SendJSONByParseId(match_id, request);

                displayMessage(chatMessage);
                message.saveInBackground();


            }
        });


    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
        if (adapter.getCount()%5==0){
            if (adapter.getCount()==5){
                String help1 = getIntent().getStringExtra("help1");
                if (help1==null || help1.length()<1)
                    return;
                ChatMessage helpMessage = new ChatMessage();
                helpMessage.setSpecialType("MatcherMsg");
                helpMessage.setMessage(help1);
                adapter.add(helpMessage);
                adapter.notifyDataSetChanged();
                scroll();
            }
            if (adapter.getCount()==10){
                String help1 = getIntent().getStringExtra("help2");
                if (help1==null || help1.length()<1)
                    return;
                ChatMessage helpMessage = new ChatMessage();
                helpMessage.setSpecialType("MatcherMsg");
                helpMessage.setMessage(help1);
                adapter.add(helpMessage);
                adapter.notifyDataSetChanged();
                scroll();
            }
            if (adapter.getCount()==15){
                String help1 = getIntent().getStringExtra("help3");
                if (help1==null || help1.length()<1)
                    return;
                ChatMessage helpMessage = new ChatMessage();
                helpMessage.setSpecialType("MatcherMsg");
                helpMessage.setMessage(help1);
                adapter.add(helpMessage);
                adapter.notifyDataSetChanged();
                scroll();
            }
        }
    }

    private void updateUserNameByParseID(final String match_id){



        ParseQuery<ParseUser> query_user = ParseUser.getQuery();
        query_user.whereEqualTo("objectId", match_id);
        final String[] name = {null};
        query_user.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.d("Chat Activity: ", "Error load Name by id: " + match_id);
                    e.printStackTrace();
                    handleParseError(ChatActivity.this.getBaseContext(), e);
                }
                name[0] = user.getString("name");
                match_name = name[0];
            }
        });



    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }


    private void loadHistory(String id) {
//        final String id = getIntent().getStringExtra("id");

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
        if (messages == null)
            return;
        for (ParseObject message : messages) {
            if (message.has("type") && message.getString("type").equals("msg")) {
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
    }


    private void loadDummyHistory(){

        chatHistory = new ArrayList<>();

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



                for(int i=0; i<chatHistory.size(); i++) {
                    ChatMessage message = chatHistory.get(i);
                    displayMessage(message);
                }

    }


}
