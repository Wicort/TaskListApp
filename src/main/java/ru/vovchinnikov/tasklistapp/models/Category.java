package ru.vovchinnikov.tasklistapp.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Ovchinnikov Viktor
 */
@Entity
@Data
@Table(name="category")
public class Category {
    @Id
    @Column(name="id")
    private UUID id;

    @Column(name = "category_name")
    @Size(min = 3, max = 100, message = "Название списка должно содержать от 3 до 100 символов")
    private String name;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;
}
