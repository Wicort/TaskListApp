package ru.vovchinnikov.tasklistapp.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vovchinnikov.tasklistapp.dto.TaskItemDTO;
import ru.vovchinnikov.tasklistapp.models.TaskItem;
import ru.vovchinnikov.tasklistapp.models.User;
import ru.vovchinnikov.tasklistapp.repositories.TaskItemsRepository;
import ru.vovchinnikov.tasklistapp.util.errors.TaskNotFoundError;
import ru.vovchinnikov.tasklistapp.util.errors.UserNotFoundError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<TaskItemDTO> findByUser(String userId) {
        User user = usersService.findUserByStringId(userId);
        return taskItemsRepository.findAllByOwner(user)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    public void createTask(String userId, TaskItemDTO taskItemDTO) {

        //todo check taskItem.ownerId is from users contact list. Else - Exception
        taskItemDTO.setId(null); // we can't create new Task with existing Id. Setting id to null

        String ownerId = taskItemDTO.getOwnerId().toString();
        if ("".equals(ownerId)) {
            ownerId = userId;
        }

        User owner = usersService.findUserByStringId(ownerId);

        TaskItem taskItem = convertToTaskItem(taskItemDTO);
        taskItem.setOwner(owner);
        taskItem.setAuthor(usersService.findUserByStringId(userId));

        taskItemsRepository.save(taskItem);

    }

    public TaskItemDTO findUserTaskById(String userIdStr, String taskIdStr){
        User user = usersService.findUserByStringId(userIdStr);

        Optional<TaskItem> item = taskItemsRepository.findOneByOwnerAndId(user, getTaskIdByStr(taskIdStr));

        if (item.isPresent())
            return modelMapper.map(item, TaskItemDTO.class);
        else
            throw new TaskNotFoundError();

    }

    public TaskItem findTaskById(UUID taskId){
        Optional<TaskItem> item = taskItemsRepository.findById(taskId);
        if (item.isEmpty()) throw new TaskNotFoundError();

        return item.get();
    }

    @Transactional(readOnly = false)
    public void updateTask(String strTaskId, TaskItemDTO taskItemDTO, String strUserId) {
        UUID taskId = getTaskIdByStr(strTaskId);

        TaskItem task = findTaskById(taskId);
        User editor = usersService.findUserByStringId(strUserId);

        enrichTaskItem(task, taskItemDTO);
        task.setUpdatedAt(LocalDateTime.now());
        task.setEditor(editor);

        taskItemsRepository.save(task);

    }

    private UUID getTaskIdByStr(String strTaskId) {
        UUID taskId;

        try {
            taskId = UUID.fromString(strTaskId);
        } catch (IllegalArgumentException e){
            throw new TaskNotFoundError();
        }
        return taskId;
    }

    private TaskItemDTO convertToDto(TaskItem taskItem){
        TaskItemDTO dto = modelMapper.map(taskItem, TaskItemDTO.class);
        dto.setOwnerId(taskItem.getOwner().getId());
        return dto;
    }

    private TaskItem convertToTaskItem(TaskItemDTO taskItemDTO){
        if (taskItemDTO.getId() != null) {
            // if we have TaskID, then loading it from DB
            Optional<TaskItem> taskListItem = taskItemsRepository.findById(taskItemDTO.getId());
            if (taskListItem.isPresent()) {
                return taskListItem.get();
            }
        }

        TaskItem item = modelMapper.map(taskItemDTO, TaskItem.class);
        if ((taskItemDTO.getOwnerId() != null) && (!"".equals(taskItemDTO.getOwnerId().toString()))) {
            User user = usersService.findUserByStringId(taskItemDTO.getOwnerId().toString());
            item.setOwner(user);
        }

        return enrichTaskItem(item);
    }

    private TaskItem enrichTaskItem(TaskItem taskItem){
        if (taskItem.getId() == null)
            taskItem.setId(UUID.randomUUID());

        if (taskItem.getCreatedAt() == null)
            taskItem.setCreatedAt(LocalDateTime.now());

        return taskItem;
    }

    private void enrichTaskItem(TaskItem item, TaskItemDTO dto){
        if ((!"".equals(dto.getName()) &&
                (!dto.getName().equals(item.getName()))))
            item.setName(dto.getName());

        if ((!"".equals(dto.getDescription()) &&
                (!dto.getDescription().equals(item.getDescription()))))
            item.setDescription(dto.getDescription());

        if ((dto.getPriority() != 0) &&
                (dto.getPriority() != item.getPriority()))
            item.setPriority(dto.getPriority());

        if ((dto.getDeadlineAt() != null) &&
                (dto.getDeadlineAt() != item.getDeadlineAt()))
            item.setDeadlineAt(dto.getDeadlineAt());

        if (dto.getReleasedAt() != null)
            item.setReleasedAt(dto.getReleasedAt());

        if (dto.getOwnerId() != null)
            item.setOwner(usersService.findUserById(dto.getOwnerId()));

    }


}
