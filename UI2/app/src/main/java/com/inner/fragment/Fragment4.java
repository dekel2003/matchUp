package com.inner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chat.ChatActivity;
import com.facebook.AccessToken;
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
                Intent intent = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
                Bundle args = new Bundle();
//                args.putString("id", AccessToken.getCurrentAccessToken().getUserId());
//                args.putString("id1", "895079157265629");  // TODO: give me the first User-ID
//                args.putString("id2", "966642636741330");  // TODO: give me the second User-ID
                // It's better to work in parse with the parse "Object-ID" rather than the Facebook ID.
                args.putString("id", ParseUser.getCurrentUser().getObjectId());
                args.putString("id1", "Ksmw7nvF86");  // TODO: give me the first User-Object_ID
                args.putString("id2", "T4S0XHNx2L");  // TODO: give me the second User-Object_ID
                args.putInt("chatId",1234); // TODO: create unique chat-id for this specific conversation.
                intent.putExtras(args);
                startActivity(intent);
            }
        });



        return v;
    }
}
