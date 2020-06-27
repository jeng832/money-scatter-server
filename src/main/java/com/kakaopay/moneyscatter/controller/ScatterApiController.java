package com.kakaopay.moneyscatter.controller;

import com.kakaopay.moneyscatter.model.http.GetScatterResponseBody;
import com.kakaopay.moneyscatter.model.http.PutScatterRequestBody;
import com.kakaopay.moneyscatter.service.MoneyScatterApiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScatterApiController {
    private Logger logger = LoggerFactory.getLogger(ScatterApiController.class);

    private MoneyScatterApiService apiService;

    @Autowired
    public ScatterApiController(MoneyScatterApiService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping(value="/scatter", method= RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> putScatter(@RequestHeader(value = "X-USER-ID") int userId, @RequestHeader(value = "X-ROOM-ID") String roomId, @RequestBody PutScatterRequestBody body) {
        logger.info("PUT scatter userId: " + userId + ", roomId: " + roomId + ", body: " + body);
        long requestedTime = System.currentTimeMillis();
        String token = apiService.putScatter(userId, roomId, body, requestedTime);
        return new ResponseEntity<String>(token, HttpStatus.OK);
    }

    @RequestMapping(value="/pick/token/{token}", method= RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Integer> postPick(@RequestHeader(value = "X-USER-ID") int userId, @RequestHeader(value = "X-ROOM-ID") String roomId, @PathVariable String token) {
        logger.info("POST scatter userId: " + userId + ", roomId: " + roomId + ", token: " + token);
        long requestedTime = System.currentTimeMillis();
        int pickedMoney = apiService.postPick(userId, roomId, token, requestedTime);
        return new ResponseEntity<Integer>(pickedMoney, HttpStatus.OK);
    }

    @RequestMapping(value="/scatter/token/{token}", method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<GetScatterResponseBody> getScatter(@RequestHeader(value = "X-USER-ID") int userId, @RequestHeader(value = "X-ROOM-ID") String roomId, @PathVariable String token) {
        logger.info("GET scatter userId: " + userId + ", roomId: " + roomId + ", token: " + token);
        long requestedTime = System.currentTimeMillis();
        GetScatterResponseBody responseBody = apiService.getScatter(userId, roomId, token, requestedTime);
        return new ResponseEntity<GetScatterResponseBody>(responseBody, HttpStatus.OK);
    }
}
