package com.project.rhounsell.testapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rhounsell on 12/05/15.
 */
public class ProcessAdapter extends BaseAdapter {
    private final List<Process> processes;
    private final Context context;
    public ProcessAdapter(Context context, List<Process>processes){
        this.processes=processes;
        this.context=context;
    }
    @Override
    public int getCount() {
        return processes.size();
    }

    @Override
    public Process getItem(int position) {
        return processes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Process proc= getItem(position);

        LayoutInflater inflater= LayoutInflater.from(context);
        View linha= inflater.inflate(R.layout.process_item,null);

        TextView processNameRow=(TextView) linha.findViewById(R.id.process_name);

        processNameRow.setText(proc.getProcName());
        return linha;
    }
}
