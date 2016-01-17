package com.inner.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.com.adapters.NavAdapterFacebook;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.models.NavItemFacebook;
import com.parse.ParseObject;
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
public class ConfirmMatch extends DialogFragment {

    Button confirm_button;
    EditText rec1, rec2;
    EditText help1, help2, help3;
//    NavAdapterFacebook navAdapterFacebook;
//    List<NavItemFacebook> ListFacebookcopy;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.confirm_match_layout, container, false);
        getDialog().setTitle("Wrtite reccomodations about each friend");



//        listView=(ListView) v.findViewById(R.id.listView2);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent()
//                        .putExtra("name", ListFacebook.get(position).getTitle())
//                        .putExtra("id", ListFacebook.get(position).getId());
//
//                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
//                dismiss();
//            }
//        });

        rec1=(EditText)v.findViewById(R.id.reccomodation1);
        rec2=(EditText)v.findViewById(R.id.reccomodation2);

        help1=(EditText)v.findViewById(R.id.help1);
        help2=(EditText)v.findViewById(R.id.help2);
        help3=(EditText)v.findViewById(R.id.help3);

        confirm_button=(Button)v.findViewById(R.id.completeMatch);

        final Bundle args = getArguments();

        rec1.setHint(rec1.getHint() + args.getString("name1"));
        rec2.setHint(rec2.getHint() + args.getString("name2"));

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ParseObject addMatch = new ParseObject("Matches");
                addMatch.put("id1", args.getString("id1"));
                addMatch.put("id2", args.getString("id2"));

                addMatch.put("name1", args.getString("name1"));
                addMatch.put("name2", args.getString("name2"));

                addMatch.put("rec1", rec1.getText().toString());
                addMatch.put("rec2", rec2.getText().toString());

                addMatch.put("help1", help1.getText().toString());
                addMatch.put("help2", help2.getText().toString());
                addMatch.put("help3", help3.getText().toString());

                String id = AccessToken.getCurrentAccessToken().getUserId();
                addMatch.put("matcher", id);
                addMatch.put("matcherName", args.getString("user_name"));
                addMatch.put("approve1", "0");
                addMatch.put("approve2", "0");

                addMatch.saveInBackground();

                Intent i = new Intent();
//                        .putExtra("name", ListFacebook.get(position).getTitle())
//                        .putExtra("id", ListFacebook.get(position).getId());
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                dismiss();
            }
        });


        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onStart()
    {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        int dialogWidth = width*8/9 ;// specify a value here
        int dialogHeight = height*4/5; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

        // ... other stuff you want to do in your onStart() method
    }

}
