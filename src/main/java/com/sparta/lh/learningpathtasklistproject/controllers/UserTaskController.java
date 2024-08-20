package com.sparta.lh.learningpathtasklistproject.controllers;

import com.sparta.lh.learningpathtasklistproject.entities.Task;
import com.sparta.lh.learningpathtasklistproject.entities.UserTask;
import com.sparta.lh.learningpathtasklistproject.repositories.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user-tasks")
public class UserTaskController {

    private final UserTaskRepository userTaskRepository;

    @Autowired
    public UserTaskController(UserTaskRepository userTaskRepository) {
        this.userTaskRepository = userTaskRepository;
    }

    @GetMapping
    public ResponseEntity<List<UserTask>> getAllUserTasks() {
        List<UserTask> userTasks = userTaskRepository.findAll();
        return ResponseEntity.ok(userTasks);
    }

    @PostMapping
    public ResponseEntity<UserTask> createUserTask(@RequestBody UserTask userTask) {
        UserTask savedTask = userTaskRepository.save(userTask);
        URI location = URI.create("/user-tasks/" + savedTask.getId());
        return ResponseEntity.created(location).body(savedTask);
    }
}
