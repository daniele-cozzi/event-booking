package org.elis.event_booking.dto.mapper;

import org.elis.event_booking.dto.CategoryDTO;
import org.elis.event_booking.model.Category;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO categoryToCategoryDTO(Category category);

    List<CategoryDTO> categoriesToCategoryDTOs(List<Category> categories);

    @InheritInverseConfiguration
    Category categoryDTOToCategory(CategoryDTO categoryDTO);
}
