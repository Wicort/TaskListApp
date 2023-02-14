package ru.vovchinnikov.tasklistapp.util.errors.category;

import ru.vovchinnikov.tasklistapp.util.enums.CategoryExceptions;
import ru.vovchinnikov.tasklistapp.util.enums.TaskItemExceptions;
import ru.vovchinnikov.tasklistapp.util.errors.TaskListError;

/**
 * @author Ovchinnikov Viktor
 */
public class CategoryNotFoundError extends TaskListError {

    public CategoryNotFoundError() {
        super(CategoryExceptions.CATEGORY_NOT_FOUND);
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
