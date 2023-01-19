package ru.vovchinnikov.tasklistapp.util.errors;

import ru.vovchinnikov.tasklistapp.util.enums.UserExceptions;

/**
 * @author Ovchinnikov Viktor
 */

public class UserAlereadyExistsError extends TaskListError {

    public UserAlereadyExistsError() {
        super(UserExceptions.USER_ALREADY_EXISTS);
    }

    @Override
    public String getMessage() {
        return getException().getMessage();
    }

    @Override
    public int getCode() {
        return getException().getCode();
    }
}