package com.example.simonglish;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketStuff extends AsyncTask<JSONObject, Void, JSONObject> {
    private final static String IP_ADDRESS =  "IP ADDRESS";    // Host's IP address in the LAN
    private static final int PORT = 6000;       // HTTP port
    private static final int PACKET_SIZE = 1024;    // standard 1kb packet size

    // Properties
    private InputStreamReader streamReader;
    private Socket socket;
    private JSONObject sendingJson, readingJson;

    public SocketStuff(JSONObject object){
        // Constructor for SocketStuff
        this.sendingJson = object;
        this.readingJson = new JSONObject();
    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        try{
            // Socket setting
            this.socket = new Socket(IP_ADDRESS, PORT);
            send(this.sendingJson);
            receive();
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.toString());
        }

        return this.readingJson;
    }

    private void receive() throws IOException {
        // Function that reads the data that comes from the server
        this.streamReader = new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8);
        char[] charBuffer = new char[PACKET_SIZE];
        StringBuilder stringBuilder = new StringBuilder();
        this.streamReader.read(charBuffer);
        stringBuilder.append(charBuffer);
        Log.d("stringBuilder", stringBuilder.toString());
        this.streamReader.close();

        try {
            this.readingJson = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void send(JSONObject dataJson) {
        // Function for sending data to the server
        String data = dataJson.toString();
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8);
            outputStreamWriter.write(data);
            System.out.println(data);
            outputStreamWriter.flush();
        } catch(IOException e) {
            Log.d("Exception", e.toString());
        }
        Log.d("Send function", "Finished process");
    }
}
