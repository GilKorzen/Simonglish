package com.example.simonglish;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ArrayListAdapter extends ArrayAdapter<ScoreBoard> {

    Context context;
    List<ScoreBoard> objects;
    public ArrayListAdapter(Context context, int resource, int textViewResourceId, List<ScoreBoard> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_leaderboard_layout,parent,false);

        TextView username = (TextView)view.findViewById(R.id.username_);
        TextView score = (TextView)view.findViewById(R.id.score_);
        LinearLayout layout= view.findViewById(R.id.main_layout);
        ScoreBoard temp = objects.get(position);

        if (temp.getColor().equals("yes")) {
            layout.setBackgroundColor(context.getResources().getColor(R.color.youColor));
            username.setTextColor(context.getResources().getColor(R.color.youText));
            score.setTextColor(context.getResources().getColor(R.color.youText));
        }


        username.setText(String.valueOf(temp.getUsername()));
        score.setText(String.valueOf(temp.getScore()));


        return view;
    }
}