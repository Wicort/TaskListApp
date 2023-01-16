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
public class TalkListErrorItemDTO {

    private String message;

    private int code;

}
