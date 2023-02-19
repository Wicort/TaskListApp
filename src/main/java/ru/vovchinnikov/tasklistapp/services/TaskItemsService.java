package ru.vovchinnikov.tasklistapp.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vovchinnikov.tasklistapp.dto.TaskItemDTO;
import ru.vovchinnikov.tasklistapp.models.TaskItem;
import ru.vovchinnikov.tasklistapp.models.User;
import ru.vovchinnikov.tasklistapp.repositories.TaskItemsRepository;
import ru.vovchinnikov.tasklistapp.util.errors.task.TaskNotFoundError;

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
        String ownerId = "";
        if (taskItemDTO.getOwnerId() != null) {
            ownerId = taskItemDTO.getOwnerId().toString();
        }
        if ("".equals(ownerId)) {
            ownerId = userId;
        }

        taskItemDTO.setOwnerId(UUID.fromString(ownerId));

        if (taskItemDTO.getAuthorId() == null)
            taskItemDTO.setAuthorId(taskItemDTO.getOwnerId());

        if (taskItemDTO.getEditorId() == null)
            taskItemDTO.setEditorId(taskItemDTO.getOwnerId());

        TaskItem taskItem = convertToTaskItem(taskItemDTO);

        taskItemsRepository.save(taskItem);
    }

    public TaskItemDTO findUserTaskById(String taskIdStr){
        Optional<TaskItem> item = taskItemsRepository.findOneById(getTaskIdByStr(taskIdStr));

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
    public void updateTask(String strTaskId, TaskItemDTO taskItemDTO) {
        UUID taskId = getTaskIdByStr(strTaskId);

        TaskItem task = findTaskById(taskId);

        enrichTaskItem(task, taskItemDTO);
        task.setUpdatedAt(LocalDateTime.now());

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

    public TaskItemDTO convertToDto(TaskItem taskItem){
        TaskItemDTO dto = modelMapper.map(taskItem, TaskItemDTO.class);

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

        if (taskItem.getUpdatedAt() == null)
            taskItem.setUpdatedAt(LocalDateTime.now());

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

        if (dto.getEditorId() != null)
            item.setEditor(usersService.findUserById(dto.getEditorId()));
        else
            item.setEditor(item.getOwner());

        if (dto.getAuthorId() != null)
            item.setAuthor(usersService.findUserById(dto.getAuthorId()));
        else
            item.setAuthor(item.getOwner());

    }


}
