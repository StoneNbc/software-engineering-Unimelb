public class clientController {

    // the client object for communication with the server
    private Client client;
    
    // constructor
    public clientController(clientGUI clientGUI){
        try {
            this.client = new Client("localhost", 4445);
            this.client.start();
        } catch (Exception e) {
            System.out.println("client create failed "+ e.getMessage());
        }

    }

    // sends an "add" action and the word to the server and returns the server's response.
    public String addWord(String word) {
        String action = "add";
        client.sendMsg(formatData(action, word));
        return client.getMsg();
    }

    // sends a "query" action and the word to the server and returns the server's response.
    public String queryWord(String word){
        client.sendMsg(formatData("query", word));
        return client.getMsg();
    }

    // sends an "update" action and the word to the server and returns the server's response.
    public String updateWord(String word){
        client.sendMsg(formatData("update", word));
        return client.getMsg();
    }
    
    // sends a "delete" action and the word to the server and returns the server's response.
    public String deleteWord(String word){
        client.sendMsg(formatData("delete", word));
        return client.getMsg();
    }

    // format the action and word into a single string
    private String formatData(String action, String word) {
        return action + "," + word;
    }


    
}
