/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package contactsaver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sunnyblaze
 */
public class ContactSaver extends CSGUI{
    private static ContactSaver contactSaver;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ContactSaver.contactSaver = new ContactSaver();
        ContactSaver.makeDir();
        
        

    }
    
    //Make required directories
    private static void makeDir(){
        File dir = new File("Contact Saver");
        File file = new File("Contact Saver\\settings.ini");
        if(!dir.exists() ){
            dir.mkdir();
            try {
                file.createNewFile();
                Properties setting = new Properties();
                setting.put("db_username", "");
                setting.put("db_password", "");
                setting.put("db_url", "");
                setting.put("db_server", "");
                String comments = "########################## Contact Saver ################"
                        + "##########\n#\t\t\tVersion 1.0.2\t\t\t#\n#\t\t@Author: Amowe"
                        + " Sunday Alexander\t\t#\n########## Personal Contact Saver ##########\n"
                        + "This is an application setting file\nThis setting file uses key=value "
                        + "model\nNote: the keys and values are not quoted.";
                setting.store(new FileOutputStream(file), comments);
            } catch (IOException ex) {
                Logger.getLogger(ContactSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
