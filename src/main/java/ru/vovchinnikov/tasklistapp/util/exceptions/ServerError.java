package ru.vovchinnikov.tasklistapp.util.exceptions;

import ru.vovchinnikov.tasklistapp.util.Const.ServerExceptions;
import ru.vovchinnikov.tasklistapp.util.TaskListException;

/**
 * @author Ovchinnikov Viktor
 */
public class ServerError extends TaskListError{
    public ServerError() {
        super(ServerExceptions.SERVER_EXCEPTION);
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
