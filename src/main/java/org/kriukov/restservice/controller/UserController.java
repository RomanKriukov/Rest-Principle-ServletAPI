package org.kriukov.restservice.controller;

import org.kriukov.restservice.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return users;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        Optional<User> userOptional = users.stream().filter(user -> user.getId() == id).findFirst();

        return userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (users.stream().anyMatch(user1 -> user1.getId() == user.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (users.stream().anyMatch(user1 -> user1.getSurname().equals(user.getSurname()))) {
            return ResponseEntity.badRequest().build();
        }
        users.add(user);
        return ResponseEntity.created(URI.create("/users")).build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User userBody, @PathVariable("id") int id) {
        if (users.stream()
                .anyMatch(user1 -> user1.getId() != userBody.getId()
                        && user1.getSurname().equals(userBody.getSurname()))) {

            return ResponseEntity.badRequest().build();
        }
        for (User user : users) {
            if (user.getId() == id) {
                user.setAge(userBody.getAge());
                user.setName(userBody.getName());
                user.setSurname(userBody.getSurname());
            }
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") int id) {
        Optional<User> userOptional = users.stream().filter(user -> user.getId() == id).findFirst();

        if (userOptional.isPresent()) {
            users.remove(userOptional.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}