package CanvasManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.simple.JSONObject;


public class Listener implements MouseMotionListener, MouseListener, ActionListener {

	// to store the mouse's position
	int startX, startY, endX, endY;
	static Color color = Color.BLACK;
	// set the default funciontn is Line
	String command = "Line";

	Stroke stroke = new BasicStroke(2);
	JFrame colorFrame;  // Color frame for selected colors.
	Canvas canvas;
	Graphics2D graphics2D;
	JFrame frame;
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
		if (text != null) {
			content.put("Text", text);
		} else {
			content.put("Text", "");
		}
		content.put("Color", Integer.toString(color.getRGB()));
		canvas.addContent(content);
		ConnectionManager.broadcast(content);

	}

	@Override
	// chose the tool
	public void actionPerformed(ActionEvent e) {
		command = e.getActionCommand();
		graphics2D.setStroke(stroke);
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
	// get the mouse's first position when pressed
	@Override
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
			case "Line":
				graphics2D.setStroke(stroke);
				graphics2D.drawLine(startX, startY, endX, endY);
				broadcast("Line", startX, startY, endX, endY, color, null);
				break;
			case "Rect":
				graphics2D.setStroke(stroke);
				graphics2D.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.abs(startX - endX), Math.abs(startY - endY));
				broadcast("Rect", startX, startY, endX, endY, color, null);
				break;
			case "Circle":
				graphics2D.setStroke(stroke);
				int diameter = (int) Math.min(Math.abs(startX - endX), Math.abs(startY - endY));
				;
				graphics2D.drawOval(Math.min(startX, endX), Math.min(startY, endY), diameter, diameter);
				broadcast("Circle", startX, startY, endX, endY, color, null);
				break;
			case "Oval":
				graphics2D.setStroke(stroke);
				graphics2D.drawOval(Math.min(startX, endX), Math.min(startY, endY), Math.abs(startX - endX), Math.abs(startY - endY));
				broadcast("Oval", startX, startY, endX, endY, color, null);
				break;
			case "Text":
				String text = JOptionPane.showInputDialog("Please Enter Input Text:");
				if (text != null) {
					graphics2D.drawString(text, startX, startY);
					broadcast("Text", startX, startY, endX, endY, color, text);
				}
				break;

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
		if (command.equals("Free")) {
			graphics2D.setStroke(stroke);
			graphics2D.setColor(color);
			graphics2D.drawLine(startX, startY, endX, endY);
			broadcast("Line", startX, startY, endX, endY, color, null);
			startX = endX;
			startY = endY;
		} if (command.equals("Erase")) {
			graphics2D.setStroke(new BasicStroke(2));
			graphics2D.setColor(Color.WHITE);
			graphics2D.drawLine(startX, startY, endX, endY);
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
