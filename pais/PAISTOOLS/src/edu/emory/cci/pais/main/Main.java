package edu.emory.cci.pais.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

import edu.emory.cci.pais.controller.ConvertController;
import edu.emory.cci.pais.dataloader.dataloadingmanager.DataLoadingManager;
import edu.emory.cci.pais.dataloader.documentuploader.DocumentUploader;
import edu.emory.cci.pais.view.MainPanel;

public class Main {
	//the main window
public static void  main(String[] args){
	    if(args.length==0)
	    {
		JFrame jf = new JFrame("PAIS");
		jf.setBounds(0, 0, 660, 600);
		jf.setLocationRelativeTo(null);
		jf.add(new MainPanel());
		jf.setVisible(true);
		jf.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        System.exit(0);
		      }
		    });
       }
       else// command line
       {
    	String work = args[0];
    	String otherargs[] = new String[args.length-1];
    	for(int i=0;i<otherargs.length;i++)
    	{
    		otherargs[i]=args[i+1];
    	}
   		
   		if(work.equalsIgnoreCase("converter"))
   		{
   			 ConvertController.convertermain(otherargs);
   		}
   		else if(work.equalsIgnoreCase("uploader"))
   		{		
	         DocumentUploader.documentUploadermain(otherargs);
   		}
   		else if(work.equalsIgnoreCase("loadmanager"))
   		{
   			DataLoadingManager.loadmanagermain(otherargs);;
   		}
   		else
   		{
   			System.out.println("the first argument must be the work type\n e.g. java -jar paistools.jar loadmanager/converter/uploader <otherargs>");
			System.exit(0);
   		}

       }
}
       
}
