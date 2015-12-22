package com.peek.matchup.ui2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.com.adapters.NavAdapterFacebook;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.models.NavItemFacebook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyFacbookFriendsList extends ActionBarActivity {
    ListView listView;
    AccessToken accessToken;
    List<NavItemFacebook> ListFacebook;
    List<NavItemFacebook> ListFacebookcopy;
    String caller;
    EditText searchtxt;
    NavAdapterFacebook navAdapterFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_facbook_friends_list);

        String caller=getIntent().getStringExtra("place");
        listView=(ListView) findViewById(R.id.listView2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = getIntent();
                data.putExtra("name", ListFacebook.get(position).getTitle());
                data.putExtra("id", ListFacebook.get(position).getId());
// set result code and bundle data for response
                setResult(RESULT_OK, data);
                finish();
            }
        });
        initList();

        searchtxt=(EditText)findViewById(R.id.txtsearch);
        searchtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }



            @Override
            public void afterTextChanged(Editable s) {
                String text = searchtxt.getText().toString().toLowerCase(Locale.getDefault());
                List<NavItemFacebook> tmp= new ArrayList<NavItemFacebook>();;
                tmp.addAll(ListFacebookcopy);

                    ListFacebook.clear();
                    if (text.length() == 0 ) {
                        ListFacebook.addAll(tmp);
                    } else {
                        for (NavItemFacebook wp : tmp) {
                            if (wp.getTitle().toLowerCase(Locale.getDefault())
                                    .contains(text)) {
                                ListFacebook.add(wp);
                            }
                        }
                    }
                navAdapterFacebook.notifyDataSetChanged();
                }

        });


    }


    void initList()
    {
        accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken!=null) {
            GraphRequestBatch batch = new GraphRequestBatch(
                    GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject jsonObject,
                                        GraphResponse response) {
                                    // Application code for user
                                }
                            }),
                    GraphRequest.newMyFriendsRequest(
                            accessToken,
                            new GraphRequest.GraphJSONArrayCallback() {
                                @Override
                                public void onCompleted(
                                        JSONArray jsonArray,
                                        GraphResponse response) {
                                    JSONObject object = response.getJSONObject();
                                    //JSONObject data=object.optJSONObject("data");
                                    try {
                                        JSONArray values = object.getJSONArray("data");
                                        ListFacebook = new ArrayList<NavItemFacebook>();
                                        ListFacebookcopy = new ArrayList<NavItemFacebook>();
                                        for (int i = 0; i < values.length(); i++) {

                                            JSONObject friend = values.getJSONObject(i);
                                            String name = friend.getString("name");
                                            String id = friend.getString("id");
                                            //Log.d("na:", name);
                                            ListFacebook.add(new NavItemFacebook(name, id));
                                            ListFacebookcopy.add(new NavItemFacebook(name, id));


                                        }

                                         navAdapterFacebook = new NavAdapterFacebook(getApplicationContext(), R.layout.facebook_friend, ListFacebook);
                                        listView.setAdapter(navAdapterFacebook);
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }


                                }
                            })
            );
            batch.addCallback(new GraphRequestBatch.Callback() {
                @Override
                public void onBatchCompleted(GraphRequestBatch graphRequests) {
                    // Application code for when the batch finishes
                }
            });
            batch.executeAsync();
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        accessToken = AccessToken.getCurrentAccessToken();

    }

}

