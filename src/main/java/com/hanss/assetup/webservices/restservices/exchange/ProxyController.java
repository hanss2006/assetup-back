package com.hanss.assetup.webservices.restservices.exchange;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin("*")
public class ProxyController {
    static final String URL_CANDLES =
    "https://iss.moex.com/iss/engines/stock/markets/shares/securities/%1$s/candles.json?interval=24&from=%2$s&till=%3$s";

    @GetMapping("/api/proxy/{ticker}/{startDate}/{endDate}")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(@PathVariable String ticker,
                                                              @PathVariable String startDate,
                                                              @PathVariable String endDate) {
        if (ticker.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method and default Headers.
        String result = restTemplate.getForObject(
                String.format(URL_CANDLES, ticker,startDate, endDate),
                String.class);

        if (result.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(result);
        }
    }
}
