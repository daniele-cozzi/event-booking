package org.elis.event_booking.dto.mapper;

import org.elis.event_booking.dto.PlaceDTO;
import org.elis.event_booking.model.Place;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PlaceMapper {
    PlaceMapper INSTANCE = Mappers.getMapper(PlaceMapper.class);

    PlaceDTO placeToPlaceDTO(Place place);

    List<PlaceDTO> placesToPlaceDTOs(List<Place> places);

    @InheritInverseConfiguration
    Place placeDTOToPlace(PlaceDTO placeDTO);
}
