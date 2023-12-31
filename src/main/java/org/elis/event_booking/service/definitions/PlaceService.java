package org.elis.event_booking.service.definitions;

import org.elis.event_booking.dto.PlaceDTO;
import org.elis.event_booking.model.Place;

import java.util.List;

public interface PlaceService {
    PlaceDTO save(PlaceDTO placeDTO);
    List<PlaceDTO> findAll();
    PlaceDTO findById(long id);
    List<PlaceDTO> findByName(String name);
    boolean remove(long id);
}
