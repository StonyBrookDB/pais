package edu.emory.cci.pais.view;

public class RefreshConsol implements Runnable{

	Console console;
	
	public RefreshConsol(Console console)
	{
		this.console = console;
	}
	@Override
	public void run() {

        while(true)
        {
        	
        	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	console.getTextArea().revalidate();
        }
		
	}

}
