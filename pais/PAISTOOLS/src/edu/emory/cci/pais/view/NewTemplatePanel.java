package edu.emory.cci.pais.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
import edu.emory.cci.pais.util.TengUtils;
/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class realize one panel which can help the user define new template
 *
 */
public class NewTemplatePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String insertSql = "insert into pais.template(templatename,description,lastselecttime) values(?,?,current_timestamp)";
	public static final String checkTemplateExistSql = "select count(*) from pais.template where templatename = ?";
	public static final String insertFTRLSql = "insert into pais.templatefeaturesrl values(?,?)";
	public static final String getFeaturesSql = "select featurename,type from pais.features order by featurename";
	public static final String NewMarkupTableSql = 
			"create table pais.markup_nameoftable"+
			"(MARKUP_ID DECIMAL(30,0) NOT NULL," +
			"NAME VARCHAR(64)," +
			"GEOMETRICSHAPE_ID DECIMAL(30,0)  NOT NULL," +
			"ANNOTATION_ID  DECIMAL(30,0)  NOT NULL," +
			"POLYGON DB2GSE.ST_POLYGON INLINE LENGTH 30000," +
			"PROVENANCE_ID DECIMAL(30,0)," +
			"TILENAME VARCHAR(64)  NOT NULL," +
			"PAIS_UID VARCHAR(64)  NOT NULL," +
			"CENTROID_X DOUBLE," +
			"CENTROID_Y DOUBLE)";
	
	public static HashMap<String,String[]> featuresMap = new HashMap<String,String[]>();
	
	JPanel uploadPanel = new JPanel();
	JTextField nameField = new JTextField();
	MyButton uploadButton = new MyButton("upload","upload this loading config template");
	MyButton selectFeaturesButton = new MyButton("features","select the features for this template");
	Console description = new Console("description:",9,2,true);
	
	PAISDBHelper db;
	TemplateDialog parent;
	FeatureTableDialog featuredialog;
	
	public FeatureTableDialog getFeaturedialog() {
		return featuredialog;
	}
	public void setFeaturedialog(FeatureTableDialog featuredialog) {
		this.featuredialog = featuredialog;
	}
	
	public NewTemplatePanel(TemplateDialog parent, PAISDBHelper db)
	{
		super();
		this.parent = parent;
		this.db = db;
		initiateFeatures(db);//initiate the featureMap from database
		
		this.setBorder(new LineBorder(Color.black));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setSize( new Dimension(500,250));
		
		uploadPanel.setLayout(new FlowLayout(FlowLayout.LEFT));	
		nameField.setColumns(10);
		
		uploadPanel.add(new JLabel("name:"));
		uploadPanel.add(nameField);
		uploadPanel.add(selectFeaturesButton);
		uploadPanel.add(uploadButton);
		
		
		this.add(uploadPanel);
		this.add(description);
		
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
		selectFeaturesButton.addMouseListener(new MouseListener(){

			@Override
			public void mousePressed(MouseEvent e) {
				getFeatures();
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
	 
	}
	//upload the loading config file
	public void upload()
	{
		//check the name, if it is empty
		if(nameField.getText().trim().equalsIgnoreCase(""))
		{
			JOptionPane.showMessageDialog(null, "please type in name!", "Error", JOptionPane.ERROR_MESSAGE);	
			return;
		}
		//check if this name exists or not
		try {
			PreparedStatement pstmt = db.getPreparedStatement(checkTemplateExistSql);
			pstmt.setString(1, nameField.getText().toUpperCase());
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			if(rs.getInt(1)!=0)
			{
				JOptionPane.showMessageDialog(null, "this name already exist!", "Error", JOptionPane.ERROR_MESSAGE);	
				return;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "upload failed! SQL ErrorCode "+e.getErrorCode(), "Error", JOptionPane.ERROR_MESSAGE);	
			return;
		}
		//name is ok, check the description, if it is empty, ask the user continue upload or not
		if(description.getTextArea().getText().trim().equalsIgnoreCase(""))
		{
			if(JOptionPane.showConfirmDialog(this, "description is empty do you want to upload with out description?", "confirm upload", JOptionPane.OK_CANCEL_OPTION)!=0)
				{
				     return;
				}	
		}
		//description is ok, then try to upload
		try {
			//insert into template table
			PreparedStatement ps = db.getPreparedStatement(insertSql);
			//generate the config xml file according to the table name and features selected
			//String xmlContent = TengUtils.generateloadingConfigXML(nameField.getText(),featuresMap);
            
			ps.setString(1, nameField.getText());
			ps.setString(2, description.getTextArea().getText());
			//ps.setString(3, xmlContent);
			ps.execute();
			//create table pais.markup_templatename
			try{
			db.getPreparedStatement(NewMarkupTableSql.replaceAll("nameoftable", nameField.getText())).execute();
			}catch(SQLException e)
			{//create table failed, if table already exists, inform user, if other reason, delete this from template
				if(e.getErrorCode()==-601)
					JOptionPane.showMessageDialog(null, nameField.getText()+" already exists!", "Error", JOptionPane.ERROR_MESSAGE);
				else
				{
				 try{db.getPreparedStatement("delete from pais.template where name="+nameField.getText()).execute();}catch(SQLException e1){}
				 JOptionPane.showMessageDialog(null, nameField.getText()+" table create failed!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				return;
			}
			
		  }catch (SQLException e) {//insert into template table failed
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null, "upload failed!"+e.getSQLState(), "Error", JOptionPane.ERROR_MESSAGE);	
			try{db.getPreparedStatement("drop table pais.markup_"+nameField.getText()).execute();}catch(SQLException e1){}
			return;
		}
		//record the features of this template
		for(String key:featuresMap.keySet())
		{
			if(featuresMap.get(key)[1].equalsIgnoreCase("selected"))//this feature is part of this template
				{
				try{
					PreparedStatement ps = db.getPreparedStatement(insertFTRLSql);
					ps.setString(1, key);
					ps.setString(2, nameField.getText());
					ps.execute();
					}catch(SQLException e1){e1.printStackTrace();}
				}
		}
		parent.loadFromTemplate();
		JOptionPane.showMessageDialog(null, "upload sucessfully!", "success", JOptionPane.INFORMATION_MESSAGE);	
		initiateFeatures(db);
		this.nameField.setText("");
		this.description.getTextArea().setText("");
		this.parent.showOrHide();
	}
	//show feature dialog 
	public void getFeatures()
	{
		if(featuredialog==null)
			featuredialog = new FeatureTableDialog(this,db);
		else {
			featuredialog.setAlwaysOnTop(true);
			featuredialog.setAlwaysOnTop(false);
		}	
	}
	
	public static void initiateFeatures(PAISDBHelper db)
	{
		featuresMap.clear();
		try {
			ResultSet rs = db.getSqlQueryResult(getFeaturesSql);
			while(rs.next())
			{
				featuresMap.put(rs.getString(1), new String[]{rs.getString(2),"selected"});
			}
			System.out.println("initiate features successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//print out all the features in featuresMap. just for test	
	public static void outputFeatures()
	{
		for(String key:NewTemplatePanel.featuresMap.keySet())
		{
			System.out.println(key+" "+NewTemplatePanel.featuresMap.get(key)[0]);
		}
	}

	public static void main(String[] args)
	{
		
		//new TemplateDialog(null,null,new PAISDBHelper("localhost","50000","teng","terry08161043","tengdb"));
		
	}
}
