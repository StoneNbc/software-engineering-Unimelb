package CanvasManager;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;


public class ManagerBoard {

	// username
	private String managerName;
	static Canvas canvas;
	// listen the mouse
	static Listener listener;
	JList userList;
	JFrame frame;
	File save_file;
	// chat message input box
	private JTextField textInput;
	// print chat history
	private JTextArea chatArea;
	// store all chat history
	static ArrayList<JSONObject> chatHistory = new ArrayList<JSONObject>();
	// initialize the Icon
	ImageIcon line = new ImageIcon(ManagerBoard.class.getResource("/Icon/line.png"));
	ImageIcon circle = new ImageIcon(ManagerBoard.class.getResource("/Icon/circle.png"));
	ImageIcon rect = new ImageIcon(ManagerBoard.class.getResource("/Icon/rect.png"));
	ImageIcon oval = new ImageIcon(ManagerBoard.class.getResource("/Icon/oval.png"));
	ImageIcon pencil = new ImageIcon(ManagerBoard.class.getResource("/Icon/pencil.png"));
	ImageIcon color = new ImageIcon(ManagerBoard.class.getResource("/Icon/color.png"));
	ImageIcon erase = new ImageIcon(ManagerBoard.class.getResource("/Icon/erase.png"));
	ImageIcon[] icons = {line, circle, rect, oval, pencil, color,erase};

	public ManagerBoard(String managerName) {
		this.managerName = managerName;
		initialize();
	}

	// initialise the board
	private void initialize() {

		frame = new JFrame();
		frame.setTitle("Distributed Whiteboard (Manager): " + managerName);
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

		
		
		JPanel toolPanel = new JPanel();
		toolPanel.setBounds(0, 0, 80, 450);
		toolPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		frame.getContentPane().add(toolPanel);

		// tool box
		JComboBox menu = getComboBox();
		toolPanel.add(menu);

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
//        text.addActionListener(createDrawListener);
		if (text != null) {
			toolPanel.add(text);
		}
//        createDrawListener.setThickness(6);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(toolPanel);
		text.addActionListener(listener);

		// user list
		userList = new JList<Object>();
		frame.getContentPane().add(userList);
		ArrayList<String>  list_names = CanvasManager.server.guestNames;
		list_names.add(managerName);
		userList.setListData(list_names.toArray());

		JScrollPane scrollList = new JScrollPane(userList);
		scrollList.setBounds(1200, 0, 100, 170);
		scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollList);

		// kick user button
		JButton kickButton = getKickButton();
		frame.getContentPane().add(kickButton);
		
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
		JButton send = getSend();
		frame.getContentPane().add(send);

