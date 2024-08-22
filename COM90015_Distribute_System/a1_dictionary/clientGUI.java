

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import java.awt.event.ActionEvent;

public class clientGUI extends Thread{

	private JFrame frmDictionaryClient;
	private JTextField textField_word;
	private JTextField textField_Meaning;
	private JTextArea textArea;
	// Client client = new Client();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					clientGUI window = new clientGUI();
					window.frmDictionaryClient.setVisible(true);
				} catch (Exception e) {
					System.out.println("init client UI failed");
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public clientGUI() {
		// client.start();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		clientController controller = new clientController(this);

		frmDictionaryClient = new JFrame();
		frmDictionaryClient.setTitle("Dictionary Client");
		frmDictionaryClient.setBounds(100, 100, 450, 300);
		frmDictionaryClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDictionaryClient.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Word");
		lblNewLabel.setBounds(41, 66, 58, 15);
		frmDictionaryClient.getContentPane().add(lblNewLabel);
		
		textField_word = new JTextField();
		textField_word.setBounds(41, 89, 66, 21);
		frmDictionaryClient.getContentPane().add(textField_word);
		textField_word.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Meaning");
		lblNewLabel_1.setBounds(164, 66, 58, 15);
		frmDictionaryClient.getContentPane().add(lblNewLabel_1);
		
		textField_Meaning = new JTextField();
		textField_Meaning.setBounds(161, 89, 100, 21);
		frmDictionaryClient.getContentPane().add(textField_Meaning);
		textField_Meaning.setColumns(10);
		
		textArea = new JTextArea();
		textArea.setBounds(41, 141, 220, 89);
		textArea.setEditable(false);
		frmDictionaryClient.getContentPane().add(textArea);
		
		JButton btnNewButton_create = new JButton("Create");
		btnNewButton_create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// use SwingWorker to perform background operations and svoid the GUI blocking
				SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
					

					protected String doInBackground() {
						
						System.out.println("clicked create");
						textArea.setText(null);
						// get the word and meaning form GUI
						String word = textField_word.getText();
						String meaning = textField_Meaning.getText();
						// check if client input the meaning
						if( word.isEmpty()){
							return "word cannot be empty";
						}
						else if(meaning.isEmpty()){
							return "meaning cannot be empty";
						}
						

						//format the cammand and send it
						String command = word + ":" + meaning;
						return controller.addWord(command); 
					}
					
					// after background task completion, update the GUI with the response
					protected void done(){
						
						try {
							// get the result of action
							String res = get(); 
							// check connection 
							if (res == null) {
								textArea.setText("disconnected");
							} else {
								// display the result
								textArea.setText(res);
							}
						} catch (Exception ex) {
							textArea.setText("Failed to execute the command " + e.getModifiers());
						}
					}
					
				};
				worker.execute(); //start the worker


			}
		});
		btnNewButton_create.setBounds(306, 62, 97, 23);
		frmDictionaryClient.getContentPane().add(btnNewButton_create);
		
		JButton btnNewButton_update = new JButton("Update");
		btnNewButton_update.addActionListener(new ActionListener() {
			public  void actionPerformed(ActionEvent e){
				// use SwingWorker to perform background operations and svoid the GUI blocking
				SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

					protected String doInBackground() {
						
						System.out.println("clicked update");
						textArea.setText(null);
						// get the word and meaning form GUI
						String word = textField_word.getText();
						String meaning = textField_Meaning.getText();
						if( word.isEmpty()){
							return "word cannot be empty";
						}
						else if(meaning.isEmpty()){
							return "meaning cannot be empty";
						}
						 
						String command = word + ":" + meaning;
						return controller.updateWord(command); 
					}
					// after background task completion, update the GUI with the response
					protected void done() {
						
						try {
							// get the result of action
							String res = get(); 
							// check connection 

							if (res == null) {
								textArea.setText("disconnected");
							} else {
								textArea.setText(res);
							}
						} catch (Exception ex) {
							textArea.setText("Failed to execute the update command." + ex.getMessage());
						}
					}
				};
				worker.execute(); // start SwingWorker

			}
		});
		btnNewButton_update.setBounds(306, 109, 97, 23);
		frmDictionaryClient.getContentPane().add(btnNewButton_update);
		
		JButton btnNewButton_query = new JButton("Query");
		btnNewButton_query.addActionListener(new ActionListener() {
			public  void actionPerformed(ActionEvent e){
				// use SwingWorker to perform background operations and svoid the GUI blocking
				SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

					protected String doInBackground() throws Exception {
						
						System.out.println("clicked query");
						
						textArea.setText(null);
						textField_Meaning.setText(null);
						// get the word and meaning form GUI
						String word = textField_word.getText();
						if( word.isEmpty()){
							return "word cannot be empty";
						}

						return controller.queryWord(word+":"); 
					}
					// after background task completion, update the GUI with the response
					protected void done() {
						
						try {
							// get the result of action
							String res = get(); 
							// check connection 
							if (res == null) {
								textArea.setText("disconnected");
							} 
							if (res == "word cannot be empty"){
								textArea.setText(res);
							}
							else {
								if(res.equals("0"))
									textArea.setText("Word does not exist in the dictionary.");
								else {
									// split the word and meaning
									String[] parts = res.split(":", 2);
									String wordRes = parts[0].trim();
									String meanings = parts[1].trim(); 
									// display the results
									textField_word.setText(wordRes);
									textField_Meaning.setText(meanings);
									textArea.setText("query sucess");
								}
								
								
							}
						} catch (InterruptedException | ExecutionException ex) {
							
							textArea.setText("Failed to execute the query command." + ex.getMessage());
						}
					}
				};
				worker.execute(); // start SwingWorker	
									
			}
		});
		btnNewButton_query.setBounds(306, 162, 97, 23);
		frmDictionaryClient.getContentPane().add(btnNewButton_query);
		
		JButton btnNewButton_delete = new JButton("Delete");
		btnNewButton_delete.addActionListener(new ActionListener() {
			public  void actionPerformed(ActionEvent e){
				// use SwingWorker to perform background operations and svoid the GUI blocking
				SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

           			protected String doInBackground() {
                		
                		System.out.println("clicked delete");
						textArea.setText(null);
						// get the word and meaning form GUI
                		String word = textField_word.getText();
						if( word.isEmpty()){
							return "word cannot be empty";
						}
						
						textField_Meaning.setText(null);
                		
                		return controller.deleteWord(word+":");
            		}
					// after background task completion, update the GUI with the response
	            	protected void done() {
    	            	
        	        	try {
            	        	String res = get(); 
                	    	textArea.setText(res);
                    		
	                	} catch (InterruptedException | ExecutionException ex) {
        	            	textArea.setText("Failed to execute the delete command." + ex.getMessage());
            	    	}
            		}
       			};
        		worker.execute(); 		

			}
		});
		btnNewButton_delete.setBounds(306, 211, 97, 23);
		frmDictionaryClient.getContentPane().add(btnNewButton_delete);

		JButton btnNewButton_Clear = new JButton("Clear");
		btnNewButton_Clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(null);
				textField_word.setText(null);
				textField_Meaning.setText(null);

			}
		});
		btnNewButton_Clear.setBounds(306,12,97,23);
		frmDictionaryClient.getContentPane().add(btnNewButton_Clear);
		
		JLabel lblNewLabel_2 = new JLabel("Client");
		lblNewLabel_2.setBounds(164, 21, 58, 15);
		frmDictionaryClient.getContentPane().add(lblNewLabel_2);
	}
}
