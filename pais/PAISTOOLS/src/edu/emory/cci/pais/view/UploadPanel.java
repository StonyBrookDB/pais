package edu.emory.cci.pais.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import edu.emory.cci.pais.controller.UploadController;
import edu.emory.cci.pais.dataloader.db2helper.DBConfig;
import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
import edu.emory.cci.pais.util.TengUtils;
/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class realize the upload panel
 *
 */
public class UploadPanel extends BasicPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//invoke the data loading manager
	public static String updatestatussql = "update pais.DLMSTATUS set workstatus=?";
	private static Long heartbeat = (long) 5000;//longest gap time, longer than this, the data loading manager must died
	public static PAISDBHelper db = null;
	String host ;
	String port ;
	String username ;
	String passwd ;
	String database ;
	
	TemplateDialog lcdialog;
	public TemplateDialog getLcdialog() {
		return lcdialog;
	}

	public void setLcdialog(TemplateDialog lcdialog) {
		this.lcdialog = lcdialog;
	}

	MainPanel mainPanel;

	JRadioButton slideRB = new JRadioButton("Slides");
	JRadioButton collectionRB = new JRadioButton("Collection");
	ButtonGroup dirTypeBG = new ButtonGroup();
	
	JLabel fileTypeLabel = new JLabel("File Type");
	JLabel dbcfileLabel  = new JLabel("  db config file:");
	JLabel inputLabel    = new JLabel("          input file:");
	JLabel lcLabel       = new JLabel("         template:");
	JLabel hostLabel     = new JLabel("                  host:");
	JLabel portLabel     = new JLabel("port:");
	JLabel pwdLabel      = new JLabel("password:");
	JLabel userLabel     = new JLabel("       username:");
	JLabel dbLabel       = new JLabel("database:");
	
	JTextField inputfileField =new JTextField();
	LocateButton inputfileButton = new LocateButton("locate","choose the input directory",inputfileField,JFileChooser.DIRECTORIES_ONLY,this);
	
	MyButton dbcfileButton = new MyButton("path","choose the db config file");
	MyButton reloadButton = new MyButton("load","reload the db parameters from db config file");
	MyButton testdbButton = new MyButton("connect","test db connection");
	MyButton uploadButton = new MyButton("start upload","start uploading");
	MyButton lcButton = new MyButton("select","select loading config template");
	
	
	JTextField dbcfileField = new JTextField();
	JTextField hostField = new JTextField();
	JTextField portField = new JTextField();
	JTextField pwdField = new JTextField();
	JTextField userField = new JTextField();
	JTextField dbField = new JTextField();
	JTextField lcField = new JTextField();
	
	
    public JTextField getLcField() {
		return lcField;
	}

	public void setLcField(JTextField lcField) {
		this.lcField = lcField;
	}

	JPanel typeChoosePanel = new JPanel();
	JPanel inputPanel = new JPanel();
	JPanel dbcPanel = new JPanel();
	JPanel dbPanel = new JPanel();
	JPanel userPanel = new JPanel();
	JPanel lcPanel = new JPanel();
		

	public UploadPanel(MainPanel mainPanel)
	{
		super();
		this.mainPanel = mainPanel;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		typeChoosePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		dbcPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		dbPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		lcPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		
		dirTypeBG.add(slideRB);
		dirTypeBG.add(collectionRB);
		slideRB.setSelected(true);
		
		typeChoosePanel.add(fileTypeLabel);
		typeChoosePanel.add(slideRB);
		typeChoosePanel.add(collectionRB);
		typeChoosePanel.add(uploadButton);
		
		
		inputfileField.setColumns(30);
		inputPanel.add(inputLabel);
		inputPanel.add(inputfileField);
		inputPanel.add(inputfileButton);
		
		dbcfileField.setColumns(30);
		dbcPanel.add(dbcfileLabel);
		dbcPanel.add(dbcfileField);
		dbcPanel.add(dbcfileButton);
		dbcPanel.add(reloadButton);
		dbcPanel.add(testdbButton);

		dbcfileButton.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {
				parsedbcFile();
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}	
			});
		
		reloadButton.addMouseListener(new MouseListener(){

			@Override
			public void mousePressed(MouseEvent e) {
				loaddbConfig(dbcfileField.getText());
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}	
			});
		testdbButton.addMouseListener(new MouseListener(){

			@Override
			public void mousePressed(MouseEvent e) {
				if(testConnect()!=null)
					{
					ImageIcon ic = new ImageIcon("resource"+File.separator+"duihao.jpg");
					JOptionPane.showMessageDialog(null, "connect to db "+database+" successfully!", "Success", JOptionPane.OK_OPTION,ic);
					}
				else
				{
					   JOptionPane.showMessageDialog(null, "cannot connect to database "+database+" !", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}	
			});
		uploadButton.addMouseListener(new MouseListener(){

			@Override
			public void mousePressed(MouseEvent e) {
				upload();
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}	
			});
		
		lcButton.addMouseListener(new MouseListener(){

			@Override
			public void mousePressed(MouseEvent e) {
				selectlc();
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}	
			});
		
		hostField.setColumns(20);
		portField.setColumns(5);
		userField.setColumns(10);
		pwdField.setColumns(10);
		dbField.setColumns(6);
		lcField.setColumns(20);
		lcField.setEditable(false);
		lcField.setBackground(Color.white);
		
		dbPanel.add(hostLabel);
		dbPanel.add(hostField);
		dbPanel.add(portLabel);
		dbPanel.add(portField);
		dbPanel.add(dbLabel);
		dbPanel.add(dbField);

		userPanel.add(userLabel);
		userPanel.add(userField);
		userPanel.add(pwdLabel);
		userPanel.add(pwdField);
		lcPanel.add(lcLabel);
		lcPanel.add(lcField);
		lcPanel.add(lcButton);
		
		this.add(typeChoosePanel);
		this.add(inputPanel);
		this.add(dbcPanel);
		this.add(dbPanel);
		this.add(userPanel);
		this.add(lcPanel);
		
	}
		
	
	public JTextField getInputfileField() {
		return inputfileField;
	}

	public void setInputfileField(JTextField inputfileField) {
		this.inputfileField = inputfileField;
	}
    //locate the dbconfig file, and parse the parameters from it
	public void parsedbcFile()
	{
			String path = null;
			JFileChooser fc = new JFileChooser(); 
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY); 
	        int intRetVal = fc.showOpenDialog(this); 
	        if( intRetVal == JFileChooser.FILES_ONLY)
	        {
	          path = fc.getSelectedFile().getPath();
	        }
	        if(path==null)
	        	return;
	        dbcfileField.setText(path);
	        loaddbConfig(path);
	}
	//load parameters from db config file	
	public void loaddbConfig(String path)
	{
		File file = new File(path);
		if(file.exists()==false||file.isDirectory())
		{
	        JOptionPane.showMessageDialog(null, path + " is not find!", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
		}
		
		try{
			DBConfig dbc = new DBConfig(file);
			Properties props = dbc.getProperties();
			host = props.getProperty("host");
			port = props.getProperty("port");
			username = props.getProperty("username");
			passwd = props.getProperty("passwd");
			database = props.getProperty("database");
			
			this.hostField.setText(host);
			this.portField.setText(port);
			this.dbField.setText(database);
			this.userField.setText(username);
			this.pwdField.setText(passwd);	
			
			System.out.println("loaded db information from "+path);	
		 }
        catch(Exception e)
        {
        	dbcfileField.setText("");
        	JOptionPane.showMessageDialog(null, "this is not a valid db config file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
   }
   //test the connection use the parameters, if success, return one PAISDBHelper object	else null
   public PAISDBHelper testConnect()
   {
			host = this.hostField.getText();
			port = this.portField.getText();
			database = this.dbField.getText();
			username = this.userField.getText();
			passwd = this.pwdField.getText();	
		   try{
			PAISDBHelper db =  new PAISDBHelper(host,port,username,passwd,database);
			UploadPanel.db = db;
			return db;
		   }
		   catch(Exception e)
		   {
			   return null;  
		   }
	}
   //open loading config template dialog if not exists, if exists, set it on top
	public void selectlc()
	{
	   //if exists, set it on top
	   if(this.lcdialog!=null)
	   {
			this.lcdialog.setAlwaysOnTop(true);
			this.lcdialog.setAlwaysOnTop(false);
			return;
	   }
	   //if not, generate one db connection, and then open one template load dialog   
	   PAISDBHelper db = getConnect();
	   if(db!=null)
	   {
			this.lcdialog = new TemplateDialog(this,db);
	   }
	   else//connect to db failed
	   {
		   JOptionPane.showMessageDialog(null, "cannot connect to database "+database+" !", "Error", JOptionPane.ERROR_MESSAGE);
		   return;   
	   }
	}
	
	public PAISDBHelper getConnect()
	{
		if(UploadPanel.db==null)
		{
			return testConnect();
		}
		else 
		   return UploadPanel.db;
	}

	//start upload from the input file	
    public void upload()
    {
    	   //check the input path
    	   if(inputfileField.getText().equalsIgnoreCase(""))
		   {
			   JOptionPane.showMessageDialog(null, "please select one input directory!", "Error", JOptionPane.ERROR_MESSAGE);
			   return;
		   }
    	   //test database 
		   PAISDBHelper db = getConnect();
		   if(db==null)
		   {
			   JOptionPane.showMessageDialog(null, "cannot connect to database "+database+" !", "Error", JOptionPane.ERROR_MESSAGE);
			   return;
		   }
		   //then check the template field, if empty, return
		   if(lcField.getText().equalsIgnoreCase(""))
		   {
			   JOptionPane.showMessageDialog(null, "please choose one loading config file!", "Error", JOptionPane.ERROR_MESSAGE);
			   return;
		   }
		   //start upload
		   try{
		   UploadController controller = new UploadController();
           controller.documentUploader(db,this.inputfileField.getText(),this.collectionRB.isSelected()?TengUtils.COLLECTION:TengUtils.SLIDE,lcField.getText());
		   }catch(Exception e)
		   {
			   JOptionPane.showMessageDialog(null, "upload failed !", "Error", JOptionPane.ERROR_MESSAGE);
		   }
		   
		   //after upload, check the status of data loading manager
		   int check = checkStatus(db);
		   if(check==TengUtils.DLMDIED)//the data loading manager is died, first reset its workstatus to 1, which means not work
		   {
			   PreparedStatement resetstatus = db.getPreparedStatement("update pais.DLMSTATUS SET WORKSTATUS=1");
			   try {
				resetstatus.execute();
			} catch (SQLException e) {
				System.out.println("reset workstatus failed!");
				
			}
		   }
		   //if manager is died or not work, set the workstatus to 0, this operation will trigger stored procedure to run data loading manager
		   if(check==TengUtils.DLMNOTWORKING||check==TengUtils.DLMDIED)
		   {
			   PreparedStatement invokestmp = db.getPreparedStatement("update pais.DLMSTATUS SET WORKSTATUS=0");
			   try {
				invokestmp.execute();
			} catch (SQLException e) {
				System.out.println("invoke data loading manager failed!");
			}   
		   }
	
		}
		
		//check the working status of data loading manager
		public int checkStatus(PAISDBHelper db)
		{
			PreparedStatement pstmt = db.getPreparedStatement("select * from pais.DLMSTATUS");
			try {
				ResultSet rs = pstmt.executeQuery();
				rs.next();
				if(rs.getInt(1)==1)
				{
					return TengUtils.DLMNOTWORKING;//data loading manager is not working
				}
				else //else check the gap between current time and last heat beat time, if the gap is larger than threshold, it must be died
				{
					Long currenttime = System.currentTimeMillis();
					Long lastheartbeat = rs.getTimestamp(2).getTime();
					if(currenttime-lastheartbeat>=2*heartbeat)//data loading manager is dead
						return TengUtils.DLMDIED;
					else return TengUtils.DLMWORKING;//data loading manager is still running
				}	
			} catch (SQLException e) {
				e.printStackTrace();
				return TengUtils.DLMDIED;//catch exception, then assume it is died
			}			
		}
	
		
		
	public static void  main(String[] args){
		
		JFrame jf = new JFrame("test");
		jf.setBounds(0, 0, 650, 500);
		jf.setLocationRelativeTo(null);
		jf.add(new UploadPanel(null));
		jf.setVisible(true);
		}

}
