package com.com.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.inner.fragment.Fragment6;
import com.models.RangeSeekBar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peek.matchup.ui2.MainActivity;
import com.peek.matchup.ui2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by User on 06/12/2015.
 */
public class Profile extends Fragment {

    String myId;
    String pic1="";
    String pic2="";
    String pic3="";
    String pic4="";
    String pic5="";
    ProfilePictureView profilePictureView;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    EditText aboutme;
    Button save;
    FragmentActivity myContext;
    Fragment f;
     FragmentManager fragManager;

    public Profile() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_profile,container,false);
        f=this;
        fragManager = myContext.getSupportFragmentManager();
        profilePictureView=(ProfilePictureView) v.findViewById(R.id.ppic);
        imageView1=(ImageView)v.findViewById(R.id.image1);
        imageView2=(ImageView)v.findViewById(R.id.image2);
        imageView3=(ImageView)v.findViewById(R.id.image3);
        imageView4=(ImageView)v.findViewById(R.id.image4);
        imageView5=(ImageView)v.findViewById(R.id.image5);
        aboutme=(EditText)v.findViewById(R.id.editText);
        aboutme.setText("");
        save=(Button) v.findViewById(R.id.button);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlbums(11);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlbums(12);
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlbums(13);
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlbums(14);
            }
        });
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlbums(15);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("profiles");
                query.whereEqualTo("facebookId", myId);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> nameList, ParseException e) {
                        if (e == null && nameList!=null) {

                            for (ParseObject Obj : nameList) {
                                Obj.put("pic1", pic1);
                                Obj.put("pic2", pic2);
                                Obj.put("pic3", pic3);
                                Obj.put("pic4", pic4);
                                Obj.put("pic5", pic5);
                                Obj.put("aboutme", aboutme.getText().toString());

                                Obj.saveInBackground();
                                Toast.makeText(getActivity().getApplicationContext(), "Profile saved succesfully", Toast.LENGTH_LONG).show();
                            }

                        } else {

                        }
                    }
                });


            }
        });
        getPpic();
        return v;
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void ShowAlbums(int num)
    {
        MyAlbums dialog  = new MyAlbums();
        dialog.setTargetFragment(f, num);
        // Show DialogFragment
        dialog .show(fragManager, "Dialog Fragment");

    }

    private void initProfile()
    {
       // ParseQuery<ParseObject> query =ParseQuery.getQuery("profiles");
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("profiles");
        query.whereEqualTo("facebookId", myId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> nameList, ParseException e) {
                if(e==null)
                {
                    if(nameList.size()>=1) {
                        pic1 = nameList.get(0).get("pic1").toString();
                        pic2 = nameList.get(0).get("pic2").toString();
                        pic3 = nameList.get(0).get("pic3").toString();
                        pic4 = nameList.get(0).get("pic4").toString();
                        pic5 = nameList.get(0).get("pic5").toString();
                        if (pic1.compareTo("")!=0)
                            Picasso.with(getContext()).load(pic1).resize(200, 200)
                                    .centerCrop().into(imageView1);
                        if (pic2.compareTo("")!=0)
                            Picasso.with(getContext()).load(pic2).resize(200, 200)
                                    .centerCrop().into(imageView2);
                        if (pic3.compareTo("")!=0)
                            Picasso.with(getContext()).load(pic3).resize(200, 200)
                                    .centerCrop().into(imageView3);
                        if (pic4.compareTo("")!=0)
                            Picasso.with(getContext()).load(pic4).resize(200, 200)
                                    .centerCrop().into(imageView4);
                        if (pic5.compareTo("")!=0)
                            Picasso.with(getContext()).load(pic5).resize(200, 200)
                                    .centerCrop().into(imageView5);
                        aboutme.setText(nameList.get(0).get("aboutme").toString());
                        aboutme.setSelection(aboutme.getText().length());
                    }
                    else
                    {  ParseObject addProf= new ParseObject("profiles");
                        addProf.put("facebookId", myId);
                        addProf.put("pic1", "");
                        addProf.put("pic2", "");
                        addProf.put("pic3", "");
                        addProf.put("pic4", "");
                        addProf.put("pic5", "");
                        addProf.put("aboutme","");
                        addProf.saveInBackground();
                    }

                }
                else
                {

                }
            }
        });

    }

   private void getPpic()
    {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        myId=object.optString("id");
                        profilePictureView.setProfileId(myId);
                        initProfile();

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode ==getActivity().RESULT_OK )
        {
            String id = data.getExtras().getString("id", "");

            if (requestCode == 11) {
                if(id.compareTo("")!=0)
                    Picasso.with(getContext()).load(id).resize(200, 200)
                        .centerCrop().into(imageView1);
                else
                    Picasso.with(getContext()).load(R.mipmap.pictmp).resize(200, 200)
                            .centerCrop().into(imageView1);
                pic1=id;

            }
            if (requestCode == 12) {
                if(id.compareTo("")!=0)
                    Picasso.with(getContext()).load(id).resize(200, 200)
                        .centerCrop().into(imageView2);
                else
                    Picasso.with(getContext()).load(R.mipmap.pictmp).resize(200, 200)
                            .centerCrop().into(imageView2);

                pic2=id;

            }
            if (requestCode == 13) {
                if(id.compareTo("")!=0)
                  Picasso.with(getContext()).load(id).resize(200, 200)
                        .centerCrop().into(imageView3);
                else
                    Picasso.with(getContext()).load(R.mipmap.pictmp).resize(200, 200)
                            .centerCrop().into(imageView3);
                pic3=id;

            }
            if (requestCode == 14) {
                if(id.compareTo("")!=0)
                    Picasso.with(getContext()).load(id).resize(200, 200)
                            .centerCrop().into(imageView4);
                else
                    Picasso.with(getContext()).load(R.mipmap.pictmp).resize(200, 200)
                            .centerCrop().into(imageView4);
                pic4=id;
            }
            if (requestCode == 15) {

                if(id.compareTo("")!=0)
                    Picasso.with(getContext()).load(id).resize(200, 200)
                            .centerCrop().into(imageView5);
                else
                    Picasso.with(getContext()).load(R.mipmap.pictmp).resize(200, 200)
                            .centerCrop().into(imageView5);
                pic5=id;
            }

        }


    }



}
