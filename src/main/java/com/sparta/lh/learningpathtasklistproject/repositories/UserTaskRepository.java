package com.sparta.lh.learningpathtasklistproject.repositories;

import com.sparta.lh.learningpathtasklistproject.entities.UserTask;
import com.sparta.lh.learningpathtasklistproject.entities.UserTaskId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTaskRepository extends JpaRepository<UserTask, UserTaskId> {

    List<UserTask> findUserTasksByUserId(Integer userId);
    List<UserTask> findUserTasksByTaskId(Integer taskId);
}