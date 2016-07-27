package server;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThreads implements Runnable{
 
	protected Socket socket;
	protected ServerGUI serverGUI;
	
	public ServerThreads(Socket clientSocket, ServerGUI serverGUI){
		this.socket = clientSocket;
		this.serverGUI = serverGUI;
	}
	
	public void run(){
		   OutputStream output; // output stream to client
		   InputStream input; // input stream from client
		   BufferedReader buffRead;
		   System.out.println("Setting up I/O Streams");
		   try{
			   input = socket.getInputStream();
			   buffRead = new BufferedReader(new InputStreamReader(input));
			   output = socket.getOutputStream();
			   long time = System.currentTimeMillis();
			   output.write(("HTTP/1.1 200 OK\n\nServerThreads: " + time).getBytes());
			   output.close();
			   input.close();
			   serverGUI.displayArea.append("\nInet: " + socket.getInetAddress().toString());
//			   serverGUI.displayArea.append("\nLocalAddress: " + socket.getLocalAddress().toString());
//			   serverGUI.displayArea.append("\nPort: " + socket.getPort());
//			   serverGUI.displayArea.append("\nLocalSocketAddress: " + socket.getLocalSocketAddress().toString());
			   serverGUI.displayArea.append("\nLocalPort: " + socket.getLocalPort());

			   System.out.println("Request processed: " + time);
			   
		   }catch(IOException e){
			   System.out.println("IO Exception during socket initialization");
			   e.printStackTrace();;
		   }
	}
	
}
