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
package com.pentaho.ctools.issues.cde;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import com.pentaho.ctools.utils.ElementHelper;
import com.pentaho.ctools.utils.PageUrl;
import com.pentaho.selenium.BaseTest;

/**
 * The script is testing the issue:
 *  - http://jira.pentaho.com/browse/CDE-430
 *  
 * and the automation test is described:
 *  - http://jira.pentaho.com/browse/QUALITY-1085
 *  
 * NOTE To test this script it is required to have CDE plugin installed.
 *
 * Naming convention for test: 'tcN_StateUnderTest_ExpectedBehavior'
 *
 */
public class CDE430 extends BaseTest {
  // Access to wrapper for webdriver
  private final ElementHelper elemHelper = new ElementHelper();
  // Log instance
  private final Logger log = LogManager.getLogger( CDE430.class );

  /**
   * ############################### Test Case 1 ###############################
   *
   * Test Case Name: 
   *   Assert Popup Export Component properties show list with type options
   *
   * Description: 
   *   The test pretends validate the CDE-430 issue, so when user clicks the down arron when Chart Exprt or Data Export Types are
   *   selected, a drop down list is shown with valid type options
   *   
   * Steps:
   *  1. Open new CDE dashboard and go to Components Panel
   *  2. Add Export Popup Component and go to Advanced Properties
   *  3. Assert list is shown for Chart Export Type
   *  4. Assert list is shown for Data Export Type
   */
  @Test
  public void tc01_PopupExportComponent_TypeListShown() {
    this.log.info( "tc01_PopupExportComponent_TypeListShown" );

    /* 
     * ## Step 1 
     */
    //New CDE dashboard
    driver.get( PageUrl.CDE_DASHBOARD );
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );

    //Go to components Panel
    WebElement componentsButton = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@class='componentsPanelButton']" ) );
    assertNotNull( componentsButton );
    componentsButton.click();
    WebElement componentsPanel = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.id( "panel-componentens_panel" ) );
    assertNotNull( componentsPanel );

    /* 
     * ## Step 2 
     */
    //Add Export Popup Component
    WebElement expandOthers = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='cdfdd-components-pallete']/div[@id='cdfdd-components-palletePallete']/div[2]/h3/a" ) );
    assertNotNull( expandOthers );
    expandOthers.click();
    WebElement exportPopup = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='cdfdd-components-palletePallete']//a[@title='Export Popup Component']" ) );
    assertNotNull( exportPopup );
    exportPopup.click();

    //Click Advanced Properties
    WebElement advancedProperties = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='cdfdd-components-properties']/div/div/div[@class='advancedProperties propertiesUnSelected']" ) );
    assertNotNull( advancedProperties );
    advancedProperties.click();
    this.elemHelper.WaitForAttributeValueEqualsTo( driver, By.xpath( "//div[@id='cdfdd-components-properties']/div/div/div[3]" ), "class", "advancedProperties propertiesSelected" );

    /* 
     * ## Step 3 
     */
    //Assert list appears for Chart Types to Export
    WebElement chartType = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//td[@title='Type for Chart Image to Export']/../td[2]" ) );
    assertNotNull( chartType );
    chartType.click();
    WebElement inputChart = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//td[@title='Type for Chart Image to Export']/../td[2]/form/input" ) );
    assertNotNull( inputChart );
    inputChart.clear();

    /* Robot robot1;
     try {
       robot1 = new Robot();
       robot1.keyPress( KeyEvent.VK_ENTER );
       robot1.keyRelease( KeyEvent.VK_ENTER );
       robot1.keyPress( KeyEvent.VK_ENTER );
       robot1.keyRelease( KeyEvent.VK_ENTER );
       robot1.keyPress( KeyEvent.VK_DOWN );
       robot1.keyRelease( KeyEvent.VK_DOWN );
     } catch ( AWTException e ) {
       e.printStackTrace();
     }*/
    Actions a = new Actions( driver );
    a.sendKeys( Keys.ENTER ).sendKeys( Keys.ENTER ).sendKeys( Keys.DOWN ).build().perform();
    WebElement listOption1 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//body/ul/li/a" ) );
    assertNotNull( listOption1 );
    String textOption1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//body/ul/li/a" ) );
    assertEquals( "png", textOption1 );
    WebElement listOption2 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//body/ul/li[2]/a" ) );
    assertNotNull( listOption2 );
    String textOption2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//body/ul/li[2]/a" ) );
    assertEquals( "svg", textOption2 );
    a.sendKeys( Keys.ENTER ).sendKeys( Keys.ENTER ).build().perform();
    this.elemHelper.WaitForElementNotPresent( driver, By.xpath( "//body/ul/li/a" ) );

    /* 
     * ## Step 4 
     */
    //Assert list appears for Data Types to Export
    WebElement dataType = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//td[@title='Type for Data File to Export']/../td[2]" ) );
    assertNotNull( dataType );
    dataType.click();
    WebElement inputData = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//td[@title='Type for Data File to Export']/../td[2]/form/input" ) );
    assertNotNull( inputData );
    inputData.clear();

    /*try {
      robot1 = new Robot();
      robot1.keyPress( KeyEvent.VK_ENTER );
      robot1.keyRelease( KeyEvent.VK_ENTER );
      robot1.keyPress( KeyEvent.VK_ENTER );
      robot1.keyRelease( KeyEvent.VK_ENTER );
      robot1.keyPress( KeyEvent.VK_DOWN );
      robot1.keyRelease( KeyEvent.VK_DOWN );
    } catch ( AWTException e ) {
      e.printStackTrace();
    }*/
    a.sendKeys( Keys.ENTER ).sendKeys( Keys.ENTER ).sendKeys( Keys.DOWN ).build().perform();
    listOption1 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//body/ul/li/a" ) );
    assertNotNull( listOption1 );
    textOption1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//body/ul/li/a" ) );
    assertEquals( "xls", textOption1 );
    listOption2 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//body/ul/li[2]/a" ) );
    assertNotNull( listOption2 );
    textOption2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//body/ul/li[2]/a" ) );
    assertEquals( "csv", textOption2 );
    WebElement listOption3 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//body/ul/li[3]/a" ) );
    assertNotNull( listOption3 );
    String textOption3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//body/ul/li[3]/a" ) );
    assertEquals( "xml", textOption3 );
    WebElement listOption4 = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//body/ul/li[4]/a" ) );
    assertNotNull( listOption4 );
    String textOption4 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//body/ul/li[4]/a" ) );
    assertEquals( "json", textOption4 );
  }
}
