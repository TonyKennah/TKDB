package uk.org.kennah.tkdb;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final DocumentDB database;

    // Use constructor injection for best practices
    @Autowired
    public UserService(DocumentDB database) {
        this.database = database;
    }

    public String getUserJson(String userId) {
        // Accessing the shared, thread-safe database instance
        return database.get("user:" + userId);
    }

    public void saveUserJson(String userId, String jsonData) {
        database.put("user:" + userId, jsonData);
    }

    public Map<String, String> getAllUsers() {
        // Filter the data store to return only entries with the "user:" prefix,
        // and strip the prefix from the key to return a clean map of userId -> userJson.
        return database.getAll().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("user:"))
                .collect(Collectors.toMap(entry -> entry.getKey().substring(5), Map.Entry::getValue));
    }

    public void saveDatabase() {
        database.saveToDisk();
    }

    public void loadDatabase() {
        database.loadFromDisk();
    }
}
