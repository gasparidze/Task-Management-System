package com.example.domain.entity;

import com.example.domain.enums.Priority;
import com.example.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность Задача
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"author", "performer"})
@Data
@Builder
@Entity
@Table(schema = "task_service", name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String header;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id", nullable = false)
    private User performer;

    @Builder.Default
    @OneToMany(mappedBy = "task")
    private List<Comment> comments = new ArrayList<>();
}
