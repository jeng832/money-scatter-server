package com.kakaopay.moneyscatter.dao.entity;

import com.kakaopay.moneyscatter.dao.repository.PickRepository;
import com.kakaopay.moneyscatter.dao.repository.ScatterRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EntityTest {

    @Autowired
    ScatterRepository scatterRepository;
    @Autowired
    PickRepository pickRepository;
    @Autowired
    TestEntityManager entityManager;


    @Test
    public void testScatterIdGenerate() {
        Pick pick = new Pick();
        entityManager.persistAndFlush(pick);
        ScatterId id = new ScatterId("ROOM1", "TOK");
        Scatter scatter = new Scatter(id, 111, 3, 100, new Timestamp(System.currentTimeMillis()));
        entityManager.persistAndFlush(scatter);

        Assert.assertNotNull(scatter.getScatterId());
    }
    @Test
    public void testPickIdGenerate() {
        Pick pick = new Pick(10);
        entityManager.persistAndFlush(pick);
        ScatterId id = new ScatterId("ROOM1", "TOK");
        Scatter scatter = new Scatter(id, pick, 111, 3, 100, new Timestamp(System.currentTimeMillis()));
        entityManager.persistAndFlush(scatter);

        Assert.assertNotNull(pick.getPickId());
    }

    @Test
    public void testProperPickFromScatter() {
        Pick pick = new Pick();
        entityManager.persistAndFlush(pick);
        ScatterId id = new ScatterId("ROOM1", "TOK");
        Scatter scatter = new Scatter(id, pick, 111, 3, 100, new Timestamp(System.currentTimeMillis()));
        entityManager.persistAndFlush(scatter);

        Assert.assertTrue(scatterRepository.findById(scatter.getScatterId()).get().getPicks().contains(pick));
     //   Assert.assertNull(pickRepository.findById(pick.getPickId()).get());
    }
}
