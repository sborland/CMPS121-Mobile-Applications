package com.mobileapp.sab.localchat;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.drm.DrmStore;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String>{
    public static String LOG_TAG = "App Messages";

    private final Activity context;
    private final String[] postings;
    private final String[] nicknames;
    private final String username;
    // private final Integer[] imageId;
    public CustomList(Activity context,
                String[] postings, String[] nicknames, String username){//,Integer[] imageId) {
        super(context, R.layout.list_single, postings);
        this.context = context;
        this.postings = postings;
        this.nicknames = nicknames;
        this.username = username;


    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtPost = (TextView) rowView.findViewById(R.id.txt);
        TextView txtName = (TextView) rowView.findViewById(R.id.txtname);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        ImageView imageView2 = (ImageView) rowView.findViewById(R.id.img2);

        String n = nicknames[position];
        if(n.equals(username)){
            txtPost.setTextColor(Color.rgb(94,128,174));
            txtPost.setGravity(Gravity.RIGHT);
            imageView2.setImageResource(R.drawable.pawa);
        }else{
            txtPost.setTextColor(Color.rgb(174,94,128));
            txtPost.setGravity(Gravity.LEFT);
            imageView.setImageResource(R.drawable.pawb);
        }

       // txtName.setText(nicknames[position]);
        txtPost.setText(postings[position]);


        return rowView;
    }
}