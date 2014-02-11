package edu.emory.cci.pais.view;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class define a work area, there is a label on the top as the name, and one working area as a output or input
 *
 */
public class Console extends JPanel{
	
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JPanel labelPanel = new JPanel();
    JTextArea textArea = new JTextArea();
    JScrollPane consoleContainer;
    //define the label on the top, the rows and columns of text area, and if it is editable. 
    public Console(String name,int rows, int columns,boolean editable)
     {
    	 
    	 this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	 labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    	 labelPanel.add(new JLabel(name));
    	 textArea.setEditable(false);
    	
    	 textArea.setBackground(Color.white);
    	 textArea.setLineWrap(true);
    	 textArea.setRows(rows);
    	 textArea.setColumns(columns);
    	 textArea.setEditable(editable);
    	 
    	 consoleContainer = new JScrollPane(textArea);
    	 consoleContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    	 consoleContainer.setBorder(new LineBorder(Color.black));
    	 
    	 
    	 this.add(labelPanel);
    	 this.add(consoleContainer);
     }
   
	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

}
