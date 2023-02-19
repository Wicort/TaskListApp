package ru.vovchinnikov.tasklistapp.services;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vovchinnikov.tasklistapp.dto.CategoryDTO;
import ru.vovchinnikov.tasklistapp.dto.TaskItemDTO;
import ru.vovchinnikov.tasklistapp.models.Category;
import ru.vovchinnikov.tasklistapp.models.TaskItem;
import ru.vovchinnikov.tasklistapp.models.User;
import ru.vovchinnikov.tasklistapp.repositories.CategoriesRepository;
import ru.vovchinnikov.tasklistapp.util.errors.category.CategoryAlereadyExistsError;
import ru.vovchinnikov.tasklistapp.util.errors.category.CategoryNotFoundError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Ovchinnikov Viktor
 */
@Service
@Slf4j
public class CategoriesService {
    private final CategoriesRepository repository;
    private final UsersService usersService;
    private final TaskItemsService taskItemsService;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoriesService(CategoriesRepository repository,
                             UsersService usersService,
                             TaskItemsService taskItemsService,
                             ModelMapper modelMapper) {
        this.repository = repository;
        this.usersService = usersService;
        this.taskItemsService = taskItemsService;
        this.modelMapper = modelMapper;
    }

    public List<CategoryDTO> findAllByUserId(String userId){
        log.info("Getting all categories for user {}", userId);
        User user = usersService.findUserByStringId(userId);
        List<Category> categoryList = repository.findAllByOwner(user);
        return categoryList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void create(String userIdStr, CategoryDTO categoryDTO) {
        log.info("Creating new category. UserId: {}, object: {}", userIdStr, categoryDTO);
        User user = usersService.findUserByStringId(userIdStr);
        Optional<Category> existingCategory = repository.findOneByOwnerAndName(user, categoryDTO.getName());
        if (existingCategory.isPresent()) {
            log.error("Category already exists. UserId: {}, category: {}", userIdStr, categoryDTO);
            throw new CategoryAlereadyExistsError();
        }

        categoryDTO.setOwnerId(user.getId());

        Category category = convertToCategory(categoryDTO);
        repository.save(category);
    }

    private CategoryDTO convertToDto (Category category){
        CategoryDTO dto = modelMapper.map(category, CategoryDTO.class);
        dto.setOwnerId(category.getOwner().getId());
        return dto;
    }

    private Category convertToCategory(CategoryDTO dto){
        Category category = modelMapper.map(dto, Category.class);
        User user = usersService.findUserById(dto.getOwnerId());
        category.setOwner(user);

        return enrichCategory(category);
    }

    private Category enrichCategory(Category category) {
        if (category.getId() == null) {
            category.setId(UUID.randomUUID());
        }

        if (category.getCreatedAt() == null) {
            category.setCreatedAt(LocalDateTime.now());
        }
        return category;
    }

    public CategoryDTO findOneById(String categoryId) {
        log.info("Finding category by id {}", categoryId);
        try {
            Optional<Category> category = repository.findOneById(UUID.fromString(categoryId));
            if (category.isPresent()) {
                log.info("Found category for id {}: {}", categoryId, category.get());
                return modelMapper.map(category.get(), CategoryDTO.class);
            } else
                log.error("Can not found category for id {}", categoryId);
                throw new CategoryNotFoundError();
        } catch (IllegalArgumentException e) {
            log.error("CategoryId {} is not valid UUID", categoryId);
            throw new CategoryNotFoundError();
        }
    }

    public Category findById(UUID id){
        Optional<Category> category = repository.findById(id);
        if (category.isPresent()) {
            return category.get();
        } else
            throw new CategoryNotFoundError();
    }

    public Category findByStrId(String id){
        try {
            return findById(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new CategoryNotFoundError();
        }
    }

    public List<TaskItemDTO> getCategoryTasks(String categoryId) {
        Category category = findByStrId(categoryId);
        List<TaskItem> tasks = category.getTasks();

        return tasks.stream().map(taskItemsService::convertToDto).collect(Collectors.toList());
    }
}
