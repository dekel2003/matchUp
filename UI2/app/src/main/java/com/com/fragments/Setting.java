package com.com.fragments;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.models.RangeSeekBar;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.peek.matchup.ui2.LoginFacebook;
import com.peek.matchup.ui2.R;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by User on 06/12/2015.
 */
public class Setting extends Fragment {

    public Setting() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_setting,container,false);

        Button btn_leave = (Button) v.findViewById(R.id.leaveBtn);

        btn_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure you want to leave MatchUp?")
                        .setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==DialogInterface.BUTTON_NEGATIVE)
                            return;
                        deleteUser();
                    }
                }).create().show();
            }
        });



        return v;
    }

    private void deleteUser() {

        String id = ParseUser.getCurrentUser().getObjectId();
        String fidA = AccessToken.getCurrentAccessToken().getUserId();
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Matches");
        query1.whereEqualTo("id1", fidA);

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Matches");
        query2.whereEqualTo("id2", fidA);

        ParseQuery<ParseObject> query3a = ParseQuery.getQuery("OpenChats");
        query3a.whereEqualTo("id1", id);

        ParseQuery<ParseObject> query3b = ParseQuery.getQuery("OpenChats");
        query3b.whereEqualTo("id2", id);

        List<ParseQuery<ParseObject>> _query3 = new ArrayList<>();
        _query3.add(query3a);
        _query3.add(query3b);

        ParseQuery<ParseObject> query3 = ParseQuery.or(_query3);

        ParseQuery<ParseObject> query4 = ParseQuery.getQuery("ChatMessages");
        query4.whereMatchesKeyInQuery("chatId", "objectId", query3);

        ParseQuery<ParseObject> query5 = ParseQuery.getQuery("imageChat");
        query5.whereMatchesKeyInQuery("chatId", "objectId", query3);

        ParseQuery<ParseObject> query6 = ParseQuery.getQuery("profiles");
        query6.whereEqualTo("facebookId", fidA);


        List<ParseQuery<ParseObject>> query = new ArrayList<>();
        query.add(query1);
        query.add(query2);
        query.add(query3);
        query.add(query4);
        query.add(query5);
        query.add(query6);

//        ParseQuery<ParseObject> mainQuery = ParseQuery.or(query);

        for (ParseQuery<ParseObject> q : query) {
            List<ParseObject> objectList = null;
            try {
                objectList = q.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (objectList == null)
                return;

            for (ParseObject o : objectList) {
                o.deleteInBackground();
            }
        }
        ParseFacebookUtils.unlinkInBackground(ParseUser.getCurrentUser());
        ParseUser.getCurrentUser().deleteInBackground();


        getActivity().finish();

    }


}
