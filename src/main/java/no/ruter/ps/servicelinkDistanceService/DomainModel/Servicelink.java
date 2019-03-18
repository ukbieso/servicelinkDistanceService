package no.ruter.ps.servicelinkDistanceService.DomainModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Servicelink {

    private String serviceLinkID;
    private String poslistID;
    private float distance;
    private List<Linklocation> linklocations;
    private FromPoint fromPoint;
    private ToPoint toPoint;

}
