package no.ruter.ps.servicelinkDistanceService.servicelinkController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ruter.ps.servicelinkDistanceService.servicelinkService.ServicelinkDistanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sl")
@AllArgsConstructor
@Slf4j
public class ServicelinkController {
    private ServicelinkDistanceService servicelinkDistanceService;

    @GetMapping()
    public String getDistance(){
        return servicelinkDistanceService.getServicelinkById("b335e335-ed32-43bc-9b0c-f47c6844f582");
    }
    @GetMapping("/q")
    public  void getJourneyId(){
        servicelinkDistanceService.getAll();
    }

    @GetMapping("/t")
    public  float getAll(){
        return servicelinkDistanceService.calcluateTotalDistnaceByJourneyPatternId("RUT:JourneyPattern:380-434");
    }

    @GetMapping("/{id}")
    public float calcaluateTotalDistance(@PathVariable(value = "id")String id){
        servicelinkDistanceService.calcluateTotalDistnaceByJourneyPatternId(id);
        return servicelinkDistanceService.calculateTotalDistnaceByJourneyId(id);

    }

    @GetMapping("/line/{lineCode}")
    public float getDistance(@PathVariable(value = "lineCode") String linecode){
        log.info("input data: {}", linecode);
        return servicelinkDistanceService.calculateTotalDistanceByLinePublicCod(linecode);
    }
}
