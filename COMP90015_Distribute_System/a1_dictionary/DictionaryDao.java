import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DictionaryDao {

    // use a hashmap to store the dictionary
    private Map<String, String> dictionary = new HashMap<>();

    private String filename = "dictionary.txt";

    // constructor
    public DictionaryDao() {
        // loadDictionary();
    }

    // action to add word to dic
    public synchronized String addWord(String word, String meanings) {
        // clear cache
        dictionary.clear();

        // reload the dictionary to avoid mistake made during update from other client
        loadDictionary();

        // check it the word already exists
        if (dictionary.containsKey(word)) {
            System.out.println("Word already exists in the dictionary.");
            return "Word already exists in the dictionary.";
        }
        // add the word and its meanings to the dictionary
        dictionary.put(word, meanings);
        // Save changes to the file.
        saveDictionary(); 
        dictionary.clear();
        return "add success";
    }
    
    // updates the meanings of an existing word
    public synchronized String updateWord(String word, String meanings) {
        dictionary.clear();
        loadDictionary();
        // check it the word already exists
        if (!dictionary.containsKey(word)) {
            System.out.println("Word does not exist in the dictionary.");
            return "Word does not exist in the dictionary.";
        }
        // update the word and its meanings to the dictionary
        dictionary.put(word, meanings);

        saveDictionary(); 
        dictionary.clear();
        return "update success";
    }
    
    // delete the meanings of an existing word
    public synchronized String deleteWord(String word) {
        dictionary.clear();
        loadDictionary();
        // check it the word already exists
        if (!dictionary.containsKey(word)) {
            System.out.println("Word does not exist in the dictionary.");
            return "Word does not exist in the dictionary.";
        }
        // delete the word and its meanings from the dictionary
        dictionary.remove(word);

        saveDictionary(); 
        dictionary.clear();
        return "delete success";
    }
    
    // query the meanings of an existing word
    public synchronized String queryMeanings(String word) {
        dictionary.clear();
        loadDictionary();

        String res = dictionary.get(word);
        dictionary.clear();
        // get and return the word's meanings
        return res;
    }
    
    // loads the dictionary from a file into the dictionary map
    public void loadDictionary() {

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String lineStr;
            while ((lineStr = reader.readLine()) != null) {
                // split each line into word and meanings
                String[] parts = lineStr.split(":", 2);
                if (parts.length == 2) {
                    String word = parts[0].trim();
                    String meanings = parts[1].trim();
                    // add it to the hashmap
                    dictionary.put(word, meanings);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading dictionary: " + e.getMessage());
        }
    }

    // saves the dictionary to a file
    public void saveDictionary() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String item : dictionary.keySet()) {
                // Write each word to the file
                writer.write(item + ":" + dictionary.get(item));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving dictionary: " + e.getMessage());
        }
    }



}
