package ru.vovchinnikov.tasklistapp.util.enums;

import ru.vovchinnikov.tasklistapp.util.TaskListException;

public enum UserExceptions implements TaskListException {
    USER_NOT_FOUND("Пользователь с указанным идентификатором не найден", -101),
    USER_NOT_CREATED("Не удалось создать пользователя", -102),
    USER_ALREADY_EXISTS("Пользователь с таким именем уже зарегистрирован", -103),
    USER_EMAIL_ALREADY_EXISTS("Пользователь с указанным email уже зарегистрирован", -104);

    private final String message;
    private final int code;

    UserExceptions(String message, int code) {
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
