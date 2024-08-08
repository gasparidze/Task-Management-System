package com.example.repository;

import com.example.domain.entity.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * репозиторий для работы с таблицей task в БД
 */
public interface TaskRepository extends JpaRepository<Task, Long>{
    Slice<Task> findAllByAuthorId(Long authorId, Pageable pageable);

    Slice<Task> findAllByPerformerId(Long performerId, Pageable pageable);
}
