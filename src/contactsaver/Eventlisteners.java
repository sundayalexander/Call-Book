/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package contactsaver;



import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


/**
 *
 * @author Sunnyblaze
 */
public class Eventlisteners implements ActionListener {
    //Properties / Variable declaration
    private JButton saveBtn;
    private JButton addPic;
    private JOptionPane option;
    private JFileChooser file;
    private int approveStatus;
    private Dimension parentSize, screenSize;
    private FileFilter fileFilter;
    private InputStream fileInput;
    private FileOutputStream fileOutput;
    private File selectedFile;
    private DatabaseConnector database;
    private JFrame frame, popPanel;
    private JPopupMenu popupMenu;
    private JTable table;
    private PrintRequestAttributeSet requestAttribute;
    private DefaultTableModel tableModel;
    private TableRowSorter rowSorter;
    
    //Methods
    //Constructor
    public Eventlisteners(JButton SaveButton, JButton AddPicture, Dimension parentSize, DefaultTableModel tableModel){
        this.tableModel = tableModel;
        this.saveBtn = SaveButton;
        this.addPic = AddPicture;
        this.parentSize = parentSize;
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.database =  new DatabaseConnector(this.frame);
    }
    
    public Eventlisteners(JMenu newContact, JMenu viewContact, JFrame parent){
        this.frame = parent;
    }
    
    public Eventlisteners(JFrame parent, JTable table){
         this.popPanel = parent;
         this.table = table;
    }
    
    //Method for Action performed
    @Override
    public void actionPerformed(ActionEvent e) {
        //Create databse object
        this.database = new DatabaseConnector(this.frame);
        
        //Process Add picture button event
        if(e.getSource().equals(this.addPic)){
           this.initComponent();
           this.approveStatus = this.file.showOpenDialog(this.addPic);
           if(this.approveStatus == JFileChooser.APPROVE_OPTION){
               this.selectedFile = this.file.getSelectedFile();
               try {
                   this.fileInput = new FileInputStream(this.selectedFile);
               } catch (FileNotFoundException ex) {
                   //Logger.getLogger(Eventlisteners.class.getName()).log(Level.SEVERE, null, ex);
               }
               //Copy Selected passport to defualt folder
              // this.passportCopier();
           }
           
       }
    }
    
    //Initialize FileChooser Component
    private void initComponent(){
        this.file = new JFileChooser();
        this.fileFilter = new FileNameExtensionFilter("JPeg Files","jpg","jpeg");
        this.file.setFileFilter(this.fileFilter);
    }
    
    //Method to copy selected passort to contact saver folder
    private synchronized void passportCopier(){
         try {
                   
                   this.fileOutput = new FileOutputStream(".\\Contact Saver\\"+this.selectedFile.getName());
                   int readFile = 0;
                   byte[] bytes = new byte[512];
                   while((readFile = this.fileInput.read(bytes)) != -1){
                       this.fileOutput.write(bytes, 0, readFile);
                   }
                   //this.fileInput.close();
                   this.fileOutput.close();
                  // NewContact.setProfilePic(Toolkit.getDefaultToolkit().getImage(".\\Contact Saver\\"+this.selectedFile.getName()));
               } catch (FileNotFoundException ex) {
                   Logger.getLogger(Eventlisteners.class.getName()).log(Level.SEVERE, null, ex);
               } catch (IOException ex) {
                   Logger.getLogger(Eventlisteners.class.getName()).log(Level.SEVERE, null, ex);
               }
            
    }

