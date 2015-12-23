package com.helperClasses;

import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Dekel on 12/23/2015.
 */
public class sendNotifications {
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
}
