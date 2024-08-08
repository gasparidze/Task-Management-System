package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Сущность Комментарий
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "task")
@Data
@Builder
@Entity
@Table(schema = "task_service", name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
