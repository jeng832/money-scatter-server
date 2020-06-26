package com.kakaopay.moneyscatter.manager;

import com.kakaopay.moneyscatter.dao.entity.Scatter;
import com.kakaopay.moneyscatter.dao.entity.ScatterId;
import com.kakaopay.moneyscatter.dao.repository.ScatterRepository;
import com.kakaopay.moneyscatter.exception.NotValidScatterException;
import com.kakaopay.moneyscatter.model.vo.PickVo;
import com.kakaopay.moneyscatter.model.vo.ScatterVo;
import com.kakaopay.moneyscatter.provider.TokenProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScatterManagerTest {

    @Autowired
    ScatterManager scatterManager;

    @Autowired
    private ScatterRepository scatterRepository;

    @Test
    public void testSaveScatter() {
        String roomId = "R1";
        long time = System.currentTimeMillis();
        String token = TokenProvider.getToken(roomId, 1, 100, 2, time);
        ScatterVo vo = scatterManager.makeScatter(1, 2, 100, roomId, time);
        scatterManager.saveScatter(vo);

        Scatter scatter = scatterRepository.findById(new ScatterId(roomId, token)).get();
        Assert.assertEquals(1, scatter.getUserId());
        Assert.assertEquals(roomId, scatter.getScatterId().getRoomId());
        Assert.assertEquals(100, scatter.getTotalCost());
        Assert.assertEquals(2, scatter.getNumberOfPerson());
    }

    @Test
    public void testGetScatter() {
        String roomId = "R1";
        long time = System.currentTimeMillis();
        String token = TokenProvider.getToken(roomId, 1, 100, 2, time);
        ScatterVo vo = scatterManager.makeScatter(1, 2, 100, roomId, time);
        scatterManager.saveScatter(vo);

        ScatterVo scatter = scatterManager.getScatter(roomId, token);
        Assert.assertEquals(1, scatter.getUserId());
        Assert.assertEquals(roomId, scatter.getRoomId());
        Assert.assertEquals(100, scatter.getCost());
        Assert.assertEquals(2, scatter.getPerson());
        Assert.assertEquals(2, scatter.getPicks().length);

    }

    @Test
    public void testGetScatterNotExist() {
        ScatterVo scatter = scatterManager.getScatter("R", "TOK");
        Assert.assertNull(scatter);
    }

    @Test
    public void testGetScatterPick() {
        String roomId = "R1";
        long time = System.currentTimeMillis();
        String token = TokenProvider.getToken(roomId, 1, 100, 2, time);
        ScatterVo vo = scatterManager.makeScatter(1, 2, 100, roomId, time);
        scatterManager.saveScatter(vo);
        List<PickVo> picks = scatterManager.getScatterPick(roomId, token);
        Assert.assertEquals(2, picks.size());

    }

    @Test
    public void testMakeScatter() {
        long time = System.currentTimeMillis();
        ScatterVo scatter = scatterManager.makeScatter(1, 3, 100, "R1", time);
        Assert.assertNotNull(scatter);
        Assert.assertEquals(1, scatter.getUserId());
        Assert.assertEquals(3, scatter.getPerson());
        Assert.assertEquals(100, scatter.getCost());
        Assert.assertEquals("R1", scatter.getRoomId());
        Assert.assertEquals(time, scatter.getRequestedTime());
    }

    @Test
    public void testMakeScatterWithNullRoomId() {
        long time = System.currentTimeMillis();
        ScatterVo scatter = scatterManager.makeScatter(1, 3, 100, null, time);
        Assert.assertNotNull(scatter);
        Assert.assertEquals(1, scatter.getUserId());
        Assert.assertEquals(3, scatter.getPerson());
        Assert.assertEquals(100, scatter.getCost());
        Assert.assertEquals(time, scatter.getRequestedTime());
    }

    @Test
    public void testPickCountFromMakeScatter() {
        long time = System.currentTimeMillis();
        ScatterVo scatter = scatterManager.makeScatter(1, 3, 100, "R1", time);
        Assert.assertEquals(3, scatter.getPicks().length);
    }

    @Test
    public void testPickMoneyFromMakeScatter() {
        long time = System.currentTimeMillis();
        ScatterVo scatter = scatterManager.makeScatter(1, 3, 100, "R1", time);
        PickVo[] picks = scatter.getPicks();
        int sum = 0;
        for (PickVo pick : picks) {
            sum += pick.getCost();
        }
        Assert.assertEquals(100, sum);
    }

    @Test
    public void testPickMoney() {
        long time = System.currentTimeMillis();
        int total = 1000;
        ScatterVo scatter = scatterManager.makeScatter(1, 2, total, "R1", time);
        scatterManager.saveScatter(scatter);
        int sum = 0;
        sum += scatterManager.pickMoney(2, scatter);
        sum += scatterManager.pickMoney(3, scatter);
        Assert.assertEquals(total, sum);
    }

    @Test(expected = NotValidScatterException.class)
    public void testPickMoneyOver() {
        long time = System.currentTimeMillis();
        int total = 1000;
        ScatterVo scatter = scatterManager.makeScatter(1, 2, total, "R1", time);
        scatterManager.saveScatter(scatter);

        scatterManager.pickMoney(2, scatter);
        scatterManager.pickMoney(3, scatter);
        scatterManager.pickMoney(4, scatter);
    }

    @Test
    public void testIsPickExpired() {
        long time = System.currentTimeMillis();
        ScatterVo scatter = scatterManager.makeScatter(1, 3, 100, "R1", time);

        Assert.assertFalse(scatterManager.isPickExpired(scatter, time + (10 * 60 * 1000) - 1));
        Assert.assertTrue(scatterManager.isPickExpired(scatter, time + (10 * 60 * 1000) + 1));
    }

    @Test
    public void testIsSearchExpired() {
        long time = System.currentTimeMillis();
        ScatterVo scatter = scatterManager.makeScatter(1, 3, 100, "R1", time);

        Assert.assertFalse(scatterManager.isSearchExpired(scatter, time + (7 * 24 * 60 * 60 * 1000) - 1));
        Assert.assertTrue(scatterManager.isSearchExpired(scatter, time + (7 * 24 * 60 * 60 * 1000) + 1));
    }
}

