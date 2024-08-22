package Server;


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class serverGUI {

	private JFrame frmDictionaryServer;
	private JTextField textField;
	private Timer timer;
	private Timer timer2;
	private Server server = new Server();
	private String filename = "dictionary.txt";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					serverGUI window = new serverGUI();
					window.frmDictionaryServer.setVisible(true);
				} catch (Exception e) {
					System.out.println("server GUI init wrong " + e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public serverGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		server.start();

		frmDictionaryServer = new JFrame();
		frmDictionaryServer.setTitle("Dictionary Server ");
		frmDictionaryServer.setBounds(100, 100, 450, 300);
		frmDictionaryServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDictionaryServer.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Current clients：");
		lblNewLabel.setBounds(75, 40, 108, 15);
		frmDictionaryServer.getContentPane().add(lblNewLabel);	
		
		// text field for displaying the number of connected clients
		textField = new JTextField();
		textField.setBounds(182, 37, 66, 21);
		textField.setEditable(false);
		frmDictionaryServer.getContentPane().add(textField);
		textField.setColumns(10);
		// timer to periodically update the number of connected clients
		timer = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                textField.setText(String.valueOf(server.getCount()));
            }
        });
        timer.start();
		
		// textarea for displaying the dictionary content
		JTextArea textArea = new JTextArea();
		textArea.setBounds(182, 86, 189, 167);
		frmDictionaryServer.getContentPane().add(textArea);
		textArea.setEditable(false);
		// timer to periodically update the dictionary content displayed in the text area
		timer2 = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText(getDictionary());
            }
        });
		timer2.start();
		JLabel lblNewLabel_1 = new JLabel("Dictionary input：");
		lblNewLabel_1.setBounds(71, 86, 123, 15);
		frmDictionaryServer.getContentPane().add(lblNewLabel_1);
	}

	// reads the dictionary from the file
	public String getDictionary(){
		String lineStr;
		String text = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            text = reader.readLine()+"\n";
            while ((lineStr = reader.readLine()) != null) {
                text = text + lineStr + "\n";
            }
        } catch (IOException e) {
            System.out.println("Error loading dictionary: " + e.getMessage());
        }
		return text;
	}
}
