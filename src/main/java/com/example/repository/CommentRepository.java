package com.example.repository;

import com.example.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * репозиторий для работы с таблицей comment в БД
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
