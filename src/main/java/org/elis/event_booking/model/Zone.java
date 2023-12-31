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
@Table(
        name = "zone",
        uniqueConstraints = { @UniqueConstraint( columnNames = {"name", "place_id"} ) }
)
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();
}
