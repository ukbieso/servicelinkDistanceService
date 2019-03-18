package no.ruter.ps.servicelinkDistanceService.DomainModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FromPoint {
    private String stopId;
    private String stopName;
    private String quayId;
    //private String arrivalTime;
    //private String departureTime;
    //private String quayOldId;
    //private String destinationDisplay;
    //private String destinationDisplayVia;
    private long order;
    //private PositionDto positionDto;
}
