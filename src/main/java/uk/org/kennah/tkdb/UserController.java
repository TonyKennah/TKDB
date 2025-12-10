package uk.org.kennah.tkdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUser(@PathVariable String userId) {
        String userJson = userService.getUserJson(userId);
        if (userJson != null) {
            return ResponseEntity.ok(userJson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getAllUsers() {
        Map<String, String> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> saveUser(@PathVariable String userId, @RequestBody String jsonData) {
        userService.saveUserJson(userId, jsonData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/admin/save")
    public ResponseEntity<Void> saveDatabaseToDisk() {
        userService.saveDatabase();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/load")
    public ResponseEntity<Void> loadDatabaseFromDisk() {
        userService.loadDatabase();
        return ResponseEntity.ok().build();
    }
}
