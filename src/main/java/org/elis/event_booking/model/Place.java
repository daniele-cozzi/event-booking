package org.elis.event_booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "road", length = 30, nullable = false)
    private String road;

    @Column(name = "city", length = 20, nullable = false)
    private String city;

    @Column(name = "province", length = 2, nullable = false)
    private String province;

    @Column(name = "postal_code", length = 5, nullable = false)
    private String postalCode;

    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<EventDate> eventDates = new ArrayList<>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Zone> zones = new ArrayList<>();
}
