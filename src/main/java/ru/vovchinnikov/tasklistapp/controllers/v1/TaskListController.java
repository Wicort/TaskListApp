package ru.vovchinnikov.tasklistapp.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vovchinnikov.tasklistapp.dto.TaskItemDTO;
import ru.vovchinnikov.tasklistapp.dto.TaskListErrorDTO;
import ru.vovchinnikov.tasklistapp.services.TaskItemsService;
import ru.vovchinnikov.tasklistapp.services.UsersService;
import ru.vovchinnikov.tasklistapp.util.BindingResultUtil;
import ru.vovchinnikov.tasklistapp.util.errors.TaskNotCreatedError;
import ru.vovchinnikov.tasklistapp.util.errors.TaskNotUpdatedError;
import ru.vovchinnikov.tasklistapp.util.errors.UserNotFoundError;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Ovchinnikov Viktor
 */
@RestController
@RequestMapping("/tasks")
public class TaskListController {
    private final TaskItemsService taskItemsService;
    private final UsersService usersService;

    @Autowired
    public TaskListController(TaskItemsService taskItemsService, UsersService usersService) {
        this.taskItemsService = taskItemsService;
        this.usersService = usersService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<TaskItemDTO>> findAllByUser(@PathVariable("userId") String userId){
        return ResponseEntity.ok(taskItemsService.findByUser(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<HttpStatus> createTask(@PathVariable("userId") String userId,
                                                 @RequestBody @Valid TaskItemDTO taskItemDTO,
                                                 BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            throw new TaskNotCreatedError(BindingResultUtil.getBindingResultErrorsList(bindingResult));
        }
        taskItemsService.createTask(userId, taskItemDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{userId}/{taskId}")
    public ResponseEntity<HttpStatus> updateTask(@PathVariable("userId") String userId,
                                                 @PathVariable("taskId") String taskId,
                                                 @RequestBody @Valid TaskItemDTO taskItemDTO,
                                                 BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new TaskNotUpdatedError(BindingResultUtil.getBindingResultErrorsList(bindingResult));
        }
        taskItemsService.updateTask(taskId, taskItemDTO, userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    public ResponseEntity<TaskListErrorDTO> handleException(UserNotFoundError error){
        TaskListErrorDTO response = new TaskListErrorDTO(error.getMessage(), error.getCode());

        return ResponseEntity.badRequest().body(response);

    }

    @ExceptionHandler
    public ResponseEntity<TaskListErrorDTO> handleException(TaskNotCreatedError error){
        TaskListErrorDTO response = new TaskListErrorDTO(error.getMessage(), error.getCode());

        return ResponseEntity.badRequest().body(response);

    }
}