    //This method performs mouse
    //clicked Event.
    public void mouseClicked(JFrame parent, JTable table, MouseEvent e) {
        this.popupMenu = new JPopupMenu("Settings");
        this.popupMenu.setBorderPainted(true);
        JMenuItem deleteEntry = new JMenuItem("Delete Record");
        JMenuItem veiwData = new JMenuItem("Veiw Record");
        JMenuItem refresh = new JMenuItem("Refresh");
        JMenuItem search = new JMenuItem("Search Record");
        JMenuItem print = new JMenuItem("Print");
        JMenuItem deselect = new JMenuItem("Deselect");
        JPopupMenu unselectMenu = new JPopupMenu();
        JMenuItem update = new JMenuItem("Edit");
        unselectMenu.setBorderPainted(true);
        this.table = table;
        //Unselect Menu items
        unselectMenu.add(search);
        unselectMenu.add(refresh);
        unselectMenu.add(print);
        print.addActionListener(this.performAction());
        refresh.addActionListener(this.performAction());
        veiwData.addActionListener(this.performAction());
        deselect.addActionListener(this.performAction());
        deleteEntry.addActionListener(this.performAction());
        update.addActionListener(this.performAction());
        search.addActionListener(this.performAction());
        this.popupMenu.add(veiwData);
        this.popupMenu.add(deselect);
        this.popupMenu.addSeparator();
        this.popupMenu.add(update);
        this.popupMenu.add(deleteEntry);
        
        if((e.getButton() == 3) && table.getSelectedRow() != -1){
            if(this.popupMenu.isShowing()){
                this.popupMenu.setVisible(false);
            }else{
                this.popupMenu.show(parent, e.getPoint().x, e.getPoint().y);
            }
            
        }else if((e.getButton() == 3) && table.getSelectedRow() == -1){
            if(unselectMenu.isShowing()){
                unselectMenu.setVisible(false);
            }else{
                unselectMenu.show(parent, e.getPoint().x, e.getPoint().y);
            }
            
        }
    }

    
    
