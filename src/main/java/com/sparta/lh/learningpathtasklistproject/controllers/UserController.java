package com.sparta.lh.learningpathtasklistproject.controllers;

import com.sparta.lh.learningpathtasklistproject.entities.Task;
import com.sparta.lh.learningpathtasklistproject.entities.User;
import com.sparta.lh.learningpathtasklistproject.entities.UserTask;
import com.sparta.lh.learningpathtasklistproject.repositories.TaskRepository;
import com.sparta.lh.learningpathtasklistproject.repositories.UserRepository;
import com.sparta.lh.learningpathtasklistproject.repositories.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserTaskRepository userTaskRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository, UserTaskRepository userTaskRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.userTaskRepository = userTaskRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/tasks")
    public ResponseEntity<List<Task>> getUserTasks(@PathVariable Integer userId) {
        List<UserTask> userTasks = userTaskRepository.findUserTasksByUserId(userId);
        List<Task> tasks = userTasks.stream().map(UserTask::getTask).collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        URI location = URI.create("/users/" + savedUser.getId());
        return ResponseEntity.created(location).body(savedUser);
    }

    @PostMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<String> assignTaskToUser(@PathVariable Integer userId, @PathVariable Integer taskId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (userOptional.isPresent() && taskOptional.isPresent()) {
            User user = userOptional.get();
            Task task = taskOptional.get();

            user.getTasks().add(task);
            task.getUsers().add(user);

            userRepository.save(user);
            taskRepository.save(task);

            return ResponseEntity.ok("Task assigned to user successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Task not found.");
        }
    }

    @DeleteMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<String> deleteTaskToUser(@PathVariable Integer userId, @PathVariable Integer taskId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (userOptional.isPresent() && taskOptional.isPresent()) {
            User user = userOptional.get();
            Task task = taskOptional.get();

            user.getTasks().remove(task);
            task.getUsers().remove(user);

            userRepository.save(user);
            taskRepository.save(task);

            return ResponseEntity.ok("Task unassigned from user successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Task not found.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            User updatedUser = userRepository.save(existingUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
