package org.elis.event_booking.dto.mapper;

import org.elis.event_booking.dto.ZoneDTO;
import org.elis.event_booking.model.Zone;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ZoneMapper {
    ZoneMapper INSTANCE = Mappers.getMapper(ZoneMapper.class);

    @Mapping(source = "place.id", target = "place_id")
    ZoneDTO zoneToZoneDTO(Zone zone);

    List<ZoneDTO> zonesToZoneDTOs(List<Zone> zones);

    @InheritInverseConfiguration
    Zone zoneDTOToZone(ZoneDTO zoneDTO);
}
