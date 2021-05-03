package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.net.wifi.WifiManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.utils.debug.Mock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UtilsInstrumentedTest {

    Context appContext;
    private UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());


    @Before
    public void useAppContext() throws Exception {
        // Context of the app under test.
        appContext = ApplicationProvider.getApplicationContext();
        //assertEquals("com.openclassrooms.realestatemanager", appContext.getPackageName());
    }

    @Test
    public void checkIsConnected() throws IOException, InterruptedException {
        device.executeShellCommand("svc wifi enable");
        device.executeShellCommand("svc data enable");
        Thread.sleep(1000);
        assertTrue(Utils.isInternetAvailable(appContext));
    }

    @Test
    public void checkWithNetworkOff() throws IOException, InterruptedException {
        device.executeShellCommand("svc wifi disable");
        device.executeShellCommand("svc data disable");
        Thread.sleep(1000);
        assertFalse(Utils.isInternetAvailable(appContext));
    }

    @After
    public void enableNetwork() throws IOException, InterruptedException {
        device.executeShellCommand("svc wifi enable");
        device.executeShellCommand("svc data enable");
        Thread.sleep(1000);
    }

}
