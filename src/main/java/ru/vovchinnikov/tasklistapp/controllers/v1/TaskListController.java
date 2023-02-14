package ru.vovchinnikov.tasklistapp.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vovchinnikov.tasklistapp.dto.TaskItemDTO;
import ru.vovchinnikov.tasklistapp.dto.TaskListErrorDTO;
import ru.vovchinnikov.tasklistapp.services.TaskItemsService;
import ru.vovchinnikov.tasklistapp.util.BindingResultUtil;
import ru.vovchinnikov.tasklistapp.util.errors.task.TaskNotCreatedError;
import ru.vovchinnikov.tasklistapp.util.errors.task.TaskNotFoundError;
import ru.vovchinnikov.tasklistapp.util.errors.task.TaskNotUpdatedError;
import ru.vovchinnikov.tasklistapp.util.errors.user.UserNotFoundError;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Ovchinnikov Viktor
 */
@RestController
@RequestMapping("/api/v1/tasks")
@CrossOrigin
public class TaskListController {
    private final TaskItemsService taskItemsService;

    @Autowired
    public TaskListController(TaskItemsService taskItemsService) {
        this.taskItemsService = taskItemsService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskItemDTO>> findAllByUser(@PathVariable("userId") String userId){
        return ResponseEntity.ok(taskItemsService.findByUser(userId));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<HttpStatus> createTask(@PathVariable("userId") String userId,
                                                 @RequestBody @Valid TaskItemDTO taskItemDTO,
                                                 BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            throw new TaskNotCreatedError(BindingResultUtil.getBindingResultErrorsList(bindingResult));
        }
        taskItemsService.createTask(userId, taskItemDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<HttpStatus> updateTask(@PathVariable("taskId") String taskId,
                                                 @RequestBody @Valid TaskItemDTO taskItemDTO,
                                                 BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new TaskNotUpdatedError(BindingResultUtil.getBindingResultErrorsList(bindingResult));
        }
        taskItemsService.updateTask(taskId, taskItemDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskItemDTO> findUserTaskById(@PathVariable("taskId") String taskId){
        TaskItemDTO response = taskItemsService.findUserTaskById(taskId);
        return ResponseEntity.ok(response);
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

    @ExceptionHandler
    public ResponseEntity<TaskListErrorDTO> handleException(TaskNotFoundError error){
        TaskListErrorDTO response = new TaskListErrorDTO(error.getMessage(), error.getCode());

        return ResponseEntity.badRequest().body(response);
    }
}
