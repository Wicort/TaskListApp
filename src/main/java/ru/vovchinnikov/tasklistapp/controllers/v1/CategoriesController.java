package ru.vovchinnikov.tasklistapp.controllers.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vovchinnikov.tasklistapp.dto.CategoryDTO;
import ru.vovchinnikov.tasklistapp.dto.TaskListErrorDTO;
import ru.vovchinnikov.tasklistapp.services.CategoriesService;
import ru.vovchinnikov.tasklistapp.util.BindingResultUtil;
import ru.vovchinnikov.tasklistapp.util.errors.CategoryNotCreatedError;
import ru.vovchinnikov.tasklistapp.util.errors.TaskNotCreatedError;
import ru.vovchinnikov.tasklistapp.util.errors.UserNotFoundError;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Ovchinnikov Viktor
 */
@RestController
@RequestMapping("/api/v1/categories")
public class CategoriesController {
    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CategoryDTO>> findAllByUser(@PathVariable("userId") String userId){
        return ResponseEntity.ok(categoriesService.findAllByUserId(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<HttpStatus> createCategory(@PathVariable("userId") String userId,
                                                     @RequestBody @Valid CategoryDTO category,
                                                     BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new CategoryNotCreatedError(BindingResultUtil.getBindingResultErrorsList(bindingResult));
        }
        categoriesService.create(userId, category);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<TaskListErrorDTO> handleException(UserNotFoundError error){
        TaskListErrorDTO response = new TaskListErrorDTO(error.getMessage(), error.getCode());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler
    public ResponseEntity<TaskListErrorDTO> handleException(CategoryNotCreatedError error){
        TaskListErrorDTO response = new TaskListErrorDTO(error.getMessage(), error.getCode());

        return ResponseEntity.badRequest().body(response);
    }
}
