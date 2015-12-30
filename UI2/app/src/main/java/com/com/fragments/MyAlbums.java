package com.com.fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;

import com.com.adapters.NavAdapterFacePic;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.models.NavItemFacebook;
import com.peek.matchup.ui2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 06/12/2015.
 */
public class MyAlbums extends DialogFragment {
    GridView gridView;
    Spinner spinner;
    List<NavItemFacebook> ListFacebook;
    List<String> Albums;
    List<String> Albumsid;
    Button del;
    NavAdapterFacePic navAdapterFacePic;
    
   // private ArrayList<FacebookAlbum> alFBAlbum;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.myalbum_fragment,container,false);
        getDialog().setTitle("Change Photo");
        del=(Button)v.findViewById(R.id.delbutton);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent()
                        .putExtra("id", "");

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                dismiss();
            }
        });
        gridView=(GridView) v.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent()
                        .putExtra("id", ListFacebook.get(position).getId());

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                dismiss();
            }
        });
        spinner=(Spinner)  v.findViewById(R.id.planets_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetFacebookImages(Albumsid.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Albums=new ArrayList<String>();
        Albumsid=new ArrayList<String>();



      //  new DownloadImageTask((ImageView) v.findViewById(R.id.image1))
        //        .execute("https://fbcdn-sphotos-d-a.akamaihd.net//hphotos-ak-xtp1//v//t1.0-9//10155190_561760367264178_23134930_n.jpg?oh=87f39d2f427856e626a58ac73838ac7a&oe=571E5D75&__gda__=1459531547_778f1973f7c517e43aa0235323696430");
      //   alFBAlbum = new ArrayList<>();
/*make API call*/
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),  //your fb AccessToken
                "/" + AccessToken.getCurrentAccessToken().getUserId() + "/albums",//user id of login user
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("TAG", "Facebook Albums: " + response.toString());
                        try {
                            if (response.getError() == null) {
                                JSONObject joMain = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data"); //find JSONArray from JSONObject
                                   // alFBAlbum = new ArrayList<>();
                                    for (int i = 0; i < jaData.length(); i++) {//find no. of album using jaData.length()
                                        JSONObject joAlbum = jaData.getJSONObject(i); //convert perticular album into JSONObject
                                        Albums.add(joAlbum.optString("name"));
                                        Albumsid.add(joAlbum.optString("id"));
                                       // if(i==0)
                                         //   GetFacebookImages(joAlbum.optString("id")); //find Album ID and get All Images from album

                                    }
                                    ArrayAdapter<String> navAdapterDDL = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spiner_item,R.id.albumname, Albums);
                                    spinner.setAdapter(navAdapterDDL);
                                }

                            } else {
                                Log.d("Test", response.getError().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();


                        }
                    }
                }
        ).executeAsync();




        return v;

    }

    public void GetFacebookImages(final String albumId) {
//        String url = "https://graph.facebook.com/" + "me" + "/"+albumId+"/photos?access_token=" + AccessToken.getCurrentAccessToken() + "&fields=images";
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Log.v("TAG", "Facebook Photos response: " + response);
                       // tvTitle.setText("Facebook Images");
                        try {
                            if (response.getError() == null) {


                                JSONObject joMain = response.getJSONObject();
                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data");
                                    List <Object>lstFBImages = new ArrayList<>();
                                    ListFacebook = new ArrayList<NavItemFacebook>();
                                    for (int i = 0; i < jaData.length(); i++)//Get no. of images
                                     {
                                         //if (i>5)break;
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                          JSONArray jaImages=joAlbum.getJSONArray("images"); //get images Array in JSONArray format

                                               if(jaImages.length()>0)
                                                {



                                                 ListFacebook.add(new NavItemFacebook("a",jaImages.getJSONObject(0).getString("source").toString()));



                                       // Images objImages=new Images();//Images is custom class with string url field
                                        //objImages.setImage_url(jaImages.getJSONObject(0).getString("source"));
                                        //lstFBImages.add(objImages);//lstFBImages is Images object array
                                               }
                                     }

                                //set your adapter
                                    navAdapterFacePic = new NavAdapterFacePic(getActivity().getApplicationContext(), R.layout.facebbokpic_view, ListFacebook);
                                    // listView.setAdapter(navAdapterFacebook);
                                    gridView.setAdapter(navAdapterFacePic);
                            }

                        }else {
                            Log.v("TAG", response.getError().toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
    }
    ).executeAsync();
}


}
