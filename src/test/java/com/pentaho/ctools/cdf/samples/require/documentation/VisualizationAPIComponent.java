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

package com.pentaho.ctools.cdf.samples.require.documentation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.pentaho.ctools.utils.ElementHelper;
import com.pentaho.ctools.utils.PageUrl;
import com.pentaho.selenium.BaseTest;

/**
 * Testing the functionalities related with component Visualization API.
 *
 * Naming convention for test: 'tcN_StateUnderTest_ExpectedBehavior'
 *
 */
public class VisualizationAPIComponent extends BaseTest {
  // Access to wrapper for webdriver
  private final ElementHelper elemHelper = new ElementHelper();
  // Log instance
  private final Logger log = LogManager.getLogger( VisualizationAPIComponent.class );

  /**
   * ############################### Test Case 0 ###############################
   *
   * Test Case Name:
   *    Open Sample Page
   */
  @Test
  public void tc0_OpenSamplePage_Display() {
    this.log.info( "tc0_OpenSamplePage_Display" );

    // The URL for the VisualizationAPIComponent under CDF samples
    // This samples is in: Public/plugin-samples/CDF/Require Samples/Documentation/Component Reference/Core Components/VisualizationAPIComponent
    this.elemHelper.Get( BaseTest.driver, PageUrl.VISUALIZATION_API_COMPONENT_REQUIRE );

    // NOTE - we have to wait for loading disappear
    this.elemHelper.WaitForElementPresence( BaseTest.driver, By.cssSelector( "div.blockUI.blockOverlay" ), 30 );
    this.elemHelper.WaitForElementInvisibility( BaseTest.driver, By.cssSelector( "div.blockUI.blockOverlay" ), 120 );

    //Wait for button sample to render with properly css
    final WebElement buttonVisible = this.elemHelper.WaitForElementPresenceAndVisible( BaseTest.driver, By.cssSelector( "#example > ul > li.ui-state-default.ui-corner-top.ui-tabs-active.ui-state-active" ) );
    Assert.assertNotNull( buttonVisible );
  }

  /**
   * ############################### Test Case 1 ###############################
   *
   * Test Case Name:
   *    Validate Page Contents
   * Description:
   *    Here we want to validate the page contents.
   * Steps:
   *    1. Check the widget's title.
   */
  @Test
  public void tc1_PageContent_DisplayTitle() {
    this.log.info( "tc1_PageContent_DisplayTitle" );

    // Wait for title become visible and with value 'Community Dashboard Framework'
    final String expectedTitlePage = "Community Dashboard Framework";
    final String actualTitlePage = this.elemHelper.WaitForTitle( BaseTest.driver, expectedTitlePage );

    // Wait for visibility of 'VisualizationAPIComponent'
    final String expectedSampleTitle = "VisualizationAPIComponent";
    final String actualSampleTitle = this.elemHelper.WaitForTextPresence( BaseTest.driver, By.cssSelector( "div.webdetailsBoxShadow h2 span:nth-child(2)" ), expectedSampleTitle );

    // Validate the sample that we are testing is the one
    Assert.assertEquals( actualTitlePage, expectedTitlePage );
    Assert.assertEquals( actualSampleTitle, expectedSampleTitle );
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

    /*
     * ## Step 1
     */
    // Render again the sample
    this.elemHelper.Click( BaseTest.driver, By.cssSelector( "li:nth-child(2) > a" ) );
    this.elemHelper.Click( BaseTest.driver, By.cssSelector( "#code > button" ) );

    // NOTE - we have to wait for loading disappear
    this.elemHelper.WaitForElementPresence( BaseTest.driver, By.cssSelector( "div.blockUI.blockOverlay" ), 5 );
    this.elemHelper.WaitForElementInvisibility( BaseTest.driver, By.cssSelector( "div.blockUI.blockOverlay" ) );

    // Now sample element must be displayed
    Assert.assertTrue( this.elemHelper.FindElement( BaseTest.driver, By.id( "sample" ) ).isDisplayed() );
  }

  /**
   * ############################### Test Case 3 ###############################
   *
   * Test Case Name:
   *    Validate MAX number
   * Description:
   *    When the user access the component, it is presented the max number of
   *    array set.
   * Steps:
   *    1. Check the presented value for MAX.
   */
  @Test
  public void tc3_MaxNumber_PresentCorrectValue() {
    this.log.info( "tc3_MaxNumber_PresentCorrectValue" );

    /*
     * ## Step 1
     */
    final String expectedValue = "The result is 35659.00";
    final String actualValue = this.elemHelper.WaitForTextPresence( BaseTest.driver, By.cssSelector( "#sampleObject>span" ), expectedValue );
    Assert.assertEquals( actualValue, expectedValue );
  }

