/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.cliqr;

import ua.pp.msk.cliqr.exceptions.ClientSslException;
import ua.pp.msk.cliqr.exceptions.ResponseException;
import ua.pp.msk.cliqr.job.JobFilter;
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
