package ru.vovchinnikov.tasklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ovchinnikov Viktor
 */

@Getter
@Setter
@AllArgsConstructor
public class TaskListErrorDTO {

    private TalkListErrorItemDTO error;

    public TaskListErrorDTO(String message, int code){
        this.error = new TalkListErrorItemDTO(message, code);
    }
}
