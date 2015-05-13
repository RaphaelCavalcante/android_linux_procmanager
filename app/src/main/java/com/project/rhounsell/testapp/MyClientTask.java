package com.project.rhounsell.testapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by rhounsell on 06/05/15.
 */
public class MyClientTask extends AsyncTask<Void, Void, Void> {
    private String dstAddress;
    private int dstPort;
    private static String response="";
    private Socket socket;
    private static boolean connected;
    private StringBuilder builder;


    private BufferedReader input;
    private JSONObject json;

    public static PrintWriter out;
    public BufferedReader in ;


    private static final String TAG= MyClientTask.class.getSimpleName();

    MyClientTask(String addr, int port){
        builder= new StringBuilder();
        json= new JSONObject();
        dstAddress = addr;
        dstPort=port;
        connected=false;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {
        JSONArray json;

        try {
            InetAddress serverAddr= InetAddress.getByName(dstAddress);
            socket= new Socket(serverAddr, dstPort);
            connected=true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

            String msg="{";
            response+="{";
            int databuffer;
            while(((databuffer= in.read()) != -1)){
                msg=in.readLine();
                response+=msg;
            }
            
            /*while(((msg=in.readLine()))!=null){
                response+=msg;
            }*/

            //Log.d(TAG, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute(result);
    }
    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);

    }
    public static void sendMessage(String message){
        out.print(message);
        out.flush();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static String getOutPutString(){
        return response;
    }

    public OutputStream getOutputStream() throws IOException, InterruptedException {
        Thread.sleep(3000);
        return socket.getOutputStream();
    }
    public void cleanResponse() throws InterruptedException {
        //Thread.sleep(3000);
        response="";
    }
    public static boolean isConnected(){
        return connected;
    }

    public void doDisconnect() throws IOException {
        if(connected){
            out.close();
            in.close();
            socket.close();

            connected=false;
        }
    }
}