  /**
   * ############################### Test Case 4 ###############################
   *
   * Test Case Name:
   *    Validate MIN number
   * Description:
   *    When the user access the component, it is presented the min number of
   *    array set.
   * Steps:
   *    1. Change the option parameter to MIN and reload sample.
   *    2. Check the presented value for MIN.
   *
   * @throws InterruptedException
   */
  @Test
  public void tc4_MinNumber_PresentCorrectValue() {
    this.log.info( "tc4_MinNumber_PresentCorrectValue" );

    /*
     * ## Step 1 - Change the option parameter to MIN and reload sample
     */
    // Render again the sample
    this.elemHelper.Click( BaseTest.driver, By.cssSelector( "li:nth-child(2) > a" ) );

    // Wait for board load
    this.elemHelper.WaitForElementPresenceAndVisible( BaseTest.driver, By.id( "code" ) );
    // Change contains to MIN
    String text = this.elemHelper.WaitForElementPresenceAndVisible( BaseTest.driver, By.id( "samplecode" ) ).getText().replace( "max", "min" );
    text = text.replace( "// Using postFetch to convert data types, because the query is passing // a Numeric field as String and that breaks the sample visualization. ", "" );
    this.elemHelper.Clear( BaseTest.driver, By.xpath( "//div[@id='code']/textarea" ) );
    this.elemHelper.SendKeys( BaseTest.driver, By.xpath( "//div[@id='code']/textarea" ), text );

    // Click in Try me
    this.elemHelper.Click( BaseTest.driver, By.cssSelector( "#code > button" ) );
    // NOTE - we have to wait for loading disappear
    this.elemHelper.WaitForElementPresence( BaseTest.driver, By.cssSelector( "div.blockUI.blockOverlay" ), 5 );
    this.elemHelper.WaitForElementInvisibility( BaseTest.driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    // Now sample element must be displayed
    Assert.assertTrue( this.elemHelper.FindElement( BaseTest.driver, By.id( "sample" ) ).isDisplayed() );

    /*
     * ## Step 2 - Check the presented value for MIN.
     */
    this.elemHelper.WaitForElementPresenceAndVisible( BaseTest.driver, By.id( "sample" ) );
    this.elemHelper.WaitForElementPresenceAndVisible( BaseTest.driver, By.cssSelector( "#sampleObject>span" ), 5 );

    final String expectedValue = "The result is 490.00";
    final String actualValue = this.elemHelper.WaitForTextPresence( BaseTest.driver, By.cssSelector( "#sampleObject>span" ), expectedValue );

    Assert.assertEquals( expectedValue, actualValue );
  }

  /**
   * ############################### Test Case 5 ###############################
   *
   * Test Case Name:
   *    Validate AVG number
   * Description:
   *    When the user access the component, it is presented the avg number of
   *    array set.
   * Steps:
   *    1. Change the option parameter to AVG and reload sample
   *    2. Check the presented value for AVG.
   */
  @Test
  public void tc5_AvgNumber_PresentCorrectValue() {
    this.log.info( "tc5_AvgNumber_PresentCorrectValue" );

    /*
     * ## Step 1 - Change the option parameter to AVG and reload sample
     */
    // Render again the sample
    this.elemHelper.Click( BaseTest.driver, By.cssSelector( "li:nth-child(2) > a" ) );

    // Wait for board load
    this.elemHelper.WaitForElementPresenceAndVisible( BaseTest.driver, By.id( "code" ) );
    // Replace text
    String text = this.elemHelper.WaitForElementPresenceAndVisible( BaseTest.driver, By.id( "samplecode" ) ).getText().replace( "max", "avg" );
    text = text.replace( "// Using postFetch to convert data types, because the query is passing // a Numeric field as String and that breaks the sample visualization. ", "" );
    this.elemHelper.Clear( BaseTest.driver, By.xpath( "//div[@id='code']/textarea" ) );
    this.elemHelper.SendKeys( BaseTest.driver, By.xpath( "//div[@id='code']/textarea" ), text );

    // Click in Try me
    this.elemHelper.Click( BaseTest.driver, By.cssSelector( "#code > button" ) );
    // NOTE - we have to wait for loading disappear
    this.elemHelper.WaitForElementPresence( BaseTest.driver, By.cssSelector( "div.blockUI.blockOverlay" ), 5 );
    this.elemHelper.WaitForElementInvisibility( BaseTest.driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    // Now sample element must be displayed
    Assert.assertTrue( this.elemHelper.FindElement( BaseTest.driver, By.id( "sample" ) ).isDisplayed() );

    /*
     * ## Step 2 - Check the presented value for MIN.
     */
    this.elemHelper.WaitForElementPresenceAndVisible( BaseTest.driver, By.id( "sample" ) );
    this.elemHelper.WaitForElementPresenceAndVisible( BaseTest.driver, By.cssSelector( "#sampleObject>span" ) );
    final String expectedValue = "The result is 4787.77";
    final String actualValue = this.elemHelper.WaitForTextPresence( BaseTest.driver, By.cssSelector( "#sampleObject>span" ), expectedValue );
    Assert.assertEquals( actualValue, expectedValue );
  }
}
