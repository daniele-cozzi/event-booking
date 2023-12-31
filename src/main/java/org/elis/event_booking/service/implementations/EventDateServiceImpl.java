package org.elis.event_booking.service.implementations;

import jakarta.transaction.Transactional;
import org.elis.event_booking.dto.EventDateDTO;
import org.elis.event_booking.dto.mapper.EventDateMapper;
import org.elis.event_booking.exceptions.EntityCreationException;
import org.elis.event_booking.exceptions.EntityDeletionException;
import org.elis.event_booking.exceptions.EntityNotFoundException;
import org.elis.event_booking.exceptions.EventDateOverlappingException;
import org.elis.event_booking.model.Event;
import org.elis.event_booking.model.EventDate;
import org.elis.event_booking.model.Place;
import org.elis.event_booking.repository.EventDateRepository;
import org.elis.event_booking.repository.EventRepository;
import org.elis.event_booking.repository.PlaceRepository;
import org.elis.event_booking.service.definitions.EventDateService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class EventDateServiceImpl implements EventDateService {

    private final EventDateRepository eventDateRepository;
    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;

    public EventDateServiceImpl(EventDateRepository eventDateRepository, EventRepository eventRepository, PlaceRepository placeRepository) {
        this.eventDateRepository = eventDateRepository;
        this.eventRepository = eventRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public EventDateDTO save(EventDateDTO eventDateDTO) {
        EventDate eventDate = EventDateMapper.INSTANCE.eventDateDTOToEventDate(eventDateDTO);

        Event event = eventRepository.findById(eventDate.getEvent().getId()).orElseThrow(() -> new EntityNotFoundException("event", "id", eventDateDTO.getEvent_id()));
        Place place = placeRepository.findById(eventDate.getPlace().getId()).orElseThrow(() -> new EntityNotFoundException("place", "id", eventDateDTO.getPlace_id()));

        if (eventDate.getStart().isAfter(eventDate.getEnd())) throw new IllegalArgumentException("The start date cannot be after the end date.");

        // I'm looking for dates that overlap with the specified time range for a given place
        List<EventDate> eventDates = eventDateRepository.checkForDateOverlaps(eventDate.getPlace(), eventDate.getStart(), eventDate.getEnd());

        if (!eventDates.isEmpty())
            throw new EventDateOverlappingException(eventDate.getStart(), eventDate.getEnd(), eventDate.getPlace().getId());

        eventDate.setEvent(event);
        eventDate.setPlace(place);

        try {
            eventDate = eventDateRepository.save(eventDate);
        } catch (Exception e) {
            throw new EntityCreationException("eventDate");
        }

        return EventDateMapper.INSTANCE.eventDateToEventDateDTO(eventDate);
    }

    @Override
    public List<EventDateDTO> findAll() {
        List<EventDate> eventDates = eventDateRepository.findAll();
        return EventDateMapper.INSTANCE.eventDatesToEventDateDTOs(eventDates);
    }

    @Override
    public EventDateDTO findById(long id) {
        EventDate eventDate = eventDateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("eventDate", "id", id));
        return EventDateMapper.INSTANCE.eventDateToEventDateDTO(eventDate);
    }

    @Override
    public List<EventDateDTO> findAllByPlaceId(long id) {
        Place place = placeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("place", "id", id));

        List<EventDate> eventDates = eventDateRepository.findAllByPlaceId(id);
        return EventDateMapper.INSTANCE.eventDatesToEventDateDTOs(eventDates);
    }

    @Override
    public List<EventDateDTO> findAllByEventId(long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("event", "id", id));

        List<EventDate> eventDates = eventDateRepository.findAllByEventId(id);
        return EventDateMapper.INSTANCE.eventDatesToEventDateDTOs(eventDates);
    }

    @Override
    public List<EventDateDTO> findByDate(LocalDate date) {
        List<EventDate> eventDateDTOs = eventDateRepository.findByDate(date);
        return EventDateMapper.INSTANCE.eventDatesToEventDateDTOs(eventDateDTOs);
    }

    @Override
    public boolean remove(long id) {
        EventDate eventDate = eventDateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("eventDate", "id", id));

        try {
            eventDateRepository.delete(eventDate);
        } catch (Exception e) {
            throw new EntityDeletionException("eventDate", "id", id);
        }

        return true;
    }
}
