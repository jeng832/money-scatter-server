package com.kakaopay.moneyscatter.service.impl;

import com.kakaopay.moneyscatter.exception.NotValidScatterException;
import com.kakaopay.moneyscatter.manager.ScatterManager;
import com.kakaopay.moneyscatter.model.http.GetScatterResponseBody;
import com.kakaopay.moneyscatter.model.http.Picker;
import com.kakaopay.moneyscatter.model.http.PutScatterRequestBody;
import com.kakaopay.moneyscatter.model.vo.PickVo;
import com.kakaopay.moneyscatter.model.vo.ScatterVo;
import com.kakaopay.moneyscatter.service.MoneyScatterApiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MoneyScatterApiServiceImpl implements MoneyScatterApiService {

    private Logger logger = LoggerFactory.getLogger(MoneyScatterApiServiceImpl.class);

    ScatterManager scatterManager;

    @Autowired
    public MoneyScatterApiServiceImpl(ScatterManager scatterManager) {
        this.scatterManager = scatterManager;
    }

    @Override
    public String putScatter(int userId, String roomId, PutScatterRequestBody request, long requestedTime) {
        // token 발급
        // 금액 분배
        // Pick에 저장
        int cost = request.getCost();
        int nPerson = request.getNumberOfPerson();
        ScatterVo scatter = scatterManager.makeScatter(userId, nPerson, cost, roomId, requestedTime);
        scatterManager.saveScatter(scatter);
        return scatter.getToken();
    }

    @Override
    public int postPick(int userId, String roomId, String token, long requestedTime) {
        // 분배 중 하나를 할당하고 금액을 return
        // 뿌리기 당 한 사용자는 한번만 받을 수 있음
        // 자기 자신이 뿌린건 받을 수 없음
        // 동일 대화방의 사용자만 받을 수 있다.
        ScatterVo scatter = scatterManager.getScatter(roomId, token);
        List<PickVo> picks = scatterManager.getScatterPick(roomId, token);
        logger.debug("picks: " + picks.toString());
        Set<Integer> userIds = new HashSet<>();
        picks.forEach(pick -> {
            userIds.add(pick.getUserId());
        });
        if (scatter == null) throw new NotValidScatterException("Scatter is NOT valid");
        if (userId == scatter.getUserId()) throw new NotValidScatterException("You cannot pick money scatter to yours");
        if (userIds.contains(userId)) throw new NotValidScatterException("You cannot pick scatter twice");
        if (!roomId.equals(scatter.getRoomId())) throw new NotValidScatterException("You cannot pick scatter from other room");
        if (scatterManager.isPickExpired(scatter, requestedTime)) throw new NotValidScatterException("Scatter is expired");
        int pickedMoney = scatterManager.pickMoney(userId, scatter);

        return pickedMoney;
    }

    @Override
    public GetScatterResponseBody getScatter(int userId, String roomId, String token, long requestedTime) {
        ScatterVo scatter = scatterManager.getScatter(roomId, token);
        List<PickVo> picks = scatterManager.getScatterPick(roomId, token);
        if (scatter == null) throw new NotValidScatterException("Scatter is NOT valid");
        if (userId != scatter.getUserId()) throw new NotValidScatterException("You cannot search others money scatter");
        if (scatterManager.isPickExpired(scatter, requestedTime)) throw new NotValidScatterException("Scatter is expired");
        if (scatterManager.isSearchExpired(scatter, requestedTime)) throw new NotValidScatterException("You cannot search older than 7 days");

        List<Picker> pickers = new ArrayList<>();
        int pickedCost = 0;
        for (PickVo pick : picks) {
            Integer uid = pick.getUserId();
            if (uid == null) continue;
            int eachCost = pick.getCost();
            pickers.add(new Picker(uid, eachCost));
            pickedCost += pick.getCost();
        }

        return new GetScatterResponseBody(scatter.getRequestedTime(), scatter.getCost(), pickedCost, pickers);
    }
}
