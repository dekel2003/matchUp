package com.inner.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.com.adapters.NavAdapterFacebook;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.ProfilePictureView;
import com.models.NavItemFacebook;
import com.peek.matchup.ui2.MyFacbookFriendsList;
import com.peek.matchup.ui2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by User on 05/12/2015.
 */
public class Fragment6 extends DialogFragment {
    Button button;
    ListView listView;
    AccessToken accessToken;
    List<NavItemFacebook> ListFacebook;
    EditText searchtxt;
    NavAdapterFacebook navAdapterFacebook;
    List<NavItemFacebook> ListFacebookcopy;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment6_layout, container, false);
        getDialog().setTitle("Choose Your Friend");



        listView=(ListView) v.findViewById(R.id.listView2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent()
                        .putExtra("name", ListFacebook.get(position).getTitle())
                        .putExtra("id", ListFacebook.get(position).getId());

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                dismiss();
            }
        });
        searchtxt=(EditText)v.findViewById(R.id.txtsearch);
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






        return v;
    }

    void initList()
    {

        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        try {
                            JSONArray values = response.getJSONObject().getJSONArray("data");
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

                            navAdapterFacebook = new NavAdapterFacebook(getActivity().getApplicationContext(), R.layout.facebook_friend, ListFacebook);
                            listView.setAdapter(navAdapterFacebook);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).executeAsync();
    }




    @Override
    public void onResume() {
        super.onResume();
        initList();

    }
    @Override
    public void onStart()
    {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        int dialogWidth = 600 ;// specify a value here
        int dialogHeight = 800; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

        // ... other stuff you want to do in your onStart() method
    }

}
