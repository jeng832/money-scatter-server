package com.kakaopay.moneyscatter.provider;

import com.kakaopay.moneyscatter.model.vo.ScatterVo;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Random;

@Component
public class TokenProvider {

    private static char[] CHARACTER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                                       'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                                       'A', 'B', 'C', 'D', 'F', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static int MAX_NUMBER = CHARACTER.length * CHARACTER.length * CHARACTER.length;  //238,328

    @NonNull
    public static String getToken(String roomId, int userId, int cost, int persons, long time) {
        int hash = Objects.hash(roomId, userId, cost, persons, time);
        Random rand = new Random(hash);
        int seed = rand.nextInt(MAX_NUMBER);

        int[] charIdx = new int[3];

        for (int i = 0; i < charIdx.length; i++) {
            charIdx[i] = seed % CHARACTER.length;
            seed /= CHARACTER.length;
        }

        char[] token = new char[3];
        for (int i = 0; i < charIdx.length; i++) {
            token[i] = CHARACTER[charIdx[i]];
        }

        return new String(token);
    }
}
