package CanvasManager;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.JTextField;

public class ManagerLogin {

	private String username;
	private JFrame frame;
	private JTextField textField;
	public static ManagerBoard managerBoard;


	public ManagerLogin(String username) {
		this.username = username;
		initialize();
	}


	private void initialize() {

		frame = new JFrame();
		frame.setBounds(0, 0, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Login button
		JButton loginButton = new JButton("LOGIN");
		loginButton.setBounds(150, 150, 150, 60);
		frame.getContentPane().add(loginButton);
		
		// Once the Login button has been clicked it will automatically open a canvas window
		loginButton.addActionListener(e -> {
			if (e.getActionCommand().equals("LOGIN")) {
				username = textField.getText();
				frame.dispose();
				try {
					managerBoard = new ManagerBoard(username);
					// createWhiteBoard = new Manager(username); // 假设有一个Manager类和Manager窗口
					// createWhiteBoard.setFrame(createWhiteBoard);
				} catch (Exception e1) {
					e1.printStackTrace();
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
