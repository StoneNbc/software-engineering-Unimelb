package	CanvasGuest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Canvas extends JPanel {

	// Store the json history which used to send to new guest
	private ArrayList<JSONObject> contents = new ArrayList<JSONObject>();
	// set default stroke
	Stroke stroke = new BasicStroke(2);

	Graphics2D graphics2D;

	// the constructor
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		this.graphics2D = (Graphics2D) graphics;
		draw(graphics2D, contents);
	}

	// draw function to draw canvas
	public void draw(Graphics2D graphic, ArrayList<JSONObject> contents) {
		System.out.println("draw new");
		try{
			// iterate every received json
			for(JSONObject content : contents){
//                get method
				String method = content.get("Method").toString();
				System.out.println("Method2: " + method);
//                get position
				int startX, startY, endX, endY;
//                get color
				String color = (String) content.get("Color");
				graphic.setColor(new Color(Integer.parseInt(color)));
				graphic.setStroke(stroke);

				// set position from json
				if ((content.get("StartX") instanceof Integer && content.get("StartY") instanceof Integer
						&& content.get("EndX") instanceof Integer && content.get("EndY") instanceof Integer)) {
					startX = (int) content.get("StartX");
					startY = (int) content.get("StartY");
					endX = (int) content.get("EndX");
					endY = (int) content.get("EndY");
				} else {
					startX = (int)(long) content.get("StartX");
					startY = (int)(long) content.get("StartY");
					endX = (int)(long) content.get("EndX");
					endY = (int)(long) content.get("EndY");
				}

				// draw rules
				switch(method){
					// draw line
					case "Line":
						graphic.drawLine(startX, startY, endX, endY);
						break;
						// draw circle
					case "Circle":
						int diameter = Math.min(Math.abs(startX - endX), Math.abs(startY - endY));
						graphic.drawOval(Math.min(startX, endX), Math.min(startY, endY), diameter, diameter);
						break;
						// draw rectangle
					case "Rect":
						graphic.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.abs(startX - endX), Math.abs(startY - endY));
						break;
						// draw oval
					case "Oval":
						graphic.drawOval(Math.min(startX, endX), Math.min(startY, endY), Math.abs(startX - endX), Math.abs(startY - endY));
						break;
						// add text
					case "Text":
						String text = content.get("Text").toString();
						graphic.drawString(text, startX, startY);
						break;
					default:
						System.out.println("unknown method: " + method);
						break;
				}

			}
		}catch (Exception e){
//			e.printStackTrace();
            System.out.println("draw failed"+ e.getMessage());
		}

	}

	// add this operation to json history
	public void addContent(JSONObject content) {
		contents.add(content);
	}
	// get the json history
	public ArrayList<JSONObject> getContents() {
		return this.contents;
	}
	// clear all json
	public void clearAll() {
		this.contents.clear();
	}


	public void saveAsImage(String filename) {
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		graphics2D = image.createGraphics();
		paint(graphics2D);
		graphics2D.dispose();
		try {
			ImageIO.write(image, "png", new File(filename));
			JOptionPane.showMessageDialog(this, "Image saved successfully!");
		} catch (IOException ex) {
//			ex.printStackTrace();
			System.out.println("Error saving image" + ex.getMessage());
			JOptionPane.showMessageDialog(this, "Failed to save image!");
		}
	}

	public void saveFile(File save_file) {
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(new FileWriter(save_file));
			outputStream.write(contents.toString());
		}catch (IOException e1) {
			System.out.println("Error opening the file "+save_file );
			return;
		}finally {
			outputStream.flush();
			outputStream.close();
			System.out.println("Saved");
		}

	}

	public void openFile(File selectedFile) {
		JSONParser parser = new JSONParser();
		try (FileReader reader = new FileReader(selectedFile)) {
			//Read JSON file
			Object obj = parser.parse(reader);
			JSONArray contents = (JSONArray) obj;
			for (Object content : contents) {
				addContent((JSONObject) content);
				System.out.println("added content" + content.toString());
			}

//            ConnectionManager.broadcastDraw(contents);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "There is no such file exist!" );
		} catch (IOException e) {
			e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error occuring when openning the file!" );
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error occuring when parsing the file!" );
		}
		repaint();

	}

}
