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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.helperClasses.SendNotifications;
import com.parse.ParseObject;
import com.peek.matchup.ui2.R;

import org.json.JSONObject;


public class Fragment2 extends Fragment {

    ProfilePictureView profilePictureView1;
    ProfilePictureView profilePictureView2;
    TextView textView1;
    TextView textView2;
    EditText editText1;
    EditText editText2;
    private final int REQUEST_CODE1 = 20;
    private final int REQUEST_CODE2 = 21;
    public static final int DATEPICKER_FRAGMENT1=20;
    public static final int DATEPICKER_FRAGMENT2=21;
    public static final int DATEPICKER_FRAGMENT3=23;
    private FragmentActivity myContext;

    private String id1 = null;
    private String id2 = null;
    private String name1 = "";
    private String name2 = "";
    private Button btn;

    private String user_name;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment2_layout, container, false);
        final Fragment f = this;
        final FragmentManager fragManager = myContext.getSupportFragmentManager();

        user_name = getArguments().getString("user_name");
        Log.i("Fragment 2:", "username is-" + user_name);

        editText1 = (EditText) v.findViewById(R.id.editText1);
        editText2 = (EditText) v.findViewById(R.id.editText2);
//        editText1.setText("");
//        editText2.setText("");

        profilePictureView1 = (ProfilePictureView) v.findViewById(R.id.image1);
        profilePictureView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Intent i=new Intent(getActivity(),MyFacbookFriendsList.class);


                //   getActivity().startActivityForResult(i, REQUEST_CODE1);
                Fragment6 dialog = new Fragment6();
                dialog.setTargetFragment(f, DATEPICKER_FRAGMENT1);
                // Show DialogFragment
                dialog.show(fragManager, "Dialog Fragment");

            }
        });
        profilePictureView2 = (ProfilePictureView) v.findViewById(R.id.image2);
        profilePictureView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Intent i=new Intent(getActivity(),MyFacbookFriendsList.class);

                // getActivity().startActivityForResult(i, REQUEST_CODE2);
                Fragment6 dialog = new Fragment6();
                dialog.setTargetFragment(f, DATEPICKER_FRAGMENT2);
                // Show DialogFragment
                dialog.show(fragManager, "Dialog Fragment");
            }
        });
        textView1 = (TextView) v.findViewById(R.id.textView2);
        textView2 = (TextView) v.findViewById(R.id.textView3);


        btn = (Button) v.findViewById(R.id.btnMatchNow);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                GraphRequest request = GraphRequest.newMeRequest(
//                        AccessToken.getCurrentAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(
//                                    JSONObject object,
//                                    GraphResponse response) {
//                                String name = object.optString("name");
//                                String id = object.optString("id");
//                                String link = object.optString("link");
//
//                                SendNotifications.newMatch(id1, id2);
//                                Toast.makeText(getActivity().getApplicationContext(),"Match has been proposed succesfully",Toast.LENGTH_LONG).show();
//                                ParseObject addMatch = new ParseObject("Matches");
//                                addMatch.put("id1", id1);
//                                addMatch.put("id2", id2);
//
//                                addMatch.put("name1", name1);
//                                addMatch.put("name2", name2);
//
//                                addMatch.put("rec1", editText1.getText().toString());
//                                addMatch.put("rec2",  editText2.getText().toString());
//
//
//                                addMatch.put("matcher", id);
//                                addMatch.put("matcherName", name);
//                                addMatch.put("approve1", "0");
//                                addMatch.put("approve2", "0");
//
//                                addMatch.saveInBackground();
//
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,link");
//                request.setParameters(parameters);
//                request.executeAsync();
//
//
//
//            }
//        });

                ConfirmMatch dialog = new ConfirmMatch();
                Bundle args = new Bundle();
                args.putString("id1",id1);
                args.putString("id2",id2);
                args.putString("name1",name1);
                args.putString("name2",name2);
                args.putString("user_name",user_name);
                dialog.setArguments(args);
                dialog.setTargetFragment(f, DATEPICKER_FRAGMENT3);
                // Show DialogFragment
                dialog.show(fragManager, "Dialog Fragment");

            }
        });

        return v;
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode ==getActivity().RESULT_OK )
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == DATEPICKER_FRAGMENT1) {
                String name = data.getExtras().getString("name", "");
                String id = data.getExtras().getString("id", "");
                profilePictureView1.setProfileId(id);
                textView1.setText(name);
                id1 = id;
                name1=name;
            }
            else if (requestCode == DATEPICKER_FRAGMENT2)  {
                String name = data.getExtras().getString("name", "");
                String id = data.getExtras().getString("id", "");
                profilePictureView2.setProfileId(id);
                textView2.setText(name);
                id2 = id;
                name2=name;
            }else if (requestCode == DATEPICKER_FRAGMENT3)  {
                profilePictureView1.setProfileId(null);
                profilePictureView2.setProfileId(null);
                textView1.setText("");
                textView2.setText("");
                id1 = null;
                id2 = null;
                name1=null;
                name2=null;
                btn.setVisibility(View.GONE);
                SendNotifications.newMatch(id1, id2);
                Toast.makeText(getActivity().getApplicationContext(),"Match has been proposed succesfully",Toast.LENGTH_LONG).show();
            }
            if (id1!=null && id2!=null)
                btn.setVisibility(View.VISIBLE);
        }
    }
}
