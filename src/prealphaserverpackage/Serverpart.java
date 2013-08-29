package prealphaserverpackage;
/**
 * 
 */


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @author Sebastian
 *
 */
public class Serverpart {

	/**
	 * 
	 */
	
	
	@SuppressWarnings("unchecked")
	public void neuerserversocket(int port){
		ServerSocket serversocket= null;
		Socket clientside = null;
		FileWriter fw = null;
		PrintWriter pw=null;
		FileReader fr=null;
		BufferedReader br=null;
		InputStream i=null;
		OutputStream o=null;
		ObjectInputStream ois=null;
		
		
		char[] cbuf = new char [100];
		HashMap<String,Number> anzahluser = null;
		HashMap<String,String> behelfsmap=null;
		String UserID = null;
		String Restaurant=null;
		try {
			serversocket = new ServerSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			clientside = serversocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	//Ende try catch
	
	try {
		i= clientside.getInputStream();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//Ende try catch
	try {
	     o=clientside.getOutputStream();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//Ende try catch
	
	try {
		ois= new ObjectInputStream(i);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//Ende try catch
	try {
		UserID=(String) ois.readObject();//get Users ID
		behelfsmap=(HashMap<String, String>)ois.readObject();
	} catch (ClassNotFoundException | IOException e3) {
		// TODO Auto-generated catch block
		e3.printStackTrace();
	}
//	try {
//		anzahluser=(HashMap<String, Number>) ois.readObject();
//	} catch (ClassNotFoundException | IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	System.out.println(anzahluser);
	System.out.println("Behelfsmap:"+behelfsmap);
//	switch (behelfsmap.getKey())
//	{
//	case "Sebastian":
//		fw= new FileWriter("Sebastian_Bestellung.txt");
//		pw = new PrintWriter(fw);
//		pw.write(behelfsmap.get("Sebastian"));
//		pw.flush();
//	case "Mozez":
//		fw= new FileWriter("Moritz_Thelenberg_Bestellung.txt");
//		pw = new PrintWriter(fw);
//		pw.write(behelfsmap.get("Mozez"));
//		pw.flush();
//	case "David":
//		fw= new FileWriter("David_Bestellung.txt");
//		pw = new PrintWriter(fw);
//		pw.write(behelfsmap.get("David"));
//		pw.flush();
//	case "Moritz":
//	    fw= new FileWriter("Moritz_Müller_Bestellung.txt");
//	    pw = new PrintWriter(fw);
//	    pw.write(behelfsmap.get("Moritz"));
//	    pw.flush();
//	case "Oliver":
//		fw= new FileWriter("Oliver_Bestellung.txt");
//		pw = new PrintWriter(fw);
//		pw.write(behelfsmap.get("Oliver"));
//		pw.flush();
//	case "Gabriel":
//		fw= new FileWriter("Gabriel_Bestellung.txt");
//		pw = new PrintWriter(fw);
//		pw.write(behelfsmap.get("Gabriel"));
//		pw.flush();
//	case "Jörg":
//		fw= new FileWriter("Jörg_Bestellung.txt");
//		pw = new PrintWriter(fw);
//		pw.write(behelfsmap.get("Jörg"));
//		pw.flush();
//	case "Lukas":
//		fw= new FileWriter("Lukas_Bestellung.txt");
//		pw = new PrintWriter(fw);
//		pw.write(behelfsmap.get("Lukas"));
//		pw.flush();
//	case"Andreas":
//		fw= new FileWriter("Andreas_Bestellung.txt");
//		pw = new PrintWriter(fw);
//		pw.write(behelfsmap.get("Andreas"));
//		pw.flush();
//	}
	try {
		fw = new FileWriter (UserID+"s_Bestellung.txt");
	} catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	pw = new PrintWriter(fw);
	pw.write(behelfsmap.get(UserID));
	pw.flush();
	try {
		fr = new FileReader (UserID+"s_Bestellung.txt");
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	try {
		clientside.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try{
		
		serversocket.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	}
	
		

	public void bildereinspeichern(){
		BufferedImage img = null;
		File f =new File("C:/Users/Sebastian/Desktop/andr.jpg");
		try {
			img= ImageIO.read(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(img);
		
		
	}
	
}
