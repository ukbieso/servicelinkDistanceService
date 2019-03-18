package no.ruter.ps.servicelinkDistanceService.DomainModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Servicelinkj {
    private String journeyPatternId;
    private RouteDto routeDto;
    private List<Servicelink> servicelinks;

}
