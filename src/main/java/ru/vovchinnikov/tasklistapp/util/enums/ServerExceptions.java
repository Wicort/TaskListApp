package ru.vovchinnikov.tasklistapp.util.enums;

import ru.vovchinnikov.tasklistapp.util.TaskListException;

public enum ServerExceptions implements TaskListException {
    SERVER_EXCEPTION("Ошибка в работе сервиса или сервис недоступен. Повторите попытку позже.", -1);

    private final String message;
    private final int code;

    ServerExceptions(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
