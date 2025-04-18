package com.energy.community.restapi.controller;
import com.energy.community.restapi.model.EnergyData;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@RestController
@RequestMapping("/energy")
public class EnergyController {
    @GetMapping("/current")
    public EnergyData getCurrent() {
        return new EnergyData(LocalDateTime.now(), 45.2, 39.1);
    }
    @GetMapping("/historical")
    public List<EnergyData> getHistorical(@RequestParam String start, @RequestParam String end) {
        return IntStream.range(0, 5)
                .mapToObj(i -> new EnergyData(LocalDateTime.now().minusHours(i), 30 + i, 25 + i))
                .collect(Collectors.toList());
    }
}