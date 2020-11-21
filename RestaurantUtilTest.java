package com.github.huitk;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;

public class RestaurantUtilTest {
    //Test getRecommendations no nulls in the array
    @Test
    public void test0(){
        String criteria = "korean 22030";
        Object [] temp = RestaurantUtil.getRecommendations(criteria);
        assertNotNull(temp);
        for(int i = 0; i < temp.length; i++) {
            assertNotNull(temp[i]);
        }
    }

    //Test getRecommendations to make sure that there is at least 1 item in the array with no more than 3
    @Test
    public void test1(){
        String criteria = "korean 22030";
        Object [] temp = RestaurantUtil.getRecommendations(criteria);
        assertNotNull(temp);
        assertTrue(temp.length>0 && temp.length<=3);
    }

    //Test getRecommendations to make sure that there are no duplicates in the array
    @Test
    public void test2(){
        String criteria = "korean 22030";
        Object [] temp = RestaurantUtil.getRecommendations(criteria);
        assertNotNull(temp);
        Set<Object> items = new HashSet<>();
        for(int i = 0; i < temp.length; i++) {
            items.add(temp[i]);
        }
        assertEquals(temp.length, items.size());
    }

    //Test findRecentlyOpened return value not being null
    @Test
    public void test3(){
        String criteria = "21401";
        Object temp = RestaurantUtil.findRecentlyOpened(criteria);
        assertNotNull(temp);
    }

    //Test findRecentlyOpened return value being null
    @Test
    public void test4(){
        String criteria = "18458";
        Object temp = RestaurantUtil.findRecentlyOpened(criteria);
        assertNull(temp);
    }

    //Test getRecommendations to make sure that null is returned
    @Test
    public void test5(){
        String criteria = "dim sum 20176";
        Object [] temp = RestaurantUtil.getRecommendations(criteria);
        assertNull(temp);
    }
}