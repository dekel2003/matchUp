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

import com.com.adapters.NavAdapterFacebook;
import com.com.fragments.MatchDetailes;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.models.NavItemFacebook;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peek.matchup.ui2.MatchDetailsActivity;
import com.peek.matchup.ui2.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 05/12/2015.
 */
public class Fragment3 extends Fragment {

    GridView gridView;
    GridView gridView2;
    List<NavItemFacebook> ListFacebook;
    List<NavItemFacebook> ListFacebook2;



    NavAdapterFacebook navAdapterFacebook;
    NavAdapterFacebook navAdapterFacebook2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment3_layout, container, false);
       // listView=(ListView) v.findViewById(R.id.listViewMatches);

        gridView=(GridView) v.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MatchDetailsActivity.class);
                intent.putExtra("namemacher", ListFacebook.get(position).getMetcher().toString());
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
                                        if(matches.get(i).get("approve1").toString().compareTo("1")==0 && matches.get(i).get("approve2").toString().compareTo("1")==0)
                                            ListFacebook2.add(new NavItemFacebook(matches.get(i).get("name2").toString(), matches.get(i).get("id2").toString(),matches.get(i).get("matcherName").toString(), matches.get(i).get("matcher").toString(),matches.get(i).get("rec2").toString(),matches.get(i).getObjectId().toString(),"1"));
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
                                                if(matches2.get(i).get("approve1").toString().compareTo("1")==0 && matches2.get(i).get("approve2").toString().compareTo("1")==0)
                                                    ListFacebook2.add(new NavItemFacebook(matches2.get(i).get("name1").toString(), matches2.get(i).get("id1").toString(),matches2.get(i).get("matcherName").toString(), matches2.get(i).get("matcher").toString(),matches2.get(i).get("rec1").toString(),matches2.get(i).getObjectId().toString(),"2"));
                                                else  if( matches2.get(i).get("approve2").toString().compareTo("1")!=0)
                                                {
                                                    ListFacebook.add(new NavItemFacebook(matches2.get(i).get("name1").toString(), matches2.get(i).get("id1").toString(),matches2.get(i).get("matcherName").toString(), matches2.get(i).get("matcher").toString(),matches2.get(i).get("rec1").toString(),matches2.get(i).getObjectId().toString(),"2"));

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
