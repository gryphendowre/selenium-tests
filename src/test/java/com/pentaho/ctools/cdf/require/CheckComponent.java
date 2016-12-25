/*!*****************************************************************************
 *
 * Selenium Tests For CTools
 *
 * Copyright (C) 2002-2016 by Pentaho : http://www.pentaho.com
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
package com.pentaho.ctools.cdf.require;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.pentaho.ctools.utils.ElementHelper;
import com.pentaho.ctools.utils.PageUrl;
import com.pentaho.selenium.BaseTest;

/**
 * Testing the functionalities related with Check Component.
 *
 * Naming convention for test:
 *  'tcN_StateUnderTest_ExpectedBehavior'
 *
 */
public class CheckComponent extends BaseTest {
  // Access to wrapper for webdriver
  private final ElementHelper elemHelper = new ElementHelper();
  //Log instance
  private final Logger log = LogManager.getLogger( CheckComponent.class );

  /**
   * ############################### Test Case 0 ###############################
   *
   * Test Case Name:
   *    Open Sample
   */
  @Test
  public void tc0_OpenSamplePage_Display() {
    this.log.info( "tc0_OpenSamplePage_Display" );

    // The URL for the CheckComponent under CDF samples
    // This samples is in: Public/plugin-samples/CDF/Documentation/Component Reference/Core Components/CheckComponent
    driver.get( PageUrl.CHECK_COMPONENT_REQUIRE );

    // NOTE - we have to wait for loading disappear
    this.elemHelper.WaitForElementPresence( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
  }

  /**
   * ############################### Test Case 2 ###############################
   *
   * Test Case Name:
   *    Reload Sample
   * Description:
   *    Reload the sample (not refresh page).
   * Steps:
   *    1. Click in Code and then click in button 'Try me'.
   */
  @Test
  public void tc1_PageContent_DisplayTitle() {
    this.log.info( "tc1_PageContent_DisplayTitle" );

    // Wait for title become visible and with value 'Community Dashboard Framework'
    String pageTitle = this.elemHelper.WaitForTitle( driver, "Community Dashboard Framework" );

    // Wait for visibility of 'CheckComponent'
    String sampleTitle = this.elemHelper.WaitForTextPresence( driver, By.xpath( "//div[@id='dashboardContent']/div/div/div/h2/span[2]" ), "CheckComponent" );

    // Validate the sample that we are testing is the one
    assertEquals( pageTitle, "Community Dashboard Framework" );
    assertEquals( sampleTitle, "CheckComponent" );
  }

  /**
   * ############################### Test Case 2 ###############################
   *
   * Test Case Name:
   *    Reload Sample
   * Description:
   *    Reload the sample (not refresh page).
   * Steps:
   *    1. Click in Code and then click in button 'Try me'.
   */
  @Test
  public void tc2_ReloadSample_SampleReadyToUse() {
    this.log.info( "tc2_ReloadSample_SampleReadyToUse" );

    // ## Step 1
    // Render again the sample
    this.elemHelper.ClickJS( driver, By.xpath( "//div[@id='example']/ul/li[2]/a" ) );
    this.elemHelper.ClickJS( driver, By.xpath( "//div[@id='code']/button" ) );

    // NOTE - we have to wait for loading disappear
    this.elemHelper.WaitForElementPresence( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );

    // Now sample element must be displayed
    assertTrue( this.elemHelper.FindElement( driver, By.id( "sample" ) ).isDisplayed() );
  }

  /**
   * ############################### Test Case 3 ###############################
   *
   * Test Case Name:
   *    Check options
   * Description:
   *    Here we pretend to check each option one by one and validate if an alert
   *    is raised with correct message.
   * Steps:
   *    1. Check in Southern and validate alert
   *    2. Check in Eastern and validate alert
   *    3. Check in Central and validate alert
   *    4. Check in Western and validate alert
   */
  @Test
  public void tc3_CheckEachOption_AfterCheckAnAlertIsDisplayed() {
    this.log.info( "tc3_CheckEachOption_AfterCheckAnAlertIsDisplayed" );

    String confirmationMsg = "";
    // ## Step 1
    //Click in La Rochelle Gifts
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='La Rochelle Gifts']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: La Rochelle Gifts", confirmationMsg );

    // ## Step 2
    //Click in Land of Toys Inc.
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='Land of Toys Inc.']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: La Rochelle Gifts,Land of Toys Inc.", confirmationMsg );

