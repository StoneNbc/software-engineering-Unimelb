

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server extends Thread{
    
    // port number
    static int port = 4445;
    // thread number of pool
    static final int threadNum = 10;
    // count the client number
    public static int count = 0;

    
    public void run() {
        
        //listen server socket
        ServerSocket liServerSocket = null;

        try {
            // create a thread pool 
            ExecutorService pool = Executors.newFixedThreadPool(threadNum);
            // Initialize the ServerSocket to listen on the specified port
            liServerSocket = new ServerSocket(port);
            System.out.println("server starts, listen the port:" + port);

            // Continuously listen
            while (true) {
                // Accept a connection from a client.
                Socket clientSocket = liServerSocket.accept();
                System.out.println("new clinet connected");

                // Create a ServerController to handle the client in a seperate thread
                ServerController servercontroller = new ServerController(clientSocket, new DictionaryDao());
                pool.execute(servercontroller);
            }
        } catch (Exception e) {
            System.out.println("server cannot connect " + e.getMessage());
        }

    }

    // Getter method to return the current count.
    public int getCount(){
        return count;
    }
}
