package CanvasManager;

public class CanvasManager {

	static String IpAddress;
	static int port;
	static String username;
	static ManagerLogin managerLogin;
	static Server server;
	
	public static void main(String[] args) {
		
		// Checking command input, if there's no command input use the default option.
		if(args.length >= 3) {
			try {
				// set ip address, port, and username from args
				IpAddress = args[0];
				port = Integer.parseInt(args[1]);
				username = args[2];
			} catch (Exception e) {
				System.out.println("Wrong arguments, exit.");
				System.exit(1);
			}
		} else {
			// set default ip, port, and username
			IpAddress = "localhost";
			port = 8888;
			username = "Manager";
			System.out.println("Default lunch.");
		}
		
		// Initialize a Login GUI for Manager to login.
		try {
			managerLogin = new ManagerLogin(username);
		} catch (Exception e) {
			System.out.println("Error Login initialisation " + e.getMessage());
		}
		// Launch the server waiting for guest connections.
		server = new Server(port, IpAddress);
		server.lanuch();
	}
}
