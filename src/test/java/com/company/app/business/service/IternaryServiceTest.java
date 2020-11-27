package com.company.app.business.service;

import com.company.app.data.entity.Guest;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class IternaryServiceTest {

    private IternaryService classUnderTest;

    @Before
    public void onEveryTestCase(){
        classUnderTest = new IternaryService();
    }

    @Test
    public void generateInternaryNumber() {
        Map<Guest, String> guestStringMap = classUnderTest.generateInternaryNumber(null);
        assertEquals("Should be empty of length", 0,guestStringMap.size());
    }
}