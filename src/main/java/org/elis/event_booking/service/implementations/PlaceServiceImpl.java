package org.elis.event_booking.service.implementations;

import jakarta.transaction.Transactional;
import org.elis.event_booking.dto.PlaceDTO;
import org.elis.event_booking.dto.mapper.PlaceMapper;
import org.elis.event_booking.exceptions.EntityCreationException;
import org.elis.event_booking.exceptions.EntityDeletionException;
import org.elis.event_booking.exceptions.EntityNotFoundException;
import org.elis.event_booking.model.Place;
import org.elis.event_booking.repository.PlaceRepository;
import org.elis.event_booking.service.definitions.PlaceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public PlaceDTO save(PlaceDTO placeDTO) {
        Place place = PlaceMapper.INSTANCE.placeDTOToPlace(placeDTO);

        try {
            place = placeRepository.save(place);
        } catch (Exception e) {
            throw new EntityCreationException("place");
        }

        return PlaceMapper.INSTANCE.placeToPlaceDTO(place);
    }

    @Override
    public List<PlaceDTO> findAll() {
        List<Place> places = placeRepository.findAll();
        return PlaceMapper.INSTANCE.placesToPlaceDTOs(places);
    }

    @Override
    public PlaceDTO findById(long id) {
        Place place = placeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("place", "id", id));
        return PlaceMapper.INSTANCE.placeToPlaceDTO(place);
    }

    @Override
    public List<PlaceDTO> findByName(String name) {
        List<Place> places = placeRepository.findByNameIgnoreCase(name);
        return PlaceMapper.INSTANCE.placesToPlaceDTOs(places);
    }

    @Override
    public boolean remove(long id) {
        Place place = placeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("place", "id", id));

        try {
            placeRepository.delete(place);
        } catch (Exception e) {
            throw new EntityDeletionException("place", "id", id);
        }

        return true;
    }
}