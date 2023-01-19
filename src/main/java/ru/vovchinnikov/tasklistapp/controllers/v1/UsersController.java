package ru.vovchinnikov.tasklistapp.controllers.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vovchinnikov.tasklistapp.dto.TaskListErrorDTO;
import ru.vovchinnikov.tasklistapp.dto.UserDTO;
import ru.vovchinnikov.tasklistapp.services.UsersService;
import ru.vovchinnikov.tasklistapp.util.BindingResultUtil;
import ru.vovchinnikov.tasklistapp.util.errors.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Ovchinnikov Viktor
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll(){
        return ResponseEntity.ok(usersService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getOne(@PathVariable("id") String id){
        return ResponseEntity.ok(usersService.findByUUID(id));
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO userDTO,
                                             BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new UserNotCreatedError(BindingResultUtil.getBindingResultErrorsList(bindingResult));
        }
        usersService.createUser(userDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<TaskListErrorDTO> handleException(UserNotFoundError exception){
        TaskListErrorDTO response = new TaskListErrorDTO(exception.getMessage(), exception.getCode());

        return ResponseEntity.badRequest().body(response);

    }

    @ExceptionHandler
    public ResponseEntity<TaskListErrorDTO> handleException(UserNotCreatedError exception){
        TaskListErrorDTO response = new TaskListErrorDTO(exception.getMessage(), exception.getCode());

        return ResponseEntity.badRequest().body(response);

    }

    @ExceptionHandler
    public ResponseEntity<TaskListErrorDTO> handleException(ServerError exception){
        TaskListErrorDTO response = new TaskListErrorDTO(exception.getMessage(), exception.getCode());

        return ResponseEntity.internalServerError().body(response);

    }

    @ExceptionHandler
    public ResponseEntity<TaskListErrorDTO> handleException(UserAlereadyExistsError exception){
        TaskListErrorDTO response = new TaskListErrorDTO(exception.getMessage(), exception.getCode());

        return ResponseEntity.internalServerError().body(response);

    }

    @ExceptionHandler
    public ResponseEntity<TaskListErrorDTO> handleException(UserEmailAlereadyExistsError exception){
        TaskListErrorDTO response = new TaskListErrorDTO(exception.getMessage(), exception.getCode());

        return ResponseEntity.internalServerError().body(response);

    }

}
