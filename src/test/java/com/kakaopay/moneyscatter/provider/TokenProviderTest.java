package com.kakaopay.moneyscatter.provider;

import org.junit.Assert;
import org.junit.Test;

public class TokenProviderTest {

    @Test
    public void testGetToken() {
        String token = TokenProvider.getToken("R1", 1,1,1,1);

        Assert.assertEquals(3, token.length());
    }

    @Test
    public void testGetTokenFromBigNumber() {
        String token = TokenProvider.getToken("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,Long.MAX_VALUE);

        Assert.assertEquals(3, token.length());
    }

    @Test
    public void testGetTokenFromSmallNumber() {
        String token = TokenProvider.getToken("a", Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Long.MIN_VALUE);

        Assert.assertEquals(3, token.length());
    }
    @Test
    public void testGetTokenFromNullRoomId() {
        String token = TokenProvider.getToken(null, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,Long.MAX_VALUE);

        Assert.assertEquals(3, token.length());
    }

    @Test
    public void testGetTokenFromEmptyRoomId() {
        String token = TokenProvider.getToken("", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,Long.MAX_VALUE);

        Assert.assertEquals(3, token.length());
    }
}