    //This method performs some action
    //based on the event action command 
    //string given.
    private ActionListener performAction(){
        return new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                switch(actionCommand){
                    
                    //Perform Print Action
                    case "Print":
                        Eventlisteners.this.doPrint();
                        break;
                        
                    //Perform Veiw Operation
                    case "Veiw Record":
                        Eventlisteners.this.veiwContact();
                        break;
                    
                    //Perform Delete Operation
                    case "Delete Record":
                        Eventlisteners.this.deleteContact();
                        break;
                    
                    //Perform Refresh Operation
                    case "Refresh":
                        Eventlisteners.this.doRefresh();
                        break;
                        
                    //Perform Unselect or deselect operation
                    case "Deselect":
                        Eventlisteners.this.deselect();
                        break;
                        
                     //Perform update operation
                    case "Edit":
                        Eventlisteners.this.updateContact();
                        break;
                        
                    //Perform search operation
                    case "Search Record":
                        Eventlisteners.this.searchContact();
                        break;
                    default:
                }
            }
        };
    }
    
    //Print out table Data
    public void doPrint(){
        try {
            this.table.print(JTable.PrintMode.FIT_WIDTH, new MessageFormat("Contact Saver"), null);
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this.frame, ex.getLocalizedMessage());
        }
    }
    
    //Refresh table data
    public void doRefresh(){
        this.tableModel.setDataVector(this.database.getSavedContact(), this.database.getColumnName());
    }
    
    //Veiw selected contact
    public void veiwContact(){
        int selectedrow = this.table.getSelectedRow();
        //Set the column names
        ArrayList<String> userInfo = new ArrayList<>();
        for(int i = 1; i < 9; i++){
            userInfo.add(this.table.getValueAt(selectedrow, i).toString());
        }
        Image img = Toolkit.getDefaultToolkit().getImage(this.database.getPassport(userInfo));
        ImageIcon passport = new ImageIcon(img.getScaledInstance(100, 100, selectedrow));
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constriant = new GridBagConstraints();
        constriant.insets = new Insets(10,10,10,10);
        constriant.gridx = 0;
        panel.add(new JLabel("Surname: ", JLabel.RIGHT), constriant);
        constriant.gridx = 1;
        panel.add(new JLabel(this.table.getValueAt(selectedrow, 1).toString(), JLabel.CENTER), constriant);
        constriant.gridx = 0;
        constriant.gridy = 1;
        panel.add(new JLabel("Other Name(s): ", JLabel.RIGHT), constriant);
        constriant.gridx = 1;
        panel.add(new JLabel(this.table.getValueAt(selectedrow, 2).toString(), JLabel.CENTER), constriant);
        constriant.gridx = 0;
        constriant.gridy = 2;
        panel.add(new JLabel("Occupation: ", JLabel.RIGHT), constriant);
        constriant.gridx = 1;
        panel.add(new JLabel(this.table.getValueAt(selectedrow, 3).toString(), JLabel.CENTER), constriant);
        constriant.gridx = 0;
        constriant.gridy = 3;
        panel.add(new JLabel("Office Address: ", JLabel.RIGHT), constriant);
        constriant.gridx = 1;
        panel.add(new JLabel(this.table.getValueAt(selectedrow, 4).toString(), JLabel.CENTER), constriant);
        constriant.gridx = 0;
        constriant.gridy = 4;
        panel.add(new JLabel("Home Address: ", JLabel.RIGHT), constriant);
        constriant.gridx = 1;
        panel.add(new JLabel(this.table.getValueAt(selectedrow, 5).toString(), JLabel.CENTER), constriant);
        constriant.gridx = 0;
        constriant.gridy = 5;
        panel.add(new JLabel("Email: ", JLabel.RIGHT), constriant);
        constriant.gridx = 1;
        panel.add(new JLabel(this.table.getValueAt(selectedrow, 6).toString(), JLabel.LEFT), constriant);
        constriant.gridx = 0;
        constriant.gridy = 6;
        panel.add(new JLabel("Home Phone: ", JLabel.RIGHT), constriant);
        constriant.gridx = 1;
        panel.add(new JLabel(this.table.getValueAt(selectedrow, 7).toString(), JLabel.LEFT), constriant);
        constriant.gridx = 0;
        constriant.gridy = 7;
        panel.add(new JLabel("Office Phone: ", JLabel.RIGHT), constriant);
        constriant.gridx = 1;
        panel.add(new JLabel(this.table.getValueAt(selectedrow, 8).toString()), constriant);
        constriant.gridx = 2;
        constriant.gridy = 0;
        constriant.gridheight = 5;
        constriant.gridwidth = GridBagConstraints.REMAINDER;
         panel.add(new JLabel(passport), constriant);
        
        //Display the contact on message
        //dialog box
        JOptionPane.showMessageDialog(this.frame, panel);
        
    }
    
    //Delete selected contact
    public void deleteContact(){
        int[] selectedRows = this.table.getSelectedRows();
        String data1, data2, data3, data4, data5, data6, data7, data8;
        boolean status = false;
        for(int i = 0; i < selectedRows.length; i++){
            data1 = this.table.getValueAt(selectedRows[i], 1).toString();
            data2 = this.table.getValueAt(selectedRows[i], 2).toString();
            data3 = this.table.getValueAt(selectedRows[i], 3).toString();
            data4 = this.table.getValueAt(selectedRows[i], 4).toString();
            data5 = this.table.getValueAt(selectedRows[i], 5).toString();
            data6 = this.table.getValueAt(selectedRows[i], 6).toString();
            data7 = this.table.getValueAt(selectedRows[i], 7).toString();
            data8 = this.table.getValueAt(selectedRows[i], 8).toString();
            status = this.database.deleteContact(data1, data2, data3, data4, data5, data6, data7, data8);
        }
        if(status){
            this.doRefresh();
            JOptionPane.showMessageDialog(this.frame, "Contact deleted successfully");
        }
    }
    
    //Unselect or Deselected selected rows
    public void deselect(){
        this.table.clearSelection();
    }
    
    //Update the selected row
    public void updateContact(){
        int row = this.table.getSelectedRow();
        int column = this.table.getSelectedColumn();
        String columnTitle = this.table.getColumnName(column);
        String updateString = this.table.getValueAt(row,column ).toString();
        String value = JOptionPane.showInputDialog(this.frame, columnTitle, updateString);

        //Set the column names
        ArrayList<String> columnNames = new ArrayList<String>();
        ArrayList<String> columnDatas = new ArrayList<String>();
        for(int i = 1; i < 9; i++){
            columnNames.add(this.table.getColumnName(i));
            columnDatas.add(this.table.getValueAt(row, i).toString());
        }
        columnNames.remove(this.table.getColumnName(column));
        columnDatas.remove(this.table.getValueAt(row,column ).toString());
        if(this.database.update(columnTitle, value, columnNames, columnDatas)){
            this.table.setValueAt(value, row, column);
        }
        
    }
    
    //Save contact to database 
    public boolean save(String surname, String otherName,String occupation,
                String officeAdd,String homeAdd,String email,String homePhone,
                String workPhone) throws FileNotFoundException{
        if(this.fileInput == null){
            this.fileInput = getClass().getResourceAsStream("..\\images\\avatar.png");
        }
        if(this.database.insert(surname, otherName, occupation, officeAdd, 
                homeAdd, email, homePhone, workPhone, fileInput)){
            try {
                this.fileInput.close();
                this.fileInput = null;
            } catch (IOException ex) {
                
            }
            return true;
        }else{
            return false;
        }
        
    }
    
    //Save Contact from file to Database
    public void fileToServer(File selectedFile){
         try {
            BufferedReader file = new BufferedReader(new FileReader(selectedFile));
            String read;
            Properties fileData = new Properties();
            int counter = 0;
            while((read = file.readLine()) != null){
                if(read.startsWith("FN") || read.startsWith("TEL")){
                   fileData.put(read.substring(0, read.indexOf(":")), read.substring(read.indexOf(":")+1));
                    counter++;
                    if(counter >= 2){
                        String name = fileData.getProperty("FN");
                        String phone = fileData.getProperty("TEL;CELL");
                        if(!this.database.insert(name.split("\\s")[0], 
                                name.substring(name.indexOf(" ")+1), phone)){
                            JOptionPane.showMessageDialog(this.frame, "Unable to save contact: "+name);
                        }                      
                        fileData.clear();
                        counter = 0;
                    }               
                }               
            }
            
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this.frame, ex.getLocalizedMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.frame, ex.getLocalizedMessage());
        }
    }
    
    //Perform save contact from file action
    public ActionListener fileToServer(JButton buttonClicked){
        return new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //Perform save contact from file action
                Eventlisteners.this.file = new JFileChooser();
                Eventlisteners.this.fileFilter = new FileNameExtensionFilter("Contact Files","vcf");
                Eventlisteners.this.file.setFileFilter(Eventlisteners.this.fileFilter);
                Eventlisteners.this.approveStatus = Eventlisteners.this.file.showOpenDialog(Eventlisteners.this.addPic);
                if(Eventlisteners.this.approveStatus == JFileChooser.APPROVE_OPTION){
                    Eventlisteners.this.fileToServer(Eventlisteners.this.file.getSelectedFile());
                    
                    //Copy Selected passport to defualt folder
                   // this.passportCopier();
                }
            }
            
        };
    }
    
    //Search the record table using the specified
    //string or datas
    public void searchContact(){
        String query = JOptionPane.showInputDialog(this.frame, "Search for a "
                + "record.\n Note: each field is case sensitive");
        RowFilter rowFilter = null;
        try{
            rowFilter = RowFilter.regexFilter(query);
            
        }catch(PatternSyntaxException ex){
            JOptionPane.showMessageDialog(this.frame, ex.getMessage());
        }catch(NullPointerException ex){
            
        }
        this.rowSorter.setRowFilter(rowFilter);
        
    }
    
    //Set a table row sorter to be used
    //by search contact method
    public void setRowSorter(TableRowSorter sorter){
        this.rowSorter = sorter;
    }
    
    /*>>>>>>>>>>>>>>>>>>>>>>>
    * Insert data into Database
    <<<<<<<<<<<<<<<<<<<<<<<<<< */
 /*   public void clearData(){
        this.surname.setText(null);
        this.otherName.setText(null);
        this.occupation.setText(null);
        this.officeAdd.setText(null);
        this.homeAdd.setText(null);
        this.email.setText(null);
        this.homePhone.setText(null);
        this.workPhone.setText(null);
       
    }

    /*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    * This method handles the event listner
    * of this frame
    <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< 
    @Override
    public void actionPerformed(ActionEvent e) {
        //Process Save button Event
       if(e.getSource().equals(this.saveBtn)){
           try {
               if(this.actionListener.save(this.surname.getText(),this.otherName.getText(),this.occupation.getText(),
                       this.officeAdd.getText(),this.homeAdd.getText(),this.email.getText(),this.homePhone.getText(),
                       this.workPhone.getText())){
                   JOptionPane.showMessageDialog(this, "Contact Saved Successfully.");
                   this.clearData();
               }else{
                   JOptionPane.showMessageDialog(this, "Unable to save Data");       
               }
           } catch (FileNotFoundException ex) {
               //Logger.getLogger(NewContact.class.getName()).log(Level.SEVERE, null, ex);
           }
       }else{
           
       }
           
    }
    */
    
//End of class
}