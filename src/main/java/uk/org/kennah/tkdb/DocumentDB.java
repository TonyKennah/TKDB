package uk.org.kennah.tkdb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
// Import the concurrent version
import java.util.concurrent.ConcurrentHashMap; 
import org.springframework.stereotype.Component;

@Component
public class DocumentDB {
    // Change the Map type to ConcurrentHashMap
    private final Map<String, String> dataStore;
    private final String databaseFilePath;
    private final Gson gson = new Gson();

    public DocumentDB() {
        // You can initialize the filePath here or use a @Value annotation 
        // to read from application.properties
        this.databaseFilePath = "my_local_data.db"; 
        this.dataStore = loadFromFile();
    }

    public DocumentDB(String filePath) {
        this.databaseFilePath = filePath;
        // The loadFromFile method should return a ConcurrentHashMap as well
        this.dataStore = loadFromFile(); 
    }

    // These methods are now implicitly thread-safe because they use ConcurrentHashMap operations
    public void put(String key, String jsonDocument) {
        dataStore.put(key, jsonDocument);
    }

    public String get(String key) {
        return dataStore.get(key);
    }

    public void remove(String key) {
        dataStore.remove(key);
    }

    public Map<String, String> getAll() {
        return dataStore;
    }

    /**
     * Persists the current in-memory data store to a file.
     * Note: File I/O operations inherently need careful synchronization if 
     * multiple threads call 'saveToDisk' simultaneously. We can add a simple 
     * synchronized block here to prevent race conditions during the save action itself.
     */
    public synchronized void saveToDisk() {
        try (Writer writer = new FileWriter(databaseFilePath)) {
            // Gson serialization is thread-safe on its own, but the file write isn't atomic
            gson.toJson(dataStore, writer);
            System.out.println("Database saved to disk successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the data store from a file when the database is initialized.
     */
    private Map<String, String> loadFromFile() {
        if (Files.exists(Paths.get(databaseFilePath))) {
            try (Reader reader = new FileReader(databaseFilePath)) {
                Type typeOfMap = new TypeToken<ConcurrentHashMap<String, String>>() {}.getType();
                Map<String, String> loadedData = gson.fromJson(reader, typeOfMap);
                return (loadedData != null) ? loadedData : new ConcurrentHashMap<>();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ConcurrentHashMap<>(); // Return an empty concurrent map if file missing
    }

    /**
     * Loads the data store from the file on disk, replacing the current in-memory data.
     * This operation is synchronized to prevent conflicts with other read/write operations.
     */
    public synchronized void loadFromDisk() {
        Map<String, String> dataFromFile = loadFromFile();
        dataStore.clear();
        dataStore.putAll(dataFromFile);
        System.out.println("Database loaded from disk successfully.");
    }
}
