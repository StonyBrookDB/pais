package edu.emory.cci.pais.view;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class realizes the main panel, contains the convert panel, upload panel and one console
 *
 */

public class MainPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JTabbedPane tab = new JTabbedPane();
	ConvertPanel convertPanel;
	UploadPanel uploadPanel;
	SqlGeneratorPanel sqlPanel;
	Console console = new Console("console:",15,55,false);
	
	public UploadPanel getUploadPanel() {
		return uploadPanel;
	}

	public void setUploadPanel(UploadPanel uploadPanel) {
		this.uploadPanel = uploadPanel;
	}

	public JTabbedPane getTab() {
		return tab;
	}

	public void setTab(JTabbedPane tab) {
		this.tab = tab;
	}

	public Console getConsole() {
		return console;
	}

	public void setConsole(Console console) {
		this.console = console;
	}

	public MainPanel()
	{
		
		convertPanel = new ConvertPanel(this);
		uploadPanel = new UploadPanel(this);
		tab.addTab("converter", convertPanel);
		tab.addTab("uploader", uploadPanel);
		tab.addTab("SqlGenerator", sqlPanel);
		tab.setSelectedIndex(1);
		tab.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(tab.getSelectedIndex()==2)
				  selectSqlTab();
			}
		});
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(tab);
		this.add(console);
		this.setVisible(true);

		//set the system output to console
        MyPrintStream mps = new MyPrintStream(System.out, console.getTextArea());  
		System.setOut(mps);  
		System.setErr(mps);
		//refresh console in time
		//Thread tr = new Thread(new RefreshConsol(console));
		//tr.start();
	}
	
	public void selectSqlTab()
	{
		if(sqlPanel==null)
		{
		PAISDBHelper db = uploadPanel.getConnect();
		if(db==null)
		{
			JOptionPane.showMessageDialog(null, "database connection failed, please insert a valid connection config!", "Error", JOptionPane.ERROR_MESSAGE);
			tab.setSelectedComponent(uploadPanel);
			return;
		}
		else
		{
			tab.remove(sqlPanel);
			sqlPanel = new SqlGeneratorPanel(db);
			tab.add("SqlGenerator",sqlPanel);
			tab.setSelectedComponent(sqlPanel);
		}
		}
		
		
		
	}

}
