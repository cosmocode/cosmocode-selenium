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
     * Simulates the ENTER key pressed and waits for page to load.
     *
     * @param locator where to tigger the event
     * @since 1.2
     */
    public void pressEnterAndWait(String locator) {
        pressEnter(locator);
        waitForPageToLoad();
    }

    /**
     * Opens another page and waits for it to load.
     *
     * @param url the URL to open
     */
    public void openAndWait(String url) {
        selenium.open(url);
        waitForPageToLoad();
    }

    /**
     * Submits the given formular and waits for it to load.
     *
     * @param locator form identifier
     * @since 1.2
     */
    public void submitAndWait(String locator) {
        selenium.submit(locator);
        waitForPageToLoad();
    }

    /**
     * Asserts that an alert with the given message is present.
     *
     * @param message message
     */
    public void assertAlert(String message) {
        assertEquals(message, selenium.getAlert());
    }

    /**
     * Asserts that an alert with the given message is present.
     *
     * @param message message
     * @param failMessage message
     */
    public void assertAlert(String message, String failMessage) {
        assertEquals(failMessage, message, selenium.getAlert());
    }

    /**
     * Asserts that a javascript alert is present.
     *
     * @since 1.3
     */
    public void assertAlertPresent() {
        assertTrue(selenium.isAlertPresent());
    }

    /**
     * Asserts that a javascript alert is present.
     *
     * @param failMessage message
     * @since 1.4
     */
    public void assetAlertPresent(String failMessage) {
        assertTrue(failMessage, selenium.isAlertPresent());
    }

    /**
     * Asserts that no javascript alerts are present.
     *
     * @since 1.3
     */
    public void assertAlertNotPresent() {
        assertFalse(selenium.isAlertPresent());
    }

    /**
     * Asserts that no javascript alerts are present.
     *
     * @param failMessage message
     * @since 1.4
     */
    public void assertAlertNotPresent(String failMessage) {
        assertFalse(failMessage, selenium.isAlertPresent());
    }

    /**
     * Asserts that a javascript prompt is present.
     *
     * @since 1.3
     */
    public void assertPromptPresent() {
        assertTrue(selenium.isPromptPresent());
    }

    /**
     * Asserts that a javascript prompt is present.
     *
     * @param failMessage message
     * @since 1.4
     */
    public void assertPromptPresent(String failMessage) {
        assertTrue(failMessage, selenium.isPromptPresent());
    }

    /**
     * Asserts that no javascript prompts are present.
     *
     * @since 1.3
     */
    public void assertPromptNotPresent() {
        assertFalse(selenium.isPromptPresent());
    }

    /**
     * Asserts that no javascript prompts are present.
     *
     * @param failMessage message
     * @since 1.4
     */
    public void assertPromptNotPresent(String failMessage) {
        assertFalse(failMessage, selenium.isPromptPresent());
    }

    /**
     * Asserts that a javascript confirmation is present.
     *
     * @since 1.3
     */
    public void assertConfirmationPresent() {
        assertTrue(selenium.isConfirmationPresent());
    }

    /**
     * Asserts that a javascript confirmation is present.
     *
     * @param failMessage message
     * @since 1.4
     */
    public void assertConfirmationPresent(String failMessage) {
        assertTrue(failMessage, selenium.isConfirmationPresent());
    }

    /**
     * Asserts that no javascript confirmations are present.
     *
     * @since 1.3
     */
    public void assertConfirmationNotPresent() {
        assertFalse(selenium.isConfirmationPresent());
    }

    /**
     * Asserts that no javascript confirmations are present.
     *
     * @param failMessage message
     * @since 1.4
     */
    public void assertConfirmationNotPresent(String failMessage) {
        assertFalse(failMessage, selenium.isConfirmationPresent());
    }

    /**
     * Asserts that an element is checked.
     *
     * @param locator element location
     * @since 1.3
     */
    public void assertChecked(String locator) {
        assertTrue(selenium.isChecked(locator));
    }

    /**
     * Asserts that an element is checked.
     *
     * @param failMessage message
     * @param locator element location
     * @since 1.4
     */
    public void assertChecked(String locator, String failMessage) {
        assertTrue(failMessage, selenium.isChecked(locator));
    }

    /**
     * Asserts that no element is checked.
     *
     * @param locator element location
     * @since 1.3
     */
    public void assertNotChecked(String locator) {
        assertFalse(selenium.isChecked(locator));
    }

    /**
     * Asserts that no element is checked.
     *
     * @param locator element location
     * @param failMessage message
     * @since 1.4
     */
    public void assertNotChecked(String locator, String failMessage) {
        assertFalse(failMessage, selenium.isChecked(locator));
    }

    /**
     * Asserts that something is selected.
     *
     * @param selectLocator element location
     * @since 1.3
     */
    public void assertSomethingSelected(String selectLocator) {
        assertTrue(selenium.isSomethingSelected(selectLocator));
    }

    /**
     * Asserts that something is selected.
     *
     * @param selectLocator element location
     * @param failMessage message
     * @since 1.4
     */
    public void assertSomethingSelected(String selectLocator, String failMessage) {
        assertTrue(failMessage, selenium.isSomethingSelected(selectLocator));
    }

    /**
     * Asserts that nothing is selected.
     *
     * @param selectLocator element location
     * @since 1.3
     */
    public void assertNothingSelected(String selectLocator) {
        assertFalse(selenium.isSomethingSelected(selectLocator));
    }

    /**
     * Asserts that nothing is selected.
     *
     * @param selectLocator element location
     * @param failMessage message
     * @since 1.4
     */
    public void assertNothingSelected(String selectLocator, String failMessage) {
        assertFalse(failMessage, selenium.isSomethingSelected(selectLocator));
    }

    /**
     * Checks if the given pattern is present in the current browser window.
     *
     * @param pattern search text
     * @since 1.2
     */
    public void verifyTextPresent(String pattern) {
        verifyTrue(selenium.isTextPresent(pattern));
    }

    /**
     * Asserts that the given pattern is present in the current browser window.
     *
     * @param pattern search text
     * @since 1.3
     */
    public void assertTextPresent(String pattern) {
        assertTrue(selenium.isTextPresent(pattern));
    }

    /**
     * Asserts that the given pattern is present in the current browser window.
     *
     * @param pattern search text
     * @param failMessage message
     * @since 1.4
     */
    public void assertTextPresent(String pattern, String failMessage) {
        assertTrue(failMessage, selenium.isTextPresent(pattern));
    }

    /**
     * Checks if the given pattern is not present in the current browser window.
     *
     * @param pattern search text
     * @since 1.2
     */
    public void verifyTextNotPresent(String pattern) {
        verifyFalse(selenium.isTextPresent(pattern));
    }

    /**
     * Asserts that the given pattern is not present in the current browser window.
     *
     * @param pattern search text
     * @since 1.3
     */
    public void assertTextNotPresent(String pattern) {
        assertFalse(selenium.isTextPresent(pattern));
    }

    /**
     * Asserts that the given pattern is not present in the current browser window.
     *
     * @param pattern search text
     * @param failMessage message
     * @since 1.4
     */
    public void assertTextNotPresent(String pattern, String failMessage) {
        assertFalse(failMessage, selenium.isTextPresent(pattern));
    }

    /**
     * Asserts that an element is present.
     *
     * @param locator element location
     * @since 1.3
     */
    public void assertElementPresent(String locator) {
        assertTrue(selenium.isElementPresent(locator));
    }

    /**
     * Asserts that an element is present.
     *
     * @param locator element location
     * @param failMessage message
     * @since 1.4
     */
    public void assertElementPresent(String locator, String failMessage) {
        assertTrue(failMessage, selenium.isElementPresent(locator));
    }

    /**
     * Asserts that an element is not present.
     *
     * @param locator element location
     * @since 1.3
     */
    public void assertElementNotPresent(String locator) {
        assertFalse(selenium.isElementPresent(locator));
    }

    /**
     * Asserts that an element is not present.
     *
     * @param locator element location
     * @param failMessage message
     * @since 1.4
     */
    public void assertElementNotPresent(String locator, String failMessage) {
        assertFalse(failMessage, selenium.isElementPresent(locator));
    }

    /**
     * Asserts that an element is visible.
     *
     * @param locator element location
     * @since 1.3
     */
    public void assertVisible(String locator) {
        assertTrue(selenium.isVisible(locator));
    }

    /**
     * Asserts that an element is visible.
     *
     * @param locator element location
     * @param failMessage message
     * @since 1.4
     */
    public void assertVisible(String locator, String failMessage) {
        assertTrue(failMessage, selenium.isVisible(locator));
    }

    /**
     * Asserts that an element is not visible.
     *
     * @param locator element location
     * @since 1.3
     */
    public void assertNotVisible(String locator) {
        assertFalse(selenium.isVisible(locator));
    }

    /**
     * Asserts that an element is not visible.
     *
     * @param locator element location
     * @param failMessage message
     * @since 1.4
     */
    public void assertNotVisible(String locator, String failMessage) {
        assertFalse(failMessage, selenium.isVisible(locator));
    }

    /**
     * Asserts that an element is editable.
     *
     * @param locator element location
     * @since 1.3
     */
    public void assertEditable(String locator) {
        assertTrue(selenium.isEditable(locator));
    }

    /**
     * Asserts that an element is editable.
     *
     * @param locator element location
     * @param failMessage message
     * @since 1.4
     */
    public void assertEditable(String locator, String failMessage) {
        assertTrue(failMessage, selenium.isEditable(locator));
    }

    /**
     * Asserts that an element is not editable.
     *
     * @param locator element location
     * @since 1.3
     */
    public void assertNotEditable(String locator) {
        assertFalse(selenium.isEditable(locator));
    }

    /**
     * Asserts that an element is not editable.
     *
     * @param locator element location
     * @param failMessage message
     * @since 1.4
     */
    public void assertNotEditable(String locator, String failMessage) {
        assertFalse(failMessage, selenium.isEditable(locator));
    }

    /**
     * Asserts that an element is ordered.
     *
     * @param locator1 element1 location
     * @param locator2 element2 location
     * @since 1.3
     */
    public void assertOrdered(String locator1, String locator2) {
        assertTrue(selenium.isOrdered(locator1, locator2));
    }

    /**
     * Asserts that an element is ordered.
     *
     * @param locator1 element1 location
     * @param locator2 element2 location
     * @param failMessage message
     * @since 1.4
     */
    public void assertOrdered(String locator1, String locator2, String failMessage) {
        assertTrue(failMessage, selenium.isOrdered(locator1, locator2));
    }

    /**
     * Asserts that an element is not ordered.
     *
     * @param locator1 element1 location
     * @param locator2 element2 location
     * @since 1.3
     */
    public void assertNotOrdered(String locator1, String locator2) {
        assertFalse(selenium.isOrdered(locator1, locator2));
    }

    /**
     * Asserts that an element is not ordered.
     *
     * @param locator1 element1 location
     * @param locator2 element2 location
     * @param failMessage message
     * @since 1.4
     */
    public void assertNotOrdered(String locator1, String locator2, String failMessage) {
        assertFalse(failMessage, selenium.isOrdered(locator1, locator2));
    }

    /**
     * Asserts that a cookie is present.
     *
     * @param name cookie's name
     * @since 1.3
     */
    public void assertCookiePresent(String name) {
        assertTrue(selenium.isCookiePresent(name));
    }

    /**
     * Asserts that a cookie is present.
     *
     * @param name cookie's name
     * @param failMessage message
     * @since 1.4
     */
    public void assertCookiePresent(String name, String failMessage) {
        assertTrue(failMessage, selenium.isCookiePresent(name));
    }

    /**
     * Asserts that a cookie is not present.
     *
     * @param name cookie's name
     * @since 1.3
     */
    public void assertCookieNotPresent(String name) {
        assertFalse(selenium.isCookiePresent(name));
    }

    /**
     * Asserts that a cookie is not present.
     *
     * @param name cookie's name
     * @param failMessage message
     * @since 1.4
     */
    public void assertCookieNotPresent(String name, String failMessage) {
        assertFalse(failMessage, selenium.isCookiePresent(name));
    }


    /* DELEGATION */
    /* CHECKSTYLE:OFF */

    public void setExtensionJs(String extensionJs) {
        selenium.setExtensionJs(extensionJs);
    }

    public void showContextualBanner() {
        selenium.showContextualBanner();
    }

    public void showContextualBanner(String className, String methodName) {
        selenium.showContextualBanner(className, methodName);
    }

    public void click(String locator) {
        selenium.click(locator);
    }

    public void doubleClick(String locator) {
        selenium.doubleClick(locator);
    }

    public void contextMenu(String locator) {
        selenium.contextMenu(locator);
    }

    public void clickAt(String locator, String coordString) {
        selenium.clickAt(locator, coordString);
    }

    public void doubleClickAt(String locator, String coordString) {
        selenium.doubleClickAt(locator, coordString);
    }

    public void contextMenuAt(String locator, String coordString) {
        selenium.contextMenuAt(locator, coordString);
    }

    public void fireEvent(String locator, String eventName) {
        selenium.fireEvent(locator, eventName);
    }

    public void focus(String locator) {
        selenium.focus(locator);
    }

    public void keyPress(String locator, String keySequence) {
        selenium.keyPress(locator, keySequence);
    }

    public void shiftKeyDown() {
        selenium.shiftKeyDown();
    }

    public void shiftKeyUp() {
        selenium.shiftKeyUp();
    }

    public void metaKeyDown() {
        selenium.metaKeyDown();
    }

    public void metaKeyUp() {
        selenium.metaKeyUp();
    }

    public void altKeyDown() {
        selenium.altKeyDown();
    }

    public void altKeyUp() {
        selenium.altKeyUp();
    }

    public void controlKeyDown() {
        selenium.controlKeyDown();
    }

    public void controlKeyUp() {
        selenium.controlKeyUp();
    }

    public void keyDown(String locator, String keySequence) {
        selenium.keyDown(locator, keySequence);
    }

    public void keyUp(String locator, String keySequence) {
        selenium.keyUp(locator, keySequence);
    }

    public void mouseOver(String locator) {
        selenium.mouseOver(locator);
    }

    public void mouseOut(String locator) {
        selenium.mouseOut(locator);
    }

    public void mouseDown(String locator) {
        selenium.mouseDown(locator);
    }

    public void mouseDownRight(String locator) {
        selenium.mouseDownRight(locator);
    }

    public void mouseDownAt(String locator, String coordString) {
        selenium.mouseDownAt(locator, coordString);
    }

    public void mouseDownRightAt(String locator, String coordString) {
        selenium.mouseDownRightAt(locator, coordString);
    }

    public void mouseUp(String locator) {
        selenium.mouseUp(locator);
    }

    public void mouseUpRight(String locator) {
        selenium.mouseUpRight(locator);
    }

    public void mouseUpAt(String locator, String coordString) {
        selenium.mouseUpAt(locator, coordString);
    }

    public void mouseUpRightAt(String locator, String coordString) {
        selenium.mouseUpRightAt(locator, coordString);
    }

    public void mouseMove(String locator) {
        selenium.mouseMove(locator);
    }

    public void mouseMoveAt(String locator, String coordString) {
        selenium.mouseMoveAt(locator, coordString);
    }

    public void type(String locator, String value) {
        selenium.type(locator, value);
    }

    public void typeKeys(String locator, String value) {
        selenium.typeKeys(locator, value);
    }

    public void setSpeed(String value) {
        selenium.setSpeed(value);
    }

    public String getSpeed() {
        return selenium.getSpeed();
    }

    public void check(String locator) {
        selenium.check(locator);
    }

    public void uncheck(String locator) {
        selenium.uncheck(locator);
    }

    public void select(String selectLocator, String optionLocator) {
        selenium.select(selectLocator, optionLocator);
    }

    public void addSelection(String locator, String optionLocator) {
        selenium.addSelection(locator, optionLocator);
    }

    public void removeSelection(String locator, String optionLocator) {
        selenium.removeSelection(locator, optionLocator);
    }

    public void removeAllSelections(String locator) {
        selenium.removeAllSelections(locator);
    }

    public void submit(String formLocator) {
        selenium.submit(formLocator);
    }

    public void open(String url) {
        selenium.open(url);
    }

    public void openWindow(String url, String windowID) {
        selenium.openWindow(url, windowID);
    }

    public void selectWindow(String windowID) {
        selenium.selectWindow(windowID);
    }

    public void selectPopUp(String windowID) {
        selenium.selectPopUp(windowID);
    }

    public void deselectPopUp() {
        selenium.deselectPopUp();
    }

    public void selectFrame(String locator) {
        selenium.selectFrame(locator);
    }

    public boolean getWhetherThisFrameMatchFrameExpression(String currentFrameString, String target) {
        return selenium.getWhetherThisFrameMatchFrameExpression(currentFrameString, target);
    }

    public boolean getWhetherThisWindowMatchWindowExpression(String currentWindowString, String target) {
        return selenium.getWhetherThisWindowMatchWindowExpression(currentWindowString, target);
    }

    public void waitForPopUp(String windowID, String timeout) {
        selenium.waitForPopUp(windowID, timeout);
    }

    public void chooseCancelOnNextConfirmation() {
        selenium.chooseCancelOnNextConfirmation();
    }

    public void chooseOkOnNextConfirmation() {
        selenium.chooseOkOnNextConfirmation();
    }

    public void answerOnNextPrompt(String answer) {
        selenium.answerOnNextPrompt(answer);
    }

    public void goBack() {
        selenium.goBack();
    }

    public void refresh() {
        selenium.refresh();
    }

    public void close() {
        selenium.close();
    }

    public boolean isAlertPresent() {
        return selenium.isAlertPresent();
    }

    public boolean isPromptPresent() {
        return selenium.isPromptPresent();
    }

    public boolean isConfirmationPresent() {
        return selenium.isConfirmationPresent();
    }

    public String getAlert() {
        return selenium.getAlert();
    }

    public String getConfirmation() {
        return selenium.getConfirmation();
    }

    public String getPrompt() {
        return selenium.getPrompt();
    }

    public String getLocation() {
        return selenium.getLocation();
    }

    public String getTitle() {
        return selenium.getTitle();
    }

    public String getBodyText() {
        return selenium.getBodyText();
    }

    public String getValue(String locator) {
        return selenium.getValue(locator);
    }

    public String getText(String locator) {
        return selenium.getText(locator);
    }

    public void highlight(String locator) {
        selenium.highlight(locator);
    }

    public String getEval(String script) {
        return selenium.getEval(script);
    }

    public boolean isChecked(String locator) {
        return selenium.isChecked(locator);
    }

    public String getTable(String tableCellAddress) {
        return selenium.getTable(tableCellAddress);
    }

    public String[] getSelectedLabels(String selectLocator) {
        return selenium.getSelectedLabels(selectLocator);
    }

    public String getSelectedLabel(String selectLocator) {
        return selenium.getSelectedLabel(selectLocator);
    }

    public String[] getSelectedValues(String selectLocator) {
        return selenium.getSelectedValues(selectLocator);
    }

    public String getSelectedValue(String selectLocator) {
        return selenium.getSelectedValue(selectLocator);
    }

    public String[] getSelectedIndexes(String selectLocator) {
        return selenium.getSelectedIndexes(selectLocator);
    }

    public String getSelectedIndex(String selectLocator) {
        return selenium.getSelectedIndex(selectLocator);
    }

    public String[] getSelectedIds(String selectLocator) {
        return selenium.getSelectedIds(selectLocator);
    }

    public String getSelectedId(String selectLocator) {
        return selenium.getSelectedId(selectLocator);
    }

    public boolean isSomethingSelected(String selectLocator) {
        return selenium.isSomethingSelected(selectLocator);
    }

    public String[] getSelectOptions(String selectLocator) {
        return selenium.getSelectOptions(selectLocator);
    }

    public String getAttribute(String attributeLocator) {
        return selenium.getAttribute(attributeLocator);
    }

    public boolean isTextPresent(String pattern) {
        return selenium.isTextPresent(pattern);
    }

    public boolean isElementPresent(String locator) {
        return selenium.isElementPresent(locator);
    }

    public boolean isVisible(String locator) {
        return selenium.isVisible(locator);
    }

    public boolean isEditable(String locator) {
        return selenium.isEditable(locator);
    }

    public String[] getAllButtons() {
        return selenium.getAllButtons();
    }

    public String[] getAllLinks() {
        return selenium.getAllLinks();
    }

    public String[] getAllFields() {
        return selenium.getAllFields();
    }

    public String[] getAttributeFromAllWindows(String attributeName) {
        return selenium.getAttributeFromAllWindows(attributeName);
    }

    public void dragdrop(String locator, String movementsString) {
        selenium.dragdrop(locator, movementsString);
    }

    public void setMouseSpeed(String pixels) {
        selenium.setMouseSpeed(pixels);
    }

    public Number getMouseSpeed() {
        return selenium.getMouseSpeed();
    }

    public void dragAndDrop(String locator, String movementsString) {
        selenium.dragAndDrop(locator, movementsString);
    }

    public void dragAndDropToObject(String locatorOfObjectToBeDragged, String locatorOfDragDestinationObject) {
        selenium.dragAndDropToObject(locatorOfObjectToBeDragged, locatorOfDragDestinationObject);
    }

    public void windowFocus() {
        selenium.windowFocus();
    }

    public void windowMaximize() {
        selenium.windowMaximize();
    }

    public String[] getAllWindowIds() {
        return selenium.getAllWindowIds();
    }

    public String[] getAllWindowNames() {
        return selenium.getAllWindowNames();
    }

    public String[] getAllWindowTitles() {
        return selenium.getAllWindowTitles();
    }

    public String getHtmlSource() {
        return selenium.getHtmlSource();
    }

    public void setCursorPosition(String locator, String position) {
        selenium.setCursorPosition(locator, position);
    }

    public Number getElementIndex(String locator) {
        return selenium.getElementIndex(locator);
    }

    public boolean isOrdered(String locator1, String locator2) {
        return selenium.isOrdered(locator1, locator2);
    }

    public Number getElementPositionLeft(String locator) {
        return selenium.getElementPositionLeft(locator);
    }

    public Number getElementPositionTop(String locator) {
        return selenium.getElementPositionTop(locator);
    }

    public Number getElementWidth(String locator) {
        return selenium.getElementWidth(locator);
    }

    public Number getElementHeight(String locator) {
        return selenium.getElementHeight(locator);
    }

    public Number getCursorPosition(String locator) {
        return selenium.getCursorPosition(locator);
    }

    public String getExpression(String expression) {
        return selenium.getExpression(expression);
    }

    public Number getXpathCount(String xpath) {
        return selenium.getXpathCount(xpath);
    }

    public void assignId(String locator, String identifier) {
        selenium.assignId(locator, identifier);
    }

    public void allowNativeXpath(String allow) {
        selenium.allowNativeXpath(allow);
    }

    public void ignoreAttributesWithoutValue(String ignore) {
        selenium.ignoreAttributesWithoutValue(ignore);
    }

    public void waitForCondition(String script, String timeout) {
        selenium.waitForCondition(script, timeout);
    }

    public void setTimeout(String timeout) {
        selenium.setTimeout(timeout);
    }

    public void waitForPageToLoad(String timeout) {
        selenium.waitForPageToLoad(timeout);
    }

    public void waitForFrameToLoad(String frameAddress, String timeout) {
        selenium.waitForFrameToLoad(frameAddress, timeout);
    }

    public String getCookie() {
        return selenium.getCookie();
    }

    public String getCookieByName(String name) {
        return selenium.getCookieByName(name);
    }

    public boolean isCookiePresent(String name) {
        return selenium.isCookiePresent(name);
    }

    public void createCookie(String nameValuePair, String optionsString) {
        selenium.createCookie(nameValuePair, optionsString);
    }

    public void deleteCookie(String name, String optionsString) {
        selenium.deleteCookie(name, optionsString);
    }

    public void deleteAllVisibleCookies() {
        selenium.deleteAllVisibleCookies();
    }

    public void setBrowserLogLevel(String logLevel) {
        selenium.setBrowserLogLevel(logLevel);
    }

    public void runScript(String script) {
        selenium.runScript(script);
    }

    public void addLocationStrategy(String strategyName, String functionDefinition) {
        selenium.addLocationStrategy(strategyName, functionDefinition);
    }

    public void captureEntirePageScreenshot(String filename, String kwargs) {
        selenium.captureEntirePageScreenshot(filename, kwargs);
    }

    public void rollup(String rollupName, String kwargs) {
        selenium.rollup(rollupName, kwargs);
    }

    public void addScript(String scriptContent, String scriptTagId) {
        selenium.addScript(scriptContent, scriptTagId);
    }

    public void removeScript(String scriptTagId) {
        selenium.removeScript(scriptTagId);
    }

    public void useXpathLibrary(String libraryName) {
        selenium.useXpathLibrary(libraryName);
    }

    public void setContext(String context) {
        selenium.setContext(context);
    }

    public void attachFile(String fieldLocator, String fileLocator) {
        selenium.attachFile(fieldLocator, fileLocator);
    }

    public void captureScreenshot(String filename) {
        selenium.captureScreenshot(filename);
    }

    public String captureScreenshotToString() {
        return selenium.captureScreenshotToString();
    }

    public String captureNetworkTraffic(String type) {
        return selenium.captureNetworkTraffic(type);
    }

    public String captureEntirePageScreenshotToString(String kwargs) {
        return selenium.captureEntirePageScreenshotToString(kwargs);
    }

    public String retrieveLastRemoteControlLogs() {
        return selenium.retrieveLastRemoteControlLogs();
    }

    public void keyDownNative(String keycode) {
        selenium.keyDownNative(keycode);
    }

    public void keyUpNative(String keycode) {
        selenium.keyUpNative(keycode);
    }

    public void keyPressNative(String keycode) {
        selenium.keyPressNative(keycode);
    }

    /* CHECKSTYLE:ON */
}
