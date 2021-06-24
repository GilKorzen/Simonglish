package com.example.simonglish;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Leaderboards extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {
    Spinner spinner;
    List<String> options;
    ListView listView;
    ArrayListAdapter arrayListAdapter;
    ArrayList<ScoreBoard> scoreBoardArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);

        spinner=findViewById(R.id.spinner);
        options=new ArrayList<String>();
        options.add("Easy");
        options.add("Medium");
        options.add("Hard");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, options);

        spinner.setAdapter(dataAdapter);
        spinner.setSelection(getIntent().getExtras().getInt("position"));
        spinner.setOnItemSelectedListener(this);

    }


    private void dataSending(String name) {

        JSONObject sendingData = new JSONObject();
        ScoreBoard temp_scoreBoard;


        try {
            sendingData.put("request","get"+name);
            SocketStuff dataTransportTask = new SocketStuff(sendingData);
            JSONObject received = dataTransportTask.execute().get();
            System.out.println(received.toString());
            JSONArray jsonArray=received.getJSONArray("scores");
            scoreBoardArrayList=new ArrayList<>();
            for (int i=0;i<jsonArray.length();i++)
            {
                JSONArray working_in_array=jsonArray.getJSONArray(i);
                String temp_username=working_in_array.getString(0);
                String score=String.valueOf(working_in_array.getInt(1));
                if (temp_username.equals(getUsernameByEmail(getIntent().getExtras().getString("email"))))

                    temp_scoreBoard=new ScoreBoard(temp_username,score,"yes");
                else
                    temp_scoreBoard=new ScoreBoard(temp_username,score,"no");

                scoreBoardArrayList.add(temp_scoreBoard);

            }



        } catch (JSONException | InterruptedException | ExecutionException e) {
            Log.e("Exception", e.toString());
        }
    }

    private String getUsernameByEmail(String email){
        //gets and email address
        //returns the username associated with it


        JSONObject sendingData = new JSONObject();


        try {
            sendingData.put("request","username_request");
            sendingData.put("email",email);
            SocketStuff dataTransportTask = new SocketStuff(sendingData);
            JSONObject received = dataTransportTask.execute().get();
            System.out.println(received.toString());
            return received.getString("username");




        } catch (JSONException | InterruptedException | ExecutionException e) {
            Log.e("Exception", e.toString());
            return " ";
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        dataSending(item);
        arrayListAdapter=new ArrayListAdapter(this,0,0,scoreBoardArrayList);
        listView=findViewById(R.id.listviewofleader);
        listView.setAdapter(arrayListAdapter);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}