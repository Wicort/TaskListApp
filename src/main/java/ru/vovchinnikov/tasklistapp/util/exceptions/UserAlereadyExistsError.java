package ru.vovchinnikov.tasklistapp.util.exceptions;

import ru.vovchinnikov.tasklistapp.util.Const.UserExceptions;

/**
 * @author Ovchinnikov Viktor
 */

public class UserAlereadyExistsError extends TaskListError {

    public UserAlereadyExistsError() {
        super(UserExceptions.USER_ALEREADY_EXISTS);
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
