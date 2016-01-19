package com.inner.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;

import com.chat.ChatActivity;
import com.chat.ChatMessage;
import com.com.adapters.NavAdapterFacebook;
import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.helperClasses.ParseErrorHandler;
import com.models.NavItemFacebook;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.peek.matchup.ui2.MatchDetailsActivity;
import com.peek.matchup.ui2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.helperClasses.parseHelpers.getUserIdByFacebookId;


public class Fragment3 extends Fragment {

    GridView gridView;
    GridView gridView2;
    List<NavItemFacebook> ListFacebook;
    List<NavItemFacebook> ListFacebook2;
    List<ParseObject> Chats;
    String userId = AccessToken.getCurrentAccessToken().getUserId();


    NavAdapterFacebook navAdapterFacebook;
    NavAdapterFacebook navAdapterFacebook2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment3_layout, container, false);
        final Animation anim= AnimationUtils.loadAnimation(getActivity(), R.anim.zoomoutandin);
        // listView=(ListView) v.findViewById(R.id.listViewMatches);

        Chats = new ArrayList<>();


        gridView = (GridView) v.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                view.startAnimation(anim);
               final Intent intent = new Intent(getActivity(), MatchDetailsActivity.class);
                intent.putExtra("namemacher", ListFacebook.get(position).getMetcher());
                intent.putExtra("idmacher", ListFacebook.get(position).getMetcherid());
                intent.putExtra("idmyMatch", ListFacebook.get(position).getId());
                intent.putExtra("rec", ListFacebook.get(position).getRec());
                intent.putExtra("matchId", ListFacebook.get(position).getMatchID());
                intent.putExtra("side", ListFacebook.get(position).getSide());

                 Runnable task = new Runnable() {
                    public void run() {
                        // Execute your delayed code


                        //match id and match side
                        startActivity(intent);


                    }
                };
                Handler handler = new Handler();
                int millisDelay = 800;
                handler.postDelayed(task, millisDelay);

            }
        });
        gridView2 = (GridView) v.findViewById(R.id.gridView2);

        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.startAnimation(anim);

                String facebook_matcher = Chats.get(position).getString("matcher");
                String facebook_id1 = Chats.get(position).getString("id1");
                String facebook_id2 = Chats.get(position).getString("id2");

                String matcher = getUserIdByFacebookId(facebook_matcher, getContext());

                String id1, id2;
                if (facebook_id1.equals(userId))
                    id1 = ParseUser.getCurrentUser().getObjectId();
                else
                    id1 = getUserIdByFacebookId(facebook_id1, getContext());

                if (facebook_id2.equals(userId))
                    id2 = ParseUser.getCurrentUser().getObjectId();
                else
                    id2 = getUserIdByFacebookId(facebook_id2, getContext());

                ParseQuery<ParseObject> existingChat = ParseQuery.getQuery("OpenChats");
                existingChat.whereEqualTo("matcher", matcher).whereEqualTo("id1", id1).whereEqualTo("id2", id2);

                List<ParseObject> foundExistingChat = null;
                try {
                    foundExistingChat = existingChat.find();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String chatID;
                if (foundExistingChat != null && !foundExistingChat.isEmpty()) {
                    chatID = foundExistingChat.get(0).getObjectId();
                } else {
                    ParseObject chat = new ParseObject("OpenChats");
                    chat.put("matcher", matcher);
                    chat.put("id1", id1);
                    chat.put("id2", id2);

                    try {
                        chat.save();
                    } catch (ParseException e) {
                        Log.d("Fragment 3:", "Error in creating a new chat.");
                        e.printStackTrace();
                    }

                    chatID = chat.getObjectId();
                }
                final Intent intent = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
                Bundle args = new Bundle();
                args.putString("id", matcher);

                args.putString("id1", id1);  //  the first User-Object_ID
                args.putString("id2", id2);  //  the second User-Object_ID
                args.putString("chatId", chatID); // unique chat-id for this specific conversation.

                args.putString("help1", Chats.get(position).getString("help1"));
                args.putString("help2", Chats.get(position).getString("help2"));
                args.putString("help3", Chats.get(position).getString("help3"));


                intent.putExtras(args);
                Runnable task = new Runnable() {
                    public void run() {
                        startActivity(intent);
                    }};
                Handler handler = new Handler();
                int millisDelay = 800;
                handler.postDelayed(task, millisDelay);


            }
        });


        makeTabels();

        return v;
    }


    private void makeTabels(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Matches");
        query.whereEqualTo("id1", userId).fromLocalDatastore();
        //if(query.hasCachedResult());
           // query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Matches");
        query2.whereEqualTo("id2", userId).fromLocalDatastore();
       // if(query2.hasCachedResult());
         //   query2.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        List<ParseQuery<ParseObject>> queries = new ArrayList<>();
        queries.add(query);
        queries.add(query2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries).fromLocalDatastore();

        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> matches, ParseException e) {
                if (e == null) {
                    ListFacebook = new ArrayList<>();
                    ListFacebook2 = new ArrayList<>();
                    for (ParseObject match : matches) {
                        if (match.getString("id1").equals(userId)) {
                            if (match.getString("approve1").equals("1") && match.getString("approve2").equals("1")) {
                                ListFacebook2.add(new NavItemFacebook(match.get("name2").toString(), match.get("id2").toString(), match.get("matcherName").toString(), match.get("matcher").toString(), match.get("rec2").toString(), match.getObjectId(), "1"));
                                Chats.add(match);
                            } else if (match.get("approve1").toString().compareTo("1") != 0) {
                                ListFacebook.add(new NavItemFacebook(match.get("name2").toString(), match.get("id2").toString(), match.get("matcherName").toString(), match.get("matcher").toString(), match.get("rec2").toString(), match.getObjectId(), "1"));
                            }
                        } else {
                            if (match.getString("approve1").equals("1") && match.getString("approve2").equals("1")) {
                                ListFacebook2.add(new NavItemFacebook(match.getString("name1"), match.getString("id1"), match.getString("matcherName"), match.getString("matcher"), match.getString("rec1"), match.getObjectId(), "2"));
                                Chats.add(match);
                            } else if (match.get("approve2").toString().compareTo("1") != 0) {
                                ListFacebook.add(new NavItemFacebook(match.getString("name1"), match.getString("id1"), match.getString("matcherName"), match.getString("matcher"), match.getString("rec1"), match.getObjectId(), "2"));
                            }
                        }
                    }
                } else {
                    Log.d("Matches Fragment(3):", "Error: " + e.getMessage());
                }
                if (getActivity() == null)
                    return;

                navAdapterFacebook = new NavAdapterFacebook(getActivity(), R.layout.matches_view, ListFacebook);
                navAdapterFacebook2 = new NavAdapterFacebook(getActivity(), R.layout.matches_view, ListFacebook2);

                gridView.setAdapter(navAdapterFacebook);
                gridView2.setAdapter(navAdapterFacebook2);

                ParseObject.unpinAllInBackground("Matches", matches, new DeleteCallback() {
                    public void done(ParseException e) {
                        if (e != null) {
                            // There was some error.
                            return;
                        }

                        // Add the latest results for this query to the cache.
                        ParseObject.pinAllInBackground("Matches", matches);
                    }
                });
            }
        });
    }


    //register your activity onResume()
    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(mMessageReceiver, new IntentFilter("refreshFragment3"));
        makeTabels();
    }

    //Must unregister onPause()
    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(mMessageReceiver);
        Log.i("Dekel Chat Register:", "unregistered");
    }


    //This is the handler that will manager to process the broadcast intent
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            makeTabels();
//            JSONObject json = null;
//            try {
//                json = new JSONObject(intent.getStringExtra("message"));
////                Date date = format.parse(json.getString("dateTime"));
////                json.put("dateTime",date);
//                Log.i("Dekel Chat Recieve: ", json.toString());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (NullPointerException e){
//                Log.i("Dekel Chat Recieve: ", "null pointer exception..");
//            }
//
//
//            Gson gson = new Gson();
//            ChatMessage chatMessage;
//            if (json != null) {
//                chatMessage = gson.fromJson(json.toString(), ChatMessage.class);
//                if (!chatMessage.getchatId().equals(chatId))
//                    return;
//                chatMessage.setMe(false);
//                displayMessage(chatMessage);
//            }else{
//                Log.e("Dekel Chat", "json is null");
//            }
        }
    };



