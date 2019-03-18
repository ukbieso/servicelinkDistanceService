package no.ruter.ps.servicelinkDistanceService.DomainModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionDto {
    private float lat;
    private float lon;
}
