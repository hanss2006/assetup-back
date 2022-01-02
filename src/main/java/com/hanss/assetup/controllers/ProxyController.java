package com.hanss.assetup.controllers;

import com.hanss.assetup.security.SecuredRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProxyController implements SecuredRestController {
    static final String URL_CANDLES =
    "https://iss.moex.com/iss/engines/stock/markets/shares/securities/%1$s/candles.json?interval=24";
    static final String URL_FROM =
            "&from=%1$s";
    static final String URL_TILL =
            "&till=%1$s";

    @GetMapping("/proxy/{ticker}")
    public ResponseEntity<?> getCandlesFromMoex(@PathVariable String ticker,
                                                              @RequestParam(required = false) String startDate,
                                                              @RequestParam(required = false) String endDate) {

        if (ticker==null) {
            return ResponseEntity.badRequest().body(null);
        }

        String url = String.format(URL_CANDLES, ticker);
        if (startDate!=null){
            url += String.format(URL_FROM, startDate);
        }
        if (endDate!=null){
            url += String.format(URL_TILL, endDate);
        }

        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method and default Headers.
        String result = restTemplate.getForObject(url, String.class);

        if (result.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(result);
        }
    }
}
