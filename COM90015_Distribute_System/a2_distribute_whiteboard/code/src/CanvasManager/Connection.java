package CanvasManager;

import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JOptionPane;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


public class Connection extends Thread {

	protected String username;
	protected Socket socket = null;
	protected JSONParser parser = new JSONParser();
	protected Server server = CanvasManager.server;
	protected DataInputStream input;
	protected DataOutputStream output;

    // constructor
    public Connection(Socket socket) {
        this.socket = socket;
        try {
            // The JSON Parser
            parser = new JSONParser();
            // Input stream
            input = new DataInputStream(socket.getInputStream());
            // Output Stream
            output = new DataOutputStream(socket.getOutputStream());

        } catch(SocketException e) {
            System.out.println("Illegal socket!");
        } catch (IOException e) {
            System.out.println("Error creating buffers");
        }
    }
	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void run() {
		try {
			while (true) {
				JSONObject request = null;
				try {
                    // received the josn data from manager
					request = (JSONObject) parser.parse(input.readUTF());
					System.out.println("received " + request.toJSONString());
				} catch (ParseException | IOException e) {
					JSONObject res = new JSONObject();
					res.put("resType", "Remove");
					res.put("resBody", username);
					ConnectionManager.broadcastMsg(res);
					server.guests.remove(this);
					server.guestNames.remove(username);
					ConnectionManager.updateGuestList(server.getNames());
                    socket.close();
					break;
				}

                // get the resType
                String reqType = (String) request.get("reqType");
                // execute the draw method according to the resType
                switch(reqType) {
                    // build a connection with guest
                    case "Connect":
                        username = (String) request.get("username");
                        // if the new guest's name has already in board
                        if (server.guestNames.contains(username)) {
                            JSONObject res = new JSONObject();
                            res.put("resType", "Connect");
                            res.put("resBody", "Repeat");
                            output.writeUTF(res.toJSONString());
                            output.flush();
                            System.out.println("send " + res.toJSONString());
                            username = "R";
                        } else {
                            Integer confirmation = ConnectionManager.checkin(username);
                            // if the manager clicked yes
                            if (confirmation == 0) {
                                if (socket.isConnected()) {
                                    JSONObject res = new JSONObject();
                                    res.put("resType", "Connect");
                                    res.put("resBody", "Accept");
                                    output.writeUTF(res.toJSONString());
                                    output.flush();
                                    System.out.println("send " + res.toJSONString());
                                    server.addName(username);
                                    ConnectionManager.updateGuestList(server.getNames());
                                }else {
                                    System.out.println("socket disconnected");
                                }
                            } else {
                                // manager clicked no
                                JSONObject res = new JSONObject();
                                res.put("resType", "Connect");
                                res.put("resBody", "Rejected");
                                output.writeUTF(res.toJSONString());
                                output.flush();
                                System.out.println("send " + res.toJSONString());
                                server.guests.remove(this);
                                server.guestNames.remove(username);
                                ConnectionManager.updateGuestList(server.getNames());
                                username = "R";
                            }
                        }
                        break;
                        // send synchronized history to new guest
                    case "Sync":
                        ArrayList<JSONObject> contents = ManagerBoard.canvas.getContents();
                        ArrayList<JSONObject> chatHistory = ManagerBoard.getChat();
                        ArrayList<String> usernames = CanvasManager.server.guestNames;
                        JSONObject res = new JSONObject();
                        res.put("resType", "Sync");
                        res.put("resBody", usernames);
                        ConnectionManager.broadcastDraw(contents);
                        ConnectionManager.broadcastMsg(res);
                        for (JSONObject chat : chatHistory) {
                            output.writeUTF(chat.toJSONString());
                            output.flush();
                        }

                        break;
                        // broadcast the draw action to all menbers
                    case "Paint":
                        JSONObject content = (JSONObject) request.get("reqBody");
                        ArrayList<JSONObject> new_contents = ManagerBoard.canvas.getContents();
                        new_contents.add(content);
                        ConnectionManager.broadcast(content);
                        ManagerBoard.canvas.repaint();
                        break;
                        // send chat message to all members
                    case "Chat":
                        String msg = (String) request.get("reqBody");
                        String who = (String) request.get("Sender");
                        ManagerLogin.managerBoard.addText(msg, who);
                        JSONObject res2 = new JSONObject();
                        res2.put("resType", "Chat");
                        res2.put("resBody", msg);
                        res2.put("Sender", who);
                        output.writeUTF(res2.toJSONString());
                        output.flush();
                        ManagerBoard.addChat(res2);
                        System.out.println("send " + res2.toJSONString());
                        ConnectionManager.broadcastMsg(res2);
                        break;
                }
            }
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(CanvasManager.managerLogin.managerBoard.frame, "Time out ");
		} catch (IOException e) {
			System.out.println("Json IO Exception. " + e.getMessage());
		}finally {
            try {
                input.close();
				output.close();
				server.guestNames.remove(username);
                if (!Objects.equals(username, "R")) {
                    JOptionPane.showMessageDialog(CanvasManager.managerLogin.managerBoard.frame, "User " + username + " has left ");
                }
				ConnectionManager.updateGuestList(server.getNames());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
			System.out.println("leave");
		}
	}

}
