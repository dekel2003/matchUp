package com.inner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.com.adapters.NavAdapterFacebook;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.models.NavItemFacebook;
import com.peek.matchup.ui2.MyFacbookFriendsList;
import com.peek.matchup.ui2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 05/12/2015.
 */
public class Fragment6 extends Fragment {
    Button button;
    ListView listView;
    AccessToken accessToken;
    List<NavItemFacebook> ListFacebook;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment6_layout,container,false);





        accessToken = AccessToken.getCurrentAccessToken();
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
                                    for (int i = 0; i < values.length(); i++) {

                                        JSONObject friend = values.getJSONObject(i);
                                        String name = friend.getString("name");
                                        String id = friend.getString("id");
                                        //Log.d("na:", name);
                                        ListFacebook.add(new NavItemFacebook(name,id));

                                    }

                                    NavAdapterFacebook navAdapterFacebook = new NavAdapterFacebook(getActivity().getApplicationContext(), R.layout.facebook_friend, ListFacebook);
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



        listView=(ListView)v.findViewById(R.id.listView3);

        return v;
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
