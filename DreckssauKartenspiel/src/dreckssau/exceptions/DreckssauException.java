package dreckssau.exceptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class DreckssauException extends Exception {

	public DreckssauException(String e) {
		super(e);
		System.err.println("Error: "+e);
		try {
			PrintWriter pr =new PrintWriter(new FileOutputStream(new File("log.txt"),true));
			SimpleDateFormat dfDate  = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String datum = dfDate.format(System.currentTimeMillis());
			
			pr.append("Error: "+this.getClass().getName() +" - " + e +" - " +datum);
			pr.append("\n");
			pr.close();
		} catch (Exception ex){
		}
	}
}