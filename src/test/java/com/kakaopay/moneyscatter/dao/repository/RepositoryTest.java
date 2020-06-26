package com.kakaopay.moneyscatter.dao.repository;

import com.kakaopay.moneyscatter.dao.entity.Pick;
import com.kakaopay.moneyscatter.dao.entity.Scatter;
import com.kakaopay.moneyscatter.dao.entity.ScatterId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTest {
    @Autowired
    ScatterRepository scatterRepository;
    @Autowired
    PickRepository pickRepository;

    @Test
    public void testSaveAndFindScatter() {
        Pick pick = new Pick();
        pickRepository.save(pick);
        ScatterId id = new ScatterId("ROOM1", "TOK");
        Scatter scatter = new Scatter(id, pick, 111, 3, 100, new Timestamp(System.currentTimeMillis()));
        scatterRepository.save(scatter);
        Assert.assertEquals(scatter, scatterRepository.findById(id).get());
    }

    @Test
    public void testSaveAndFindPick() {
        Scatter scatter = new Scatter(new ScatterId("ROOM1", "TOK"), 111, 3, 100, new Timestamp(System.currentTimeMillis()));
        scatterRepository.save(scatter);
        Pick pick = new Pick(10);

        pickRepository.save(pick);
        Assert.assertEquals(pick, pickRepository.findById(pick.getPickId()).get());
    }
    @Test
    public void testSaveMultiplePick() {
        List<Pick> picks = new ArrayList<>();
        picks.add(new Pick(10));
        picks.add(new Pick(10));
        picks.add(new Pick(10));
        pickRepository.saveAll(picks);

        Scatter scatter = new Scatter(new ScatterId("ROOM1", "TOK"), picks, 111, 3, 100, new Timestamp(System.currentTimeMillis()));

        scatterRepository.saveAndFlush(scatter);

        Assert.assertEquals(3, scatterRepository.findById(scatter.getScatterId()).get().getPicks().size());
    }

}
