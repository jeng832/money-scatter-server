package com.kakaopay.moneyscatter.service;

import com.kakaopay.moneyscatter.exception.NotValidScatterException;
import com.kakaopay.moneyscatter.model.http.GetScatterResponseBody;
import com.kakaopay.moneyscatter.model.http.PutScatterRequestBody;
import com.kakaopay.moneyscatter.provider.TokenProvider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoneyScatterServiceTest {

    @Autowired
    MoneyScatterApiService service;

    @Test
    public void testPutScatter() {
        int userId = 1;
        String roomId = "A";
        PutScatterRequestBody request = new PutScatterRequestBody(1000, 10);
        long time = System.currentTimeMillis();
        String token = service.putScatter(userId, roomId, request, time);
        Assert.assertEquals(TokenProvider.getToken(roomId, userId, 1000, 10, time), token);
    }

    @Test
    public void testPostPick() {
        int userId = 1;
        String roomId = "A";
        int cost = 1000;
        int persons = 3;
        long time = System.currentTimeMillis();
        PutScatterRequestBody request = new PutScatterRequestBody(cost, persons);
        String token = service.putScatter(userId, roomId, request, time);
        int money = service.postPick(userId + 1, roomId, token, time);
        money += service.postPick(userId + 2, roomId, token, time);
        money += service.postPick(userId + 3, roomId, token, time);
        Assert.assertEquals(1000, money);
    }

    @Test(expected = NotValidScatterException.class)
    public void testPostPickNotValid() {
        int userId = 1;
        String roomId = "A";
        int cost = 1000;
        int persons = 3;
        long time = System.currentTimeMillis();
        PutScatterRequestBody request = new PutScatterRequestBody(cost, persons);
        String token = service.putScatter(userId, roomId, request, time);
        service.postPick(userId + 1, roomId, "", time);
    }

    @Test(expected = NotValidScatterException.class)
    public void testPostPickOwnScatter() {
        int userId = 1;
        String roomId = "A";
        int cost = 1000;
        int persons = 3;
        long time = System.currentTimeMillis();
        PutScatterRequestBody request = new PutScatterRequestBody(cost, persons);
        String token = service.putScatter(userId, roomId, request, time);
        service.postPick(userId, roomId, token, time);
    }

    @Test(expected = NotValidScatterException.class)
    public void testPostPickTwice() {
        int userId = 1;
        String roomId = "A";
        int cost = 1000;
        int persons = 3;
        long time = System.currentTimeMillis();
        PutScatterRequestBody request = new PutScatterRequestBody(cost, persons);
        String token = service.putScatter(userId, roomId, request, time);
        service.postPick(userId + 1, roomId, token, time);
        service.postPick(userId + 1, roomId, token, time);
    }

    @Test(expected = NotValidScatterException.class)
    public void testPostPickOtherRoom() {
        int userId = 1;
        String roomId = "A";
        int cost = 1000;
        int persons = 3;
        long time = System.currentTimeMillis();
        PutScatterRequestBody request = new PutScatterRequestBody(cost, persons);
        String token = service.putScatter(userId, roomId, request, time);
        service.postPick(userId + 1, "B", token, time);
    }

    @Test(expected = NotValidScatterException.class)
    public void testPostPickOverTime() {
        int userId = 1;
        String roomId = "A";
        int cost = 1000;
        int persons = 3;
        long time = System.currentTimeMillis();
        PutScatterRequestBody request = new PutScatterRequestBody(cost, persons);
        String token = service.putScatter(userId, roomId, request, time);
        service.postPick(userId + 1, "B", token, time + (10 * 60 * 1000) + 1);
    }

    @Test
    public void testGetScatter() {
        int userId = 1;
        String roomId = "A";
        PutScatterRequestBody request = new PutScatterRequestBody(1000, 10);
        long time = System.currentTimeMillis();
        String token = service.putScatter(userId, roomId, request, time);
        GetScatterResponseBody body = service.getScatter(userId, roomId, token, time);
        Assert.assertNotNull(body);
    }

    @Test(expected = NotValidScatterException.class)
    public void testGetScatterNotValid() {
        int userId = 1;
        String roomId = "A";
        PutScatterRequestBody request = new PutScatterRequestBody(1000, 10);
        long time = System.currentTimeMillis();
        String token = service.putScatter(userId, roomId, request, time);
        GetScatterResponseBody body = service.getScatter(userId, roomId, "", time);
    }

    @Test(expected = NotValidScatterException.class)
    public void testGetScatterTryToOthersScatter() {
        int userId = 1;
        String roomId = "A";
        PutScatterRequestBody request = new PutScatterRequestBody(1000, 10);
        long time = System.currentTimeMillis();
        String token = service.putScatter(userId, roomId, request, time);
        GetScatterResponseBody body = service.getScatter(userId + 1, roomId, token, time);
    }

    @Test(expected = NotValidScatterException.class)
    public void testGetScatterOverTime() {
        int userId = 1;
        String roomId = "A";
        PutScatterRequestBody request = new PutScatterRequestBody(1000, 10);
        long time = System.currentTimeMillis();
        String token = service.putScatter(userId, roomId, request, time);
        GetScatterResponseBody body = service.getScatter(userId, roomId, token, time + (7 * 24 * 60 * 60 * 1000) + 1);
    }
}
