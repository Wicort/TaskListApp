package ru.vovchinnikov.tasklistapp.util.exceptions;

import ru.vovchinnikov.tasklistapp.util.Const.UserExceptions;

/**
 * @author Ovchinnikov Viktor
 */

public class UserNotFoundError extends TaskListError {

    public UserNotFoundError() {
        super(UserExceptions.USER_NOT_FOUND);
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
