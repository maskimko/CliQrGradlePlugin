/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.pp.msk.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class InfoTask extends DefaultTask {
 
    
    @TaskAction
    public void info(){
        getLogger().info("Starting info task");
        System.out.println("Here should be a cliqr info");
    }
}
