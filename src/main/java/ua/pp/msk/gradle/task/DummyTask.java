/*
 * Copyright 2016 Maksym Shkolnyi self project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ua.pp.msk.gradle.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
@Deprecated
public class DummyTask extends DefaultTask {

    private String someFile = null;
    
    
    private File getFile(){
        File file = getProject().file(someFile);
        return file;
    }

    public String getSomeFile() {
        return someFile;
    }

    public void setSomeFile(String someFile) {
        this.someFile = someFile;
    }
    
    
    @TaskAction
    public void dummyTask() {
        System.out.println("It is a very dummy plugin yet. Hello!!!");
//       
//        RunTaskExtension ext = getProject().getExtensions().findByType(RunTaskExtension.class);
//        if (ext == null) {
//            ext = new RunTaskExtension();
//            ext.setHost("localhost");
//        }
//        
//        System.out.println("CliQr hostname: " + ext.getHost());
        if (someFile != null && someFile.length() > 0 ){
            try (FileWriter fw = new FileWriter(getFile())) {
               getLogger().warn(getFile().getAbsolutePath());
                getFile().getParentFile().mkdirs();
                fw.write("File info from a very dummy plugin!");
            } catch (IOException ex){
                getLogger().error("IO Error"+ ex.getMessage());}
        } else {
            getLogger().warn("Some file value is null");
        }
        
    }
    
 

}
