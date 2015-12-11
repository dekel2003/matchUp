import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

class Examples {
    private void learnHowToPushAndPullDataFromParseServer(){

//      This is how we upload data to the server:
//        it will be under a database called "GameScore"
        ParseObject gameScore = new ParseObject("GameScore");
//        it have the following 3 fields:
        gameScore.put("score", 1337);
        gameScore.put("playerName", "Sean Plott");
        gameScore.put("cheatMode", false);
//        save the data using thread:
        gameScore.saveInBackground();

//        And this is how we pull back the data from the server:
//        SELECT FROM GameScore:
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");

//        WHERE playername = "Sean Plott".
//        there are more methods under query, for querying the database.
        query.whereEqualTo("playerName", "Sean Plott");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
}
