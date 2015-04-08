/*!*****************************************************************************
 *
 * Selenium Tests For CTools
 *
 * Copyright (C) 2002-2014 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package org.pentaho.ctools.issues.cde;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.pentaho.ctools.suite.CToolsTestSuite;
import org.pentaho.ctools.utils.ElementHelper;
import org.pentaho.ctools.utils.ScreenshotTestRule;

/**
 * The script is testing the issue:
 * - http://jira.pentaho.com/browse/CDE-395
 *
 * and the automation test is described:
 * - http://jira.pentaho.com/browse/QUALITY-925
 *
 * NOTE
 * To test this script it is required to have CDE plugin installed.
 *
 * Naming convention for test:
 *  'tcN_StateUnderTest_ExpectedBehavior'
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CDE395 {
  // Instance of the driver (browser emulator)
  private static WebDriver  driver;
  // The base url to be append the relative url in test
  private static String     baseUrl;
  // Log instance
  private static Logger     log                = LogManager.getLogger(CDE395.class);
  // Getting screenshot when test fails
  @Rule
  public ScreenshotTestRule screenshotTestRule = new ScreenshotTestRule(driver);

  @BeforeClass
  public static void setUpClass() {
    log.info("setUp##" + CDE395.class.getSimpleName());
    driver = CToolsTestSuite.getDriver();
    baseUrl = CToolsTestSuite.getBaseUrl();
  }

  /**
   * ############################### Test Case 1 ###############################
   *
   * Test Case Name:
   *    Asserting new CDE dashboard has Bootstrap as renderer option
   *
   * Description:
   *    The test pretends validate the CDE-395 issue, so when user creates a new CDE
   *    dashboard it has the renderer option as Bootstrap by default.
   *
   * Steps:
   *    1. Create New Dashboard, assert elements on page and click "Settings"
   *    4. Focus on popup, assert elements and assert Bootstrap option is selected by default
   */
  @Test(timeout = 120000)
  public void tc01_NewCdeDashboard_DefaultRendererBootstrap() {
    log.info("tc01_NewCdeDashboard_DefaultRendererBootstrap");

    /*
     * ## Step 1
     */
    //Create new CDE dashboard
    driver.get(baseUrl + "api/repos/wcdf/new");
    ElementHelper.WaitForElementInvisibility(driver, By.xpath("//div[@class='blockUI blockOverlay']"));
    //assert buttons and click Settings
    String newText = ElementHelper.WaitForElementPresentGetText(driver, By.xpath("//div[@id='headerLinks']/div/a"));
    String saveText = ElementHelper.WaitForElementPresentGetText(driver, By.xpath("//div[@id='headerLinks']/div[2]/a"));
    String saveasText = ElementHelper.WaitForElementPresentGetText(driver, By.xpath("//div[@id='headerLinks']/div[3]/a"));
    String reloadText = ElementHelper.WaitForElementPresentGetText(driver, By.xpath("//div[@id='headerLinks']/div[4]/a"));
    String settingsText = ElementHelper.WaitForElementPresentGetText(driver, By.xpath("//div[@id='headerLinks']/div[5]/a"));
    assertEquals("New", newText);
    assertEquals("Save", saveText);
    assertEquals("Save as...", saveasText);
    assertEquals("Reload", reloadText);
    assertEquals("Settings", settingsText);
    ElementHelper.ClickJS(driver, By.xpath("//div[@id='headerLinks']/div[5]/a"));

    /*
     * ## Step 4
     */
    WebElement element = ElementHelper.WaitForElementPresenceAndVisible(driver, By.id("popup"));
    assertNotNull(element);
    WebElement obj1 = ElementHelper.FindElement(driver, By.xpath("//select[@id='rendererInput']/option[@value='bootstrap']"));
    assertEquals(obj1.isSelected(), true);
  }

  @AfterClass
  public static void tearDownClass() {
    log.info("tearDown##" + CDE395.class.getSimpleName());
  }
}
