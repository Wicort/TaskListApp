package ru.vovchinnikov.tasklistapp.util.errors;

import ru.vovchinnikov.tasklistapp.util.TaskListException;
import ru.vovchinnikov.tasklistapp.util.enums.TaskItemExceptions;

import java.util.List;

/**
 * @author Ovchinnikov Viktor
 */
public class TaskNotCreatedError extends TaskListErrorWithDetail{

    public TaskNotCreatedError(List<String> detail) {
        super(TaskItemExceptions.TASK_NOT_CREATED, detail);
    }

    @Override
    public int getCode() {
        return getException().getCode();
    }
}
