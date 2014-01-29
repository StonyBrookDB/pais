package edu.emory.cci.pais.view;

import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * @author dejun teng, Center for Comprehensive Informatics, Emory University
 * @version 1.0
 * This class realize setting the system output to the console
 *
 */
public class MyPrintStream extends PrintStream {  
	 
private JTextComponent text;
private StringBuffer sb = new StringBuffer();
   
   public MyPrintStream(OutputStream out, JTextComponent text) {  
        super(out);  
        this.text = text;  
   }
 
    public void write(byte[] buf, int off, int len) {  
         final String message = new String(buf, off, len);   
         SwingUtilities.invokeLater(new Runnable(){
         public void run(){
          sb.append(message);
          text.setText(sb.toString());
         }
      });
   }
}
