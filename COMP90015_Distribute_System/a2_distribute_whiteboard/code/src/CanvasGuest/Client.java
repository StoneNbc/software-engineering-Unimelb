package CanvasGuest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

// Client server to connect with manager
public class Client {

	String IpAddress;
	int port;
	String username;
	Socket socket;
	static DataInputStream input;
	static DataOutputStream output;
	static Connection connection;
	// constructor
	public Client(int port, String username) {
		this.port = port;
		this.username = username;
	}

	public void run() {
		try {
			// connect to manager
			socket = new Socket(IpAddress, port);
			// initialize input and output
			input = new DataInputStream(socket.getInputStream());
		    output = new DataOutputStream(socket.getOutputStream());
			// use connection class to separate the communication and control
		    connection = new Connection(socket,input,output);
		    connection.run();
			
		} catch (UnknownHostException e) {
			System.out.println("Canvas not found");
		} catch (IOException e) {
			System.out.println("Canvas closed");
		} finally {
			try {
				socket.close();
				input.close();
				output.close();
			} catch (IOException | NullPointerException e) {
				System.out.println("Canvas closed " + e.getMessage());
			}
		}
		
	}
	
}
