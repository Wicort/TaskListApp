package ru.vovchinnikov.tasklistapp.util.errors;

import ru.vovchinnikov.tasklistapp.util.enums.CategoryExceptions;
import ru.vovchinnikov.tasklistapp.util.enums.UserExceptions;

import javax.xml.catalog.CatalogException;

/**
 * @author Ovchinnikov Viktor
 */

public class CategoryAlereadyExistsError extends TaskListError {

    public CategoryAlereadyExistsError() {
        super(CategoryExceptions.CATEGORY_ALREADY_EXISTS);
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
