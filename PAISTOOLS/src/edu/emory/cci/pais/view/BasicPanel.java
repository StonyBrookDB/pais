package edu.emory.cci.pais.view;

import java.awt.Dimension;

import javax.swing.JPanel;


/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class set the shared properties of upload panel and convert panel
 *
 */
public class BasicPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BasicPanel()
	{
		super();
		this.setPreferredSize(new Dimension(600, 220));
	}

}
