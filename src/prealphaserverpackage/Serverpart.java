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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sebastian
 *
 */
public class Serverpart {

public static	ServerSocket serversocket= null;
public static	Socket clientside = null;


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
				System.out.println("accepting new client");
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
//FileReader fr=null;
BufferedReader fr=null;
InputStream i=null;
OutputStream o=null;
ObjectInputStream ois=null;
ObjectOutputStream oos=null;
ReentrantLock lock=new ReentrantLock();


char[] cbuf = new char [10000];
HashMap<String,Number> anzahluser = null;
HashMap<String,String> behelfsmap=null;
HashMap<String,String> userbestellung=null;
String UserID = null;
String Restaurant;
int request=0;
String AdminID;
String AdminPw;
File f;
String[] bestellungenliste;
String reset=null;
String Forum_msg;



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

  

public void sendForum_msg(){
	String tempMsg=null;
	try {BufferedReader fr =
	           new BufferedReader( new FileReader( "Forum.txt" ));
		
		Forum_msg=(String) ois.readObject();
		tempMsg=fr.readLine();
		fw=new FileWriter("Forum.txt");
		pw=new PrintWriter(fw);
		pw.write(tempMsg+Forum_msg);
		pw.flush();
		pw.close();
		fw.close();
		fr.close();
	           
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
}
  @SuppressWarnings("unchecked") 
 public void setOrder() {
	
	 try {
		UserID=(String) ois.readObject();
		userbestellung= (HashMap<String, String>) ois.readObject();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 catch (IOException e) {
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
	pw.write(UserID+": "+userbestellung.get(UserID));
	
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
	 System.out.println("Reading Restaurant.txt");
	try { BufferedReader fr =
	           new BufferedReader( new FileReader( "Restaurant.txt" ));
	      tempRestaurant = fr.readLine();
	      System.out.println( tempRestaurant );
	      System.out.println("writing tempRestaurant is the next Step");
	    oos.writeObject(tempRestaurant);
	    oos.flush();
	    System.out.println("tempRestaurant has been written");
	    oos.close();
	    fr.close();
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
	} catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
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
	
	 f=new File("/var/www");// TODO pfad noch anpassen!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 File[] filelistarray=f.listFiles(new FilenameFilter(){
		 public boolean accept(File f,String s){
				return s.toLowerCase().endsWith("s_bestellung.txt");
				} 
	 });
	 //fnf createn und suchalgorythmus anpassen :)	 
	 
	 String tempbestellung;
	 String tempvorherigedat;
	 String gesamtbest;
	 StringBuffer tempstringbuf = new StringBuffer();
	 for(int x=0; x<filelistarray.length;x++){
//		 System.out.println(x+": "+filelistarray[x]);
		 try{
			 fr=new BufferedReader( new FileReader( filelistarray[x]));
			 tempbestellung=fr.readLine();
			 fr= new BufferedReader(new FileReader("GesamtBestellung.txt"));
			 tempvorherigedat=fr.readLine();
			 fw= new FileWriter("GesamtBestellung.txt");
			 pw=new PrintWriter(fw);
			 pw.write(tempbestellung+"-"+tempvorherigedat);
			 pw.flush();
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			fr.close();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	
	 }//for ende
	 	//reset der gesamtbestellung
	 // vl alte bestellungen abspeichern und zeugreifbar machen möglicherweise in einem update...
	 try {
		fr=new BufferedReader(new FileReader("GesamtBestellung.txt"));
		fr.read(cbuf);
		 tempstringbuf.append(cbuf);
		 gesamtbest=tempstringbuf.toString();
		// System.out.println(gesamtbest);
			oos.writeObject(gesamtbest);
			oos.flush();
			GregorianCalendar c=new GregorianCalendar();//um einen timestamp zu erzeugen
			int day=c.get(GregorianCalendar.DAY_OF_MONTH);
			int month=c.get(GregorianCalendar.MONTH);
			String daystr= String.valueOf(day);
			String monthstr=String.valueOf(month);
			fw=new FileWriter(daystr+"_"+monthstr+"_Bestellung.txt");//systemdate+bestellung
			pw=new PrintWriter(fw);
			pw.write(gesamtbest);
			pw.flush();
			fw=new FileWriter("GesamtBestellung.txt");
			pw=new PrintWriter(fw);
			pw.write(reset);
			pw.flush();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
 }
 public void handlerequest(ObjectInputStream ois,ObjectOutputStream oos){
	 
	 try {
		 System.out.println("reading request...");
		request=(int) ois.readInt();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//problem mit switch case bei den beiden gettern sie kommt an wird aber nicht ausgeführt :((
	 switch (request){
	 
	 case 1:
		 getRestaurant();
		 break;
	 case 2:
		 setRestaurant();
		 break;
	 case 3:
	     setOrder();
	     break;
	 case 4:
		 log_in();
		 break;
	 case 5:
		 getGesamt();
		 break;
	 }
	
		
	
	
 }
 public void log_in(){
	 
	 try {
		AdminID=(String) ois.readObject();
		System.out.println("read the id");
		AdminPw=(String) ois.readObject();
		System.out.println("read the pw");
		if(AdminID.equals("Mozez")){
			if(AdminPw.equals("ghghgh")){
		oos.writeObject("Access Granted");
		oos.flush();
		System.out.println("Log in succesful");
		}
		}
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 catch (IOException e) {
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