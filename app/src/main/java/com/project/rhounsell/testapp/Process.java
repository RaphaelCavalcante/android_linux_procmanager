package com.project.rhounsell.testapp;

/**
 * Created by rhounsell on 12/05/15.
 */

public class Process {
    private String procName;
    private int procPid;
    private String statusFile;
    static final String PROC_PATH="/proc/";
    static final String FILE="/status";


    public Process(String name, int pid) {
           this.procName=name;
           this.procPid=pid;
    }
    public String getProcName(){
        return this.procName;
    }
    public int getProcPid(){
        return this.procPid;
    }
    public String getProcPath(){
        return PROC_PATH+this.procPid+FILE;
    }

}
