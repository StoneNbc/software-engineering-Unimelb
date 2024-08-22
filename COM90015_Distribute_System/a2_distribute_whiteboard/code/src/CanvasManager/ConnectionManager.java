package CanvasManager;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
// separate the broadcast operation
public class ConnectionManager {

	// guest checkin
	public static synchronized int checkin(String name) {
        return ManagerLogin.managerBoard.connectRequest(name);
	}
	@SuppressWarnings("unchecked")
	// update the guest list
	public static synchronized void updateGuestList(ArrayList<String> guests) {
		ManagerLogin.managerBoard.userList.setListData(guests.toArray());
	}


	// Send message to all guests
	public static synchronized void broadcastMsg(JSONObject msg) {
		for (Connection cnt : CanvasManager.server.guests) {

			if (cnt.socket.isConnected()) {
				if (msg.get("resType").equals("Chat")) {
					if ( cnt.username != null && !cnt.username.equals(msg.get("Sender"))){
						try {
							cnt.output.writeUTF(msg.toJSONString());
							cnt.output.flush();
							System.out.println("send" + msg);
						} catch (IOException e) {
//							System.out.println(e);
//							System.out.println("Error broadcast");
						}
					}
				} else {
					try {
						cnt.output.writeUTF(msg.toJSONString());
						cnt.output.flush();
						System.out.println("send " + msg);
					} catch (IOException e) {
//						System.out.println(e);
						System.out.println("Error broadcast " + e.getMessage());
					}
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	// broadcast all history to new guest
	public static synchronized void broadcastDraw(ArrayList<JSONObject> contents) {
		for (JSONObject content : contents) {
			for (Connection cnt : CanvasManager.server.guests) {
				if (cnt.socket.isConnected()) {
					try {
						JSONObject res = new JSONObject();
						res.put("resType", "Paint");
						res.put("resBody", content);
						cnt.output.writeUTF(res.toJSONString());
						cnt.output.flush();
						System.out.println("send" + res);
					} catch (IOException e) {
						System.out.println("Error broadcast " + e.getMessage());
					}
				}
			}
		}
	}

	// broad a operatin to all members
	public static synchronized void broadcast(JSONObject contents) {

		for (Connection cnt : CanvasManager.server.guests) {
			if (cnt.socket.isConnected()) {
				try {
					JSONObject res = new JSONObject();
					res.put("resType", "Paint");
					res.put("resBody", contents);
					cnt.output.writeUTF(res.toJSONString());
					cnt.output.flush();
					System.out.println("send" + res);
				} catch (IOException e) {
					System.out.println("Error broadcast " + e.getMessage());
				}
			}
		}

	}


}
