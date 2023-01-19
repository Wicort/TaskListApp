package ru.vovchinnikov.tasklistapp.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vovchinnikov.tasklistapp.dto.TaskItemDTO;
import ru.vovchinnikov.tasklistapp.dto.UserDTO;
import ru.vovchinnikov.tasklistapp.models.TaskItem;
import ru.vovchinnikov.tasklistapp.models.TaskListItem;
import ru.vovchinnikov.tasklistapp.models.User;
import ru.vovchinnikov.tasklistapp.repositories.TaskItemsRepository;
import ru.vovchinnikov.tasklistapp.util.errors.ServerError;
import ru.vovchinnikov.tasklistapp.util.errors.UserNotFoundError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Ovchinnikov Viktor
 */
@Service
@Transactional(readOnly = true)
public class TaskItemsService {
    private final TaskItemsRepository taskItemsRepository;
    private final UsersService usersService;
    private final ModelMapper modelMapper;

    @Autowired
    public TaskItemsService(TaskItemsRepository taskItemsRepository, UsersService usersService, ModelMapper modelMapper) {
        this.taskItemsRepository = taskItemsRepository;
        this.usersService = usersService;
        this.modelMapper = modelMapper;
    }

    public List<TaskListItem> findByUser(String userId) {
        User user;
        try {
            user = usersService.findUserById(UUID.fromString(userId));
        } catch (IllegalArgumentException e) {
            throw new UserNotFoundError();
        }
        return taskItemsRepository.findAllByOwner(user);
    }

    @Transactional(readOnly = false)
    public void createTask(String userId, TaskItemDTO taskItemDTO) {
        User user;
        try {
            user = usersService.findUserById(UUID.fromString(userId));
        } catch (IllegalArgumentException e) {
            throw new UserNotFoundError();
        }

        TaskItem taskItem = convertToTaskItem(taskItemDTO);
        taskItem.setOwner(user);

        taskItemsRepository.save(taskItem);

    }



    private TaskItemDTO convertToDto(TaskItem taskItem){
        return modelMapper.map(taskItem, TaskItemDTO.class);
    }

    private TaskItem convertToTaskItem(TaskItemDTO taskItemDTO){
        if (taskItemDTO.getId() != null) {
            Optional<TaskItem> taskListItem = taskItemsRepository.findById(taskItemDTO.getId());
            if (taskListItem.isPresent()) {
                return taskListItem.get();
            }
        }
        return enrichTaskItem(modelMapper.map(taskItemDTO, TaskItem.class));
    }

    private TaskItem enrichTaskItem(TaskItem taskItem){
        taskItem.setId(UUID.randomUUID());
        taskItem.setCreatedAt(LocalDateTime.now());
        return taskItem;
    }
}
