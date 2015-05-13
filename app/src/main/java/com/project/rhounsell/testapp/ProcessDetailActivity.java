package com.project.rhounsell.testapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class ProcessDetailActivity extends ActionBarActivity {
    private static final String TAG= ProcessDetailActivity.class.getSimpleName();
    private JSONObject obj;
    private boolean hasChild=false;
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        String extra= intent.getStringExtra("pid");
        Log.d(TAG, extra);
        try {
            obj = new JSONObject(extra + "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_detail);

        //createProcessList(obj);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_process_detail, menu);
        return true;
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
                if(MyClientTask.isConnected()) {
                  /*   out.print("" + processList.get(position).getProcPid());
                     out.flush();*/
                    MyClientTask.sendMessage("" + processList.get(position).getProcPid());
                }
                try {
                    Thread.sleep(2000);

                    String rootPid= MyClientTask.getOutPutString();
                    //Log.d(TAG, task.getOutPutString());


                    //Intent intent;
                    //intent=new Intent(ProcessDetailActivity.this, ProcessDetailActivity.class);
                    //intent.putExtra("pid", rootPid);

                    //startActivity(intent);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    private String validadeString(String str){
        String validString;
        for(int i=0;i<str.length();i++){
            /*if(str.charAt(i)){

            }*/
        }
        return null;
    }
}
