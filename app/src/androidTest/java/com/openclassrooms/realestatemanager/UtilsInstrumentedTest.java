package com.openclassrooms.realestatemanager;

import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;


import androidx.test.core.app.ApplicationProvider;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;


import com.openclassrooms.realestatemanager.utils.Utils;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


import java.io.IOException;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UtilsInstrumentedTest {

    Context appContext;
    private UiDevice device = UiDevice.getInstance(getInstrumentation());

    Instrumentation instrumentation;
    //private final UiAutomation  ui = instrumentation.getUiAutomation();

    @Before
    public void useAppContext() {
        // Context of the app under test.
        appContext = ApplicationProvider.getApplicationContext();
    }


    @Test
    public void checkIsConnected() throws IOException, InterruptedException {
        device.executeShellCommand("svc wifi enable");
        device.executeShellCommand("svc data enable");
        Thread.sleep(2000);
        assertTrue(Utils.isInternetAvailable(appContext));
    }

    @Test
    public void checkWithNetworkOff() throws IOException, InterruptedException {
        device.executeShellCommand("svc wifi disable");
        device.executeShellCommand("svc data disable");
        Thread.sleep(2000);
        assertFalse(Utils.isInternetAvailable(appContext));
    }

    @After
    public void enableNetwork() throws IOException, InterruptedException {
        device.executeShellCommand("svc wifi enable");
        device.executeShellCommand("svc data enable");
    }


    /*public static void setAirplaneMode(boolean enable) // don't work on api 19
    {
        if ((enable ? 1 : 0) == Settings.System.getInt(getInstrumentation().getContext().getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0))
        {
            return;
        }
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        device.openQuickSettings();
        // Find the text of your language
        BySelector description = By.desc("Airplane mode");
        // Need to wait for the button, as the opening of quick settings is animated.
        device.wait(Until.hasObject(description), 500);
        device.findObject(description).click();
        getInstrumentation().getContext().sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }*/
}
