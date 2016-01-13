package com.inner.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.chat.ChatActivity;
import com.com.adapters.NavAdapterFacebook;
import com.com.fragments.MatchDetailes;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.models.NavItemFacebook;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.peek.matchup.ui2.MatchDetailsActivity;
import com.peek.matchup.ui2.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.helperClasses.parseHelpers.getUserIdByFacebookId;


/**
 * Created by User on 05/12/2015.
 */
public class Fragment3 extends Fragment {

    GridView gridView;
    GridView gridView2;
    List<NavItemFacebook> ListFacebook;
    List<NavItemFacebook> ListFacebook2;
    List<ParseObject> Chats;


    NavAdapterFacebook navAdapterFacebook;
    NavAdapterFacebook navAdapterFacebook2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment3_layout, container, false);
       // listView=(ListView) v.findViewById(R.id.listViewMatches);

        Chats = new ArrayList<>();

        gridView=(GridView) v.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MatchDetailsActivity.class);
                intent.putExtra("namemacher", ListFacebook.get(position).getMetcher());
                intent.putExtra("idmacher", ListFacebook.get(position).getMetcherid());
                intent.putExtra("idmyMatch", ListFacebook.get(position).getId());
                intent.putExtra("rec", ListFacebook.get(position).getRec());
                intent.putExtra("matchId", ListFacebook.get(position).getMatchID());
                intent.putExtra("side", ListFacebook.get(position).getSide());
                //match id and match side
                startActivity(intent);


            }
        });
        gridView2=(GridView) v.findViewById(R.id.gridView2);

        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String facebook_matcher = Chats.get(position).getString("matcher");
                String facebook_id1 = Chats.get(position).getString("id1");
                String facebook_id2 = Chats.get(position).getString("id2");

                String matcher = getUserIdByFacebookId(facebook_matcher, getContext());
                String id1 = getUserIdByFacebookId(facebook_id1, getContext());
                String id2 = getUserIdByFacebookId(facebook_id2, getContext());

                ParseQuery<ParseObject> existingChat = ParseQuery.getQuery("OpenChats");
                existingChat.whereEqualTo("matcher", matcher).whereEqualTo("id1", id1).whereEqualTo("id2", id2);

                List<ParseObject> foundExistingChat = null;
                try {
                    foundExistingChat = existingChat.find();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String chatID;
                if (!foundExistingChat.isEmpty()){
                    chatID = foundExistingChat.get(0).getObjectId();
                }else {
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
                Intent intent = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
                Bundle args = new Bundle();
                args.putString("id", matcher);

                args.putString("id1", id1);  //  the first User-Object_ID
                args.putString("id2", id2);  //  the second User-Object_ID
                args.putString("chatId", chatID); // unique chat-id for this specific conversation.

                args.putString("help1", Chats.get(position).getString("help1"));
                args.putString("help2", Chats.get(position).getString("help2"));
                args.putString("help3", Chats.get(position).getString("help3"));


                intent.putExtras(args);
                startActivity(intent);

            }
        });




        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        makeTabels();
    }

    private void makeTabels()
    {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        final String id = object.optString("id");



                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Matches");
                        query.whereEqualTo("id1", id);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> matches, ParseException e) {
                                if (e == null) {


                                    ListFacebook = new ArrayList<NavItemFacebook>();
                                    ListFacebook2 = new ArrayList<NavItemFacebook>();
                                    for (int i = 0; i < matches.size(); i++) {
                                        if (matches.get(i).get("approve1").toString().compareTo("1") == 0 && matches.get(i).get("approve2").toString().compareTo("1") == 0){
                                            ListFacebook2.add(new NavItemFacebook(matches.get(i).get("name2").toString(), matches.get(i).get("id2").toString(), matches.get(i).get("matcherName").toString(), matches.get(i).get("matcher").toString(), matches.get(i).get("rec2").toString(), matches.get(i).getObjectId().toString(), "1"));
                                            Chats.add(matches.get(i));
                                        }
                                        else if(matches.get(i).get("approve1").toString().compareTo("1")!=0)
                                        {
                                            ListFacebook.add(new NavItemFacebook(matches.get(i).get("name2").toString(), matches.get(i).get("id2").toString(),matches.get(i).get("matcherName").toString(), matches.get(i).get("matcher").toString(),matches.get(i).get("rec2").toString(),matches.get(i).getObjectId().toString(),"1"));
                                        }

                                    }

                                } else {
                                    Log.d("Matches Fragment(3):", "Error: " + e.getMessage());
                                }
                                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Matches");
                                query2.whereEqualTo("id2", id);
                                query2.findInBackground(new FindCallback<ParseObject>() {
                                    public void done(List<ParseObject> matches2, ParseException e) {
                                        if (e == null) {

                                            //allMatches.addAll(matches);
                                            for (int i = 0; i < matches2.size(); i++) {
                                                if(matches2.get(i).getString("approve1").compareTo("1")==0 && matches2.get(i).getString("approve2").compareTo("1")==0) {
                                                    ListFacebook2.add(new NavItemFacebook(matches2.get(i).getString("name1"), matches2.get(i).getString("id1"), matches2.get(i).getString("matcherName"), matches2.get(i).getString("matcher"), matches2.get(i).getString("rec1"), matches2.get(i).getObjectId(), "2"));
                                                    Chats.add(matches2.get(i));
                                                }
                                                else  if(matches2.get(i).getString("approve2").compareTo("1")!=0)
                                                {
                                                    ListFacebook.add(new NavItemFacebook(matches2.get(i).getString("name1"), matches2.get(i).getString("id1"), matches2.get(i).getString("matcherName"), matches2.get(i).getString("matcher"), matches2.get(i).getString("rec1"), matches2.get(i).getObjectId(),"2"));

                                                }

                                            }

                                            navAdapterFacebook = new NavAdapterFacebook(getActivity().getApplicationContext(), R.layout.matches_view, ListFacebook);
                                            navAdapterFacebook2 = new NavAdapterFacebook(getActivity().getApplicationContext(), R.layout.matches_view, ListFacebook2);
                                            // listView.setAdapter(navAdapterFacebook);
                                            gridView.setAdapter(navAdapterFacebook);
                                            gridView2.setAdapter(navAdapterFacebook2);

                                        } else {
                                            Log.d("score", "Error: " + e.getMessage());

                                            }

                                        }

                                });
                            }
                        });


                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id");
        request.setParameters(parameters);
        request.executeAsync();


    }


}
