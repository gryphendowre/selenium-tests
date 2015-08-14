/*!*****************************************************************************
 *
 * Selenium Tests For CTools
 *
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.pentaho.ctools.suite.CToolsTestSuite;
import com.pentaho.ctools.utils.BaseTest;
import com.pentaho.ctools.utils.ElementHelper;

/**
 * Testing the functionalities related with Text Area Input Component.
 *
 * Naming convention for test:
 *  'tcN_StateUnderTest_ExpectedBehavior'
 *
 */
public class TextAreaInputComponent extends BaseTest {
  // Instance to be used on wait commands
  private final Wait<WebDriver> wait = CToolsTestSuite.getWait();
  // The base url to be append the relative url in test
  private final String baseUrl = CToolsTestSuite.getBaseUrl();
  //Access to wrapper for webdriver
  private final ElementHelper elemHelper = new ElementHelper();
  //Log instance
  private final Logger log = LogManager.getLogger( TextAreaInputComponent.class );

  /**
   * Go to the TextAreaInputComponent web page.
   */
  @BeforeTest
  public void setUpTestCase() {
    //Go to AddinReference
    this.driver.get( this.baseUrl + "api/repos/%3Apublic%3Aplugin-samples%3Apentaho-cdf%3Apentaho-cdf-require%3A30-documentation%3A30-component_reference%3A10-core%3A38-TextareaInputComponent%3Atext_area_input_component.xcdf/generatedContent" );

    //NOTE - we have to wait for loading disappear
    this.elemHelper.WaitForElementInvisibility( this.driver, By.xpath( "//div[@class='blockUI blockOverlay']" ) );
  }

  /**
   * ############################### Test Case 2 ###############################
   *
   * Test Case Name:
   *    Page Content
   * Description:
   *    This test pretends to validate the contents present in the page.
   * Steps:
   *    1. Check sample title.
   *    2. Check sample description.
   */
  @Test
  public void tc1_PageContent_ContentPresent() {
    this.log.info( "tc1_PageContent_ContentPresent" );

    /*
     * ## Step 1
     */
    // Page title
    assertEquals( "Community Dashboard Framework", this.driver.getTitle() );
    //Sample Title
    String sampleTitle = this.elemHelper.WaitForElementPresentGetText( this.driver, By.xpath( "//span[2]" ) );
    assertEquals( "TextareaInputComponent", sampleTitle );

    /*
     * ## Step 2
     */
    //Sample Description
    String sampleDescTitle = this.elemHelper.WaitForElementPresentGetText( this.driver, By.xpath( "//h3" ) );
    String sampleDescription = this.elemHelper.WaitForElementPresentGetText( this.driver, By.xpath( "//p" ) );
    assertEquals( "Description", sampleDescTitle );
    assertEquals( "Renders a multi-line text input box to collect user input. Change event is fired after user edits the content and removes the focus from the box. Pre/postChange functions can be used to make data validation.", sampleDescription );

    /*
     * ## Step 3
     */
    //Options
    String optionsTitle = this.elemHelper.WaitForElementPresentGetText( this.driver, By.xpath( "//h3[2]" ) );
    String options1 = this.elemHelper.WaitForElementPresentGetText( this.driver, By.xpath( "//dt[7]" ) );
    String options2 = this.elemHelper.WaitForElementPresentGetText( this.driver, By.xpath( "//dt[8]" ) );
    assertEquals( "Options", optionsTitle );
    assertEquals( "charWidth", options1 );
    assertEquals( "maxChars", options2 );

    /*
     * ## Step 4
     */
    //Samples
    String samplesTitle = this.elemHelper.WaitForElementPresentGetText( this.driver, By.xpath( "//h3[3]" ) );
    assertEquals( "Sample", samplesTitle );
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
    // ## Step 1
    // Render again the sample
    this.elemHelper.FindElement( this.driver, By.xpath( "//div[@id='example']/ul/li[2]/a" ) ).click();
    this.elemHelper.FindElement( this.driver, By.xpath( "//div[@id='code']/button" ) ).click();

    // NOTE - we have to wait for loading disappear
    this.elemHelper.WaitForElementInvisibility( this.driver, By.xpath( "//div[@class='blockUI blockOverlay']" ) );

    // Now sample element must be displayed
    assertTrue( this.elemHelper.FindElement( this.driver, By.id( "sample" ) ).isDisplayed() );

    //Check the number of divs with id 'SampleObject'
    //Hence, we guarantee when click Try Me the previous div is replaced
    int nSampleObject = this.driver.findElements( By.id( "sampleObject" ) ).size();
    assertEquals( 1, nSampleObject );
  }

