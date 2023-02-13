package ru.vovchinnikov.tasklistapp.util.enums;

import ru.vovchinnikov.tasklistapp.util.TaskListException;

public enum CategoryExceptions implements TaskListException {
    CATEGORY_NOT_FOUND("Список не найден", -301),
    CATEGORY_NOT_CREATED("Не удалось создать список", -302),
    CATEGORY_NOT_UPDATED("Не удалось изменить список", -303),
    CATEGORY_ALREADY_EXISTS("Список с таким именем уже существует", -304)
    ;


    private final String message;
    private final int code;

    CategoryExceptions(String message, int code) {
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
