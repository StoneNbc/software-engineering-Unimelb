package CanvasGuest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class Connection extends Thread{

	protected String status = "Waiting";
	protected static Socket socket;
	protected ArrayList<String> userList = new ArrayList<String>();
	protected JSONParser parser = new JSONParser();
	protected DataInputStream input;
	protected DataOutputStream output;

	// constructor
	public Connection(Socket socket, DataInputStream input, DataOutputStream output) {
		Connection.socket = socket;
		this.input = input;
		this.output = output;
	}

	public void run() {
		while(true) {
			JSONObject response = null;
			try {
				// received the josn data from manager
				response = (JSONObject) parser.parse(input.readUTF());
				response.toJSONString();
				System.out.println("received" + response.toJSONString());
			} catch (ParseException | IOException e) {
				JOptionPane.showMessageDialog(CanvasGuest.guestLogin.frame, "The Manager is closed.");
				System.exit(1);
				break;
			}
			// get the resType
			String resType = (String) response.get("resType");
			switch(resType) {
				// execute the draw method according to the resType
				case "Paint":
					JSONObject content = (JSONObject) response.get("resBody");
					if(GuestBoard.canvas !=null) {
						Canvas canvas = GuestBoard.canvas;
						canvas.addContent(content);
						canvas.repaint();
					}
					break;
					// Clear the board
				case "Clear":
					Canvas canvas = GuestBoard.canvas;
					canvas.removeAll();
					canvas.updateUI();
					canvas.clearAll();
					break;
					// Sync the userlist to board
				case "Sync":
					userList = (ArrayList<String>) response.get("resBody");
					if (GuestLogin.guestBoard !=null) {
						GuestLogin.guestBoard.userList.setListData(userList.toArray());
					}
					break;
					// the connection feedback
				case "Connect":
					status = (String) response.get("resBody");
					break;
					// exit when being kicked
				case "Kick":
					JOptionPane.showMessageDialog(GuestLogin.guestBoard.frame, "You have been kicked out.");
					try {
						Connection.socket.close();
					} catch (IOException e) {
						System.out.println("Unable to close socket");
					}
					System.exit(1);
					break;
					// kick some user
				case "Remove":
					String removed_user = (String) response.get("resBody");
					userList.remove(removed_user);
					GuestLogin.guestBoard.userList.setListData(userList.toArray());
					break;
					// received the chat message
				case "Chat":
					String msg = (String) response.get("resBody");
					String who = (String) response.get("Sender");
					GuestLogin.guestBoard.addText(msg, who);
					break;
			}
		}
	}
	// reture the status
	public String isPermitted() {
		return this.status;
	}
	// reset status
	public void reset() {
		this.status = "Waiting";
	} 
}
