package ru.vovchinnikov.tasklistapp.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vovchinnikov.tasklistapp.dto.TaskItemDTO;
import ru.vovchinnikov.tasklistapp.dto.TaskListErrorDTO;
import ru.vovchinnikov.tasklistapp.models.TaskItem;
import ru.vovchinnikov.tasklistapp.models.TaskListItem;
import ru.vovchinnikov.tasklistapp.services.TaskItemsService;
import ru.vovchinnikov.tasklistapp.services.UsersService;
import ru.vovchinnikov.tasklistapp.util.errors.UserNotFoundError;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<List<TaskListItem>> findAllByUser(@PathVariable("userId") String userId){
        return ResponseEntity.ok(taskItemsService.findByUser(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<HttpStatus> createTask(@PathVariable("userId") String userId,
                                                 @RequestBody TaskItemDTO taskItemDTO){

        taskItemsService.createTask(userId, taskItemDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<TaskListErrorDTO> handleException(UserNotFoundError exception){
        TaskListErrorDTO response = new TaskListErrorDTO(exception.getMessage(), exception.getCode());

        return ResponseEntity.badRequest().body(response);

    }
}
