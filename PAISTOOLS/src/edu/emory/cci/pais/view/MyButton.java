package edu.emory.cci.pais.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class realize one kind of button, when mouse enter, it show tag of what this button can do
 *
 */
public class MyButton extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String tag;
	
	public MyButton(String buttonname,String tag)
	{
		super(buttonname);
		this.tag=tag;
		this.addMouseListener(new MouseListener(){
			@Override
			public void mouseEntered(MouseEvent arg0) {
				settag();
			}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
	}
	public void settag()
	{
		this.setToolTipText(tag);
	}
}