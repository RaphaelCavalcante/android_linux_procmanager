package com.project.rhounsell.testapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MyActivity extends ActionBarActivity {
    TextView txt;
    Button b;
    PrintWriter out;
    boolean connected;
    String jsonString;

    private static final String TAG= MyActivity.class.getSimpleName();

    MyClientTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);



        txt = (TextView) findViewById(R.id.textout);

        b=(Button) findViewById(R.id.send);

        task = new MyClientTask("10.8.0.59", 8888);

        task.execute();

        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();

        /*try {

            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(task.getOutputStream())));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /*     out.print(txt.getText().toString());
                    out.flush();*/

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(task.isConnected()){
                    MyClientTask.sendMessage(txt.getText().toString());
                }
                JSONObject obj=jsonObjMaker();
                createProcessList(obj);
                try {
                    printLogResult();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(task.isConnected()){
            try {
                task.doDisconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void printLogResult() throws JSONException, InterruptedException {

        //Log.d(TAG, task.getOutPutString()+" }");

        JSONObject obj = null;
        try {
            obj = new JSONObject(task.getOutPutString() + "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        task.cleanResponse();
    }
    public JSONObject jsonObjMaker() {
        JSONObject obj = null;
        try {
            obj = new JSONObject(task.getOutPutString() + "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

//    public List<Process> createProcessList(JSONObject processes){
      public void createProcessList(JSONObject processes){
        final List <Process> processList= new ArrayList<Process>();
        Iterator it=processes.keys();
        while(it.hasNext()){
            String processName= (String) it.next();
            try {
                processList.add(new Process (processName, (int) processes.get(processName)));

                Log.d(TAG, processName+"  "+(int) processes.get(processName));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ProcessAdapter adapter = new ProcessAdapter(getApplicationContext(), processList);

        ListView list = (ListView) findViewById(R.id.process_list);
        list.setAdapter(adapter);


         list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 //Toast.makeText(getApplicationContext(), "click:"+processList.get(position).getProcPid(), Toast.LENGTH_SHORT).show();
                 if(task.isConnected()) {
                  /*   out.print("" + processList.get(position).getProcPid());
                     out.flush();*/
                     MyClientTask.sendMessage("" + processList.get(position).getProcPid());
                 }
                 try {
                     Thread.sleep(2000);

                     String rootPid= task.getOutPutString();
                     //Log.d(TAG, task.getOutPutString());
                     task.cleanResponse();

                     Intent intent;
                     intent=new Intent(MyActivity.this, ProcessDetailActivity.class);
                     intent.putExtra("pid", rootPid);

                     startActivity(intent);


                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }


             }
         });
    }
}
