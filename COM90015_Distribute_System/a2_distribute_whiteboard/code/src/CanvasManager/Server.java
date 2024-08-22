package CanvasManager;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	int portNumber;
	String IpAddress;
	ServerSocket listeningSocket;
	Socket clientSocket;
	ArrayList<Connection> guests = new ArrayList<Connection>();
	ArrayList<String> guestNames = new ArrayList<String>();

	// constructor
	public Server(int portNumber, String IpAddress) {
		this.portNumber = portNumber;
		this.IpAddress = IpAddress;
	}
	// add a guest
	public void addName(String name) {
		this.guestNames.add(name);
	}
	// get a guest
	public ArrayList<String> getNames() {
		return this.guestNames;
	}

	
	public void lanuch() {
		try {
			// create a thread pool to manage the connection
			ExecutorService pool = Executors.newFixedThreadPool(10);
			listeningSocket = new ServerSocket(portNumber);
			while (true) {
				// received a new connection
				clientSocket = listeningSocket.accept();
				// initialize a connection
				Connection connection = new Connection(clientSocket);
				System.out.println("new client connection");
				// add this connection
				guests.add(connection);
				pool.execute(connection);
			}
		} catch (Exception e) {
			System.out.println("Manager Rebooted.");
			System.exit(1);
		} finally {
//			System.out.println("client leave");
			if(listeningSocket != null) {
				try {
					listeningSocket.close();
				} catch (Exception e) {
					System.out.println("Unable to close the Canvas Manager.");
				}
			}
		}
	}
	
}
