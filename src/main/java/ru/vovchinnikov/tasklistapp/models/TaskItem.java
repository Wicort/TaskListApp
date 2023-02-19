package ru.vovchinnikov.tasklistapp.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Ovchinnikov Viktor
 */
@Data
@Entity
@Table(name="task_item")
public class TaskItem implements TaskListItem{

    @Id
    @Column(name="id")
    private UUID id;

    @Column(name="task_name")
    @Size(min=3, max=100, message="Название задачи должно быть в диапазоне от 3 до 100 символов")
    private String name;

    @Column(name="task_description")
    @Size(max=255, message="Длина описания задачи не может быть больше 255 символов")
    private String description;

    @Column(name="priority")
    private int priority;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="deadline_at")
    private LocalDateTime deadlineAt;

    @Column(name="released_at")
    private LocalDateTime releasedAt;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User owner;

    @ManyToOne
    @JoinColumn(name="author_id", referencedColumnName = "id")
    private User author;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="editor_id", referencedColumnName = "id")
    private User editor;

    /*@ManyToMany(mappedBy = "tasks")
    private List<Category> categories;*/

    @Override
    public void release() {
        this.releasedAt = LocalDateTime.now();
    }

}
