package com.github.huitk;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

public class RestaurantTest {
    //Test equals method empty
    @Test
    public void testEquals() {
        Restaurant test1 = new Restaurant();
        Restaurant test2 = new Restaurant();
        assertTrue(test1.equals(test2));
    }

    //Test equals method same restaurant same location
    @Test
    public void testEquals1() {
        Restaurant test1 = new Restaurant();
        test1.setName("Burger King");
        test1.setAddress("An address");
        Restaurant test2 = new Restaurant();
        test2.setName("Burger King");
        test2.setAddress("An address");
        assertTrue(test1.equals(test2));
    }

    //Test equals method same restaurant different location
    @Test
    public void testEquals2() {
        Restaurant test1 = new Restaurant();
        test1.setName("Burger King");
        test1.setAddress("An address1");
        Restaurant test2 = new Restaurant();
        test2.setName("Burger King");
        test2.setAddress("An address2");
        assertFalse(test1.equals(test2));
    }

    //Test empty restaurant empty
    @Test
    public void testToString() {
        //Test to string
        Restaurant test = new Restaurant();
        String output = "Name: N/A" +
                "\nAddress: N/A" +
                "\nRatings: N/A" +
                "\nNumber of Reviews: N/A" +
                "\nCost: N/A" +
                "\nPhone Number: N/A" +
                "\nWebsite: N/A" +
                "\nToday's Operational Hours: N/A\n";
        assertEquals(output, test.toString());
    }

    //Test empty restaurant filled
    @Test
    public void testToString1() {
        //Test to string
        Restaurant test = new Restaurant();
        test.setName("A");
        test.setAddress("B");
        test.setNumStars("4.5");
        test.setNumReviews("200");
        test.setDollars("C");
        test.setPhoneNumber("(111) 111-1111");
        test.setWebsite("D.com");
        test.setOperationalHours("E");
        String output = "Name: A" +
                "\nAddress: B" +
                "\nRatings: 4.5/5.0" +
                "\nNumber of Reviews: 200" +
                "\nCost: C" +
                "\nPhone Number: (111) 111-1111" +
                "\nWebsite: D.com" +
                "\nToday's Operational Hours: E\n";
        assertEquals(output, test.toString());
    }

    //Test getName/setName method with restaurant name
    @Test
    public void testGetSetName() {
        Restaurant test = new Restaurant();
        test.setName("Sisters thai");
        assertEquals(test.getName(), "Sisters thai");
    }

    //Test getNumStars/setNumStars method with some value
    @Test
    public void testGetSetNumStars() {
        Restaurant test = new Restaurant();
        test.setNumStars("3.0");
        assertEquals(test.getNumStars(),"3.0/5.0");
    }

    //Test getAddress/setAddress method with "Address"
    @Test
    public void testGetSetAddress() {
        Restaurant test = new Restaurant();
        test.setAddress("Address");
        assertEquals(test.getAddress(), "Address");
    }

    //Test getNumReviews/setNumReviews with value
    @Test
    public void testGetSetNumReviews() {
        Restaurant test = new Restaurant();
        test.setNumReviews("5,000");
        assertEquals(test.getNumReviews(), "5,000");
    }

    //Test getPhoneNumber/setPhoneNumber method with phone number
    @Test
    public void testGetSetPhoneNumber() {
        Restaurant test = new Restaurant();
        test.setPhoneNumber("(874) 654-9978");
        assertEquals(test.getPhoneNumber(), "(874) 654-9978");
    }

    //Test getWebsite/setWebsite method with website
    @Test
    public void testGetSetWebsite() {
        Restaurant test = new Restaurant();
        test.setWebsite("website.com");
        assertEquals(test.getWebsite(), "website.com");
    }

    //Test getWebsite/setWebsite method with string not website
    @Test
    public void testGetSetWebsite1() {
        Restaurant test = new Restaurant();
        test.setWebsite("website");
        assertEquals(test.getWebsite(), "N/A");
    }

    //Test getOperationalHours/setOperationalHours method with hours
    @Test
    public void testGetSetOperationalHours() {
        Restaurant test = new Restaurant();
        test.setOperationalHours("Monday 9:00Tuesday 10:00Wednesday 11:00");
        assertEquals(test.getOperationalHours(), "\nMonday 9:00\nTuesday 10:00\nWednesday 11:00");
    }

    //Test getDollars/setDollars method
    @Test
    public void testGetSetDollars() {
        Restaurant test = new Restaurant();
        test.setDollars("$");
        assertEquals(test.getDollars(), "$");
    }
}