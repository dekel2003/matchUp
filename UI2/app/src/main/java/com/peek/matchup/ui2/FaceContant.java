package com.peek.matchup.ui2;

import android.content.Context;
import android.view.View;
import android.widget.TabHost;

/**
 * Created by User on 06/12/2015.
 */
public class FaceContant implements TabHost.TabContentFactory{
    Context context;

    public FaceContant(Context mcontext)
    {
        context=mcontext;

    }


    @Override
    public View createTabContent(String tag) {
        View fake=new View(context);
        fake.setMinimumHeight(0);
        fake.setMinimumWidth(0);
        return fake;
    }
}