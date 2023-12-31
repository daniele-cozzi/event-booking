package org.elis.event_booking.service.implementations;

import jakarta.transaction.Transactional;
import org.elis.event_booking.dto.CategoryDTO;
import org.elis.event_booking.dto.EventDTO;
import org.elis.event_booking.dto.mapper.CategoryMapper;
import org.elis.event_booking.dto.mapper.EventMapper;
import org.elis.event_booking.exceptions.EntityCreationException;
import org.elis.event_booking.exceptions.EntityDeletionException;
import org.elis.event_booking.exceptions.EntityDuplicateException;
import org.elis.event_booking.exceptions.EntityNotFoundException;
import org.elis.event_booking.model.Category;
import org.elis.event_booking.model.Event;
import org.elis.event_booking.repository.CategoryRepository;
import org.elis.event_booking.repository.EventRepository;
import org.elis.event_booking.service.definitions.CategoryService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        Category category = CategoryMapper.INSTANCE.categoryDTOToCategory(categoryDTO);

        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new EntityDuplicateException("category", "name", categoryDTO.getName());
        } catch (Exception e) {
            throw new EntityCreationException("category");
        }

        return CategoryMapper.INSTANCE.categoryToCategoryDTO(category);
    }

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return CategoryMapper.INSTANCE.categoriesToCategoryDTOs(categories);
    }

    @Override
    public CategoryDTO findById(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("category", "id", id));
        return CategoryMapper.INSTANCE.categoryToCategoryDTO(category);
    }

    @Override
    public CategoryDTO findByName(String name) {
        Category category = categoryRepository.findByNameIgnoreCase(name).orElseThrow(() -> new EntityNotFoundException("category", "name", name));
        return CategoryMapper.INSTANCE.categoryToCategoryDTO(category);
    }

    @Override
    public Category addEvent(long id, Event event) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("category", "id", id));

        category.getEvents().add(event);

        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new EntityCreationException("category");
        }
    }

    @Override
    public List<EventDTO> getEvents(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("category", "id", id));
        return EventMapper.INSTANCE.eventsToEventDTOs(category.getEvents());
    }

    @Override
    public boolean remove(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("category", "id", id));

        // I remove the category from the events
        category.getEvents().forEach(e -> e.getCategories().remove(category));

        eventRepository.saveAll(category.getEvents());

        try {
            categoryRepository.delete(category);
        } catch (Exception e) {
            throw new EntityDeletionException("category", "id", id);
        }

        return true;
    }
}
