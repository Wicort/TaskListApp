package ru.vovchinnikov.tasklistapp.util.errors;

import ru.vovchinnikov.tasklistapp.util.enums.UserExceptions;

import java.util.List;

/**
 * @author Ovchinnikov Viktor
 */

public class UserNotCreatedError extends TaskListErrorWithDetail {
    public UserNotCreatedError(List<String> detail) {
        super(UserExceptions.USER_NOT_CREATED, detail);
    }


    @Override
    public int getCode() {
        return getException().getCode();
    }
}
