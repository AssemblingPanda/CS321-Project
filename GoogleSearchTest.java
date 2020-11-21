package com.github.assemblingpanda;

import org.junit.Test;

import static org.junit.Assert.*;

public class GoogleSearchTest {

    // Test consistency for getResRec() with korean restaurant in virginia zip code 22030
    //          cuisine: korean
    //          zip code: 22030
    @Test
    public void recTest0(){
        String testQuery = "korean 22030";
        String indicator = "XA Potential RestaurantX";
        double successRate;
        double consistentRate = 80.0;
        int passCount = 0;
        int runs = 100;
        for(int i = 0; i < runs; i++) {
            String recommendations = GoogleSearch.getResRec(testQuery);
            if(recommendations.contains(indicator)) passCount++;
        }
        successRate = (passCount/runs)*100;
        assertTrue(successRate >= consistentRate);
    }

    // Test consistency for getResRec() with american restaurant in alaska zip code 99712
    //          cuisine: american
    //          zip code: 99712
    @Test
    public void recTest1(){
        String testQuery = "american 99712";
        String indicator = "XA Potential RestaurantX";
        double successRate;
        double consistentRate = 80.0;
        int passCount = 0;
        int runs = 100;
        for(int i = 0; i < runs; i++) {
            String recommendations = GoogleSearch.getResRec(testQuery);
            if(recommendations.contains(indicator)) passCount++;
        }
        successRate = (passCount/runs)*100;
        assertTrue(successRate >= consistentRate);
    }

    // Test consistency for getResRec() with chinese restaurant in tennessee zip code 37130
    //          cuisine: chinese
    //          zip code: 37130
    @Test
    public void recTest2(){
        String testQuery = "chinese 37130";
        String indicator = "XA Potential RestaurantX";
        double successRate;
        double consistentRate = 80.0;
        int passCount = 0;
        int runs = 100;
        for(int i = 0; i < runs; i++) {
            String recommendations = GoogleSearch.getResRec(testQuery);
            if(recommendations.contains(indicator)) passCount++;
        }
        successRate = (passCount/runs)*100;
        assertTrue(successRate >= consistentRate);
    }

    // Test consistency for getResRec() with cafes in virginia zip code 20121
    //          type of restaurant: cafe
    //          zip code: 20121
    @Test
    public void recTest3(){
        String testQuery = "cafe 20121";
        String indicator = "XA Potential RestaurantX";
        double successRate;
        double consistentRate = 80.0;
        int passCount = 0;
        int runs = 100;
        for(int i = 0; i < runs; i++) {
            String recommendations = GoogleSearch.getResRec(testQuery);
            if(recommendations.contains(indicator)) passCount++;
        }
        successRate = (passCount/runs)*100;
        assertTrue(successRate >= consistentRate);
    }

    // Test consistency for getResRec() with diners in virginia zip code 22033
    //          type of restaurant: diner
    //          zip code: 22033
    @Test
    public void recTest4(){
        String testQuery = "diner 22033";
        String indicator = "XA Potential RestaurantX";
        double successRate;
        double consistentRate = 80.0;
        int passCount = 0;
        int runs = 100;
        for(int i = 0; i < runs; i++) {
            String recommendations = GoogleSearch.getResRec(testQuery);
            if(recommendations.contains(indicator)) passCount++;
        }
        successRate = (passCount/runs)*100;
        assertTrue(successRate >= consistentRate);
    }

    // Test consistency for getResRec() with pizza in new york zip code 10033
    //          type: pizza
    //          zip code: 10033
    @Test
    public void recTest5(){
        String testQuery = "pizza 10033";
        String indicator = "XA Potential RestaurantX";
        double successRate;
        double consistentRate = 80.0;
        int passCount = 0;
        int runs = 100;
        for(int i = 0; i < runs; i++) {
            String recommendations = GoogleSearch.getResRec(testQuery);
            if(recommendations.contains(indicator)) passCount++;
        }
        successRate = (passCount/runs)*100;
        assertTrue(successRate >= consistentRate);
    }

