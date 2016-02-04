/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.cliqr;

import ua.pp.msk.cliqr.exceptions.MissingParameterException;
import ua.pp.msk.cliqr.exceptions.RunJobException;
import java.io.File;
import java.util.Map;

/**
 *
 * @author maskimko
 */
public interface RunJob {

    
    public static final String APPID = "addId";
    /**
     * Virtual private cloud ID
     */
    public static final String VPCID = "vpcId";
    /**
     * Attach Public IP
     */
    public static final String PIP = "attachPublicIP";
    /**
     * Subnet id
     */
    public static final String NETWORK = "Network";
    public static final String APPNAME = "appName";
    public static final String JOBSERVICETIERID = "jobSTId";
    
    /**
     * StdIn
     *
     * @throws ua.pp.msk.cliqr.exceptions.RunJobException This is
     * consolidated exception if something goes wrong.
     */
    public void startJob() throws RunJobException;

    /**
     * Interactive
     *
     * @param jobId Job id to create
     * @throws ua.pp.msk.cliqr.exceptions.RunJobException This is
     * consolidated exception if something goes wrong.
     */
    public void startJob(int jobId) throws RunJobException;

    /**
     * Json file
     *
     * @param file Json file
     * @throws ua.pp.msk.cliqr.exceptions.RunJobException This is
     * consolidated exception if something goes wrong.
     */
    public void startJob(String file) throws RunJobException;

    /**
     * Json file
     *
     * @param file Json file
     * @throws ua.pp.msk.cliqr.exceptions.RunJobException This is
     * consolidated exception if something goes wrong.
     */
    public void startJob(File file) throws RunJobException;

    /**
     * Property file
     *
     * @param jobId Number of job type
     * @param serviceTierId Number of service tier in CliQr
     * @param file Property file
     * @throws ua.pp.msk.cliqr.exceptions.RunJobException This is
     * consolidated exception if something goes wrong.
     * @throws ua.pp.msk.cliqr.exceptions.MissingParameterException 
     * This exception occurs when property file does not contain mandatory option to run CliQr job
     */
    public void startJob(int jobId, int serviceTierId, String file) throws RunJobException, MissingParameterException;

    /**
     * Property file
     *
     * @param jobId Number of job type
     * @param serviceTierId Number of service tier in CliQr
     * @param file Property file
     * @throws ua.pp.msk.cliqr.exceptions.RunJobException This is
     * consolidated exception if something goes wrong.
     * @throws ua.pp.msk.cliqr.exceptions.MissingParameterException 
     * This exception occurs when property file does not contain mandatory option to run CliQr job
     */
    public void startJob(int jobId, int serviceTierId, File file) throws RunJobException, MissingParameterException;

    /**
     * Key Value input
     *
     * @param jobId Number of job type
     * @param serviceTierId Number of service tier in CliQr
     * @param options key=value[$key=value...]
     * @throws ua.pp.msk.cliqr.exceptions.RunJobException This is
     * consolidated exception if something goes wrong.
     * @throws ua.pp.msk.cliqr.exceptions.MissingParameterException if options does not contain required exception
     */
    public void startJob(int jobId,  Map<String, String> options) throws RunJobException, MissingParameterException;

    /**
     * 
     * @param jobId CliQr application Id to run
     * @param serviceTierId Service Tier Id of machine to run
     * @param appName CliQr Application Name
     * @param cloudName CliQr Cloud name
     * @param vpcId VPC id
     * @param network Network name
     * @param env CliQr Environment name
     * @param inst Instance size
     * @param apip Attach Public IP
     * @param envPairs
     * @throws ua.pp.msk.cliqr.exceptions.RunJobException
     * @throws ua.pp.msk.cliqr.exceptions.MissingParameterException
     */
    public void startJob(int jobId, String serviceTierId, String appName, String cloudName, String vpcId, String network, 
            String env, String inst, boolean apip, Map<String, String> envPairs)throws RunJobException, MissingParameterException;
}
