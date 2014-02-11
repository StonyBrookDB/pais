package edu.emory.cci.pais.view;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;


/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class help the user choose template or add new template
 *
 */

public class TemplateDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String getTemplatesSql = "select TEMPLATENAME,description from pais.template order by lastselecttime desc";
	PAISDBHelper db;
	Console description = new Console("Descrition:",5,43,false);
	boolean hidden = true;//show or hide newTemplatePanel
	UploadPanel parent;
	MyButton switchButton = new MyButton("show upload panel","show or hide the upload table");
	MyButton selectButton = new MyButton("select","choose this loading config template");
	MyButton deleteButton = new MyButton("delete","delete this loading config template");
	
	JPanel choicePanel = new JPanel();
	JPanel mainPanel = new JPanel();
	NewTemplatePanel newTemplatePanel;
	String[][] choiceItem;
	Choice choice = new Choice();
	int choiceSize = 0;
	
	
	public TemplateDialog(UploadPanel parent,PAISDBHelper db)
	{
		super();
		this.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        quit();
		      }
		    });
		this.parent = parent;
		this.db = db;
				
		this.setSize(new Dimension(500,200));
		this.setLocationRelativeTo(null);
	    this.setAlwaysOnTop(false); 
	    this.setVisible(true);
	    
		newTemplatePanel = new NewTemplatePanel(this,db);
		loadFromTemplate();
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	    choicePanel.setSize(new Dimension(500,200));
	    choicePanel.setVisible(true);
	    newTemplatePanel.setVisible(false);
	    
	    choicePanel.setLayout(new FlowLayout(FlowLayout.LEFT));	    
		choicePanel.add(choice);
		choicePanel.add(selectButton);
		choicePanel.add(deleteButton);
		choicePanel.add(switchButton);
		
        description.getTextArea().setBackground(Color.WHITE);
        mainPanel.add(choicePanel);
        mainPanel.add(description);
        mainPanel.add(newTemplatePanel);
        mainPanel.setSize(new Dimension(500,200));
        mainPanel.setVisible(true);
        
        switchButton.addMouseListener(new MouseListener(){

			@Override
			public void mousePressed(MouseEvent e) {
				showOrHide();
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
	    selectButton.addMouseListener(new MouseListener(){

			@Override
			public void mousePressed(MouseEvent e) {
				select();
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
	    deleteButton.addMouseListener(new MouseListener(){

			@Override
			public void mousePressed(MouseEvent e) {
				delete();
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
	    
	    choice.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				setDescription(choiceItem[1][choice.getSelectedIndex()]);				
			}
		});
	    
        this.add(mainPanel);
	    
	}
	
    //load the templates from database into Choice
	public boolean loadFromTemplate()
	{
		choiceItem = new String[2][100];
		try {
			ResultSet rs = db.getPreparedStatement(getTemplatesSql).executeQuery();
			choiceSize = 0;
			boolean empty = true;
			while(rs.next())
			{
			empty=false;
			choiceItem[0][choiceSize] =rs.getString(1);
			choiceItem[1][choiceSize++] =rs.getString(2);
			}
			if(empty==true)//there is no template in database
				{
				   choiceItem[0][0] ="            ";
				   choiceItem[1][0] ="the template table is empty!";
				   choiceSize=1;
				}
			
		} catch (SQLException e1) {
			choiceItem = null;
			System.out.println("error occured in query sql!");
			return false;
		}
		choice.removeAll();
		for(int i = 0;i<choiceSize;i++)
		{
			choice.addItem(choiceItem[0][i]);
		}
		
		setDescription(choiceItem[1][choice.getSelectedIndex()]);		
		return true;
	}
	//set the description area
	public void setDescription(String content)
	{
		try{
		 this.description.getTextArea().setText(content);
		}catch(Exception e)
		{
			this.description.getTextArea().setText("");
		}
	}
	//show or hide the new template panel
	public void showOrHide()
	{
	    if(hidden==true)
	    {
	    this.mainPanel.setSize(new Dimension(500,460));
		this.setSize(new Dimension(500,460));
		this.newTemplatePanel.setVisible(true);	
		hidden = false;
		this.switchButton.setText("hide upload panel");
	    }
	    else{
	    this.mainPanel.setSize(new Dimension(500,200));
		this.setSize(new Dimension(500,200));
		this.newTemplatePanel.setVisible(false);
		hidden = true;
		this.switchButton.setText("show upload panel");
	    }
		this.repaint();	
		this.validate();
	}
	//select this template
	public void select()
	{
		if(choice.getSelectedItem().trim().equalsIgnoreCase(""))//only happened when template table is empty, no template is chosen
			{
			    JOptionPane.showMessageDialog(null, "please select one template or add one!", "Error", JOptionPane.ERROR_MESSAGE);
			    return;
			}
		this.parent.getLcField().setText(choice.getSelectedItem());
	    try { 	
			PreparedStatement pstmt = db.getPreparedStatement("update pais.template set lastselecttime=current_timestamp where TEMPLATENAME=?");
			pstmt.setString(1, choice.getSelectedItem());
			pstmt.execute();
		} catch (SQLException e) {
			System.out.println("set last select time of template "+choice.getSelectedItem()+" failed");
		}
	    quit();//quit this dialog  
	}
	
	public void delete()//delete this template
	{
		
	  if(choice.getSelectedItem().trim().equalsIgnoreCase(""))//nothing can be deleted
		 return;
	  //make sure user want to delete
	  if(JOptionPane.showConfirmDialog(this, "delete template "+choice.getSelectedItem()+"?", "confirm delete", JOptionPane.OK_CANCEL_OPTION)==0)
	    {
        try {
        	//delete template in template table
			PreparedStatement pstmt1 = db.getPreparedStatement("delete from pais.template where TEMPLATENAME=?");
			pstmt1.setString(1, choice.getSelectedItem());
			pstmt1.execute();
			System.out.println("delete from template table successfully");
        }catch (SQLException e) {
		    System.out.println("failed to delete template in template table1");
	      }
        try{
			//drop related markuptable
			PreparedStatement pstmt2 = db.getPreparedStatement("drop table pais.markup_"+choice.getSelectedItem());
			pstmt2.execute();
			System.out.println("drop table pais.markup_"+choice.getSelectedItem()+" successfully");
        }catch (SQLException e) {
        	System.out.println("failed to drop table PAIS.MARKUP_"+choice.getSelectedItem()+"!");
	      }
		
        try{
			PreparedStatement pstmt3 = db.getPreparedStatement("delete from pais.templatefeaturesrl where templatename=?");
			pstmt3.setString(1, choice.getSelectedItem());
			pstmt3.execute();
			System.out.println("delete all the rows in PAIS.TEMPLATEFEATURESRL where templatename="+choice.getSelectedItem()+" successfully!");
        }catch(SQLException e){
        	System.out.println("failed to delete all the rows in PAIS.TEMPLATEFEATURESRL where templatename="+choice.getSelectedItem()+"!");
        }
	    }
	 //update the Choice after delete
	 loadFromTemplate();	
	 
	 System.out.println(choice.getSelectedItem()+" is deleted successfully!");
	}

	//quit this dialog, dispose it
	public void quit()
	{
		this.parent.setLcdialog(null);
		this.setVisible(false);
		this.dispose();
	}
	
	
	public static void main(String[] args)
	{
		
		JDialog jd = new TemplateDialog(null,new PAISDBHelper("localhost","50000","teng","terry08161043","tengdb"));
		jd.setModal(true);
	}
	

}
