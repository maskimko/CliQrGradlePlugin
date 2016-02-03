package ua.pp.msk.gradle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

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
