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
package com.pentaho.ctools.cda.samples;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.CoreMatchers;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pentaho.ctools.utils.DirectoryWatcher;
import com.pentaho.ctools.utils.ElementHelper;
import com.pentaho.ctools.utils.PageUrl;
import com.pentaho.selenium.BaseTest;

/**
 * Testing the functionalities related with Mondrianjndi.
 *
 * Naming convention for test: 'tcN_StateUnderTest_ExpectedBehavior'
 *
 */
public class MondrianJNDI extends BaseTest {
  // Access to wrapper for webdriver
  private final ElementHelper elemHelper = new ElementHelper();
  // Log instance
  private final Logger log = LogManager.getLogger( MondrianJNDI.class );

  /**
   * ############################### Test Case 0 ###############################
   *
   * Test Case Name: Open Sample Page
   */
  @Test
  public void tc0_OpenSamplePage_Display() {
    // This samples is in: Public/plugin-samples/CDA/cdafiles/mondrian-jndi
    this.elemHelper.Get( driver, PageUrl.MONDRIAN_JNDI );
  }

  /**
   * ############################### Test Case 1 ###############################
   *
   * Test Case Name: 
   *    Page Content 
   * 
   * Description: 
   *    The test case pretends to validate if the field filename and about are
   *    present and the about works as expected. 
   * 
   * Steps: 
   *    1. Check if we are reading the correct file 
   *    2. Check if About is working
   */
  @Test
  public void tc1_PageContent_DisplayeFilenameAndAbout() {
    this.log.info( "tc1_PageContent_DisplayeFilenameAndAbout" );

    /*
     * ## Step 1
     */
    // Check if the field 'filename' exist and expected value

    String filename = this.elemHelper.WaitForTextPresence( driver, By.id( "fileid" ), "/public/plugin-samples/cda/cdafiles/mondrian-jndi.cda" );
    String pleaseSelect = this.elemHelper.WaitForTextPresence( driver, By.id( "pleaseselect" ), "Please select a Data Access ID" );

    assertEquals( "/public/plugin-samples/cda/cdafiles/mondrian-jndi.cda", filename );
    assertEquals( "Please select a Data Access ID", pleaseSelect );

    /*
     * ## Step 2
     */
    // Check the About
    this.elemHelper.FindElement( driver, By.linkText( "About" ) ).click();
    // element 'fileid'
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.id( "fileid" ) );
    String textElementFileid = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "fileid" ) );
    // element image
    WebElement elemImg = this.elemHelper.FindElement( driver, By.cssSelector( "img" ) );
    String imgAttrSrc = elemImg.getAttribute( "src" );
    // element //div[@id='aboutSubContainerLeft']/div[2]/p
    String textFirstParagExpected = "CDA is a Pentaho plugin designed for accessing data with great flexibility. Born to overcoming some cons of the older implementation, CDA allows you to access any of the various Pentaho data sources and:";
    String textFirstParag = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='aboutSubContainerLeft']/div[2]/p" ) );
    // element //div[@id='aboutSubContainerLeft']/div[2]/p[2]
    String textSecondParagExpected = "CDA can be used as a standalone plugin on the Pentaho BI server or in combination with CDE / CDF.";
    String textSecondParag = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='aboutSubContainerLeft']/div[2]/p[2]" ) );

    assertEquals( "Hello!", textElementFileid );
    assertEquals( baseUrl + "api/repos/cda/static/img/cda.png", imgAttrSrc );
    assertEquals( textFirstParagExpected, textFirstParag );
    assertEquals( textSecondParagExpected, textSecondParag );

    // Back to Mondrian JNDI
    this.elemHelper.Click( driver, By.linkText( "Back to CDA" ) );
    // Check if we are in the correct page
    filename = this.elemHelper.WaitForTextPresence( driver, By.id( "fileid" ), "/public/plugin-samples/cda/cdafiles/mondrian-jndi.cda" );
    pleaseSelect = this.elemHelper.WaitForTextPresence( driver, By.id( "pleaseselect" ), "Please select a Data Access ID" );

    assertEquals( "/public/plugin-samples/cda/cdafiles/mondrian-jndi.cda", filename );
    assertEquals( "Please select a Data Access ID", pleaseSelect );
    assertNotNull( this.elemHelper.FindElement( driver, By.linkText( "About" ) ) );
  }

  /**
   * ############################### Test Case 2 ###############################
   *
   * Test Case Name: 
   *    Select Data Access
   * 
   * Description: 
   *    The test case pretends to validate the data presented when we
   *    select a data access, checking the ordering and search. 
   * 
   * Steps: 
   *    1. Select data access 'Mdx Query on SampleData - Jndi' 
   *    2. Order by Year
   *    3. Order by Price
   *    4. Order by PriceInk
   *    5. Search existence content
   *    6. Search inexistance content
   */
  @Test
  public void tc2_SelectDataAccess_DisplayDataForSelectedDataAccess() {
    this.log.info( "tc2_SelectDataAccess_DisplayDataForSelectedDataAccess" );

    /*
     * ## Step 1
     */
    Select select = new Select( this.elemHelper.FindElement( driver, By.id( "dataAccessSelector" ) ) );
    select.selectByVisibleText( "Mdx Query on SampleData - Jndi" );
    // wait to render page
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ), 60 );
    // Check the presented contains
    WebElement elemStatus = this.elemHelper.FindElement( driver, By.id( "status" ) );
    assertEquals( "Shipped", elemStatus.getAttribute( "value" ) );
    // Check we have three elements and no more than that
    String textPaging = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "contents_info" ) );
    assertEquals( "View 1 to 3 of 3 elements", textPaging );
    // Column Time
    String columnOneRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td" ) );
    String columnOneRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td" ) );
    String columnOneRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td" ) );
    assertEquals( "All Years", columnOneRowOne );
    assertEquals( "All Years", columnOneRowTwo );
    assertEquals( "All Years", columnOneRowThree );
    // Column Year
    String columnTwoRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[2]" ) );
    String columnTwoRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td[2]" ) );
    String columnTwoRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td[2]" ) );
    assertEquals( "2003", columnTwoRowOne );
    assertEquals( "2004", columnTwoRowTwo );
    assertEquals( "2005", columnTwoRowThree );
    // Column Price
    String columnThreeRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[3]" ) );
    String columnThreeRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td[3]" ) );
    String columnThreeRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td[3]" ) );
    assertEquals( "3573701.2500000023", columnThreeRowOne );
    assertEquals( "4750205.889999998", columnThreeRowTwo );
    assertEquals( "1513074.4600000002", columnThreeRowThree );
    // Column PriceInk
    String columnFourRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[4]" ) );
    String columnFourRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td[4]" ) );
    String columnFourRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td[4]" ) );
    assertEquals( "3.5737012500000023", columnFourRowOne );
    assertEquals( "4.750205889999998", columnFourRowTwo );
    assertEquals( "1.5130744600000001", columnFourRowThree );

    /*
     * ## Step 2
     */
    // Click Asc
    this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='contents']/thead/tr/th[2]" ) );
    // Click Desc
    this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='contents']/thead/tr/th[2]" ) );
    // Column Time
    columnOneRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td" ) );
    columnOneRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td" ) );
    columnOneRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td" ) );
    assertEquals( "All Years", columnOneRowOne );
    assertEquals( "All Years", columnOneRowTwo );
    assertEquals( "All Years", columnOneRowThree );
    // Column Year
    columnTwoRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[2]" ) );
    columnTwoRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td[2]" ) );
    columnTwoRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td[2]" ) );
    assertEquals( "2005", columnTwoRowOne );
    assertEquals( "2004", columnTwoRowTwo );
    assertEquals( "2003", columnTwoRowThree );
    // Column Price
    columnThreeRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[3]" ) );
    columnThreeRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td[3]" ) );
    columnThreeRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td[3]" ) );
    assertEquals( "1513074.4600000002", columnThreeRowOne );
    assertEquals( "4750205.889999998", columnThreeRowTwo );
    assertEquals( "3573701.2500000023", columnThreeRowThree );
    // Column PriceInk
    columnFourRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[4]" ) );
    columnFourRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td[4]" ) );
    columnFourRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td[4]" ) );
    assertEquals( "1.5130744600000001", columnFourRowOne );
    assertEquals( "4.750205889999998", columnFourRowTwo );
    assertEquals( "3.5737012500000023", columnFourRowThree );

    /*
     * ## Step 3
     */
    // Click Asc
    this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='contents']/thead/tr/th[3]" ) );
    // Click Desc
    this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='contents']/thead/tr/th[3]" ) );
    // Column Time
    columnOneRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td" ) );
    columnOneRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td" ) );
    columnOneRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td" ) );
    assertEquals( "All Years", columnOneRowOne );
    assertEquals( "All Years", columnOneRowTwo );
    assertEquals( "All Years", columnOneRowThree );
    // Column Year
    columnTwoRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[2]" ) );
    columnTwoRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td[2]" ) );
    columnTwoRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td[2]" ) );
    assertEquals( "2004", columnTwoRowOne );
    assertEquals( "2003", columnTwoRowTwo );
    assertEquals( "2005", columnTwoRowThree );
    // Column Price
    columnThreeRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[3]" ) );
    columnThreeRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td[3]" ) );
    columnThreeRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td[3]" ) );
    assertEquals( "4750205.889999998", columnThreeRowOne );
    assertEquals( "3573701.2500000023", columnThreeRowTwo );
    assertEquals( "1513074.4600000002", columnThreeRowThree );
    // Column PriceInk
    columnFourRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[4]" ) );
    columnFourRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td[4]" ) );
    columnFourRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td[4]" ) );
    assertEquals( "4.750205889999998", columnFourRowOne );
    assertEquals( "3.5737012500000023", columnFourRowTwo );
    assertEquals( "1.5130744600000001", columnFourRowThree );

    /*
     * ## Step 4
     */
    // Click Asc
    this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='contents']/thead/tr/th[4]" ) );
    // Click Desc
    this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='contents']/thead/tr/th[4]" ) );
    // Column Time
    columnOneRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td" ) );
    columnOneRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td" ) );
    columnOneRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td" ) );
    assertEquals( "All Years", columnOneRowOne );
    assertEquals( "All Years", columnOneRowTwo );
    assertEquals( "All Years", columnOneRowThree );
    // Column Year
    columnTwoRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[2]" ) );
    columnTwoRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td[2]" ) );
    columnTwoRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td[2]" ) );
    assertEquals( "2004", columnTwoRowOne );
    assertEquals( "2003", columnTwoRowTwo );
    assertEquals( "2005", columnTwoRowThree );
    // Column Price
    columnThreeRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[3]" ) );
    columnThreeRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td[3]" ) );
    columnThreeRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td[3]" ) );
    assertEquals( "4750205.889999998", columnThreeRowOne );
    assertEquals( "3573701.2500000023", columnThreeRowTwo );
    assertEquals( "1513074.4600000002", columnThreeRowThree );
    // Column PriceInk
    columnFourRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[4]" ) );
    columnFourRowTwo = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[2]/td[4]" ) );
    columnFourRowThree = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr[3]/td[4]" ) );
    assertEquals( "4.750205889999998", columnFourRowOne );
    assertEquals( "3.5737012500000023", columnFourRowTwo );
    assertEquals( "1.5130744600000001", columnFourRowThree );

    /*
     * ## Step 5
     */
    this.elemHelper.ClearAndSendKeys( driver, By.cssSelector( "#contents_filter input" ), "2004" );
    // Check we have only one element
    textPaging = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "contents_info" ) );
    assertEquals( "View 1 to 1 of 1 elements (filter 3 elements)", textPaging );
    // Column Time
    columnOneRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td" ) );
    assertEquals( "All Years", columnOneRowOne );
    // Column Year
    columnTwoRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[2]" ) );
    assertEquals( "2004", columnTwoRowOne );
    // Column Price
    columnThreeRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[3]" ) );
    assertEquals( "4750205.889999998", columnThreeRowOne );
    // Column PriceInk
    columnFourRowOne = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td[4]" ) );
    assertEquals( "4.750205889999998", columnFourRowOne );

    /*
     * ## Step 6
     */
    this.elemHelper.ClearAndSendKeys( driver, By.cssSelector( "#contents_filter input" ), "Merry" );
    // Check we have only one element
    textPaging = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "contents_info" ) );
    assertEquals( "empty (filter 3 elements)", textPaging );
    // Check if table is empty
    String textNoResult = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='contents']/tbody/tr/td" ) );
    assertEquals( "No results.", textNoResult );

    // Clean data
    this.elemHelper.ClearAndSendKeys( driver, By.cssSelector( "#contents_filter input" ), Keys.RETURN );

    // Order by Year
    this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='contents']/thead/tr/th[2]" ) );
    // Order by Time
    this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='contents']/thead/tr/th[1]" ) );
  }

  /**
   * ############################### Test Case 3 ###############################
   *
   * Test Case Name: Export XLS Description: The test case pretends to validate the functionality to export to excel.
   * Steps: 1. Press in Export.
   */
  @Test
  public void tc3_ExportXls_FileDownload() {
    this.log.info( "tc3_ExportXls_FileDownload" );

    /*
     * ## Step 1
     */
    // Check we have three elements and no more than that
    String textPaging = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "contents_info" ) );
    assertEquals( "View 1 to 3 of 3 elements", textPaging );

    // Click in export as xls
    WebElement buttonExport = this.elemHelper.WaitForElementPresenceAndVisible( driver, By.cssSelector( "button#export.cdaButton" ) );
    assertNotNull( buttonExport );
    try {
      // Delete the existence if exist
      new File( downloadDir + "\\cda-export.xls" ).delete();

      // Click to export
      this.elemHelper.MouseOverElementAndClick( driver, By.cssSelector( "button#export.cdaButton" ) );

      // Wait for file to be created in the destination dir
      DirectoryWatcher dw = new DirectoryWatcher();
      dw.WatchForCreate( downloadDir );

      // Check if file was download
      boolean fileExist = new File( downloadDir + "\\cda-export.xls" ).exists();
      if ( fileExist == false ) {
        // >> Retrying to download the file again
        this.elemHelper.MouseOverElementAndClick( driver, By.cssSelector( "button#export.cdaButton" ) );

        // Wait for file to be created in the destination dir
        dw.WatchForCreate( downloadDir );

        fileExist = new File( downloadDir + "\\cda-export.xls" ).exists();
      }
      assertTrue( fileExist );

      new File( downloadDir + "\\cda-export.xls" ).delete();
    } catch ( Exception e ) {
      this.log.error( e.getMessage() );
    }
  }

  /**
   * ############################### Test Case 4 ###############################
   *
   * Test Case Name: 
   *    Query URL 
   * 
   * Description: 
   *    The test case pretends to validate return data when call query url.
   *    
   * Steps:
   *  1. Check query url dialog
   *  2. Open a new browser with query url
   */
  @Test
  public void tc4_QueryURL_ReturnValueIsTheSameDisplayedInPage() {
    this.log.info( "tc4_QueryURL_ReturnValueIsTheSameDisplayedInPage" );

    /*
     * ## Step 1
     */
    // Check we have three elements and no more than that
    String textPaging = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "contents_info" ) );
    assertEquals( textPaging, "View 1 to 3 of 3 elements" );

    // Check query url
    WebElement buttonQueryUrl = this.elemHelper.WaitForElementPresence( driver, By.id( "queryUrl" ) );
    assertEquals( buttonQueryUrl.getText(), "Query URL" );
    this.elemHelper.ClickJS( driver, By.id( "queryUrl" ) );

    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.id( "queryUrlDialog" ) );
    String diaLOGTitle = this.elemHelper.WaitForElementPresentGetText( driver, By.cssSelector( "div#queryUrlDialog p.dialogTitle" ) );
    assertEquals( diaLOGTitle, "Query Execution URL:" );

    WebElement inputQueryUrl = this.elemHelper.FindElement( driver, By.id( "doQueryUrl" ) );
    String queryUrl = inputQueryUrl.getAttribute( "value" );
    this.log.debug( "Query URL: " + queryUrl );

    this.elemHelper.ClickJS( driver, By.cssSelector( "#queryUrlDialog a.jqmClose" ) );
    boolean dialogQueryUrl = this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "#queryUrlDialog" ) );
    assertTrue( dialogQueryUrl );

    /*
     * ## Step 2
     */
    this.elemHelper.Get( driver, queryUrl );
    String jsonQueryActual = this.elemHelper.WaitForTextDifferentEmpty( driver, By.cssSelector( "body" ) );
    String jsonQueryExpected = "{\"queryInfo\":{\"totalRows\":\"3\"},\"resultset\":[[\"All Years\",\"2003\",3573701.2500000023,3.5737012500000023],[\"All Years\",\"2004\",4750205.889999998,4.750205889999998],[\"All Years\",\"2005\",1513074.4600000002,1.5130744600000002]],\"metadata\":[{\"colIndex\":0,\"colType\":\"String\",\"colName\":\"[Time].[(All)]\"},{\"colIndex\":1,\"colType\":\"String\",\"colName\":\"Year\"},{\"colIndex\":2,\"colType\":\"Numeric\",\"colName\":\"price\"},{\"colIndex\":3,\"colType\":\"Numeric\",\"colName\":\"PriceInK\"}]}";
    JsonParser parser = new JsonParser();
    JsonElement actual = parser.parse( jsonQueryActual );
    JsonElement expected = parser.parse( jsonQueryExpected );
    assertEquals( actual, expected );

    this.elemHelper.Get( driver, PageUrl.MONDRIAN_JNDI );
    String filename = this.elemHelper.WaitForTextPresence( driver, By.id( "fileid" ), "/public/plugin-samples/cda/cdafiles/mondrian-jndi.cda" );
    String pleaseSelect = this.elemHelper.WaitForTextPresence( driver, By.id( "pleaseselect" ), "Please select a Data Access ID" );
    assertEquals( "/public/plugin-samples/cda/cdafiles/mondrian-jndi.cda", filename );
    assertEquals( "Please select a Data Access ID", pleaseSelect );
  }

  /**
   * ############################### Test Case 4 ###############################
   *
   * Test Case Name: 
   *    Query URL
   * 
   * Description: 
   *    The test case pretends to validate return data when call query url.
   *    
   * Steps:
   *    1. Select the option 'Mdx...'
   *    2. Click in Cache this
   *    3. Set a period
   *    4. In the new window, check the schedule
   *    5. Remove the schedule
   */
  @Test
  public void tc5_CacheThisSimple_ScheduleIsSetSuccessful() {
    this.log.info( "tc5_CacheThisSimple_ScheduleIsSetSuccessful" );
    String selectedHours = "21";

    /*
     * ## Step 1
     */
    Select select = new Select( this.elemHelper.FindElement( driver, By.id( "dataAccessSelector" ) ) );
    select.selectByVisibleText( "Mdx Query on SampleData - Jndi" );
    // wait to render page
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    // Check the presented contains
    assertEquals( this.elemHelper.GetAttribute( driver, By.id( "status" ), "value" ), "Shipped" );
    // Check we have three elements and no more than that
    String textPaging = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "contents_info" ) );
    assertEquals( "View 1 to 3 of 3 elements", textPaging );

    /*
     * ## Step 2
     */
    // Click in 'Cache this'
    this.elemHelper.ClickJS( driver, By.id( "cachethis" ) );
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.id( "dialog" ) );
    String questionActual = this.elemHelper.WaitForElementPresentGetText( driver, By.cssSelector( "p.dialogTitle" ) );
    String questionExpect = "What schedule should this query run on? (advanced)";
    assertEquals( questionExpect, questionActual );

    /*
     * ## Step 3
     */
    String parentWindowHandle = driver.getWindowHandle();
    Select selectPeriod = new Select( this.elemHelper.FindElement( driver, By.id( "periodType" ) ) );
    selectPeriod.selectByValue( "1" ); // every day

    this.elemHelper.ClearAndSendKeys( driver, By.id( "startAt" ), selectedHours );
    this.elemHelper.Click( driver, By.linkText( "Ok" ) );

    Set<String> listWindows = driver.getWindowHandles();
    // wait for popup render
    this.elemHelper.WaitForNewWindow( driver );
    listWindows = driver.getWindowHandles();
    // Get popup id
    WebDriver cdaCacheManager = null;
    Iterator<String> iterWindows = listWindows.iterator();
    while ( iterWindows.hasNext() ) {
      String windowHandle = iterWindows.next();
      cdaCacheManager = driver.switchTo().window( windowHandle );
      if ( cdaCacheManager.getTitle().equals( "CDA Cache Manager" ) ) {
        break;
      }
    }
    // Validate page:
    // Title
    if ( cdaCacheManager != null ) {
      String titleCdaCacheManager = cdaCacheManager.getTitle();
      assertEquals( "CDA Cache Manager", titleCdaCacheManager );
      // Scheduled queries
      String subTitleText = this.elemHelper.WaitForElementPresentGetText( cdaCacheManager, By.xpath( "//div[@id='scheduledQueries']/div" ) );
      assertEquals( "Scheduled Queries", subTitleText );

      /*
       * ## Step 4
       */
      // Validate Query
      SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
      String strToday = sdf.format( new Date() );
      String queryName = this.elemHelper.WaitForElementPresentGetText( cdaCacheManager, By.xpath( "//div[@id='lines']/div/div[1]" ) );
      String queryParam1 = this.elemHelper.WaitForElementPresentGetText( cdaCacheManager, By.xpath( "//div[@id='lines']/div/div[2]//dl//dt" ) );
      String queryParam2 = this.elemHelper.WaitForElementPresentGetText( cdaCacheManager, By.xpath( "//div[@id='lines']/div/div[2]//dl//dd" ) );
      String queryLExec = this.elemHelper.WaitForElementPresentGetText( cdaCacheManager, By.xpath( "//div[@id='lines']/div/div[3]" ) );
      String queryNExec = this.elemHelper.WaitForElementPresentGetText( cdaCacheManager, By.xpath( "//div[@id='lines']/div/div[4]" ) );
      String queryCron = this.elemHelper.WaitForElementPresentGetText( cdaCacheManager, By.xpath( "//div[@id='lines']/div/div[5]" ) );
      String queryTime = this.elemHelper.WaitForElementPresentGetText( cdaCacheManager, By.xpath( "//div[@id='lines']/div/div[6]" ) );
      String queryStatus = this.elemHelper.WaitForElementPresentGetText( cdaCacheManager, By.xpath( "//div[@id='lines']/div/div[7]" ) );

      // AssertPopup for execute and delete
      WebElement delete = this.elemHelper.FindElement( cdaCacheManager, By.xpath( "//a/img[@title='Remove Query']" ) );
      WebElement execute = this.elemHelper.FindElement( cdaCacheManager, By.xpath( "//a/img[@title='Execute Query']" ) );
      assertNotNull( execute );
      assertNotNull( delete );

      // Validate execution details
      try {
        assertEquals( "/public/plugin-samples/cda/cdafiles/mondrian-jndi.cda (1)", queryName );
        assertEquals( "status", queryParam1 );
        assertEquals( "Shipped", queryParam2 );
        assertThat( "Last Execution: " + queryLExec, queryLExec, CoreMatchers.containsString( "1970-01-01" ) );
        assertThat( "Last Execution: " + queryLExec, queryLExec, CoreMatchers.containsString( "01:00:00" ) );
        assertThat( "Next Execution: " + queryNExec, queryNExec, CoreMatchers.containsString( strToday ) );
        assertThat( "Next Execution: " + queryNExec, queryNExec, CoreMatchers.containsString( selectedHours + ":00:00" ) );
        assertEquals( "0 0 21 * * ? *", queryCron );
        assertEquals( "-1", queryTime );
        assertEquals( "Success", queryStatus );
      } catch ( AssertionError ae ) {
        this.log.error( ae.getMessage() );

        // Remove schedule
        this.removeFirstSchedule();

        // Need guarantee we close everything
        cdaCacheManager.close();
        driver.switchTo().window( parentWindowHandle );

        // raise the exception
        fail( ae.getMessage() );
      }

      /*
       * ## Step 5
       */
      this.removeFirstSchedule();

      cdaCacheManager.close();
      driver.switchTo().window( parentWindowHandle );
      assertTrue( driver.getWindowHandles().size() == 1 );
    } else {
      assertNotNull( cdaCacheManager );
    }

  }

  /**
   * This method is usage to remove any schedule entry.
   */
  public void removeFirstSchedule() {
    boolean elementPresent = false;

    elementPresent = this.elemHelper.WaitForElementNotPresent( driver, By.cssSelector( "img.deleteIcon.button" ), 2 );

    while ( !elementPresent ) {
      // Press to delete schedule
      this.elemHelper.ClickJS( driver, By.cssSelector( "img.deleteIcon.button" ) );

      // Wait for pop-up
      String confirmationMsg = this.elemHelper.WaitForAlertReturnConfirmationMsg( driver );
      String expectedCnfText = "Want to delete this scheduler entry?";
      assertEquals( confirmationMsg, expectedCnfText );

      this.elemHelper.WaitForAlertNotPresent( driver );
      this.elemHelper.SwitchToDefault( driver );

      elementPresent = this.elemHelper.WaitForElementNotPresent( driver, By.cssSelector( "img.deleteIcon.button" ), 2 );
    }
  }
}
