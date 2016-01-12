package com.helperClasses;

import android.content.Context;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.helperClasses.ParseErrorHandler.handleParseError;

/**
 * Created by Dekel on 1/10/2016.
 */
public class parseHelpers {

    public static String getUserIdByFacebookId(String facebookId, Context context){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("facebookId", facebookId);

        List<ParseUser> results = null;
        try {
            results = query.find();
        } catch (ParseException e) {
            handleParseError(context, e);
            e.printStackTrace();
        }
        assert results != null;
        return results.get(0).getObjectId();
    }

}
