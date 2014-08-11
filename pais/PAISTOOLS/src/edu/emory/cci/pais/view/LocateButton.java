package edu.emory.cci.pais.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class realize a personalized button which can be used to locate file or directory and output the path into one specified JTextField
 *
 */

public class LocateButton extends MyButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LocateButton(String name,String tag,final JTextField t,final int fctype,final JComponent parent)
	{
		super(name,tag);
		this.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {
				locatePath(t,fctype,parent);
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
	//locate the file path and output to specified field
	public static String locatePath(JTextField field,int fctype,JComponent parent)
	{
		String path = "";
		JFileChooser fc = new JFileChooser(); 
		fc.setFileSelectionMode(fctype); 
        int intRetVal = fc.showOpenDialog(parent); 
        if( intRetVal == JFileChooser.APPROVE_OPTION)
        {
          path = fc.getSelectedFile().getPath();
        }
        field.setText(path);
        return path;
	}

}
