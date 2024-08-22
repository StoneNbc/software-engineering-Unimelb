
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class Client extends Thread{
   
    static Socket clientSocket = null;
    // static String address = "localhost";
    // static int port = 4445;
    // address 
    private String address;
    // port number
    private int port;
    // create read and write buffer
    BufferedReader reader;
    BufferedWriter writer;

    public Client(String address, int port){
        this.address = address;
        this.port = port;
    }

    // public void run(){
    public void run() {
        
        try {
            clientSocket = new Socket(address,port);
            System.out.println("Connection established");
            // initialize the buffer
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));


        } catch (Exception e) {
            System.out.println("cannot connect to Server " + e.getMessage());
        }


        
    }

    // send message to server
    public void sendMsg(String command){
        try {
            // write the command to buffer
            writer.write(command+ "\n");
            System.out.println("client send: "+ command);
            // send 
            writer.flush();
        } catch (Exception e) {
            System.out.println("client send msg failed " + e.getMessage());
        }
    }

    // recieve message from the server
    public String getMsg(){

        String msg = null;

        try {
            // read the buffer
            msg = reader.readLine();
            System.out.println("client recieved:" + msg);
        } catch (Exception e) {
            System.out.println("client get msg failed " + e.getMessage());
        }
        // return the message 
        return msg;
    }





}
