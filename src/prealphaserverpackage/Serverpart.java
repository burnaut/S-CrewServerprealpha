package prealphaserverpackage;
/**
 * 
 */


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import javax.imageio.ImageIO;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sebastian
 *
 */
public class Serverpart {

public static	ServerSocket serversocket= null;
public static	Socket clientside = null;
	
		
	/**
	 * 
	 */
	//how about a switch case scenario string with name of the method sent by a client gets procces further here :) like the simon protocol
	
	
	public void runanew(int port){
		
		try {
			serversocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true){
			try {
				clientside=serversocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	               
	   ( new clientsidethreads(clientside)).start();
		}
	}
	
}


class clientsidethreads extends Thread{
private	Socket clientside = null;
FileWriter fw = null;
PrintWriter pw=null;
FileReader fr=null;
BufferedReader br=null;
InputStream i=null;
OutputStream o=null;
ObjectInputStream ois=null;
ObjectOutputStream oos=null;
ReentrantLock lock=new ReentrantLock();


char[] cbuf = new char [100];
HashMap<String,Number> anzahluser = null;
HashMap<String,String> behelfsmap=null;
HashMap<String,String> userbestellung=null;
String UserID = null;
String Restaurant;
String request=null;
String AdminID;
String AdminPw;
File f;
FilenameFilter fnf;
public clientsidethreads(Socket clientSocket) {
    this.clientside = clientSocket;
    
    }
  public void run(){
  try {
	Thread.sleep(300);
} catch (InterruptedException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
	  try {
		ois= new ObjectInputStream(clientside.getInputStream());
		oos = new ObjectOutputStream(clientside.getOutputStream());{
			handlerequest(ois,oos);
		}
	
	  } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
		
	
//	exit();
  }
	

 @SuppressWarnings("unchecked")
 public void setOrder() {
	 System.out.println("Bestellung kommt an");
	 try {
		UserID=(String) ois.readObject();
		userbestellung= (HashMap<String, String>) ois.readObject();
	} catch (ClassNotFoundException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 try {
		fw= new FileWriter(UserID+"s_Bestellung.txt");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	pw= new PrintWriter(fw);
	pw.write(userbestellung.get(UserID));
	
	pw.close();
	try {
		fw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
System.out.println("Bestellung verarbeitet");
 }
 public void getRestaurant() {
	String tempRestaurant=null;
	try( BufferedReader fr =
	           new BufferedReader( new FileReader( "Restaurant.txt" ))) {
	      tempRestaurant = fr.readLine();
	      System.out.println( tempRestaurant );
		    
	    oos.writeObject(tempRestaurant);
	} catch (IOException ex) {
	    ex.printStackTrace();
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
 public void setRestaurant() {
    try {
		Restaurant=(String) ois.readObject();
	} catch (ClassNotFoundException | IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    try {
		lock.lock();
    	fw= new FileWriter("Restaurant.txt");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	pw= new PrintWriter(fw);
	pw.write(Restaurant);
	pw.flush();
	lock.unlock();
	try {
		pw.close();
		fw.close();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
 }
 public void getGesamt(){
	 f=new File("C:/User/Sebastian/Desktop");
	 f.listFiles(fnf);//fnf createn und suchalgorythmus anpassen :)
 }
 public void handlerequest(ObjectInputStream ois,ObjectOutputStream oos){
	 
	 try {
		request=(String) ois.readObject();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//ab hier dann switch case :)
	switch (request)
	{
	case "getRestaurant()":
		getRestaurant();
		break;
	case "setRestaurant()":
		setRestaurant();
		break;
	case "setOrder()":
		setOrder();
		break;
	case "log_in()":
		log_in();
		break;
	case "getGesamt()":
		getGesamt();
		break;
	}
	
	
 }
 public void log_in(){
	 System.out.println("Log in try");
	 try {
		AdminID=(String) ois.readObject();
		System.out.println("read the id");
		AdminPw=(String) ois.readObject();
		System.out.println("read the pw");
		if(AdminID.equals("Mozez")){
		oos.writeObject("Access Granted");
		System.out.println("Log in succesful");
		}
	} catch (ClassNotFoundException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
 }
 public void exit(){
	try {

		ois.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    try {
		oos.close();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    try {
		clientside.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
    
}
}
	