package com.sparta.lh.learningpathtasklistproject.repositories;

import com.sparta.lh.learningpathtasklistproject.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}