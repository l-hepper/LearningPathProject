package com.sparta.lh.learningpathtasklistproject.controllers;

import com.sparta.lh.learningpathtasklistproject.entities.Task;
import com.sparta.lh.learningpathtasklistproject.entities.User;
import com.sparta.lh.learningpathtasklistproject.repositories.TaskRepository;
import com.sparta.lh.learningpathtasklistproject.repositories.UserRepository;
import com.sparta.lh.learningpathtasklistproject.repositories.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserTaskController {

    private final UserTaskRepository userTaskRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public UserTaskController(UserTaskRepository userTaskRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.userTaskRepository = userTaskRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }


    @PostMapping("/users/{userId}/tasks/{taskId}")
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

    @DeleteMapping("/users/{userId}/tasks/{taskId}")
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
}
