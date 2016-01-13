package com.inner.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chat.ChatActivity;
import com.facebook.AccessToken;
import com.paint.Paint_chat;
import com.parse.ParseUser;
import com.peek.matchup.ui2.R;


/**
 * Created by User on 05/12/2015.
 */
public class Fragment4 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment4_layout,container,false);

        Button btn = (Button) v.findViewById(R.id.dummy_chat);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Paint_chat.class);

                startActivity(intent);
            }
        });
        return v;
    }
}
