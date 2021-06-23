package com.openclassrooms.realestatemanager;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

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

import static androidx.core.app.ActivityCompat.startActivityForResult;
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

    }

    @Test
    public void checkIsConnected() throws IOException, InterruptedException {
        //device.executeShellCommand("svc wifi enable");
        //device.executeShellCommand("svc data enable");
        Thread.sleep(2000);
        assertTrue(Utils.isInternetAvailable(appContext));
    }

    @Test
    public void checkWithNetworkOff() throws IOException, InterruptedException, UiObjectNotFoundException {
        //device.executeShellCommand("svc wifi disable");
        //device.executeShellCommand("svc data disable");
        Thread.sleep(2000);
        assertFalse(Utils.isInternetAvailable(appContext));
    }

    @After
    public void enableNetwork() throws IOException, InterruptedException {
        //device.executeShellCommand("svc wifi enable");
        //device.executeShellCommand("svc data enable");

    }

}
