package ru.vovchinnikov.tasklistapp.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author Ovchinnikov Viktor
 */
@Data
public class CategoryDTO {
    private UUID id;

    @Size(min = 3, max = 100, message = "Название списка должно содержать от 3 до 100 символов")
    private String name;

    private UUID ownerId;
}
