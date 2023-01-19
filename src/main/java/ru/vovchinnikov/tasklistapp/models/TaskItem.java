package ru.vovchinnikov.tasklistapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Ovchinnikov Viktor
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @Max(value=255, message="Длина описания задачи не может быть больше 255 символов")
    private String description;

    @Column(name="priority")
    private int priority;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="deadline_at")
    private LocalDateTime deadlineAt;

    @Column(name="releasedAt")
    private LocalDateTime releasedAt;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User owner;

    @Override
    public void release() {
        this.releasedAt = LocalDateTime.now();
    }
}
