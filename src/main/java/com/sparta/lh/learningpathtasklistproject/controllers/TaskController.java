package com.sparta.lh.learningpathtasklistproject.controllers;

import com.sparta.lh.learningpathtasklistproject.entities.Task;
import com.sparta.lh.learningpathtasklistproject.entities.User;
import com.sparta.lh.learningpathtasklistproject.entities.UserTask;
import com.sparta.lh.learningpathtasklistproject.repositories.TaskRepository;
import com.sparta.lh.learningpathtasklistproject.repositories.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserTaskRepository userTaskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository, UserTaskRepository userTaskRepository) {
        this.taskRepository = taskRepository;
        this.userTaskRepository = userTaskRepository;
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<Task>>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        // Convert each Task to an EntityModel with HATEOAS links
        List<EntityModel<Task>> taskModels = tasks.stream()
                .map(task -> {
                    Link selfLink = linkTo(methodOn(this.getClass()).getTaskById(task.getId())).withSelfRel();
                    Link usersLink = linkTo(methodOn(this.getClass()).getUsersByTask(task.getId())).withRel("users");
                    return EntityModel.of(task, selfLink, usersLink);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskModels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Task>> getTaskById(@PathVariable int id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            Task taskEntity = task.get();
            Link selfLink = linkTo(methodOn(this.getClass()).getTaskById(id)).withSelfRel();
            Link usersLink = linkTo(methodOn(this.getClass()).getUsersByTask(id)).withRel("users");
            EntityModel<Task> resource = EntityModel.of(taskEntity, selfLink, usersLink);
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{taskId}/users")
    public ResponseEntity<List<EntityModel<User>>> getUsersByTask(@PathVariable Integer taskId) {
        List<UserTask> userTasks = userTaskRepository.findUserTasksByTaskId(taskId);
        List<EntityModel<User>> userModels = userTasks.stream()
                .map(userTask -> {
                    User user = userTask.getUser();
                    Link selfLink = linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel();
                    Link tasksLink = linkTo(methodOn(UserController.class).getUserTasks(user.getId())).withRel("tasks");
                    return EntityModel.of(user, selfLink, tasksLink);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(userModels);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Task>> addTask(@RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        Link selfLink = linkTo(methodOn(this.getClass()).getTaskById(savedTask.getId())).withSelfRel();
        Link usersLink = linkTo(methodOn(this.getClass()).getUsersByTask(savedTask.getId())).withRel("users");
        EntityModel<Task> resource = EntityModel.of(savedTask, selfLink, usersLink);
        return ResponseEntity.created(selfLink.toUri()).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Task>> updateTask(@PathVariable int id, @RequestBody Task task) {
        Optional<Task> existingTaskOptional = taskRepository.findById(id);
        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();
            existingTask.setName(task.getName());
            existingTask.setDeadline(task.getDeadline());
            existingTask.setUsers(task.getUsers());
            Task updatedTask = taskRepository.save(existingTask);

            Link selfLink = linkTo(methodOn(this.getClass()).getTaskById(updatedTask.getId())).withSelfRel();
            Link usersLink = linkTo(methodOn(this.getClass()).getUsersByTask(updatedTask.getId())).withRel("users");
            EntityModel<Task> resource = EntityModel.of(updatedTask, selfLink, usersLink);

            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
