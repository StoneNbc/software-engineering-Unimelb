package CanvasGuest;

import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

// Board GUI
public class GuestBoard {

	// username
	private String guestName;
	static Canvas canvas;
	// listen the mouse 
	static Listener listener;

	JFrame frame;
	JList userList;
	// print chat history
	private JTextArea chatArea;
	// chat message input box
	private JTextField textInput;
	// initialize the Icon
	ImageIcon line = new ImageIcon(GuestBoard.class.getResource("/Icon/line.png"));
	ImageIcon circle = new ImageIcon(GuestBoard.class.getResource("/Icon/circle.png"));
	ImageIcon rect = new ImageIcon(GuestBoard.class.getResource("/Icon/rect.png"));
	ImageIcon oval = new ImageIcon(GuestBoard.class.getResource("/Icon/oval.png"));
	ImageIcon pencil = new ImageIcon(GuestBoard.class.getResource("/Icon/pencil.png"));
	ImageIcon color = new ImageIcon(GuestBoard.class.getResource("/Icon/color.png"));
	ImageIcon erase = new ImageIcon(GuestBoard.class.getResource("/Icon/erase.png"));
	ImageIcon[] icons = {line, circle, rect, oval, pencil, color,erase};

	// constructor
	public GuestBoard(String username) {
		this.guestName = username;
		initialize();
	}

	// initialise the board
	private void initialize() {

		frame = new JFrame();
		frame.setTitle("Distributed Whiteboard (Guest): " + guestName);
		frame.setBounds(100, 100, 1400, 700);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

		// Create a canvas for drawing
		canvas = new Canvas();
		canvas.setBackground(Color.WHITE);
		canvas.setBounds(100, 0, 1050, 680);
		canvas.setFont(new Font("Arial", Font.BOLD, 16));
		frame.getContentPane().add(canvas);
		
		// Add Canvas drawing techniques in to the canvas.
		listener = new Listener(frame,canvas.getGraphics(),canvas);
		canvas.addMouseListener(listener);
		canvas.addMouseMotionListener(listener);

		// Menu and shape Tools
		JPanel toolPanel = new JPanel();
		toolPanel.setBounds(0, 0, 80, 450);
		toolPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		frame.getContentPane().add(toolPanel);


		// Adding drawing functional buttons
		String[] tools = {"Line", "Circle", "Rect", "Oval", "Free", "Color", "Erase"};
		for (int i = 0; i < tools.length; i++) {
			JButton btn = new JButton("");
			btn.setActionCommand(tools[i]);
			btn.setPreferredSize(new Dimension(60, 30));

			// Rescale the icons
			Image temp = icons[i].getImage().getScaledInstance(21, 21, icons[i].getImage().SCALE_DEFAULT);
			icons[i] = new ImageIcon(temp);
			btn.setIcon(icons[i]);

			btn.addActionListener(listener);
			toolPanel.add(btn);
		}
		// Text
		JButton text = new JButton("A");
		text.setActionCommand("Text");
		text.setFont(new Font(null, 0, 20));
		text.setPreferredSize(new Dimension(60, 30));
        toolPanel.add(text);
        frame.getContentPane().setLayout(null);
		frame.getContentPane().add(toolPanel);
		text.addActionListener(listener);


		// user list
		userList = new JList<Object>();
		frame.getContentPane().add(userList);
		ArrayList<String>  list_names = Client.connection.userList;
		userList.setListData(list_names.toArray());

		JScrollPane scrollList = new JScrollPane(userList);
		scrollList.setBounds(1200, 0, 100, 170);
		scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollList);


		// chat part
		// input box
		textInput = new JTextField();
		textInput.setBounds(1200, 600, 90, 50);
		frame.getContentPane().add(textInput);
		// Chat function
		chatArea = new JTextArea();
		chatArea.setBounds(1200, 250, 150, 350);
		chatArea.setEnabled(false);
		chatArea.setDisabledTextColor(Color.BLACK);
		chatArea.setWrapStyleWord(true);
		chatArea.setLineWrap(true);
		frame.getContentPane().add(chatArea);
		
		// send message button
		JButton send = send();
		frame.getContentPane().add(send);

		JSONObject request = new JSONObject();
		request.put("reqType", "Sync");
		try {
			Client.output.writeUTF(request.toJSONString());
			Client.output.flush();
			System.out.println("send " + request.toJSONString());
		} catch (IOException e) {
			System.out.println("Error sending request " + e.getMessage());
		}

		frame.revalidate(); // refresh the gui
		frame.repaint(); //

	}

	// send message function
	private JButton send() {
		JButton btnNewButton = new JButton("Send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = textInput.getText();
				if (!input.isBlank()) {
					JSONObject textJsonObject = new JSONObject();
					textJsonObject.put("reqType", "Chat");
					textJsonObject.put("reqBody", input);
					textJsonObject.put("Sender", guestName);
					try {
						Client.output.writeUTF(textJsonObject.toJSONString());
						Client.output.flush();
						System.out.println("send " + textJsonObject.toJSONString());
					} catch (IOException e1) {
						System.out.println("Chat Error: " + e1.getMessage());
					}
					textInput.setText("");
				}
			}
		});
		btnNewButton.setBounds(1300, 600, 50, 50);
		return btnNewButton;
	}

	public void addText(String text, String who) {
		this.chatArea.append(who + ": " + text + "\n");
	}
}
