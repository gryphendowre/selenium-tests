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
package com.pentaho.ctools.issues.cda;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.pentaho.ctools.suite.CToolsTestSuite;
import com.pentaho.ctools.utils.BaseTest;
import com.pentaho.ctools.utils.DirectoryWatcher;
import com.pentaho.ctools.utils.ElementHelper;

/**
 * The script is testing the issue:
 * - http://jira.pentaho.com/browse/CDA-121
 *
 * and the automation test is described:
 * - http://jira.pentaho.com/browse/QUALITY-1013
 *
 * NOTE
 * To test this script it is required to have CDA plugin installed.
 *
 * Naming convention for test:
 *  'tcN_StateUnderTest_ExpectedBehavior'
 *
 */
public class CDA121 extends BaseTest {
  // The base url to be append the relative url in test
  private final String baseUrl = CToolsTestSuite.getBaseUrl();
  //Download directory
  private final String downloadDir = CToolsTestSuite.getDownloadDir();
  // The path for the export file
  private final String exportFilePath = this.downloadDir + "\\cda-export.xls";
  //Access to wrapper for webdriver
  private final ElementHelper elemHelper = new ElementHelper();
  // Log instance
  private final Logger log = LogManager.getLogger( CDA121.class );

  /**
   * ############################### Test Case 1 ###############################
   *
   * Test Case Name:
   *    Asserting that export to excel of sql query works
   * Description:
   *    The test pretends validate the CDA-121 issue, asserting that export to excel of excel query works.
   *
   * Steps:
   *    1. Open sql-jdbc CDA sample and assert elements on page and select "Sql Query on SampleData - Jdbc" on "dataAccessSelector"
   *    2. Wait for and assert elements and text on page
   *    3. Export file and assure it has same md5 as expected
   *
   */
  @Test
  public void tc01_CdaFileViewer_ExcelSqlQuery() {
    this.log.info( "tc01_CdaFileViewer_ExcelSqlQuery" );

    /*
     * ## Step 1
     */
    //Go to sql-jdbc CDA sample
    this.driver.get( this.baseUrl + "plugin/cda/api/previewQuery?path=/public/plugin-samples/cda/cdafiles/sql-jdbc.cda" );

    //wait for invisibility of waiting pop-up
    this.elemHelper.WaitForElementInvisibility( this.driver, By.xpath( "//div[@class='busy-indicator-container waitPopup']" ) );

    //Wait for buttons: button, Cache This AND Query URL
    WebElement element = this.elemHelper.FindElement( this.driver, By.id( "dataAccessSelector" ) );
    assertNotNull( element );
    Select select = new Select( this.elemHelper.FindElement( this.driver, By.id( "dataAccessSelector" ) ) );
    select.selectByVisibleText( "Sql Query on SampleData - Jdbc" );
    element = this.elemHelper.FindElement( this.driver, By.xpath( "//button[@id='button']" ) );
    assertNotNull( element );
    element = this.elemHelper.FindElement( this.driver, By.xpath( "//button[@id='cachethis']" ) );
    assertNotNull( element );
    element = this.elemHelper.FindElement( this.driver, By.xpath( "//button[@id='queryUrl']" ) );
    assertNotNull( element );

    /*
     * ## Step 2
     */
    //wait to render page
    this.elemHelper.WaitForElementInvisibility( this.driver, By.xpath( "//div[@class='blockUI blockOverlay']" ) );
    //Check the presented contains
    WebElement elemStatus = this.elemHelper.FindElement( this.driver, By.id( "status" ) );
    assertEquals( "Shipped", elemStatus.getAttribute( "value" ) );
    elemStatus = this.elemHelper.FindElement( this.driver, By.id( "orderDate" ) );
    assertEquals( "2003-03-01", elemStatus.getAttribute( "value" ) );
    //Check text on table
    String columnOneRowOne = this.elemHelper.WaitForElementPresentGetText( this.driver, By.xpath( "//table[@id='contents']/tbody/tr/td" ) );
    String columnTwoRowOne = this.elemHelper.WaitForElementPresentGetText( this.driver, By.xpath( "//table[@id='contents']/tbody/tr/td[2]" ) );
    assertEquals( "Shipped", columnOneRowOne );
    assertEquals( "2003", columnTwoRowOne );

    /*
     * ## Step 3
     */
    WebElement buttonExport = this.elemHelper.FindElement( this.driver, By.id( "export" ) );
    assertNotNull( buttonExport );
    try {
      //Delete the existence if exist
      new File( this.exportFilePath ).delete();

      //Click to export
      buttonExport.click();

      //Wait for file to be created in the destination dir
      DirectoryWatcher dw = new DirectoryWatcher();
      dw.WatchForCreate( this.downloadDir );

      //Check if the file really exist
      File exportFile = new File( this.exportFilePath );
      assertTrue( exportFile.exists() );

      //Wait for the file to be downloaded totally
      for ( int i = 0; i < 50; i++ ) { //we only try 50 times == 5000 ms
        long nSize = FileUtils.sizeOf( exportFile );
        //Since the file always contents the same data, we wait for the expected bytes
        if ( nSize >= 249856 ) {
          break;
        }
        Thread.sleep( 100 );
      }

      //Check if the file downloaded is the expected
      String md5 = DigestUtils.md5Hex( Files.readAllBytes( exportFile.toPath() ) );
      assertEquals( md5, "7091d02397d0f5b978f74809ebec34e2" );

      //The delete file
      DeleteFile();

    } catch ( Exception e ) {
      this.log.error( e.getMessage() );
    }
  }

  /**
   * The function will delete the export file.
   */
  public void DeleteFile() {
    try {
      Files.deleteIfExists( Paths.get( this.exportFilePath ) );
    } catch ( Exception e ) {
      this.log.error( e.getMessage() );
    }
  }

  @AfterTest
  public void tearDown() {
    this.log.info( "tearDown##" + CDA121.class.getSimpleName() );
    //In case something went wrong we delete the file
    DeleteFile();
  }
}