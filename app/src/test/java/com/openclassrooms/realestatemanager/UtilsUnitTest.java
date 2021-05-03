package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class UtilsUnitTest {

    @Test
    public void checkConvertDollarToEuro () {
        assertEquals(81, Utils.convertDollarToEuro(100));
    }


    @Test
    public void checkConvertEuroToDollar(){
        assertEquals(119,Utils.convertEuroToDollar(100));
    }

}