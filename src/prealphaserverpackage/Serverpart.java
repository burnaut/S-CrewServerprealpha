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

public clientsidethreads(Socket clientSocket) {
    this.clientside = clientSocket;
    
    }
  public void run(){
 
	  try {
		ois= new ObjectInputStream(clientside.getInputStream());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		oos = new ObjectOutputStream(clientside.getOutputStream());
	} catch (IOException e3) {
		// TODO Auto-generated catch block
		e3.printStackTrace();
	}
	handlerequest(ois,oos);
	exit();
  }
	//-->Am Ende

 @SuppressWarnings("unchecked")
 public void setOrder(){
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
	
	try {
		ois.close();
		pw.close();
		clientside.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
System.out.println("Bestellung verarbeitet");
 }
 public void getRestaurant(){
	String tempRestaurant=null;	 
	 try {
			 
			fr = new FileReader("Restaurant.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 try {
			fr.read(cbuf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 tempRestaurant=String.valueOf(cbuf);
		 System.out.println(tempRestaurant);
		 try {
			oos.writeObject(tempRestaurant);
			} catch (IOException e) {
			// TODO Auto-generated catch block
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
 public void setRestaurant(){
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
		ois.close();
		pw.close();
		fw.close();
		clientside.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
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
	case "setRestaurant()":
		setRestaurant();
	case "setOrder()":
		setOrder();
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
	