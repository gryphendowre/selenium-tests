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
package com.pentaho.ctools.cde.reference;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.pentaho.ctools.utils.ElementHelper;
import com.pentaho.ctools.utils.PageUrl;
import com.pentaho.selenium.BaseTest;

/**
 * Testing the functionalities related with Duplicate Component (Sniff Test).
 *
 * Naming convention for test: 'tcN_StateUnderTest_ExpectedBehavior'
 *
 */
public class DuplicateComponent extends BaseTest {
  // Access to wrapper for webdriver
  private final ElementHelper elemHelper = new ElementHelper();
  // Log instance
  private final Logger log = LogManager.getLogger( DuplicateComponent.class );

  /**
   * ############################### Test Case 1 ###############################
   *
      * Test Case Name:
   *    Sniff test to sample
   *
   * Description:
   *    This test is to assert simple functionality of sample 
   *
      * Steps:
   *    1. Open Duplicate Component sample and assert elements on page
   *    2. Assert elements on page and click first "Duplicate" link
   *    3. Assert elements are duplicated correctly and click second "Duplicate" link
   *    4. Assert elements are duplicated correctly
   */
  @Test
  public void tc01_CDEDashboard_DuplicateComponentWorks() {
    this.log.info( "tc01_CDEDashboard_DuplicateComponentWorks" );

    /*
     * ## Step 1
     */
    //Open Duplicate Component sample
    driver.get( PageUrl.DUPLICATE_COMPONENT );

    // NOTE - we have to wait for loading disappear
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );

    //Get Title of sample
    String title = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='content']/div/div[2]/div" ) );
    assertEquals( "Duplicate Component sample", title );

    //Assert presence of elements
    WebElement userLabel = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='targetHTML']/div/div/div/div/div" ) );
    WebElement userText = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='targetHTML']/div/div/div/div/div[2]/input" ) );
    WebElement userButton = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='targetHTML']/div/div/div/div/div[3]/button" ) );
    WebElement passLabel = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='targetHTML']/div/div/div/div[2]/div" ) );
    WebElement passText = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='targetHTML']/div/div/div/div[2]/div[2]/input" ) );
    WebElement passButton = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='targetHTML']/div/div/div/div[2]/div[3]/button" ) );
    assertNotNull( userLabel );
    assertNotNull( userText );
    assertNotNull( userButton );
    assertNotNull( passLabel );
    assertNotNull( passText );
    assertNotNull( passButton );

    /*
     * ## Step 2
     */
    //Duplicate elements to Target HTML (default)
    WebElement firstDuplicate = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='dup1Col']/a" ) );
    assertNotNull( firstDuplicate );
    this.elemHelper.Click( driver, By.xpath( "//div[@id='dup1Col']/a" ) );

    //Assert presence of elements
    WebElement userLabel1 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='targetHTML_1']/div/div/div/div/div" ) );
    WebElement userText1 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='targetHTML_1']/div/div/div/div/div[2]/input" ) );
    WebElement userButton1 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='targetHTML_1']/div/div/div/div/div[3]/button" ) );
    WebElement passLabel1 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='targetHTML_1']/div/div/div/div[2]/div" ) );
    WebElement passText1 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='targetHTML_1']/div/div/div/div[2]/div[2]/input" ) );
    WebElement passButton1 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='targetHTML_1']/div/div/div/div[2]/div[3]/button" ) );
    assertNotNull( userLabel1 );
    assertNotNull( userText1 );
    assertNotNull( userButton1 );
    assertNotNull( passLabel1 );
    assertNotNull( passText1 );
    assertNotNull( passButton1 );

    /*
     * ## Step 3
     */
    //Duplicate elements to Target HTML (default)
    WebElement secondDuplicate = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='dup1Col']/a" ) );
    assertNotNull( secondDuplicate );
    this.elemHelper.Click( driver, By.xpath( "//div[@id='dup2Col']/a" ) );

    //Assert presence of elements
    WebElement userLabel2 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='dupContainer']/div[@id='targetHTML_2']/div/div/div/div/div" ) );
    WebElement userText2 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='dupContainer']/div[@id='targetHTML_2']/div/div/div/div/div[2]/input" ) );
    WebElement userButton2 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='dupContainer']/div[@id='targetHTML_2']/div/div/div/div/div[3]/button" ) );
    WebElement passLabel2 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='dupContainer']/div[@id='targetHTML_2']/div/div/div/div[2]/div" ) );
    WebElement passText2 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='dupContainer']/div[@id='targetHTML_2']/div/div/div/div[2]/div[2]/input" ) );
    WebElement passButton2 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='dupContainer']/div[@id='targetHTML_2']/div/div/div/div[2]/div[3]/button" ) );
    assertNotNull( userLabel2 );
    assertNotNull( userText2 );
    assertNotNull( userButton2 );
    assertNotNull( passLabel2 );
    assertNotNull( passText2 );
    assertNotNull( passButton2 );
  }
}
