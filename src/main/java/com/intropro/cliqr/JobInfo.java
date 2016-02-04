/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intropro.cliqr;

import com.intropro.cliqr.exceptions.ClientSslException;
import com.intropro.cliqr.exceptions.ResponseException;
import com.intropro.cliqr.job.JobFilter;
import java.io.PrintStream;
import java.net.MalformedURLException;

/**
 *
 * @author maskimko
 */
public interface JobInfo {

    public void getJobInfo(int jobNumber) throws MalformedURLException, ClientSslException, ResponseException;
    public void getJobInfo() throws MalformedURLException, ClientSslException, ResponseException;
    public void getJobInfo(JobFilter... filter) throws MalformedURLException, ClientSslException, ResponseException;
    public PrintStream getOutputStream();
    public void setOutputStream(PrintStream ops);
    
}
