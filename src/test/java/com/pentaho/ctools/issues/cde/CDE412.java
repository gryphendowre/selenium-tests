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
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.pentaho.ctools.utils.ElementHelper;
import com.pentaho.ctools.utils.PageUrl;
import com.pentaho.selenium.BaseTest;

/**
 * The script is testing the issue:
 * - http://jira.pentaho.com/browse/CDE-412
 * - http://jira.pentaho.com/browse/CDE-465
 * 
 * and the automation test is described:
 * - http://jira.pentaho.com/browse/QUALITY-993
 * - http://jira.pentaho.com/browse/QUALITY-1091
 * 
 * NOTE
 * To test this script it is required to have CDE plugin installed.
 *
 * Naming convention for test:
 *  'tcN_StateUnderTest_ExpectedBehavior'
 *
 */
public class CDE412 extends BaseTest {
  // Access to wrapper for webdriver
  private final ElementHelper elemHelper = new ElementHelper();
  // Log instance
  private final Logger log = LogManager.getLogger( CDE412.class );

  /**
   * ############################### Test Case 1 ###############################
   *
   * Test Case Name:
   *    Query editor is different for different queries
   *
   * Description:
   *    The test pretends validate the CDE-412 issue, so when user opens a query editor it reflects
   *    the type of query being edited.
   *
   * Steps:
   *    1. Assert elements on page and go to Datasources Panel
   *    2. Add MDX Query element, click Query button and assert title and default text. Remove MDX query
   *    3. Add MQL Query element, click Query button and assert title and default text. Remove MQL query
   *    4. Add Scriptable Query element, assert sample query is shown and language is set to Beanshell (CDE-465),
   *       click Query button and assert title and default text. Remove Scriptable query
   *    5. Add Sql Query element, click Query button and assert title and default text. Remove Sql query
   */
  @Test
  public void tc01_CdeDashboard_QueryEditor() {
    this.log.info( "tc01_CdeDashboard_QueryEditor" );

    /*
     * ## Step 1
     */

    //Go to New CDE Dashboard
    driver.get( PageUrl.CDE_DASHBOARD );
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    //Assert elements on page and go to Datasources Panel
    WebElement element = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@class='datasourcesPanelButton']" ) );
    assertNotNull( element );
    element = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.id( "previewButton" ) );
    assertNotNull( element );
    element = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@class='layoutPanelButton']" ) );
    assertNotNull( element );
    element = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@class='componentsPanelButton']" ) );
    assertNotNull( element );
    this.elemHelper.Click( driver, By.xpath( "//div[@class='datasourcesPanelButton']" ) );

    /*
     * ## Step 2
     */
    //Add MDX query element and click Parameters
    this.elemHelper.ClickElementInvisible( driver, By.xpath( "//a[@title='denormalizedMdx over mondrianJdbc']" ) );
    element = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='table-cdfdd-datasources-properties']/tbody/tr[8]/td[2]/div/button" ) );
    assertNotNull( element );
    this.elemHelper.Click( driver, By.xpath( "//table[@id='table-cdfdd-datasources-properties']/tbody/tr[8]/td[2]/div/button" ) );
    String title = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='wizardDialog']//div[@class='popup-title-container']" ) );
    assertEquals( "MDX Editor", title );
    String text = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//pre[@id='wizardEditor']/div[2]/div/div[3]/div" ) );
    assertEquals( "select {} ON COLUMNS,", text );
    this.elemHelper.Click( driver, By.id( "cdfdd-wizard-button-ok" ) );
    this.elemHelper.Click( driver, By.xpath( "//div[@id='table-cdfdd-datasources-datasourcesOperations']/a[4]" ) );

    /*
     * ## Step 3
     */
    //Add MQL query element and click Parameters
    this.elemHelper.ClickElementInvisible( driver, By.xpath( "//a[@title='mql over metadata']" ) );
    element = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='table-cdfdd-datasources-properties']/tbody/tr[4]/td[2]/div/button" ) );
    assertNotNull( element );
    this.elemHelper.Click( driver, By.xpath( "//table[@id='table-cdfdd-datasources-properties']/tbody/tr[4]/td[2]/div/button" ) );
    title = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='wizardDialog']//div[@class='popup-title-container']" ) );
    assertEquals( "MQL Editor", title );
    text = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//pre[@id='wizardEditor']/div[2]/div/div[3]/div" ) );
    assertEquals( text, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" );
    this.elemHelper.Click( driver, By.id( "cdfdd-wizard-button-ok" ) );
    this.elemHelper.Click( driver, By.xpath( "//div[@id='table-cdfdd-datasources-datasourcesOperations']/a[4]" ) );

    /*
     * ## Step 4
     */
    //Add Scriptable query element and click Parameters
    this.elemHelper.ClickElementInvisible( driver, By.xpath( "//a[@title='scriptable over scripting']" ) );
    element = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='table-cdfdd-datasources-properties']/tbody/tr[5]/td[2]/div/button" ) );
    assertNotNull( element );

    //CDE465
    String codeLanguage = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//td[@title='Language the script is written in']/../td[2]" ) );
    assertEquals( "beanshell", codeLanguage );
    String codeSample = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//td[@title='Query to be executed in the selected datasource']/../td[2]/div/code" ) );
    assertEquals( "import org.pentaho.r (...)", codeSample );

    //View default query
    this.elemHelper.Click( driver, By.xpath( "//table[@id='table-cdfdd-datasources-properties']/tbody/tr[5]/td[2]/div/button" ) );
    title = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='wizardDialog']//div[@class='popup-title-container']" ) );
    assertEquals( "Scriptable Editor", title );
    text = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//pre[@id='wizardEditor']/div[2]/div/div[3]/div" ) );
    assertEquals( "import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;", text );
    this.elemHelper.Click( driver, By.id( "cdfdd-wizard-button-ok" ) );
    this.elemHelper.Click( driver, By.xpath( "//div[@id='table-cdfdd-datasources-datasourcesOperations']/a[4]" ) );

    /*
     * ## Step 5
     */
    //Add SQL query element and click Parameters
    this.elemHelper.ClickElementInvisible( driver, By.xpath( "//a[@title='sql over sqlJdbc']" ) );
    element = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='table-cdfdd-datasources-properties']/tbody/tr[7]/td[2]/div/button" ) );
    assertNotNull( element );
    this.elemHelper.Click( driver, By.xpath( "//table[@id='table-cdfdd-datasources-properties']/tbody/tr[7]/td[2]/div/button" ) );
    title = this.elemHelper.FindElement( driver, By.xpath( "//div[@id='wizardDialog']//div[@class='popup-title-container']" ) ).getText();
    assertEquals( "Sql Editor", title );
    text = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//pre[@id='wizardEditor']/div[2]/div/div[3]/div" ) );
    assertEquals( "", text );
    this.elemHelper.Click( driver, By.id( "cdfdd-wizard-button-ok" ) );
    this.elemHelper.Click( driver, By.xpath( "//div[@id='table-cdfdd-datasources-datasourcesOperations']/a[4]" ) );
  }
}
