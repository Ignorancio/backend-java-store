package com.example.store.category.infrastructure.mapper;

import com.example.store.category.domain.Category;
import com.example.store.category.infrastructure.dto.CategoryDTO;
import com.example.store.category.infrastructure.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    CategoryEntity categoryToCategoryEntity(Category category);

    Category categoryEntityToCategory(CategoryEntity categoryEntity);

    Category categoryDTOToCategory(CategoryDTO categoryDTO);
}
