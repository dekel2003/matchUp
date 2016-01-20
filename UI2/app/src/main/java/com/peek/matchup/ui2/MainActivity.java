package com.peek.matchup.ui2;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.com.adapters.NavAdapter;
import com.com.fragments.MyAlbums;
import com.com.fragments.Home;
import com.com.fragments.Out;
import com.com.fragments.Profile;
import com.com.fragments.Setting;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.ProfilePictureView;
import com.helperClasses.ParseErrorHandler;
import com.models.NavIteam;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ui.ParseLoginBuilder;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

import static com.helperClasses.parseHelpers.getUserIdByFacebookId;

public class MainActivity extends ActionBarActivity {

    DrawerLayout drawerLayout;
    RelativeLayout drawerPane;
    ListView listView;

    List<NavIteam> navIteamList;
    List<Fragment> fragmentList;
    Serializable sfragmentList;
    Home home;
    Profile profile;

    String name, id;


    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Log.i("Main Activity", "create all again");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.dopen, R.string.dclose) {
            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }
        };



        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        if (savedInstanceState==null)
            loadUserDetails();
        else{
            id = savedInstanceState.getString("id");
            name = savedInstanceState.getString("name");
        }

        loadActivityComponents(savedInstanceState);

//        String birthday=getIntent().getStringExtra("birthday");

//        Years age = Years.yearsBetween(LocalDate.parse(birthday, DateTimeFormat.forPattern("MM/dd/yyyy")), LocalDate.now());

//        ((TextView) findViewById(R.id.birthday)).setText(Integer.toString(age.getYears()));
//        Log.d("MainActivity: bday",age.toString());

//        String gender=getIntent().getStringExtra("gender");
//        Log.d("MainActivity: gender",gender);
//        ((TextView) findViewById(R.id.gender)).setText(gender);

    }

    private void loadActivityComponents(Bundle savedInstanceState) {

        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();

        if (mPhoneNumber != null)
            Log.d("Main Activity", mPhoneNumber);//one time when you register save phone name id


        drawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        listView = (ListView) findViewById(R.id.nav_list);

        navIteamList = new ArrayList<>();

        navIteamList.add(new NavIteam("Home", "home", R.mipmap.home));
        navIteamList.add(new NavIteam("Profile", "my profile", R.mipmap.proicon));
        navIteamList.add(new NavIteam("Setting", "change setting", R.mipmap.setting));
        navIteamList.add(new NavIteam("Logout", "leave your acoount ", R.mipmap.logout));

        NavAdapter navAdapter = new NavAdapter(this, R.layout.item_nav_list, navIteamList);
        listView.setAdapter(navAdapter);
        fragmentList = new ArrayList<>();
        if (name == null)
            name = ParseUser.getCurrentUser().getString("name");
        if (savedInstanceState!=null ) {
            fragmentList.add(getSupportFragmentManager().getFragment(savedInstanceState, "f0"));
//            fragmentList.add(getSupportFragmentManager().getFragment(savedInstanceState, "f1"));
//            fragmentList.add(getSupportFragmentManager().getFragment(savedInstanceState, "f2"));
//            fragmentList.add(getSupportFragmentManager().getFragment(savedInstanceState, "f3"));
            profile = new Profile();
            Bundle args = new Bundle();
            args.putString("name", name);

            if (fragmentList.get(0)==null){
                home = new Home();
                fragmentList.add(0,home);
                fragmentList.get(0).setArguments(args);
            }

            fragmentList.add(1,profile);
            fragmentList.add(2,new Setting());
            fragmentList.add(3,new Out());

            fragmentList.get(1).setArguments(args);
            fragmentList.get(2).setArguments(args);
            fragmentList.get(3).setArguments(args);

        }else {

            home = new Home();
            profile = new Profile();
            fragmentList.add(home);
            fragmentList.add(profile);
            fragmentList.add(new Setting());
            fragmentList.add(new Out());

            Bundle args = new Bundle();
            args.putString("name", name);
            for (Fragment f : fragmentList){
                f.setArguments(args);
            }


            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content, fragmentList.get(0)).commit();

        }

        setTitle(navIteamList.get(0).getTitle());
        listView.setItemChecked(0, true);
        drawerLayout.closeDrawer(drawerPane);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_content, fragmentList.get(position)).commit();

                setTitle(navIteamList.get(position).getTitle());
                listView.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawerPane);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        home.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i("Main Activity", "saving instance state");
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("id", id);
        savedInstanceState.putString("name", name);

        try {
            getSupportFragmentManager().putFragment(savedInstanceState, "f0", fragmentList.get(0));
        }catch (Exception ignored){

        }
//        savedInstanceState.putParcelable("fragmentList", );
//        savedInstanceState.putSerializable("fragmentList", sfragmentList);

        // etc.
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("Main Activity", "Restore instance");
    }

    private void loadUserDetails() {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    Log.d("Main Activity: ", response.getError().getErrorMessage());
                    return;
                }
                name = object.optString("name");
                id = object.optString("id");

                ParseInstallation pi = ParseInstallation.getCurrentInstallation();
                pi.put("FacebookID", id);
                pi.saveInBackground();

                ParseUser pu = ParseUser.getCurrentUser();
                pu.put("facebookID", id);
                pu.saveInBackground();

                ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profile_pic);
                profilePictureView.setProfileId(id);
                TextView nametxt = (TextView) findViewById(R.id.nametxt);
                nametxt.setText(name);
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name");
        request.setParameters(parameters);
        request.executeAsync();


    }
}