/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package contactsaver;

//Import Required classes
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.plaf.metal.MetalBorders;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;


/**
 *
 * @author Sunnyblaze
 */
public abstract class CSGUI extends JFrame {
    //Variables/Properties Declaration
    private final Image logo;
    private final Dimension size;
    private  JPanel componentPanel;
    private  JTextField surname;
    private  JTextField otherName;
    private  JTextField occupation;
    private  JTextField homeAdd;
    private  JTextField officeAdd;
    private  JTextField email;
    private  JTextField workPhone;
    private  JTextField homePhone;
    private GridBagConstraints constraint;
    private JPanel rightPanel;
    private JButton saveBtn,addPic,fromFile;
    private static JLabel picPane;
    protected Eventlisteners actionListener;
    private Eventlisteners menuListener;
    private DatabaseConnector database;
    private JTabbedPane pane;
    private JPanel backgroundPanel;
    private JTable table;
    private AppSettings appset;
    private Thread splashScreen;
    private DefaultTableModel model;
    private TableRowSorter sorter;
    
    //Method Declarations
    //Constructor
    public CSGUI(){
        MediaTracker tracker = new MediaTracker(this);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
                UnsupportedLookAndFeelException ex) {
            Logger.getLogger(CSGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.logo = Toolkit.getDefaultToolkit().getImage(".\\images\\logo.png");
        tracker.addImage(logo, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException ex) {
            Logger.getLogger(CSGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.size = Toolkit.getDefaultToolkit().getScreenSize();
        this.initComponents();
        
         this.database = new DatabaseConnector(this);
       //this.database.createTable();
       this.model = new DefaultTableModel();
       this.appset = new AppSettings(this.getSize(), this.logo);
       splashScreen = new Thread(appset);
       this.getContentPane().add(appset);
       splashScreen.start();
       this.initComponents();
       this.setIconImage(this.getLogo());
    }
    
    //Initialize component
    private void initComponents(){
        this.setTitle("Contact Saver");
                
        //Initalizing menubar components
        this.setIconImage(this.logo);
        this.setSize(this.getScreenSize().width/2, (this.getScreenSize().height/2)+100);
        this.setLocation((this.getScreenSize().width - this.getSize().width) / 2, (this.getScreenSize().height - this.getSize().height) / 2);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        
        this.pane = new JTabbedPane();
        this.componentPanel = new JPanel();
        this.rightPanel = new JPanel();
        this.rightPanel.setLayout(new GridBagLayout());
        this.saveBtn = new JButton("Save Contact");
        this.addPic = new JButton("Attach Photo");
        this.backgroundPanel = new JPanel();
        this.fromFile = new JButton("Add contact from file");
        //For left Panel Layout
        this.constraint = new GridBagConstraints();
        this.constraint.ipady = 10;
        this.constraint.ipadx = 10;
        this.constraint.fill = java.awt.GridBagConstraints.HORIZONTAL;
        this.constraint.insets = new Insets(10, 10, 10, 10);
        this.componentPanel.setLayout(new GridBagLayout());
        this.email = new JTextField();
        this.workPhone = new JTextField();
        this.homePhone = new JTextField();
        this.officeAdd = new JTextField();
        this.homeAdd = new JTextField();
        this.occupation = new JTextField();
        this.otherName = new JTextField();
        this.surname = new JTextField();
        this.surname.setToolTipText("Enter Surname");
        this.otherName.setToolTipText("Enter Other Names");
        this.occupation.setToolTipText("Enter Occupation");
        this.homeAdd.setToolTipText("Enter Home Address");
        this.officeAdd.setToolTipText("Enter Office Address");
        this.homePhone.setToolTipText("Enter Home Mobile Number");
        this.workPhone.setToolTipText("Enter Work Mobile Number");
        this.email.setToolTipText("Enter Email Address");
        this.constraint.gridx = 0;
        this.componentPanel.add(new JLabel("Surname:", JLabel.RIGHT), this.constraint);
        this.componentPanel.add(new JLabel("Other Names:",JLabel.RIGHT), this.constraint);
        this.componentPanel.add(new JLabel("Occupation:", JLabel.RIGHT), this.constraint);
        this.componentPanel.add(new JLabel("Office Address:",JLabel.RIGHT), this.constraint);
        this.componentPanel.add(new JLabel("Home Address:", JLabel.RIGHT), this.constraint);
        this.componentPanel.add(new JLabel("Email:",JLabel.RIGHT), this.constraint);
        this.componentPanel.add(new JLabel("Home Phone:", JLabel.RIGHT), this.constraint);
        this.componentPanel.add(new JLabel("Office Phone:",JLabel.RIGHT), this.constraint);
        this.constraint.weightx = 0.5;
        this.constraint.gridx = 1;
        this.componentPanel.add(this.surname, this.constraint);
        this.constraint.gridy = 1;
        this.componentPanel.add(this.otherName, this.constraint);
        this.constraint.gridy = 2;
        this.componentPanel.add(this.occupation, this.constraint);
        this.constraint.gridy = 3;
        this.componentPanel.add(this.officeAdd, this.constraint);
        this.constraint.gridy = 4;
        this.componentPanel.add(this.homeAdd, this.constraint);
        this.constraint.gridy = 5;
        this.componentPanel.add(this.email, this.constraint);
        this.constraint.gridy = 6;
        this.componentPanel.add(this.homePhone, this.constraint);
        this.constraint.gridy = 7;
        this.componentPanel.add(this.workPhone, this.constraint);
        
        //For Right Panel
        this.constraint = new GridBagConstraints();
        //NewContact.picPane = new JLabel("Please add a profile picture",JLabel.CENTER);
        this.constraint.ipady = 10;
        this.constraint.ipadx = 10;
        this.constraint.gridy = 1;
        this.constraint.weightx = 0.5;
        this.constraint.insets = new Insets(0, 10, 0, 10);
        this.constraint.gridx = 0;
        this.constraint.weighty = 0.1;
        this.constraint.fill = java.awt.GridBagConstraints.BOTH;
        this.constraint.gridwidth = GridBagConstraints.REMAINDER;
        //NewContact.picPane.setBorder(new MetalBorders.TextFieldBorder());
        //this.rightPanel.add(NewContact.picPane, this.constraint);
        this.constraint.gridy = 2;
        this.constraint.insets = new Insets(0, 10, 0, 10);
        this.constraint.weighty = 0.0;
        this.rightPanel.add(this.addPic, this.constraint);
        this.constraint.insets = new Insets(25, 10, 0, 10);
        this.constraint.gridy = 0;
        this.rightPanel.add(this.fromFile, this.constraint);
        this.constraint.gridy = 3;
        this.constraint.insets = new Insets(0, 10, 10, 10);
        this.rightPanel.add(this.saveBtn, this.constraint);
        this.backgroundPanel.setLayout(new GridLayout(1,2));
        this.backgroundPanel.add(this.componentPanel);
        this.backgroundPanel.add(this.rightPanel);
        this.pane.addTab("New Contact", this.backgroundPanel);       
        
        //Add Event Listeners to components
        this.actionListener = new Eventlisteners(this.saveBtn, this.addPic, this.getSize(), this.model);
        //this.saveBtn.addActionListener(this);
        this.addPic.addActionListener(this.actionListener);
        this.fromFile.addActionListener(this.actionListener.fileToServer(fromFile));
        
        //wait for the splash screen to finish setup
        //before rendering components
    }
    
    //Get Screen Size
    public Dimension getScreenSize(){
        return this.size;
    }
    
    //Get icon Image
    public Image getLogo(){
        return this.logo;
    }
    
    private void viewContactTab(){
        //Ananymous Jtable Class
        model.setDataVector(this.database.getSavedContact(), this.database.getColumnName());
        this.sorter = new TableRowSorter(this.model);
        this.table = new JTable(){
            @Override
            public boolean isCellEditable(int data, int columns){
                return true;
            }
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int data, int columns){
                this.getColumn("#").setPreferredWidth(5);
                this.setRowHeight(25);
                Component component = super.prepareRenderer(renderer, data, columns);
                if(data % 2 == 0){
                    component.setBackground(Color.WHITE);
                }else{
                    component.setBackground(Color.LIGHT_GRAY);
                }
                if(isCellSelected(data, columns)){
                    component.setBackground(Color.GRAY);
                }
                return component;
            }
        };
        this.table.setModel(model);
        this.table.setRowSorter(sorter);
        this.table.setGridColor(Color.WHITE);
        this.table.setPreferredScrollableViewportSize(new Dimension(super.getSize().width-50,super.getSize().height-50));
        this.table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(this.table);
        //Add Mouse listener event to table
        this.table.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
              //  NewContact.this.actionListener.mouseClicked(NewContact.this,NewContact.this.table,e);
            }
                    });
        this.getTabbedPane().addTab("Veiw Contact", scrollPane);
        this.actionListener.setRowSorter(this.sorter);
    }
    
    //Get tabbed pane component
    public JTabbedPane getTabbedPane(){
        return this.pane;
    }
    
    //Set or Change profile Picture
  /*  public static void setProfilePic(Image image){
        NewContact.picPane.setText("");
        int height = NewContact.picPane.getSize().height;
        int width = NewContact.picPane.getSize().width;
        NewContact.picPane.setIcon(new ImageIcon(image.getScaledInstance(height, width, 1)));
    }*/

   
}
