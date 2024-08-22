

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerController extends Thread{
    // client socket connection
    private Socket clientSocket = null;
    // data access object for dictionary operations
    private DictionaryDao dictionaryDao;

    private String word = null;
    private String meanings = null;
    private String[] parts = null;
    private String action = null;
    private String[] wordMeanings = null;

    //constructor
    public ServerController(Socket socket, DictionaryDao dictionaryDao){

        this.clientSocket = socket;
        this.dictionaryDao = dictionaryDao;
    }

    public void run(){

        try{
            // set read and write buffer
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));

            // the client number + 1, when the client connecting
            Server.count++;

            String clientMsg =null;

            // read messages from the client until the connection is closed
            while ((clientMsg = reader.readLine()) != null) {
                System.out.println("server recieve: " + clientMsg);

                // split the client message into action, word, and meanings
                splitCommand(clientMsg);

                String res = null;

                //excute the actoin requesting from client
                switch (action) {
                    case "add":
                        // call the DAO(Data Access Operation) class to asscess to data
                        res = dictionaryDao.addWord(word, meanings);
                        break;
                    case "update":
                        res = dictionaryDao.updateWord(word, meanings);
                        break;
                    case "query":
                        String meagning = dictionaryDao.queryMeanings(word); 
                        if (meagning == null)
                            res = "0";
                        else
                            res = word + ":" + meagning;
                        break;
                    case "delete":
                        res = dictionaryDao.deleteWord(word);
                    default:
                        System.out.println("Dao failed");
                        break;
                }

                // send the result back to the client
                writer.write(res + "\n");
                System.out.println("server send: " + res);
                writer.flush();
                dictionaryDao.loadDictionary();
            }
            
            
        } catch (Exception e) {
            System.out.println("server IO wrong " + e.getMessage());
        }finally{
            Server.count--;
        }


    }

    // splits the client command into action, word, and meaning
    public void splitCommand(String command){
        this.parts = command.split(",", 2);
        this.action = parts[0];
        this.wordMeanings = parts[1].split(":", 2);
        this.word = wordMeanings[0];
        this.meanings = wordMeanings[1];
    }
    
}