//    private void makeTabels_old() {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Matches");
//        query.whereEqualTo("id1", userId);
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> matches, ParseException e) {
//                if (e == null) {
//
//
//                    ListFacebook = new ArrayList<>();
//                    ListFacebook2 = new ArrayList<>();
//                    for (int i = 0; i < matches.size(); i++) {
//                        if (matches.get(i).get("approve1").toString().compareTo("1") == 0 && matches.get(i).get("approve2").toString().compareTo("1") == 0) {
//                            ListFacebook2.add(new NavItemFacebook(matches.get(i).get("name2").toString(), matches.get(i).get("id2").toString(), matches.get(i).get("matcherName").toString(), matches.get(i).get("matcher").toString(), matches.get(i).get("rec2").toString(), matches.get(i).getObjectId(), "1"));
//                            Chats.add(matches.get(i));
//                        } else if (matches.get(i).get("approve1").toString().compareTo("1") != 0) {
//                            ListFacebook.add(new NavItemFacebook(matches.get(i).get("name2").toString(), matches.get(i).get("id2").toString(), matches.get(i).get("matcherName").toString(), matches.get(i).get("matcher").toString(), matches.get(i).get("rec2").toString(), matches.get(i).getObjectId(), "1"));
//                        }
//
//                    }
//
//                } else {
//                    Log.d("Matches Fragment(3):", "Error: " + e.getMessage());
//                }
//                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Matches");
//                query2.whereEqualTo("id2", userId);
//                query2.findInBackground(new FindCallback<ParseObject>() {
//                    public void done(List<ParseObject> matches2, ParseException e) {
//                        if (e == null) {
//
//                            //allMatches.addAll(matches);
//                            for (int i = 0; i < matches2.size(); i++) {
//                                if (matches2.get(i).getString("approve1").compareTo("1") == 0 && matches2.get(i).getString("approve2").compareTo("1") == 0) {
//                                    ListFacebook2.add(new NavItemFacebook(matches2.get(i).getString("name1"), matches2.get(i).getString("id1"), matches2.get(i).getString("matcherName"), matches2.get(i).getString("matcher"), matches2.get(i).getString("rec1"), matches2.get(i).getObjectId(), "2"));
//                                    Chats.add(matches2.get(i));
//                                } else if (matches2.get(i).getString("approve2").compareTo("1") != 0) {
//                                    ListFacebook.add(new NavItemFacebook(matches2.get(i).getString("name1"), matches2.get(i).getString("id1"), matches2.get(i).getString("matcherName"), matches2.get(i).getString("matcher"), matches2.get(i).getString("rec1"), matches2.get(i).getObjectId(), "2"));
//
//                                }
//
//                            }
//
//                            if (getActivity() == null)
//                                return;
//                            navAdapterFacebook = new NavAdapterFacebook(getActivity(), R.layout.matches_view, ListFacebook);
//                            navAdapterFacebook2 = new NavAdapterFacebook(getActivity(), R.layout.matches_view, ListFacebook2);
//
//                            gridView.setAdapter(navAdapterFacebook);
//                            gridView2.setAdapter(navAdapterFacebook2);
//
//                        } else {
//                            Log.d("score", "Error: " + e.getMessage());
//
//                        }
//
//                    }
//
//                });
//            }
//        });
//
//    }

}
