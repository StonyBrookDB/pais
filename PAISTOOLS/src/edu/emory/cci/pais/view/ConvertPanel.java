package edu.emory.cci.pais.view;

import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.emory.cci.pais.controller.ConvertController;
import edu.emory.cci.pais.util.TengUtils;

/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class realize the convert panel
 *
 */

public class ConvertPanel extends BasicPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ConvertController controller = new ConvertController();
	
	String inputPath = "";
	String outputPath = "";
    
    String[] inputtypes = {"Aperio XML File","unfixed points","fixed points"};
    String[] outputtypes = {"zipped PAIS","unzipped PAIS"};
    Choice inputType = new Choice();
    Choice outputType = new Choice();
    
    JPanel inputPanel = new JPanel();
    JPanel outputPanel = new JPanel();
    JPanel configPanel = new JPanel();
    JPanel startPanel = new JPanel();
    
	JLabel inputPathLabel = new JLabel("         input path:");
	JLabel outputPathLabel = new JLabel("       output path:");
	JLabel configPathLabel = new JLabel("config file path:");
	MainPanel mainPanel;
	

	JTextField inputPathField = new JTextField("");
	JTextField outputPathField = new JTextField("");
	JTextField configPathField = new JTextField("");
	
	LocateButton inputPathButton = new LocateButton("locate","locate the file path of the input file or directory",inputPathField,JFileChooser.FILES_AND_DIRECTORIES,this);
	LocateButton outputPathButton = new LocateButton("locate","locate the file path of the output directory",outputPathField,JFileChooser.DIRECTORIES_ONLY,this);
	LocateButton configPathButton = new LocateButton("locate","choose the configure file",configPathField,JFileChooser.FILES_ONLY,this);
	MyButton startButton = new MyButton("start","start convert the files");
	
	
	
	public ConvertPanel(MainPanel mainPanel)
	{
		super();
		this.mainPanel = mainPanel;
		//initiate input and output file types
		for(int i=0; i < inputtypes.length; i++){
			inputType.addItem(inputtypes[i]);
		}
		for(int i=0; i < outputtypes.length; i++){
			outputType.addItem(outputtypes[i]);
		}
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		outputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		configPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		startPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		inputPathField.setColumns(30);
		outputPathField.setColumns(30);
		configPathField.setColumns(30);
		
		inputPanel.add(inputPathLabel);
		inputPanel.add(inputPathField);
		inputPanel.add(inputPathButton);
		inputPanel.add(inputType);
		
		
		outputPanel.add(outputPathLabel);
		outputPanel.add(outputPathField);
		outputPanel.add(outputPathButton);
		outputPanel.add(outputType);
		
		configPanel.add(configPathLabel);
		configPanel.add(configPathField);
		configPanel.add(configPathButton);
		
		startButton.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {
				start();
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
		this.add(inputPanel);
		this.add(outputPanel);
		this.add(configPanel);	
		this.add(new JLabel("\n"));
		this.add(new JLabel("\n"));
		this.add(new JLabel("\n"));
        startPanel.add(startButton);
		this.add(startPanel);	
	}
	
	//start convert selected files
	public void start()
	{
		//if input and output path is empty
		if(inputPathField.getText().equalsIgnoreCase("")||configPathField.getText().equalsIgnoreCase("")||outputPathField.getText().equalsIgnoreCase(""))
		{
			JOptionPane.showMessageDialog(null, "please type in or choose the input path!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//start convert file
		try{
		System.out.println("start convert files....");
	    controller.convert(inputType.getSelectedItem(),outputType.getSelectedItem(),inputPathField.getText(),outputPathField.getText(),configPathField.getText());   
		System.out.println("convert successfully!");
	    //after converting, ask if continue upload the converted files
	    if(JOptionPane.showConfirmDialog(this, "continue upload the output into database?", "confirm upload", JOptionPane.OK_CANCEL_OPTION)==0)
	      {
		   this.mainPanel.getTab().setSelectedIndex(1);
		   this.mainPanel.getUploadPanel().getInputfileField().setText(outputPathField.getText());
	      }
	    else//if not continue, open the directory of the converted files
	    	TengUtils.openDir(outputPathField.getText());
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "convert failed!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
