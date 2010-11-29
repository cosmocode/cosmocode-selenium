/**
 * Copyright 2010 CosmoCode GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cosmocode.selenium;

import org.junit.After;
import org.junit.Before;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

/**
 * Abstract base class for selenium testcases.
 * 
 * @author Tobias Sarnowski
 */
public abstract class CosmoCodeSeleniumTest extends SeleneseTestCase {

    public static final String CONFIG_SELENIUM_HOST = "selenium.host";
    public static final String CONFIG_SELENIUM_HOST_DEFAULT = "selenium.cosmo";

    public static final String CONFIG_SELENIUM_PORT = "selenium.port";
    public static final String CONFIG_SELENIUM_PORT_DEFAULT = "4444";

    public static final String CONFIG_SELENIUM_BROWSER = "selenium.browser";
    public static final String CONFIG_SELENIUM_BROWSER_DEFAULT = "*chrome";

    // in general helful constants
    public static final String ENTER = "\\13";


    /**
     * Overwrite this to change the default selenium remote control server.
     *
     * @return informations where the selenium remote control server is running.
     */
    public SeleniumServer getSeleniumServer() {
        return new SeleniumServer() {
            @Override
            public String getHost() {
                return System.getProperty(CONFIG_SELENIUM_HOST, CONFIG_SELENIUM_HOST_DEFAULT);
            }

            @Override
            public int getPort() {
                return Integer.parseInt(System.getProperty(CONFIG_SELENIUM_PORT, CONFIG_SELENIUM_PORT_DEFAULT));
            }
        };
    }

    /**
     * Overwrite to change the default browser.
     *
     * firefox = FireFox
     * chrome = FireFox with less security
     * iexplore = Internet Explorer
     * iehta = Internet Explorer with less security
     * safari = Safari
     * googlechrome = Google Chrome
     * opera = Opera
     *
     * @return which browser to use
     */
    public String getSeleniumBrowser() {
        return System.getProperty(CONFIG_SELENIUM_BROWSER, CONFIG_SELENIUM_BROWSER_DEFAULT);
    }

    /**
     * Overwrite to change the default timeout.
     *
     * @return how long to wait for new pages
     */
    public int getTimeoutInMs() {
        // selenium default
        return 30000;
    }

    /**
     * e.g. "https://admin.trip-to.com/"
     *
     * @return the test server's URL
     */
    public abstract String getTestServerUrl();

    /**
     * Don't forget to call super() when overriding this method.
     */
    @Before
    @Override
    public void setUp() {
        // lifecycle
        setUpTestServer();

        // start session with remote server
        final SeleniumServer seleniumServer = getSeleniumServer();
        selenium = new DefaultSelenium(
                seleniumServer.getHost(),
                seleniumServer.getPort(),
                getSeleniumBrowser(),
                getTestServerUrl()
        );
        selenium.start();

        // configure our own default timeout
        selenium.setTimeout(Integer.toString(getTimeoutInMs()));

        // lifecycle
        setUpWebsite();
    }

    /**
     * Don't forget to call super() when overriding this method.
     */
    @After
    @Override
    public void tearDown() {
        // lifecycle
        tearDownWebsite();

        // end the session
        selenium.stop();

        // lifecycle
        tearDownTestServer();
    }

    /**
     * Lifecycle: before selenium is set up.
     */
    public void setUpTestServer() {

    }

    /**
     * Lifecycle: after selenium is set up.
     */
    public void setUpWebsite() {

    }

    /**
     * Lifecycle: before selenium is teared down.
     */
    public void tearDownWebsite() {

    }

    /**
     * Lifecycle: after selenium is teared down.
     */
    public void tearDownTestServer() {

    }

    /**
     * Shortcut for selenium.waitForPageToLoad(String) with default timeout.
     */
    public void waitForPageToLoad() {
        selenium.waitForPageToLoad(Integer.toString(getTimeoutInMs()));
    }

    /**
     * Shortcut for selenium.waitForPageToLoad(String) with correctly typed timeout.
     *
     * @param timeoutInMs timeout for waiting
     */
    public void waitForPageToLoad(int timeoutInMs) {
        selenium.waitForPageToLoad(Integer.toString(timeoutInMs));
    }

    /**
     * Simulates the ENTER key pressed.
     *
     * @param locator where to trigger the event
     */
    public void pressEnter(String locator) {
        selenium.keyPress(locator, ENTER);
    }

    /**
     * Opens another page and waits for it.
     *
     * @param url the URL to open
     */
    public void openAndWaitForPageToLoad(String url) {
        selenium.open(url);
        waitForPageToLoad();
    }

    /**
     * Submits the given formular and waits for it.
     *
     * @param locator form identifier
     */
    public void submitAndWaitForPageToLoad(String locator) {
        selenium.submit(locator);
        waitForPageToLoad();
    }

}
