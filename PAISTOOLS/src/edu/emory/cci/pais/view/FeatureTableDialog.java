package edu.emory.cci.pais.view;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.NumberFormatter;

import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
import edu.emory.cci.pais.util.TengUtils;

/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class realize the feature selection table, it can help the user choose, add or edit features for new template
 *
 */


public class FeatureTableDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] featureCol = {"feature name","type","control"};//table title
	public String[] types = {"DOUBLE","INTEGER","VARCHAR"};//data types
	
	NewTemplatePanel parent;
	PAISDBHelper db;
	
	MyButton completeButton = new MyButton("complete","select the features selected");
	MyButton selectAllButton = new MyButton("select all","select all these features");
	MyButton unselectAllButton = new MyButton("unselect all","unselect all these features");
	MyButton editPanelButton = new MyButton("edit panel","show edit panel");
	MyButton addButton = new MyButton("add","add this features");
	MyButton choosePanelButton = new MyButton("select panel","show select panel");
	MyButton deleteButton = new MyButton("delete","delete the selected feature");
	
	JTable featureTable;
    JPanel choosePanel = new JPanel();
    JPanel editPanel = new JPanel();
    JPanel mainPanel = new JPanel();
    
    JTextField featurenameField = new JTextField();
    JTextField varcharlenthField = new JFormattedTextField(new NumberFormatter(NumberFormat.getInstance()));
    Choice typeChoice = new Choice();
    
	public FeatureTableDialog(NewTemplatePanel parent,PAISDBHelper db)
	{
		super();
		this.parent=parent;
		this.db=db;
		
		featureTable = getFeatureTable(true);
		choosePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		editPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		this.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        quit();
		      }
		    });
		this.setSize(new Dimension(600,550));
		this.setLocationRelativeTo(null);
	    this.setAlwaysOnTop(false); 
	    this.setVisible(true);
		
		completeButton.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {
				getTableContent();
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
		selectAllButton.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {
				setAll("selected");
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
		unselectAllButton.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {
				setAll("unselected");
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
		editPanelButton.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				changePanel(TengUtils.EDITPANEL);
			}	
			});
		choosePanelButton.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				changePanel(TengUtils.CHOOSEPANEL);
			}	
			});
		addButton.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				addFeature();
			}	
			});
		
		deleteButton.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				deleteSelectedFeature();	
			}	
			});
		
		choosePanel.add(completeButton);
		choosePanel.add(selectAllButton);
		choosePanel.add(unselectAllButton);
		choosePanel.add(editPanelButton);
		
		//initiate the data type Choice and text filed
		for(int j=0;j<types.length;j++)
		{
			typeChoice.addItem(types[j]);
		}
		if(typeChoice.getSelectedItem().equalsIgnoreCase("VARCHAR"))
			varcharlenthField.setEditable(true);
		else{
			varcharlenthField.setText("");
			varcharlenthField.setEditable(false);
        }
		typeChoice.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(typeChoice.getSelectedItem().equalsIgnoreCase("VARCHAR"))
					varcharlenthField.setEditable(true);
				else{
					varcharlenthField.setText("");
					varcharlenthField.setEditable(false);
		        }
			}
		});
		
		featurenameField.setColumns(8);
		varcharlenthField.setColumns(3);
		editPanel.add(new JLabel("feature"));
		editPanel.add(featurenameField);
		editPanel.add(new JLabel("type"));
		editPanel.add(typeChoice);
		editPanel.add(varcharlenthField);
		editPanel.add(addButton);
		editPanel.add(deleteButton);
		editPanel.add(choosePanelButton);
        
		//when constructed show choose panel
		mainPanel.add(choosePanel);
		mainPanel.add(new JScrollPane(featureTable));
		this.add(mainPanel);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////main function
	public static void main(String[] args)
	{		
		PAISDBHelper db = new PAISDBHelper("localhost","50000","teng","terry08161043","tengdb");
		NewTemplatePanel.initiateFeatures(db);
		new FeatureTableDialog(null,db);
		db.executeUpdate("Call Sysproc.admin_cmd ('reorg Table PAIS.CALCULATION_FLAT')");
	}
	
	//swich panel between edit panel and choose panel
	public void changePanel(int paneltype)
	{
		if(paneltype==TengUtils.EDITPANEL)
		{
			this.mainPanel.removeAll();
			this.mainPanel.add(editPanel);
			featureTable = getFeatureTable(false);//reload feature table, set editable to false
			this.mainPanel.add(new JScrollPane(featureTable));
			this.mainPanel.repaint();
			this.mainPanel.revalidate();	
		}
		else if(paneltype==TengUtils.CHOOSEPANEL)
		{
			this.mainPanel.removeAll();
			this.mainPanel.add(choosePanel);
			featureTable = getFeatureTable(true);
			this.mainPanel.add(new JScrollPane(featureTable));
			this.mainPanel.repaint();
			this.mainPanel.revalidate();
		}
	}
	//set all the table rows to selected or unselected
	public void setAll(String content)
	{
		TableModel table = featureTable.getModel();
		int count = table.getRowCount();
		for(int i =0;i<count;i++)
		{
			featureTable.setValueAt(content, i, 2);
	    	NewTemplatePanel.featuresMap.put((String) table.getValueAt(i, 0), new String[]{(String) table.getValueAt(i, 1),content});
		}
		
	}
	//read the content of table into feature map
	public void getTableContent()
	{
		TableModel table = featureTable.getModel();
		int count = table.getRowCount();
		NewTemplatePanel.featuresMap.clear();
		for(int i =0;i<count;i++)
		{
			NewTemplatePanel.featuresMap.put(table.getValueAt(i, 0).toString(),new String[]{table.getValueAt(i, 1).toString(),table.getValueAt(i, 2).toString()});
		}
		quit();
	}
	//add new feature
	public void addFeature()
	{
		if(featurenameField.getText().trim().replaceAll(" ", "").equalsIgnoreCase(""))//feature name is empty
		{
			JOptionPane.showMessageDialog(null, "please input the feature name!", "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		else if(NewTemplatePanel.featuresMap.containsKey(featurenameField.getText().replaceAll(" ", "")))//feature name already exists
		{
			JOptionPane.showMessageDialog(null, featurenameField.getText().replaceAll(" ", "")+" already exists!", "Error", JOptionPane.ERROR_MESSAGE);
		    //return;
		}
		else if(typeChoice.getSelectedItem().equalsIgnoreCase("VARCHAR")&&varcharlenthField.getText().trim().equalsIgnoreCase(""))
		{//if type is VARCHAR but the lenth is undefined
			JOptionPane.showMessageDialog(null, "please input the lenth of type VARCHAR!", "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		//satisfied the add requirement
		String featurename = featurenameField.getText().trim().replaceAll(" ", "");
		//insert feature into feature table
		String tmptype= typeChoice.getSelectedItem().equalsIgnoreCase("VARCHAR")?"VARCHAR "+"("+varcharlenthField.getText()+")":typeChoice.getSelectedItem();
		try {
			PreparedStatement pstmt = db.getPreparedStatement("insert into pais.features(featurename,type) values(?,?)");
			pstmt.setString(1, featurename);
			pstmt.setString(2, tmptype);
		    pstmt.execute();
		} catch (SQLException e) {
			if(e.getSQLState().equalsIgnoreCase("23505"))//already exists, then update the type to new type
				{
				System.out.println(featurename+" already exists in feature table!");
				PreparedStatement ps = db.getPreparedStatement("update pais.features set type=? where featurename=?");
				try {
					ps.setString(1, tmptype);
					ps.setString(2, featurename);
					ps.execute();
				} catch (SQLException e1) {
					System.out.println("update feature "+featurename+" failed. SqlState="+e1.getSQLState());			
				}
				}
			else//otherwise tell the user, add failed
			    {
				    System.out.println("add new feature "+featurename+" failed!");
				    return;
			    }
		}
		//add one column in pais.calculation_flat
	  for(int j = 0;j<TengUtils.RETRYTIME;j++)
	  {
		try{
		PreparedStatement pstmt = db.getPreparedStatement("alter table pais.CALCULATION_FLAT add "+featurename+" "+tmptype);
		pstmt.execute();
		break;
		}catch(SQLException e){	
			if(e.getSQLState().equalsIgnoreCase("42711"))
			{
				System.out.println(featurename+" already been one column of table pais.calculation_flat!");
				db.executeUpdate("Call Sysproc.admin_cmd ('reorg Table PAIS.CALCULATION_FLAT')");
				PreparedStatement pstmt = db.getPreparedStatement("alter table pais.CALCULATION_FLAT drop column "+featurename);
				try {
					pstmt.execute();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				if(j==TengUtils.RETRYTIME-1)
					j--;
			}
			else if(e.getSQLState().equalsIgnoreCase("55019"))
			   {
				   System.out.println("add column "+featurename+" failed, reorg and try again. "+(TengUtils.RETRYTIME-j)+" retry times remain");
				   db.executeUpdate("Call Sysproc.admin_cmd ('reorg Table PAIS.CALCULATION_FLAT')");//reorg before execute
			   }
			else
			   {
				   System.out.println("add new column "+featurename+" into table pais.CALCULATION_FLAT failed!");
				   db.executeUpdate("delete from pais.features where featurename='"+featurename+"'");
				   return;	   
			   }
		}
	  }
		//put into features map
		NewTemplatePanel.featuresMap.put(featurename, new String[]{tmptype,"selected"});
		changePanel(TengUtils.EDITPANEL);//update the table
		System.out.println("feature "+featurename+" is added successfully into database");
	}
	
	//delete the selected features in the table
		public void deleteSelectedFeature()
		{
			
			int[] selectedrows = featureTable.getSelectedRows();
			StringBuffer columns = new StringBuffer("(");
			StringBuffer features = new StringBuffer();
			//first generate the array of columns and features for the using of delete features and drop columns
			for(int i = 0;i<selectedrows.length;i++)
			{
				 String feature = (String)featureTable.getValueAt(selectedrows[i], 0);
				 if(i!=selectedrows.length-1)
					 {
					    columns.append(feature+",");
					    features.append("featurename='"+feature+"' or ");
					 }
				 else
				 {
					    columns.append(feature+")");
					    features.append("featurename='"+feature+"'"); 
				 }
			}
			 //get all the templates which need to be updated and notice the user
			 ArrayList<String> templateslist = new ArrayList<String>(); 
			 try{
			      ResultSet rs = db.getPreparedStatement("select distinct(templatename) from pais.templatefeaturesrl where "+features).executeQuery();
			      while(rs.next())
			      {
				     templateslist.add(rs.getString(1));
			      }
			 }catch(SQLException e)
			    {
				   System.out.println("Error occured when get the templates which needed to be updated. SqlState="+e.getSQLState());
			    }
			 StringBuffer templates = new StringBuffer();
			 for(String s:templateslist)
				 templates.append(" "+s+" ");
		     if(JOptionPane.showConfirmDialog(this, templates+" are still using the features you want to delete, still delete?(these template will be updated)", "confirm delete", JOptionPane.OK_CANCEL_OPTION)!=0)
	          return;
			//in this loop, remove features from featuresMap and drop the columns in pais.calculation_flat
		    for(int i = 0;i<selectedrows.length;i++)
		     {
			 String feature = (String)featureTable.getValueAt(selectedrows[i], 0);
			 //delete from features map
			 if(NewTemplatePanel.featuresMap.containsKey(feature))
				NewTemplatePanel.featuresMap.remove(feature);
			 //drop column in pais.calculation_flat
			 for(int j = 0;j<TengUtils.RETRYTIME;j++)
			 {
			   try{
				PreparedStatement pstmt = db.getPreparedStatement("alter table pais.CALCULATION_FLAT drop column "+feature);
				pstmt.execute();
				System.out.println("drop column "+feature+" in table pais.calculation_flat successfully");
				break;
			    }catch(SQLException e){		
				   if(e.getSQLState().equalsIgnoreCase("55019"))
				   {
					   System.out.println("drop column "+feature+" failed, reorg and try again. "+(TengUtils.RETRYTIME-j)+" retry times remain");
					   db.executeUpdate("Call Sysproc.admin_cmd ('reorg Table PAIS.CALCULATION_FLAT')");//reorg before execute
				   }
				   else
				   {
					   System.out.println("drop column "+feature+" from table pais.CALCULATION_FLAT failed! SqlState= "+e.getSQLState());	
					   break;
				   }
			    }
			  }
		     }//end for
		    
			 //delete the rows whose feature need to be deleted in the pais.templatefeaturesrl table
			 db.executeUpdate("delete from pais.templatefeaturesrl where "+features);
			 
			 /*
			  *
			  * //update the templates' loading config xml
			 for(String s:templateslist)
			 {
				HashMap<String,String[]> tmpmap = new HashMap<String,String[]>();
				  try{
					  PreparedStatement tmpps = db.getPreparedStatement("select f.featurename,f.type from pais.features f,pais.templatefeaturesrl r where r.featurename=f.featurename and r.templatename=?");
					  tmpps.setString(1, s);
					  ResultSet tmprs = tmpps.executeQuery();
					  while(tmprs.next())
					  {
						tmpmap.put(tmprs.getString(1), new String[]{tmprs.getString(2),"selected"});
					  }
					  
					  PreparedStatement pstmt = db.getPreparedStatement("update pais.template set loadingconfig=?");
					  pstmt.setString(1, TengUtils.generateloadingConfigXML(s, tmpmap));
					  pstmt.execute();
					  System.out.println("the loading config xml of "+s+" is updated successfully!");
				      }catch(SQLException e)
						{
							System.out.println("the loading config xml of "+s+" updated failed! SQLState="+e.getSQLState());
						}
			  }
			 */
			 
			 //delete feature from feature table
			  try {
				PreparedStatement pstmt = db.getPreparedStatement("delete from pais.features where "+features);
				System.out.println("delete from pais.features where "+features);
			    pstmt.execute();
			  }catch (SQLException e) {
				 System.out.println("delete from pais.features failed! SqlState= "+e.getSQLState());	
			  }
			 changePanel(TengUtils.EDITPANEL);//update the table
			 System.out.println("features deleted successfully!");
		}
	
	//get new table based on the values in feature map
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JTable getFeatureTable(final boolean celleditable)
	{
		//sort the features by name
		 List keylist = new ArrayList(NewTemplatePanel.featuresMap.entrySet());     
		 Collections.sort(keylist, new Comparator(){
		  public int compare(Object arg1, Object arg2)    
		 {   
		    Map.Entry obj1 = (Map.Entry) arg1;   
		    Map.Entry obj2 = (Map.Entry) arg2;   
		    return obj1.getKey().toString().compareToIgnoreCase((String) obj2.getKey());   
		  }   
		});   

		Object[][] featureRow = new Object[NewTemplatePanel.featuresMap.size()][3];
		int rowcount =0;
		for (Iterator iter = keylist.iterator(); iter.hasNext();)    
		{   
		   Map.Entry entry = (Map.Entry)iter.next();   
		   String  key = (String)entry.getKey();   
		   featureRow[rowcount][0] = key;
		   featureRow[rowcount][1] = NewTemplatePanel.featuresMap.get(key)[0];
		   featureRow[rowcount++][2] = NewTemplatePanel.featuresMap.get(key)[1];   
		} 
		//only the choose panel and the control column is editable
		JTable table = new JTable(new DefaultTableModel(featureRow, featureCol)){
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int rowIndex, int colIndex) {
				if(colIndex==2&&celleditable)
				  return true;
				else return false;
			}
		};
		table.setBackground(SystemColor.menu);
		table.setBorder(new LineBorder(Color.black));
		table.setShowGrid(true);
		table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
            	///press the table
            }
            });
		table.getColumn("control").setCellRenderer(new ButtonRenderer());
		table.getColumn("control").setCellEditor(new ButtonEditor(new JCheckBox()));		
		
		
		table.getColumnModel().getColumn(0).setPreferredWidth(300);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setPreferredWidth(120);
		
		return table;

	}
	
	public void quit()
	{
		if(this.parent!=null)
		   this.parent.setFeaturedialog(null);
		this.setVisible(false);
		this.dispose();
	}
}


