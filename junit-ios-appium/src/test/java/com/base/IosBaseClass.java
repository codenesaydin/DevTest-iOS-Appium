package com.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public abstract class IosBaseClass
{
    private static final Logger logger = LoggerFactory.getLogger(IosBaseClass.class);

    private static final int WEB_DRIVER_WAIT_TIMEOUT = 15;

    public AppiumDriver driver;
    public WebDriverWait webDriverWait;

    @BeforeEach
    public void init()
    {
        driver = createIosDriver();
    }

    @AfterEach
    public void close()
    {
        if (Objects.nonNull(driver))
        {
            driver.closeApp();
            driver.quit();
            driver = null;
        }
    }

    public AppiumDriver createIosDriver()
    {
        int counter = 0;

        try
        {
            File appDir = new File("/opt/");

            File app = new File(appDir.getCanonicalPath(), "");

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "13.2");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Iphone XR");
            capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "600");
            capabilities.setCapability(IOSMobileCapabilityType.USE_PREBUILT_WDA, "true");
            capabilities.setCapability(MobileCapabilityType.UDID, "00008020-001675360CD1002E");
            capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
            capabilities.setCapability(IOSMobileCapabilityType.NATIVE_INSTRUMENTS_LIB, "true");
            capabilities.setCapability(IOSMobileCapabilityType.LOCATION_SERVICES_ENABLED, true);
            capabilities.setCapability(IOSMobileCapabilityType.LOCATION_SERVICES_AUTHORIZED, true);
            capabilities.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, "false");
            capabilities.setCapability(IOSMobileCapabilityType.IOS_INSTALL_PAUSE, "8000");
            capabilities.setCapability("waitForAppScript", "$.delay(5000); $.acceptAlert();");
            capabilities.setCapability("waitForQuiescence", "false");

            URL url = null;

            try
            {
                url = new URL("http://127.0.0.1:4723/wd/hub");
                driver = new IOSDriver(url, capabilities);

                webDriverWait = new WebDriverWait(driver, WEB_DRIVER_WAIT_TIMEOUT);
                setIOSDriverTimeout();
                setLocation();
            }
            catch (Exception e)
            {
                logger.error("Failed to create driver with URL: " + url);
            }
            counter++;
        }
        catch (Exception ex)
        {
        }

        while ((driver == null) && (counter < 10)) ;

        if (Objects.isNull(driver)) throw new NullPointerException("IOS Driver is null...");

        return driver;
    }

    private void setIOSDriverTimeout()
    {
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void setLocation()
    {
        Location location = new Location(40.9617514, 29.1099772, 41);
        driver.setLocation(location);
    }

}
