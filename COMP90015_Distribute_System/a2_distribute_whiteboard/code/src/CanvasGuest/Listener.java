package CanvasGuest;

import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Listener implements MouseMotionListener, MouseListener, ActionListener{

	// to store the mouse's position
	int startX, startY, endX, endY;
	static Color color = Color.BLACK;
	// set the default funciontn is Line
	String command = "Line";

	Canvas canvas;
	Graphics2D graphics2D;
	JFrame frame;
	Stroke stroke = new BasicStroke(2);
	JFrame colorFrame;

	//constructor
	public Listener(JFrame frame, Graphics graphics, Canvas canvas) {
		this.frame = frame;
		this.graphics2D = (Graphics2D) graphics;
		this.canvas = canvas;
	}

	// add the operation to json object
	private void broadcast(String command, int startX, int startY, int endX, int endY, Color color, String text) {
		JSONObject content = new JSONObject();
		content.put("Method", command);
		content.put("StartX", startX);
		content.put("StartY", startY);
		content.put("EndX", endX);
		content.put("EndY", endY);
		// check if the text is null
		if (text != null) {
			content.put("Text", text);
		} else {
			content.put("Text", "");
		}
		content.put("Color", Integer.toString(color.getRGB()));
		JSONObject request = new JSONObject();
		request.put("reqType", "Paint");
		request.put("reqBody", content);
		// send to manager
		try {
			Client.output.writeUTF(request.toJSONString());
			Client.output.flush();
			System.out.println("send " + request);

		} catch (IOException e) {
			System.out.println("Board Error :" + e.getMessage());
		}
	}

	@Override
	// chose the tool
	public void actionPerformed(ActionEvent e) {
		command = e.getActionCommand();
		graphics2D.setStroke(stroke);
		// use the tool to change the color
		if (command.equals("Color")) {
			colorFrame = new JFrame("Color Panel");
			colorFrame.setSize(300, 300);
			colorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Color currentColor = JColorChooser.showDialog(colorFrame, "Choose Color", null);
			if (currentColor != null) {
				color = currentColor;
			}
			graphics2D.setColor(color);
		} else {
			Cursor cursor;
			// use the draw tools
			switch (command) {
				case "Line":
				case "Rect":
				case "Circle":
				case "Oval":
					cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
					frame.setCursor(cursor);
					break;
				default:
					cursor = new Cursor(Cursor.DEFAULT_CURSOR);
					frame.setCursor(cursor);
					break;

			}
		}


	}


	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	// get the mouse's first position when pressed
	public void mousePressed(MouseEvent e) {
		startX = e.getX();
		startY = e.getY();
		if (!graphics2D.getColor().equals(color))
			graphics2D.setColor(color);
	}

	// execute the draw action when mouse released
	@Override
	public void mouseReleased(MouseEvent e) {
		endX = e.getX();
		endY = e.getY();
		switch (command) {
			// draw Line
			case "Line":
				graphics2D.setStroke(stroke);
				graphics2D.drawLine(startX, startY, endX, endY);
				System.out.println("test line");
				broadcast("Line", startX, startY, endX, endY, color, null);
				break;
				// draw rectangle
			case "Rect":
				graphics2D.setStroke(stroke);
				graphics2D.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.abs(startX - endX), Math.abs(startY - endY));
				broadcast("Rect", startX, startY, endX, endY, color, null);
				break;
				// draw circle
			case "Circle":
				graphics2D.setStroke(stroke);
				int diameter = (int) Math.min(Math.abs(startX - endX), Math.abs(startY - endY));
				;
				graphics2D.drawOval(Math.min(startX, endX), Math.min(startY, endY), diameter, diameter);
				broadcast("Circle", startX, startY, endX, endY, color, null);
				break;
				// draw oval
			case "Oval":
				graphics2D.setStroke(stroke);
				graphics2D.drawOval(Math.min(startX, endX), Math.min(startY, endY), Math.abs(startX - endX), Math.abs(startY - endY));
				broadcast("Oval", startX, startY, endX, endY, color, null);
				break;
				// add text
			case "Text":
				String text = JOptionPane.showInputDialog("Please Enter Input Text:");
				if (text != null) {
					graphics2D.drawString(text, startX, startY);
					broadcast("Text", startX, startY, endX, endY, color, text);
				}


		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	// free draw and erase when mouse dragging
	@Override
	public void mouseDragged(MouseEvent e) {
		endX = e.getX();
		endY = e.getY();
//        System.out.println("drag ");
		// free draw
		if (command.equals("Free")) {
			graphics2D.setStroke(stroke);
			graphics2D.setColor(color);
			graphics2D.drawLine(startX, startY, endX, endY);
			System.out.println("test free");
			broadcast("Line", startX, startY, endX, endY, color, null);
			startX = endX;
			startY = endY;
		// erase action
		} if (command.equals("Erase")) {
			graphics2D.setStroke(new BasicStroke(6));
			graphics2D.setColor(Color.WHITE);
			graphics2D.drawLine(startX, startY, endX, endY);
			System.out.println("test erase");
			broadcast("Line", startX, startY, endX, endY, Color.WHITE, null);
			startX = endX;
			startY = endY;
		} else {
			return;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}