		frame.revalidate(); // 刷新界面
		frame.repaint(); // 刷新界面
		

	}

	private JButton getSend() {
		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = textInput.getText();
				if (!input.isBlank()) {
					JSONObject textJson = new JSONObject();
					textJson.put("resType", "Chat");
					textJson.put("resBody", input);
					textJson.put("Sender", managerName);
					ConnectionManager.broadcastMsg(textJson);
					addChat(textJson);
					textInput.setText("");
					addText(input, managerName);
				}
			}
		});
		send.setBounds(1300, 600, 50, 50);
		return send;
	}

	private JButton getKickButton() {
		JButton kickButton = new JButton("Kick");
		kickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (userList.getSelectedValue() == null) {
					return;
				}
				String selected_user = userList.getSelectedValue().toString();

				if(selected_user.equals(managerName)) {
					JOptionPane.showMessageDialog(frame, "Can't kick out yourself" );
					return;
				} else {
					Connection selected_cnt = null;
					for (Connection cnt : CanvasManager.server.guests) {
						if (selected_user.equals(cnt.username)) {
							// cnt.kicked = true;
							selected_cnt = cnt;
							JSONObject res = new JSONObject();
							res.put("resType", "Kick");
							res.put("resBody", selected_user);
							try {
								cnt.output.writeUTF(res.toJSONString());
								cnt.output.flush();
								System.out.println("send" + res);
								cnt.socket.close();
							} catch (IOException e1) {
//								e1.printStackTrace();
								System.out.println("Error send" + e1.getMessage());
							}

						}
					}
                    if (selected_cnt != null && selected_cnt.socket.isConnected()) {
                        CanvasManager.server.guestNames.remove(selected_user);
                        CanvasManager.server.guests.remove(selected_cnt);
                        JOptionPane.showMessageDialog(frame, selected_user + " has been kicked out from the Canvas!");
                    }
                    JSONObject res = new JSONObject();
					res.put("resType", "Remove");
					res.put("resBody", selected_user);
					ConnectionManager.broadcastMsg(res);
				}
			}
		});
		kickButton.setBounds(1200, 200, 80, 30);
		return kickButton;
	}

	private JComboBox getComboBox() {
		JComboBox menu = new JComboBox();
		menu.setModel(new DefaultComboBoxModel(new String[] {"New", "Open", "Save", "Save as", "Save as PNG","Clear", "Exit"}));
		menu.addActionListener(e -> {
			String func = menu.getSelectedItem().toString();
			JFileChooser fileChooser;
			switch (func) {
				case "New":
					canvas.removeAll();
					canvas.updateUI();
					canvas.clearAll();
					JSONObject clear = new JSONObject();
					clear.put("resType", "Clear");
					ConnectionManager.broadcastMsg(clear);
					break;
				case "Open":
					fileChooser = new JFileChooser();
					if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
						String filename = fileChooser.getSelectedFile().toString();
						if (!filename.endsWith(".json")) {
							JOptionPane.showMessageDialog(null, "You should open the file with a .json extension!");
						} else {
							save_file = fileChooser.getSelectedFile();
							canvas.openFile(save_file);
						}
						ArrayList<JSONObject> contents = canvas.getContents();
						ConnectionManager.broadcastDraw(contents);
					}
					break;
				case "Save":
					if(save_file == null) {
						fileChooser = new JFileChooser();
						if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
							String filename = fileChooser.getSelectedFile().toString();
							if (!filename.endsWith(".json")) {
								JOptionPane.showMessageDialog(null, "You should save the file with a .json extension!");
							} else {
								save_file = fileChooser.getSelectedFile();
								canvas.saveFile(save_file);
							}
						}
					} else {
						canvas.saveFile(save_file);
					}

					break;
				case "Save as":
					fileChooser = new JFileChooser();
					if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
						String filename = fileChooser.getSelectedFile().toString();
						if (!filename.endsWith(".json")) {
							JOptionPane.showMessageDialog(null, "You should save the file with a .json extension!");
						} else {
							save_file = fileChooser.getSelectedFile();
							canvas.saveFile(save_file);
						}
					}
					break;
				case "Save as PNG":
					fileChooser = new JFileChooser();
					if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
						String filename = fileChooser.getSelectedFile().toString();
						if (!filename.endsWith(".png")) {
							JOptionPane.showMessageDialog(null, "You should save the file with a .png extension!");
						} else {
							save_file = fileChooser.getSelectedFile();
							canvas.saveAsImage(String.valueOf(save_file));
						}
					}
					break;
				case "Clear":
					canvas.removeAll();
					canvas.updateUI();
					canvas.clearAll();
					JSONObject clear2 = new JSONObject();
					clear2.put("resType", "Clear");
					ConnectionManager.broadcastMsg(clear2);
					break;
				case "Exit":
					System.exit(1);
					break;
				default:
					System.out.println("Unknown command");
			}
		});
		menu.setPreferredSize(new Dimension(80, 40));
		return menu;
	}


	public Integer connectRequest(String name) {
		final JOptionPane optionPane = new JOptionPane("The user "+name + " wants to join",
                JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
		JDialog dialog = optionPane.createDialog("Select Yes or No");
		dialog.setLocationRelativeTo(frame);
		dialog.setTitle("Message");
		dialog.setModal(true);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();
		
        Timer timer = new Timer(5000, e -> dialog.setVisible(false));
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
        
		if (optionPane.getValue() instanceof Integer) {
			return (Integer) optionPane.getValue();
		}
		return null;
        
	}
	
	public void addText(String text, String who) {
		this.chatArea.append(who + ": " + text + "\n");
	}

	public static void addChat(JSONObject chat) {
		chatHistory.add(chat);
	}

	public static ArrayList<JSONObject> getChat(){
		return chatHistory;
	}
}
