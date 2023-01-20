package ru.vovchinnikov.tasklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class TaskItemDTO {
    private UUID id;

    @Size(min=3, max=100, message="Название задачи должно быть в диапазоне от 3 до 100 символов")
    private String name;

    @Size(max=255, message="Длина описания задачи не может быть больше 255 символов")
    private String description;

    private int priority;

    private LocalDateTime createdAt;

    private LocalDateTime deadlineAt;

    private LocalDateTime releasedAt;

    private UUID ownerId;
}
