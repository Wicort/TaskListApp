package ru.vovchinnikov.tasklistapp.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.vovchinnikov.tasklistapp.dto.CategoryDTO;
import ru.vovchinnikov.tasklistapp.models.Category;
import ru.vovchinnikov.tasklistapp.models.User;
import ru.vovchinnikov.tasklistapp.repositories.CategoriesRepository;
import ru.vovchinnikov.tasklistapp.util.errors.CategoryAlereadyExistsError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Ovchinnikov Viktor
 */
@Service
public class CategoriesService {
    private final CategoriesRepository repository;
    private final UsersService usersService;
    private final ModelMapper modelMapper;

    public CategoriesService(CategoriesRepository repository, UsersService usersService, ModelMapper modelMapper) {
        this.repository = repository;
        this.usersService = usersService;
        this.modelMapper = modelMapper;
    }

    public List<CategoryDTO> findAllByUserId(String userId){
        User user = usersService.findUserByStringId(userId);
        List<Category> categoryList = repository.findAllByOwner(user);
        return categoryList.stream()
                .map(category -> convertToDto(category))
                .collect(Collectors.toList());
    }

    public void create(String userIdStr, CategoryDTO categoryDTO) {
        User user = usersService.findUserByStringId(userIdStr);
        Optional<Category> existingCategory = repository.findOneByOwnerAndName(user, categoryDTO.getName());
        if (existingCategory.isPresent())
            throw new CategoryAlereadyExistsError();

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
}