//////////////////////////////////////////////////////////////////////////////////////////////
class ButtonRenderer extends JButton implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
      @Override
	  public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) 
	  {
		    if (isSelected) {
		      setForeground(table.getSelectionForeground());
		      setBackground(table.getSelectionBackground());
		    } else {
		      setForeground(table.getForeground());
		      setBackground(UIManager.getColor("Button.background"));
		    }
		    setText((value == null) ? "" : value.toString());
		    return this;
	  }
}

class ButtonEditor extends DefaultCellEditor {
	  private static final long serialVersionUID = 1L;

	  protected JButton button;
      private String label;
      public ButtonEditor(JCheckBox checkBox) {
	    super(checkBox);
	    button = new JButton();
	    button.setOpaque(true);
	    button.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        fireEditingStopped();
	      }
	    });
	  }

      public Component getTableCellEditorComponent(JTable table, Object value,
	      boolean isSelected, int row, int column) {
    	if(value.toString().equalsIgnoreCase("unselected"))
    	{
    	table.setValueAt("selected", row, 2);
	    label = "selected";
    	}
    	else
    	{
    		table.setValueAt("unselected", row, 2);
    		label = "unselected";
    		button.setBackground(Color.white);
    	}
    	NewTemplatePanel.featuresMap.put((String) table.getValueAt(row, 0), new String[]{(String) table.getValueAt(row, 1),label});
    	button.setText(label);
	    return button;
	  }
	  public Object getCellEditorValue() {
	    return new String(label);
	  }
	  public boolean stopCellEditing() {
	    return super.stopCellEditing();
	  }
	  protected void fireEditingStopped() {
	    super.fireEditingStopped();
	  }
}