    // Test consistency for getResRec() with seafood in virginia zip code 20176
    //          type: seafood
    //          zip code: 20176
    @Test
    public void recTest6(){
        String testQuery = "seafood 20176";
        String indicator = "XA Potential RestaurantX";
        double successRate;
        double consistentRate = 80.0;
        int passCount = 0;
        int runs = 100;
        for(int i = 0; i < runs; i++) {
            String recommendations = GoogleSearch.getResRec(testQuery);
            if(recommendations.contains(indicator)) passCount++;
        }
        successRate = (passCount/runs)*100;
        assertTrue(successRate >= consistentRate);
    }

    // Test inconsistency for getResRec() with dim sum restaurant in virginia zip code 20176
    //          cuisine: dim sum
    //          zip code: 20176
    @Test
    public void recTest7() {
        String testQuery = "dim sum 20176";
        String indicator = "XA Potential RestaurantX";
        double failureRate;
        double inconsistentRate = 100.0;
        int failCount = 0;
        int runs = 100;
        for (int i = 0; i < runs; i++) {
            String recommendations = GoogleSearch.getResRec(testQuery);
            if (!recommendations.contains(indicator)) failCount++;
        }
        failureRate = (failCount/runs) * 100;
        assertTrue(failureRate == inconsistentRate);
    }

    // Test consistency for getRecentlyOpenedRes() with maryland zip code 21401
    //          zip code: 21401
    @Test
    public void newTest0(){
        String testQuery = "21401";
        String indicator = "XA Potential RestaurantX";
        double successRate;
        double consistentRate = 80.0;
        int passCount = 0;
        int runs = 100;
        for(int i = 0; i < runs; i++) {
            String recentlyOpened = GoogleSearch.getRecentlyOpenedRes(testQuery);
            if(recentlyOpened.contains(indicator)) passCount++;
        }
        successRate = (passCount/runs)*100;
        assertTrue(successRate >= consistentRate);
    }

    // Test consistency for getRecentlyOpenedRes() with california zip code 91101
    //          zip code: 91101
    @Test
    public void newTest1(){
        String testQuery = "91101";
        String indicator = "XA Potential RestaurantX";
        double successRate;
        double consistentRate = 80.0;
        int passCount = 0;
        int runs = 100;
        for(int i = 0; i < runs; i++) {
            String recentlyOpened = GoogleSearch.getRecentlyOpenedRes(testQuery);
            if(recentlyOpened.contains(indicator)) passCount++;
        }
        successRate = (passCount/runs)*100;
        assertTrue(successRate >= consistentRate);
    }

    // Test consistency for getRecentlyOpenedRes() with washington zip code 98112
    //          zip code: 98112
    @Test
    public void newTest2(){
        String testQuery = "98112";
        String indicator = "XA Potential RestaurantX";
        double successRate;
        double consistentRate = 80.0;
        int passCount = 0;
        int runs = 100;
        for(int i = 0; i < runs; i++) {
            String recentlyOpened = GoogleSearch.getRecentlyOpenedRes(testQuery);
            if(recentlyOpened.contains(indicator)) passCount++;
        }
        successRate = (passCount/runs)*100;
        assertTrue(successRate >= consistentRate);
    }

    // Test inconsistency for getRecentlyOpenedRes() with pennsylvania zip code 18458
    //          zip code: 18458
    @Test
    public void newTest3(){
        String testQuery = "18458";
        String indicator = "XA Potential RestaurantX";
        double successRate;
        double inconsistentRate = 0.0;
        int failCount = 0;
        int runs = 100;
        for (int i = 0; i < runs; i++) {
            String recentlyOpened = GoogleSearch.getRecentlyOpenedRes(testQuery);
            if (!recentlyOpened.contains(indicator)) failCount++;
        }
        successRate = (failCount/runs) * 100;
        assertTrue(successRate == inconsistentRate);
    }
}