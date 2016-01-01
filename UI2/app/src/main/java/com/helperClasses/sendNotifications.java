package com.helperClasses;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseSession;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dekel on 12/23/2015.
 */
public class SendNotifications {
    public static void newMatch(String Alice_id, String Bob_id){
        ParseQuery<ParseInstallation> parseInstallationParseQuery = ParseInstallation.getQuery();
        parseInstallationParseQuery.whereEqualTo("FacebookID",Alice_id);

        ParsePush push = new ParsePush();
        push.setMessage("You got a new Match");
        push.setQuery(parseInstallationParseQuery);
        push.sendInBackground();

        push = new ParsePush();
        parseInstallationParseQuery = ParseInstallation.getQuery();
        parseInstallationParseQuery.whereEqualTo("FacebookID", Bob_id);
        push.setMessage("You got a new Match");
        push.setQuery(parseInstallationParseQuery);
        push.sendInBackground();

    }

    public static void testPush(String ParseId) {
        final ParseQuery<ParseSession> query = ParseSession.getQuery();

        ParseQuery<ParseUser> user_query = ParseUser.getQuery();
        user_query.whereEqualTo("objectId", ParseId);
        ParseUser user = null;
        try {
            user = user_query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Collection<String> res = new ArrayList<>();
        res.add("installationId");
        query.selectKeys(res).whereEqualTo("user", user);



        try {
            List<ParseSession> l = query.find();
            for (ParseSession i : l) {
                Log.d("Push: ", i.getString("installationId"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<ParseInstallation> qi = ParseInstallation.getQuery();
        qi.whereMatchesKeyInQuery("installationId", "installationId", query);

        ParsePush push = new ParsePush();
        push.setMessage("Test notification");
        push.setQuery(qi);
        push.sendInBackground();

    }


    public static void notifyByParseId(String ParseId) {
        final ParseQuery<ParseSession> query = ParseSession.getQuery();

        ParseQuery<ParseUser> user_query = ParseUser.getQuery();
        user_query.whereEqualTo("objectId", ParseId);
        ParseUser user = null;
        try {
            user = user_query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Collection<String> res = new ArrayList<>();
        res.add("installationId");
        query.selectKeys(res).whereEqualTo("user", user);

        ParseQuery<ParseInstallation> qi = ParseInstallation.getQuery();
        qi.whereMatchesKeyInQuery("installationId", "installationId", query);

        ParsePush push = new ParsePush();
        push.setMessage("You got a new message");
        push.setQuery(qi);
        push.sendInBackground();
    }


    public static void SendJSONByParseId(String ParseId, JSONObject jo) {
        final ParseQuery<ParseSession> query = ParseSession.getQuery();

        ParseQuery<ParseUser> user_query = ParseUser.getQuery();
        user_query.whereEqualTo("objectId", ParseId);
        ParseUser user = null;
        try {
            user = user_query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Collection<String> res = new ArrayList<>();
        res.add("installationId");
        query.selectKeys(res).whereEqualTo("user", user);

        ParseQuery<ParseInstallation> qi = ParseInstallation.getQuery();
        qi.whereMatchesKeyInQuery("installationId", "installationId", query);

        ParsePush push = new ParsePush();
////        push.setMessage("You got a new message");
//        JSONObject jo2 = new JSONObject();
//        try {
//            jo2.put("DPHM",jo);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        push.setData(jo);
        push.setQuery(qi);
        push.sendInBackground();
    }

}
