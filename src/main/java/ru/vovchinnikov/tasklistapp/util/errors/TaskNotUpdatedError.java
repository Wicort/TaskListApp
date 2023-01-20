package ru.vovchinnikov.tasklistapp.util.errors;

import ru.vovchinnikov.tasklistapp.util.enums.TaskItemExceptions;

import java.util.List;

/**
 * @author Ovchinnikov Viktor
 */
public class TaskNotUpdatedError extends TaskListErrorWithDetail{

    public TaskNotUpdatedError(List<String> detail) {
        super(TaskItemExceptions.TASK_NOT_UPDATED, detail);
    }

    @Override
    public int getCode() {
        return getException().getCode();
    }
}
