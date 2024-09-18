package com.java.codigo.examen_spring.client;

import com.java.codigo.examen_spring.aggregates.response.ReniecResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reniec-client", url = "https://api.apis.net.pe/v2/reniec/")
public interface ReniecCliente {
    @GetMapping("/dni")
    ReniecResponse getPersonReniec(@RequestParam("numero") String numero,
                                   @RequestHeader("Authorization") String authorization);
}
