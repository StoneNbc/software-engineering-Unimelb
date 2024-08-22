package CanvasGuest;

public class CanvasGuest {
	
	static int port;
	static String username;
	static String IpAddress;
	protected static GuestLogin guestLogin;
	protected static Client client;
	
	public static void main(String[] args) {
		
		// Checking command input, if there's no command input use the default option.
		if(args.length >= 3) {
			try {
				// set ip address, port, and username
				IpAddress = args[0];
				port = Integer.parseInt(args[1]);
				username = args[2];
			} catch (Exception e) {
				System.out.println("Wrong arguments, exit now.");
				System.exit(1);
			}
		} else {
			// set default ip, port, and username
			IpAddress = "localhost";
			port = 8888;
			username = "user";
			System.out.println("Default run.");
		}
		
		// Initialize a Login GUI for Guest to login.
		try {
			guestLogin = new GuestLogin(username);
		} catch (Exception e) {
			System.out.println("Error Login initialisation " + e.getMessage());
		}	
		// start connect to manager
		client = new Client(port, username);
		client.run();
	}
	
	
}
