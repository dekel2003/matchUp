package com.helperClasses;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Dekel on 1/10/2016.
 */
public class parseHelpers {

    public static String getUserIdByFacebookId(String facebookId){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("facebookId", facebookId);

        List<ParseUser> results = null;
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert results != null;
        return results.get(0).getObjectId();
    }
}
