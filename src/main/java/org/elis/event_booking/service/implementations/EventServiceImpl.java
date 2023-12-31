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
import org.elis.event_booking.service.definitions.EventService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public EventServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository, CategoryService categoryService) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    @Override
    public EventDTO save(EventDTO eventDTO) {
        Event event = EventMapper.INSTANCE.eventDTOToEvent(eventDTO);

        List<Category> eventCategories = new ArrayList<>();

        if (eventRepository.findByNameIgnoreCase(event.getName().trim()).isPresent())
            throw new EntityDuplicateException("event", "name", event.getName());

        for (String categoryName : eventDTO.getCategories()) {
            Category category = categoryRepository.findByNameIgnoreCase(categoryName).orElse(null);

            // If the category does not exist, I create it
            if (category == null) {
                CategoryDTO categoryDTO = new CategoryDTO(0L, categoryName);
                categoryDTO = categoryService.save(categoryDTO);
                category = CategoryMapper.INSTANCE.categoryDTOToCategory(categoryDTO);
            }

            // I add the event to the category
            category = categoryService.addEvent(category.getId(), event);

            // I add the category to the event
            eventCategories.add(category);
        }

        event.setCategories(eventCategories);

        try {
            event = eventRepository.save(event);
        } catch (Exception e) {
            throw new EntityCreationException("event");
        }

        return EventMapper.INSTANCE.eventToEventDTO(event);
    }

    @Override
    public List<EventDTO> findAll() {
        List<Event> events = eventRepository.findAll();
        return EventMapper.INSTANCE.eventsToEventDTOs(events);
    }

    @Override
    public EventDTO findById(long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("event", "id", id));
        return EventMapper.INSTANCE.eventToEventDTO(event);
    }

    @Override
    public EventDTO findByName(String name) {
        Event event = eventRepository.findByNameIgnoreCase(name).orElseThrow(() -> new EntityNotFoundException("event", "name", name));
        return EventMapper.INSTANCE.eventToEventDTO(event);
    }

    @Override
    public boolean remove(long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("event", "id", id));

        // I remove the event from the categories
        event.getCategories().forEach(c -> c.getEvents().remove(event));

        categoryRepository.saveAll(event.getCategories());

        try {
            eventRepository.delete(event);
        } catch (Exception e) {
            throw new EntityDeletionException("event", "id", id);
        }

        return true;
    }
}