  /**
   * ############################### Test Case 3 ###############################
   *
   * Test Case Name:
   *    Insert a small text
   * Description:
   *    We pretend validate when we insert a small text an alert is raised.
   * Steps:
   *    1. Insert text
   *    2. Check for alert
   *    3. Check the input text inserted
   */
  @Test
  public void tc3_InputSmallPhrase_AlertDispayed() {
    // ## Step 1
    String strInputString = "Hello World!";
    this.elemHelper.FindElement( this.driver, By.id( "myInput" ) ).clear();
    this.elemHelper.FindElement( this.driver, By.id( "myInput" ) ).sendKeys( strInputString );
    this.elemHelper.FindElement( this.driver, By.xpath( "//h3[3]" ) ).click();

    // ## Step 2
    this.wait.until( ExpectedConditions.alertIsPresent() );
    Alert alert = this.driver.switchTo().alert();
    String confirmationMsg = alert.getText();
    String expectedCnfText = "you typed: " + strInputString;
    alert.accept();

    assertEquals( expectedCnfText, confirmationMsg );
  }

  /**
   * ############################### Test Case 4 ###############################
   *
   * Test Case Name:
   *    Insert a long text
   * Description:
   *    We pretend validate when we insert a long text an alert is raised.
   * Steps:
   *    1. Insert text
   *    2. Check for alert
   *    3. Check the input text inserted
   */
  @Test
  public void tc4_InputLongPhrase_AlertDispayed() {
    // ## Step 1
    String strInputString = "Hello World! Hello World! Hello World! Hello World! Hello World! Hello World!";
    strInputString += strInputString;
    strInputString += strInputString;
    strInputString += strInputString;
    strInputString += strInputString;
    this.elemHelper.FindElement( this.driver, By.id( "myInput" ) ).clear();
    this.elemHelper.FindElement( this.driver, By.id( "myInput" ) ).sendKeys( strInputString );
    this.elemHelper.FindElement( this.driver, By.xpath( "//h3[3]" ) ).click();

    // ## Step 2
    this.wait.until( ExpectedConditions.alertIsPresent() );
    Alert alert = this.driver.switchTo().alert();
    String confirmationMsg = alert.getText();
    String expectedCnfText = "you typed: " + strInputString;
    alert.accept();

    assertEquals( expectedCnfText, confirmationMsg );
  }

  /**
   * ############################### Test Case 5 ###############################
   *
   * Test Case Name:
   *    Insert special characters
   * Description:
   *    We pretend validate when we insert a special characters an alert is
   *    raised.
   * Steps:
   *    1. Insert text
   *    2. Check for alert
   *    3. Check the input text inserted
   */
  @Test
  public void tc5_InputSpecialPhrase_AlertDispayed() {
    // ## Step 1
    String strInputString = "`|!\"1#$%&/()=?*��:_�<>/*-+";
    this.elemHelper.FindElement( this.driver, By.id( "myInput" ) ).clear();
    this.elemHelper.FindElement( this.driver, By.id( "myInput" ) ).sendKeys( strInputString );
    this.elemHelper.FindElement( this.driver, By.xpath( "//h3[3]" ) ).click();

    // ## Step 2
    this.wait.until( ExpectedConditions.alertIsPresent() );
    Alert alert = this.driver.switchTo().alert();
    String confirmationMsg = alert.getText();
    String expectedCnfText = "you typed: " + strInputString;
    alert.accept();

    assertEquals( expectedCnfText, confirmationMsg );
  }
}