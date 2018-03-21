/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package contactsaver;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Sunnyblaze
 */
public class AppSettings extends JPanel implements Runnable{
    //Class Property
    private String serverMessage;
    private final Dimension frameSize;
    private boolean waitStatus;
    private static Properties Properties;
    private Image logo;
    
    
    //Constructors
    public AppSettings(Dimension frameSize, Image logo){
        //Initialize all required properties
        this.frameSize = frameSize;
        this.logo = logo;
        AppSettings.Properties = new Properties();
        try {
            AppSettings.Properties.load(new FileInputStream(".\\Contact Saver\\settings.ini"));
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        
        //This thread starts the database server
        Thread startServer = new Thread(new Runnable(){

            @Override
            public void run() {
                AppSettings.this.startServer();
            }
        });
        startServer.start();
        
        
    }
    
    public AppSettings() throws IOException{
        //Load required settings file
        this.Properties = new Properties();
        this.Properties.load(new FileInputStream(".\\Contact Saver\\settings.ini"));
        this.frameSize = null;
    }
    
    //Startup Mysql Server
    private void startServer(){
        this.waitStatus = true;
        this.serverMessage = "Starting Database... Please wait";
        Runtime runtime = Runtime.getRuntime();
        String query = AppSettings.getproperty("db_server");
        try {
            String url = (query.equals("") || query.equals("\"\""))?
                         "C:\\wamp\\bin\\mysql\\mysql5.6.12\\bin\\mysqld.exe":
                          query;
            Process pr = runtime.exec(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String read;
            while((read = reader.readLine()) == null){
                System.out.println(read);
                this.repaint();
                this.serverMessage = read;
                Thread.sleep(10);
                
            }
            this.serverMessage = "Database started successfully.";
        } catch (IOException | InterruptedException ex) {
            this.serverMessage = ex.getMessage();
        }
        this.repaint();
        
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    //Paint Method to paint the splash Screen
    @Override
    public void paint(Graphics g){
        g.drawImage(this.logo, (this.frameSize.width/2)-115, (this.frameSize.height/2) - 150, this);
        g.setColor(Color.BLUE);
        g.clearRect(100, this.frameSize.height-65, 500,45);
        //g.drawString(serverMessage, 100, this.frameSize.height-40);
    }

    @Override
    //Run Thread Method
    public void run() {
        
    }
    
    //return thread wait status
    public boolean getWait(){
        return this.waitStatus;
    }
    
    //Get application property
    public String getProperty(String key){
        return this.Properties.getProperty(key);
    }
    
    //Get application property
    public static String getproperty(String key){
        return AppSettings.Properties.getProperty(key);
    }
   
}
