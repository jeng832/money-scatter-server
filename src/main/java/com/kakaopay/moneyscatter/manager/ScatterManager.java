package com.kakaopay.moneyscatter.manager;

import com.kakaopay.moneyscatter.dao.entity.Pick;
import com.kakaopay.moneyscatter.dao.entity.Scatter;
import com.kakaopay.moneyscatter.dao.entity.ScatterId;
import com.kakaopay.moneyscatter.dao.repository.PickRepository;
import com.kakaopay.moneyscatter.dao.repository.ScatterRepository;
import com.kakaopay.moneyscatter.exception.NotValidScatterException;
import com.kakaopay.moneyscatter.model.vo.PickVo;
import com.kakaopay.moneyscatter.model.vo.ScatterVo;
import com.kakaopay.moneyscatter.provider.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Component
public class ScatterManager {

    private ScatterRepository scatterRepository;
    private PickRepository pickRepository;

    private final int VALID_TIME_FOR_PICK = 10 * 60 * 1000;
    private final int VALID_TIME_FOR_SEARCH = 7 * 24 * 60 * 60 * 1000;

    @Autowired
    public ScatterManager(ScatterRepository scatterRepository, PickRepository pickRepository) {
        this.scatterRepository = scatterRepository;
        this.pickRepository = pickRepository;
    }

    public void saveScatter(@NonNull ScatterVo vo) {
        PickVo[] picks = vo.getPicks();
        List<Pick> pickList = new ArrayList<>();
        for (PickVo pick : picks) {
            pickList.add(new Pick(pick.getCost()));
        }
        this.pickRepository.saveAll(pickList);

        String roomId = vo.getRoomId();
        String token = vo.getToken();
        Integer userId = vo.getUserId();
        int numberOfPerson = vo.getPerson();
        int totalCost = vo.getCost();
        Timestamp createdTime = new Timestamp(vo.getRequestedTime());
        Scatter scatter =
                new Scatter(new ScatterId(roomId, token), pickList, userId, numberOfPerson, totalCost, createdTime);
        this.scatterRepository.save(scatter);
    }

    @Nullable
    public ScatterVo getScatter(String roomId, String token) {
        Scatter scatter = findScatter(roomId, token);
        if (scatter == null) return null;
        List<Pick> picks = scatter.getPicks();
        PickVo[] vos = new PickVo[scatter.getNumberOfPerson()];
        for (int i = 0; i < vos.length; i++) {
            Integer userId = picks.get(i).getUserId();
            int cost = picks.get(i).getEachCost();
            vos[i] = new PickVo(userId, cost, token);
        }

        return new ScatterVo(scatter.getUserId(), scatter.getScatterId().getRoomId(),
                        scatter.getScatterId().getToken(), scatter.getTotalCost(), scatter.getNumberOfPerson(),
                        scatter.getCreatedTime().getTime(), vos);
    }

    @NonNull
    public List<PickVo> getScatterPick(String roomId, String token) {
        ScatterVo scatter = getScatter(roomId, token);
        return (scatter == null) ? new ArrayList<>() : Arrays.asList(scatter.getPicks());
    }

    @NonNull
    public ScatterVo makeScatter(int userId, int nPerson, int cost, String roomId, long requestedTime) {
        String token = TokenProvider.getToken(roomId, userId, cost, nPerson, requestedTime);
        int[] moneys = divideMoney(cost, nPerson);

        PickVo[] picks = new PickVo[nPerson];
        for (int i = 0; i < moneys.length; i++) {
            picks[i] = new PickVo(moneys[i], token);
        }

        return new ScatterVo(userId, roomId, token, cost, nPerson, requestedTime, picks);
    }

    public boolean isPickExpired(ScatterVo scatter, long requestedTime) {
        return isExpired(scatter.getRequestedTime(), requestedTime, VALID_TIME_FOR_PICK);
    }

    public boolean isSearchExpired(ScatterVo scatter, long requestedTime) {
        return isExpired(scatter.getRequestedTime(), requestedTime, VALID_TIME_FOR_SEARCH);
    }

    public int pickMoney(int userId, ScatterVo scatter) {
        int cost = -1;
        PickVo[] vos = scatter.getPicks();
        for (int i = 0; i < vos.length; i++) {
            PickVo pick = vos[i];
            if (!pick.isPicked()) {
                pick.pick(userId);
                cost = pick.getCost();
                break;
            }
        }
        if (cost == -1) throw new NotValidScatterException("All Scatters are picked");
        List<Pick> picks = findScatter(scatter.getRoomId(), scatter.getToken()).getPicks();
        if (picks == null) throw new NotValidScatterException("Not Valid scatter");
        for (Pick pick : picks) {
            if (pick.getEachCost() == cost) {
                pick.setUserId(userId);
                pickRepository.save(pick);
            }
        }
        return cost;
    }

    private boolean isExpired(long startTime, long checkTime, long period) {
        return (checkTime - startTime) > period;
    }

    @Nullable
    private Scatter findScatter(String roomId, String token) {
        ScatterId id = new ScatterId(roomId, token);
        Optional<Scatter> opt = scatterRepository.findById(id);

        return opt.orElse(null);
    }

    private int[] divideMoney(int totalCost, int persons) {
        int[] money = new int[persons];
        Random rand = new Random(totalCost + persons);
        int sum = 0;
        for (int i = 0; i < persons - 1; i++) {
            money[i] = (totalCost / persons == 0) ? 0 : rand.nextInt(totalCost / persons);
            sum += money[i];
        }
        money[persons - 1] = totalCost - sum;
        shuffleArray(money);

        return money;
    }

    private void shuffleArray(int[] ar) {
        Random rand = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            // swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }


}
