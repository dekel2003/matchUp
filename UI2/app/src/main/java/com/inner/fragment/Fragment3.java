package com.inner.fragment;

import android.app.Activity;
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
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.models.NavItemFacebook;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peek.matchup.ui2.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 05/12/2015.
 */
public class Fragment3 extends Fragment {

    GridView gridView;
    List<NavItemFacebook> ListFacebook;
    private FragmentActivity myContext;


    NavAdapterFacebook navAdapterFacebook;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment3_layout, container, false);
       // listView=(ListView) v.findViewById(R.id.listViewMatches);
        final Fragment f=this;
        final FragmentManager fragManager = myContext.getSupportFragmentManager();
        gridView=(GridView) v.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment5 dialog  = new Fragment5();
                dialog.setTargetFragment(f, position);
                Bundle args = new Bundle();
                args.putString("namemacher", ListFacebook.get(position).getMetcher());
                args.putString("idmacher", ListFacebook.get(position).getMetcherid());
                dialog.setArguments(args);
                // Show DialogFragment
                dialog .show(fragManager, "Dialog Fragment");
            }
        });



            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            final String id = object.optString("id");

                            final List<ParseObject> allMatches = new ArrayList<ParseObject>();

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Matches");
                            query.whereEqualTo("id1", id);
                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> matches, ParseException e) {
                                    if (e == null) {

                                        //allMatches.addAll(matches);
                                        ListFacebook = new ArrayList<NavItemFacebook>();
                                        for (int i = 0; i < matches.size(); i++) {
                                            ListFacebook.add(new NavItemFacebook(matches.get(i).get("name2").toString(), matches.get(i).get("id2").toString(),matches.get(i).get("matcherName").toString(), matches.get(i).get("matcher").toString()));
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
                                                    ListFacebook.add(new NavItemFacebook(matches2.get(i).get("name1").toString(), matches2.get(i).get("id1").toString(),matches2.get(i).get("matcherName").toString(), matches2.get(i).get("matcher").toString()));
                                                }
                                                navAdapterFacebook = new NavAdapterFacebook(getActivity().getApplicationContext(), R.layout.matches_view, ListFacebook);
                                               // listView.setAdapter(navAdapterFacebook);
                                                gridView.setAdapter(navAdapterFacebook);








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


        return v;
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
}
