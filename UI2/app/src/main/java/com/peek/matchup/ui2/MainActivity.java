package com.peek.matchup.ui2;

import android.content.Context;
import android.content.Intent;
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
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.ProfilePictureView;
import com.models.NavIteam;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;

import java.util.List;

public class MainActivity extends ActionBarActivity {

    DrawerLayout drawerLayout;
    RelativeLayout drawerPane;
    ListView listView;

    List<NavIteam> navIteamList;
    List<Fragment> fragmentList;
    Home home;
    Profile profile;

    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String name=getIntent().getStringExtra("name");
        String id=getIntent().getStringExtra("id");
        String birthday=getIntent().getStringExtra("birthday");

        Years age = Years.yearsBetween(LocalDate.parse(birthday, DateTimeFormat.forPattern("MM/dd/yyyy")), LocalDate.now());

        ((TextView) findViewById(R.id.birthday)).setText(Integer.toString(age.getYears()));
        Log.d("MainActivity: bday",age.toString());

        String gender=getIntent().getStringExtra("gender");
        Log.d("MainActivity: gender",gender);
        ((TextView) findViewById(R.id.gender)).setText(gender);

        ProfilePictureView profilePictureView=(ProfilePictureView) findViewById(R.id.profile_pic);
        profilePictureView.setProfileId(id);
        TextView nametxt=(TextView) findViewById(R.id.nametxt);
        nametxt.setText(name);
        TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();

        if (mPhoneNumber != null)
            Log.d("dddddddddddddddddddd---",mPhoneNumber);//one time whene you register save phone name id

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        listView = (ListView) findViewById(R.id.nav_list);

        navIteamList = new ArrayList<NavIteam>();

        navIteamList.add(new NavIteam("Home", "home", R.mipmap.home));
        navIteamList.add(new NavIteam("Profile", "my profile", R.mipmap.proicon));
        navIteamList.add(new NavIteam("Setting", "change setting", R.mipmap.setting));
        navIteamList.add(new NavIteam("Logout", "leave your acoount ", R.mipmap.logout));

        NavAdapter navAdapter = new NavAdapter(getApplicationContext(), R.layout.item_nav_list, navIteamList);
        listView.setAdapter(navAdapter);
        home=new Home();
        profile=new Profile();

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(home);
        fragmentList.add(profile);
        fragmentList.add(new Setting());
        fragmentList.add(new Out());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, fragmentList.get(0)).commit();

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

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.dopen, R.string.dclose)
        {
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
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
        home.onActivityResult(requestCode,resultCode,data);

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
}