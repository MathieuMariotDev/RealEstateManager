package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class UtilsUnitTest {

    @Test
    public void checkConvertDollarToEuro() {
        assertEquals(81, Utils.convertDollarToEuro(100));
    }


    @Test
    public void checkConvertEuroToDollar() {
        assertEquals(119, Utils.convertEuroToDollar(100));
    }

    @Test
    public void checkIfGoodDateFormat() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse(Utils.getTodayDate());
        assertEquals(dateFormat.format(date), Utils.getTodayDate());
    }

    @Test
    public void checkIfGoodTimeMilis() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Long time = Utils.getTodayDateInLong("25/06/2021");
        Date date = new Date(time);
        Date dateExcepeted = dateFormat.parse("25/06/2021");
        assertEquals(dateExcepeted, date);
    }


}
