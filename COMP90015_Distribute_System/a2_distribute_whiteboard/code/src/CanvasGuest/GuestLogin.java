package CanvasGuest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GuestLogin {

	private String username;
	JFrame frame;
	private JTextField textField;
	public static GuestBoard guestBoard;
	protected static JSONParser parser = new JSONParser();

	public GuestLogin(String username) {
		this.username = username;
		initialize();
	}

	private void initialize() {

		frame = new JFrame();
		frame.setBounds(0, 0, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Login button
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(150, 150, 150, 60);
		frame.getContentPane().add(loginButton);

		// when the Login button has been clicked, it will open a canvas window and send request manager
		loginButton.addActionListener(e -> {
			if (e.getActionCommand().equals("Login")) {
				username = textField.getText();
				
				if(username.isBlank()) {
					JOptionPane.showMessageDialog(frame, "Please enter your username.");
				} else {
					JSONObject request = new JSONObject();
					request.put("reqType", "Connect");
					request.put("username", username);
					try {
						Client.output.writeUTF(request.toJSONString());
						Client.output.flush();

						//start timer to waiting the manager reponse
						int time = 0;
						while (Client.connection.isPermitted().equals("Waiting") && time < 100000) {
							TimeUnit.MILLISECONDS.sleep(100);
							time += 100;
						}

						// check the status whether being permitted by manager
						String status = Client.connection.isPermitted();
						System.out.println("status " + status);
						if (status.equals("Repeat")) {
							JOptionPane.showMessageDialog(frame, "Repeated username.");
							Client.connection.reset();
						} 
						
						else if (status.equals("Rejected")) {
							frame.dispose();
							JOptionPane.showMessageDialog(frame, "Rejected by Manager");
							CanvasGuest.client.socket.close();
							System.exit(1);
						} 
						
						else if (status.equals("Accept")) {
							frame.dispose();
							try {
								if (guestBoard == null) {
									guestBoard = new GuestBoard(username);
								}
							} catch (Exception e1) {

								System.out.println("Error occurring when initialising Manager Canvas.");
							}
						} else {
							JOptionPane.showMessageDialog(frame, "Connection Timeout, exit automatically...");
							CanvasGuest.client.socket.close();
							System.exit(1);
							frame.dispose();
						}
					} catch (IOException e2) {
						JOptionPane.showMessageDialog(frame, "Error request to manager");
					} catch (NullPointerException e2) {
						JOptionPane.showMessageDialog(frame, "The Manager is not opened yet.");
					} catch (InterruptedException ex) {
						System.out.println("Time delay Error" + ex.getMessage());
                    }
                }
			}
		});

		// Username area
		JLabel usernameLabel = new JLabel("Input your username:");
		usernameLabel.setBounds(150, 25, 200, 50);
		frame.getContentPane().add(usernameLabel);

		textField = new JTextField();
		textField.setBounds(80, 70, 280, 50);
		frame.getContentPane().add(textField);
		textField.setText(username);



		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(loginButton);
		frame.setVisible(true);
	}

}
