package com.sparta.lh.learningpathtasklistproject.controllers;

import com.sparta.lh.learningpathtasklistproject.entities.Task;
import com.sparta.lh.learningpathtasklistproject.entities.User;
import com.sparta.lh.learningpathtasklistproject.entities.UserTask;
import com.sparta.lh.learningpathtasklistproject.repositories.TaskRepository;
import com.sparta.lh.learningpathtasklistproject.repositories.UserRepository;
import com.sparta.lh.learningpathtasklistproject.repositories.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public ResponseEntity<EntityModel<User>> getUserById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User userEntity = user.get();

            Link selfLink = linkTo(methodOn(this.getClass()).getUserById(id)).withSelfRel();

            Link tasksLink = linkTo(methodOn(UserController.class).getUserTasks(id)).withRel("tasks");

            EntityModel<User> resource = EntityModel.of(userEntity, selfLink, tasksLink);

            return ResponseEntity.ok(resource);
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
