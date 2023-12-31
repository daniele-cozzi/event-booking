package org.elis.event_booking.service.implementations;

import jakarta.transaction.Transactional;
import org.elis.event_booking.dto.PlaceDTO;
import org.elis.event_booking.dto.ZoneDTO;
import org.elis.event_booking.dto.mapper.PlaceMapper;
import org.elis.event_booking.dto.mapper.ZoneMapper;
import org.elis.event_booking.exceptions.EntityCreationException;
import org.elis.event_booking.exceptions.EntityDeletionException;
import org.elis.event_booking.exceptions.EntityDuplicateException;
import org.elis.event_booking.exceptions.EntityNotFoundException;
import org.elis.event_booking.model.Place;
import org.elis.event_booking.model.Zone;
import org.elis.event_booking.repository.PlaceRepository;
import org.elis.event_booking.repository.ZoneRepository;
import org.elis.event_booking.service.definitions.PlaceService;
import org.elis.event_booking.service.definitions.ZoneService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;
    private final PlaceRepository placeRepository;

    public ZoneServiceImpl(ZoneRepository zoneRepository, PlaceRepository placeRepository) {
        this.zoneRepository = zoneRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public ZoneDTO save(ZoneDTO zoneDTO) {
        Zone zone = ZoneMapper.INSTANCE.zoneDTOToZone(zoneDTO);
        Place place = placeRepository.findById(zone.getPlace().getId()).orElseThrow(() -> new EntityNotFoundException("place", "id", zoneDTO.getPlace_id()));

        // I check that there is no zone with the same name in the same place
        if (zoneRepository.findByNameIgnoreCaseAndPlaceId(zone.getName(), zone.getPlace().getId()).isPresent())
            throw new EntityDuplicateException("zone", "name", zone.getName(), "place_id", zone.getPlace().getId());

        zone.setPlace(place);

        try {
            zone = zoneRepository.save(zone);
        } catch (Exception e) {
            throw new EntityCreationException("zone");
        }

        return ZoneMapper.INSTANCE.zoneToZoneDTO(zone);
    }

    @Override
    public List<ZoneDTO> findAll() {
        List<Zone> zones = zoneRepository.findAll();
        return ZoneMapper.INSTANCE.zonesToZoneDTOs(zones);
    }

    @Override
    public ZoneDTO findById(long id) {
        Zone zone = zoneRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("zone", "id", id));
        return ZoneMapper.INSTANCE.zoneToZoneDTO(zone);
    }

    @Override
    public List<ZoneDTO> findByName(String name) {
        List<Zone> zones = zoneRepository.findByNameIgnoreCase(name);
        return ZoneMapper.INSTANCE.zonesToZoneDTOs(zones);
    }

    @Override
    public boolean remove(long id) {
        Zone zone = zoneRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("zone", "id", id));

        try {
            zoneRepository.delete(zone);
        } catch (Exception e) {
            throw new EntityDeletionException("zone", "id", id);
        }

        return true;
    }
}
