package com.kakaopay.moneyscatter.service;

import com.kakaopay.moneyscatter.model.http.GetScatterResponseBody;
import com.kakaopay.moneyscatter.model.http.PutScatterRequestBody;

public interface MoneyScatterApiService {
    String putScatter(int userId, String roomId, PutScatterRequestBody body, long requestedTime);
    int postPick(int userId, String roomId, String token, long requestedTime);
    GetScatterResponseBody getScatter(int userId, String roomId, String token, long requestedTime);
}