    // ## Step 3
    //Click in Souveniers And Things Co.
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='Souveniers And Things Co.']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: La Rochelle Gifts,Land of Toys Inc.,Souveniers And Things Co.", confirmationMsg );

    // ## Step 4
    //Click in Salzburg Collectables
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='Salzburg Collectables']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: La Rochelle Gifts,Land of Toys Inc.,Souveniers And Things Co.,Salzburg Collectables", confirmationMsg );
  }

  /**
   * ############################### Test Case 4 ###############################
   *
   * Test Case Name:
   *    Unchecked options
   * Description:
   *    Here we pretend to unchecked each option one by one and validate if an
   *    alert is raised with correct message.
   * Steps:
   *    1. Unchecked Southern and validate alert
   *    2. Unchecked Eastern and validate alert
   *    3. Unchecked Central and validate alert
   *    4. Unchecked Western and validate alert
   */
  @Test
  public void tc4_UncheckedEachOption_AfterUncheckAnAlertIsDisplayed() {
    this.log.info( "tc4_UncheckedEachOption_AfterUncheckAnAlertIsDisplayed" );

    String confirmationMsg = "";
    // ## Step 1
    //Click in La Rochelle Gifts
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='La Rochelle Gifts']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: Land of Toys Inc.,Souveniers And Things Co.,Salzburg Collectables", confirmationMsg );

    // ## Step 2
    //Click in Land of Toys Inc.
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='Land of Toys Inc.']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: Souveniers And Things Co.,Salzburg Collectables", confirmationMsg );

    // ## Step 3
    //Click in Souveniers And Things Co.
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='Souveniers And Things Co.']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: Salzburg Collectables", confirmationMsg );

    // ## Step 4
    //Click in Salzburg Collectables
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='Salzburg Collectables']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: ", confirmationMsg );
  }

  /**
   * ############################### Test Case 5 ###############################
   *
   * Test Case Name:
   *    Check and Unchecked Arbitrary
   * Description:
   *    Here we pretend to check and unchecked arbitrary and validate the alert
   *    message according each action (check and unchecked).
   * Steps:
   *    1. Check and unchecked arbitrary, and validate alert message
   */
  @Test
  public void tc5_UncheckedEachOption_AfterUncheckAnAlertIsDisplayed() {
    this.log.info( "tc5_UncheckedEachOption_AfterUncheckAnAlertIsDisplayed" );

    String confirmationMsg = "";
    //Click in La Rochelle Gifts
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='La Rochelle Gifts']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: La Rochelle Gifts", confirmationMsg );

    //Click in Land of Toys Inc.
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='Land of Toys Inc.']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: La Rochelle Gifts,Land of Toys Inc.", confirmationMsg );

    //UnChecked Land of Toys Inc.
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='Land of Toys Inc.']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: La Rochelle Gifts", confirmationMsg );

    //Click in Souveniers And Things Co.
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='Souveniers And Things Co.']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: La Rochelle Gifts,Souveniers And Things Co.", confirmationMsg );

    //Click in Salzburg Collectables
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='Salzburg Collectables']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: La Rochelle Gifts,Souveniers And Things Co.,Salzburg Collectables", confirmationMsg );

    //Uncheck Souveniers And Things Co.
    this.elemHelper.ClickJS( driver, By.xpath( "//input[@name='checkComponent' and @value='Souveniers And Things Co.']" ) );

    confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
    assertEquals( "you chose: La Rochelle Gifts,Salzburg Collectables", confirmationMsg );
  }
}
