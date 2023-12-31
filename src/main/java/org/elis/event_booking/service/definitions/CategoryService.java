package org.elis.event_booking.service.definitions;

import org.elis.event_booking.dto.CategoryDTO;
import org.elis.event_booking.dto.EventDTO;
import org.elis.event_booking.model.Category;
import org.elis.event_booking.model.Event;

import java.util.List;

public interface CategoryService {
    CategoryDTO save(CategoryDTO categoryDTO);
    List<CategoryDTO> findAll();
    CategoryDTO findById(long id);
    CategoryDTO findByName(String name);
    Category addEvent(long id, Event event);
    List<EventDTO> getEvents(long id);
    boolean remove(long id);
}
