package edu.emory.cci.pais.view;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import edu.emory.cci.pais.controller.SqlGeneratorController;
import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
import edu.emory.cci.pais.util.TengUtils;

public class SqlGeneratorPanel extends BasicPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	SqlGeneratorController controller;

	JPanel templatesPanel = new JPanel();
	JPanel outTypePanel = new JPanel();
	JPanel outputPanel = new JPanel();
	PAISDBHelper db;
	ArrayList<JCheckBox> templateList = new ArrayList<JCheckBox>();
	
	JCheckBox indexCB = new JCheckBox("index");
	JCheckBox keyCB = new JCheckBox("key");
	JCheckBox tableCB = new JCheckBox("tables");
	public JCheckBox getTableCB() {
		return tableCB;
	}

	public void setTableCB(JCheckBox tableCB) {
		this.tableCB = tableCB;
	}
	

	JRadioButton seprateRB = new JRadioButton("seperate");
	
	JRadioButton comboRB = new JRadioButton("combo");
	ButtonGroup outputTypeBG = new ButtonGroup();
	
	
	public ArrayList<JCheckBox> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(ArrayList<JCheckBox> templateList) {
		this.templateList = templateList;
	}

	public JCheckBox getIndexCB() {
		return indexCB;
	}

	public void setIndexCB(JCheckBox indexCB) {
		this.indexCB = indexCB;
	}

	public JCheckBox getKeyCB() {
		return keyCB;
	}

	public void setKeyCB(JCheckBox keyCB) {
		this.keyCB = keyCB;
	}

	public JRadioButton getComboRB() {
		return comboRB;
	}

	public void setComboRB(JRadioButton comboRB) {
		this.comboRB = comboRB;
	}

	JTextField outdirField = new JTextField();
	LocateButton outdirButton = new LocateButton("locate","choose the directory SQL file output",outdirField,JFileChooser.DIRECTORIES_ONLY,this);
	
	MyButton generateButton = new MyButton("generate","generate the sql file and output to this directory");
	
	public SqlGeneratorPanel(final PAISDBHelper db)
	{
		this.db=db;
		templatesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		outTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		outputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		outputTypeBG.add(comboRB);
		outputTypeBG.add(seprateRB);
		seprateRB.setSelected(true);
		
		tableCB.setSelected(true);
		this.outTypePanel.add(new JLabel(  "  select outputs:"));

		outTypePanel.add(tableCB);
		outTypePanel.add(indexCB);
		outTypePanel.add(keyCB);
		outTypePanel.add(comboRB);
		outTypePanel.add(seprateRB);
		
		
		ResultSet rs;
		
		try {
			rs = db.getPreparedStatement("select templatename from pais.template").executeQuery();
			while(rs.next())
			{
				templateList.add(new JCheckBox(rs.getString(1)));
				System.out.println(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.templatesPanel.add(new JLabel("select templates:"));
		
		for(JCheckBox j:templateList)
		  {
			j.setSelected(true);
			this.templatesPanel.add(j);
		  }

		this.outdirField.setColumns(30);
		this.outputPanel.add(outdirField);
		this.outputPanel.add(outdirButton);
		generateButton.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {
				if(tableCB.isSelected()==false&&keyCB.isSelected()==false&&indexCB.isSelected()==false)
				{
					 JOptionPane.showMessageDialog(null, "please select at least one outputs!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				if(controller.generateCreateSQL(db, outdirField.getText())==false)
				{
					 JOptionPane.showMessageDialog(null, "error occurred when genrate sql!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else{
					TengUtils.openDir(outdirField.getText());
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
		this.outputPanel.add(generateButton);
		this.add(templatesPanel);
		this.add(outTypePanel);
		this.add(outputPanel);
		this.setVisible(true);
		controller = new SqlGeneratorController(this);
	}
	
	public static void  main(String[] args){
		JFrame jf = new JFrame("terry is soooooooooo good");
		jf.setBounds(0, 0, 660, 200);
		jf.setLocationRelativeTo(null);
		PAISDBHelper db = new PAISDBHelper("localhost","50000","teng","terry08161043","tengdb");
		jf.add(new SqlGeneratorPanel(db));
		jf.setVisible(true);
		jf.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        System.exit(0);
		      }
		    });
		}
}
