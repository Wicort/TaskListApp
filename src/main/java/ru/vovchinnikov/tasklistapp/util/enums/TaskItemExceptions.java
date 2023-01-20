package ru.vovchinnikov.tasklistapp.util.enums;

import ru.vovchinnikov.tasklistapp.util.TaskListException;

public enum TaskItemExceptions implements TaskListException {
    TASK_NOT_FOUND("Задача не найдена", -201),
    TASK_NOT_CREATED("Не удалось создать задачу", -202),
    TASK_NOT_UPDATED("Не удалось изменить задачу", -203);

    private final String message;
    private final int code;

    TaskItemExceptions(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage(){
        return this.message;
    }

    @Override
    public int getCode(){
        return this.code;
    }
}
