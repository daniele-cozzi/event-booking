package org.elis.event_booking.service.definitions;

import org.elis.event_booking.dto.ZoneDTO;
import org.elis.event_booking.model.Zone;

import java.util.List;

public interface ZoneService {
    ZoneDTO save(ZoneDTO zoneDTO);
    List<ZoneDTO> findAll();
    ZoneDTO findById(long id);
    List<ZoneDTO> findByName(String name);
    boolean remove(long id);
}
