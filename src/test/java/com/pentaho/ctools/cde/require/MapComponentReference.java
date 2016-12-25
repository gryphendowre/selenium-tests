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

package com.pentaho.ctools.cde.require;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import com.pentaho.ctools.utils.ElementHelper;
import com.pentaho.ctools.utils.PageUrl;
import com.pentaho.selenium.BaseTest;

/**
 * Test all different maps with markers, no markers, shapes.
 *
 * Naming convention for test:
 *  'tcN_StateUnderTest_ExpectedBehavior'
 *
 * Issues History:
 * -
 *
 */
public class MapComponentReference extends BaseTest {
  // Access to wrapper for webdriver
  private final ElementHelper elemHelper = new ElementHelper();
  //Log instance
  private final Logger log = LogManager.getLogger( MapComponentReference.class );

  /**
   * ############################### Test Case 0 ###############################
   *
   * Test Case Name:
   *    Open the sample page
   */
  @Test
  public void tc0_OpenSamplePage_Display() {
    this.log.info( "tc0_OpenSamplePage_Display" );

    //Go to MapComponentReference
    driver.get( PageUrl.MAP_COMPONENT_REFERENCE_REQUIRE );

    // NOTE - we have to wait for loading disappear
    this.elemHelper.WaitForElementPresence( driver, By.cssSelector( "div.blockUI.blockOverlay" ), 180 );
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );

    //Wait for page render on each map test
    this.elemHelper.WaitForTextPresence( driver, By.xpath( "//div[@id='simpleTest']/div/div[8]/div" ), "2000 km", 90 );
    this.elemHelper.WaitForTextPresence( driver, By.xpath( "//div[@id='testTileServices']/div/div[8]/div" ), "2000 km", 90 );
    //Wait for the three marks
    this.elemHelper.WaitForElementPresence( driver, By.xpath( "//div[@id='testWithMarker']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image']" ), 90 );
    this.elemHelper.WaitForElementPresence( driver, By.xpath( "//div[@id='testWithMarker']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image'][2]" ), 90 );
    this.elemHelper.WaitForElementPresence( driver, By.xpath( "//div[@id='testWithMarker']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image'][3]" ), 90 );
    //Wait for the three marks
    this.elemHelper.WaitForElementPresence( driver, By.xpath( "//div[@id='testWithGeoLocalization']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image']" ), 90 );
    this.elemHelper.WaitForElementPresence( driver, By.xpath( "//div[@id='testWithGeoLocalization']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image'][2]" ), 90 );
    this.elemHelper.WaitForElementPresence( driver, By.xpath( "//div[@id='testWithGeoLocalization']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image'][3]" ), 90 );
    this.elemHelper.WaitForElementPresence( driver, By.xpath( "//div[@id='testWithGeoLocalization']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image'][4]" ), 90 );
    //Wait for shapes
    this.elemHelper.WaitForElementPresence( driver, By.xpath( "//div[@id='testWithShapes']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][2]/*[local-name()='g']/*[local-name()='path'][2]" ), 90 );

    //Just check if the sample title is displayed.
    String actualSampleTitle = this.elemHelper.WaitForElementPresentGetText( driver, By.cssSelector( "div#title span" ) );
    assertEquals( "Map Component Reference", actualSampleTitle );
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
   *    2. Check Global Options
   *    3. Check Options valid in Marker mode
   *    4. Check Location Result Set in Marker mode
   *    5. Check Options valid in Shapes mode
   */
  @Test
  public void tc1_PageContent_DisplayTitle() {
    this.log.info( "tc1_PageContent_DisplayTitle" );

    /*
     * ## Step 1
     */
    // Validate the sample that we are testing is the one
    assertEquals( "Map Component Reference", driver.getTitle() );
    String sampleTitle = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='title']/span" ) );
    assertEquals( "Map Component Reference", sampleTitle );
    String sampleDesc = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/p" ) );
    assertEquals( "This component allows the user to either navigate through the map and see information about marked locations, or to represent quantities as the fill color of a set of shapes/regions.", sampleDesc );

    /*
     * ## Step 2
     */
    String subTitleGlobalOptions = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/h3" ) );
    assertEquals( "Global Options", subTitleGlobalOptions );
    String goItem1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd/b" ) );
    String goItem2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd[2]/b" ) );
    String goItem3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd[3]/b" ) );
    String goItem4 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd[4]/b" ) );
    String goItem5 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd[5]/b" ) );
    String goItem6 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd[6]/b" ) );
    String goItem7 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd[7]/b" ) );
    String goItem8 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd[8]/b" ) );
    String goItem9 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd[9]/b" ) );
    String goItem10 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd[10]/b" ) );
    String goItem11 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd[11]/b" ) );
    String goItem12 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd[12]/b" ) );
    String goItem13 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd[13]/b" ) );
    String goItem14 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl/dd[14]/b" ) );
    assertEquals( "listeners:", goItem1 );
    assertEquals( "parameters:", goItem2 );
    assertEquals( "htmlObject:", goItem3 );
    assertEquals( "executeAtStart:", goItem4 );
    assertEquals( "preExecution:", goItem5 );
    assertEquals( "postExecution:", goItem6 );
    assertEquals( "Center Longitude:", goItem7 );
    assertEquals( "Center Latitude:", goItem8 );
    assertEquals( "Default zoom Level:", goItem9 );
    assertEquals( "Datasource:", goItem10 );
    assertEquals( "Map Engine:", goItem11 );
    assertEquals( "API_KEY:", goItem12 );
    assertEquals( "Tilesets to display as layers:", goItem13 );
    assertEquals( "Operation Mode:", goItem14 );

    /*
     * ## Step 3
     */
    String subTitleOptionsMarker = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/h3[2]" ) );
    assertEquals( "Options valid in Marker mode", subTitleOptionsMarker );
    String omItem1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[2]/dd/b" ) );
    String omItem2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[2]/dd[2]/b" ) );
    String omItem3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[2]/dd[3]/b" ) );
    String omItem4 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[2]/dd[4]/b" ) );
    String omItem5 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[2]/dd[5]/b" ) );
    String omItem6 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[2]/dd[6]/b" ) );
    String omItem7 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[2]/dd[7]/b" ) );
    String omItem8 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[2]/dd[8]/b" ) );
    String omItem9 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[2]/dd[9]/b" ) );
    String omItem10 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[2]/dd[10]/b" ) );
    assertEquals( "Marker image:", omItem1 );
    assertEquals( "Marker Width:", omItem2 );
    assertEquals( "Marker Height:", omItem3 );
    assertEquals( "Marker Click Parameters:", omItem4 );
    assertEquals( "Marker click Function:", omItem5 );
    assertEquals( "Cgg Graph for Marker:", omItem6 );
    assertEquals( "Cgg Graph Parameters:", omItem7 );
    assertEquals( "Div for popup window:", omItem8 );
    assertEquals( "Popup Width:", omItem9 );
    assertEquals( "Popup Height:", omItem10 );

    /*
     * ## Step 4
     */
    String subTitleLocationMarker = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/h3[3]" ) );
    assertEquals( "Location Result Set in Marker mode", subTitleLocationMarker );
    String lmItem1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd/b" ) );
    String lmItem2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[2]/b" ) );
    String lmItem3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[3]/b" ) );
    String lmItem4 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[4]/b" ) );
    String lmItem5 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[5]/b" ) );
    String lmItem6 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[6]/b" ) );
    String lmItem7 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[7]/b" ) );
    String lmItem8 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[8]/b" ) );
    String lmItem9 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[9]/b" ) );
    String lmItem10 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[10]/b" ) );
    String lmItem11 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[11]/b" ) );
    String lmItem12 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[12]/b" ) );
    String lmItem13 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[13]/b" ) );
    String lmItem14 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[14]/b" ) );
    String lmItem15 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[3]/dd[15]/b" ) );
    assertEquals( "Longitude:", lmItem1 );
    assertEquals( "Latitude:", lmItem2 );
    assertEquals( "Address:", lmItem3 );
    assertEquals( "City:", lmItem4 );
    assertEquals( "County:", lmItem5 );
    assertEquals( "State:", lmItem6 );
    assertEquals( "Region:", lmItem7 );
    assertEquals( "Country:", lmItem8 );
    assertEquals( "Description:", lmItem9 );
    assertEquals( "marker:", lmItem10 );
    assertEquals( "markerWidth:", lmItem11 );
    assertEquals( "markerHeight:", lmItem12 );
    assertEquals( "popupContents:", lmItem13 );
    assertEquals( "popupWidth:", lmItem14 );
    assertEquals( "popupHeight:", lmItem15 );

    /*
     * ## Step 5
     */
    String subTitleOptionsSahpes = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/h3[4]" ) );
    assertEquals( "Options valid in Shapes mode", subTitleOptionsSahpes );
    String osItem1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[4]/dd/b" ) );
    String osItem2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[4]/dd[2]/b" ) );
    String osItem3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[4]/dd[3]/b" ) );
    String osItem4 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='documentation']/dl[4]/dd[4]/b" ) );
    assertEquals( "Shape Definitions:", osItem1 );
    assertEquals( "Shape Mouse Over Function:", osItem2 );
    assertEquals( "Shape Mouse Out Function:", osItem3 );
    assertEquals( "Shape Mouse Click Function:", osItem4 );
  }

  /**
   * ############################### Test Case 2 ###############################
   *
   * Test Case Name:
   *    Map with no markers and no shapes
   * Description:
   *    Validate contents in a simple map.
   * Steps:
   *    1. Check the data exist
   *    2. Perform a zoom
   *    3. Click in Globe
   */
  @Test
  public void tc2_MapWithNoMarkersAndShapes_MapMustBeVisible() {
    this.log.info( "tc2_MapWithNoMarkersAndShapes_MapMustBeVisible" );
    //wait for initialize
    wait.until( ExpectedConditions.invisibilityOfElementWithText( By.xpath( "//div[@id='simpleTest']/div/div[8]/div" ), "10000 km" ) );

    //## Step1
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='subtitle0']/span" ) );
    assertEquals( "Map with no markers and no shapes - Simple Case", driver.findElement( By.xpath( "//div[@id='subtitle0']/span" ) ).getText() );
    //Scale
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='simpleTest']/div/div[8]/div" ) );
    assertEquals( "2000 km", driver.findElement( By.xpath( "//div[@id='simpleTest']/div/div[8]/div" ) ).getText() );
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='simpleTest']/div/div[8]/div[2]" ) );
    assertEquals( "1000 mi", driver.findElement( By.xpath( "//div[@id='simpleTest']/div/div[8]/div[2]" ) ).getText() );
    //ButtonLayer
    assertNotNull( driver.findElement( By.xpath( "//div[@id='simpleTest']/div/div[7]/div[2]/img" ) ) );
    //Get same images
    assertNotNull( driver.findElement( By.xpath( "//div[@id='simpleTest']/div/div/div/img" ) ) );
    assertNotNull( driver.findElement( By.xpath( "//div[@id='simpleTest']/div/div/div/img[12]" ) ) );

    //## Step2
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='simpleTest']/div/div[5]/div" ) );
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='simpleTest']/div/div[5]/div[2]" ) );
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='simpleTest']/div/div[5]/div[3]" ) );
    driver.findElement( By.xpath( "//div[@id='simpleTest']/div/div[5]/div" ) ).click();
    //wait for the field update
    wait.until( ExpectedConditions.invisibilityOfElementWithText( By.xpath( "//div[@id='simpleTest']/div/div[8]/div" ), "200 km" ) );
    String expectedKilometers = "1000 km";
    String actualKilometers = this.elemHelper.WaitForTextPresence( driver, By.xpath( "//div[@id='simpleTest']/div/div[8]/div" ), expectedKilometers );
    assertEquals( actualKilometers, expectedKilometers );
    String expectedMiles = "1000 mi";
    String actualMiles = this.elemHelper.WaitForTextPresence( driver, By.xpath( "//div[@id='simpleTest']/div/div[8]/div[2]" ), expectedMiles );
    assertEquals( actualMiles, expectedMiles );

    //## Step3
    driver.findElement( By.xpath( "//div[@id='simpleTest']/div/div[5]/div[2]" ) ).click();
    //wait for the field update
    wait.until( ExpectedConditions.invisibilityOfElementWithText( By.xpath( "//div[@id='simpleTest']/div/div[8]/div" ), "100 km" ) );
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='simpleTest']/div/div[8]/div" ) );
    assertEquals( "10000 km", driver.findElement( By.xpath( "//div[@id='simpleTest']/div/div[8]/div" ) ).getText() );
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='simpleTest']/div/div[8]/div[2]" ) );
    assertEquals( "5000 mi", driver.findElement( By.xpath( "//div[@id='simpleTest']/div/div[8]/div[2]" ) ).getText() );
  }

  /**
   * ############################### Test Case 3 ###############################
   *
   * Test Case Name:
   *    Map engine and map tile services.
   * Description:
   *    Validate the contents on the map and check if the map is display when
   *    we change the map engine and tile services.
   * Steps:
   *    1. Check the data exist
   *    2. Chance map to Google
   *    3. Change map service to 'mapbox-world-dark'
   */
  @Test
  public void tc3_MapEngineAndTileServices_MapDisplayedAfterChanges() {
    this.log.info( "tc3_MapEngineAndTileServices_MapDisplayedAfterChanges" );
    //wait for initialize
    wait.until( ExpectedConditions.invisibilityOfElementWithText( By.xpath( "//div[@id='testTileServices']/div/div[8]/div" ), "10000 km" ) );

    //## Step1
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='subtitle1']/span" ) );
    assertEquals( "Map engine and map tile services", driver.findElement( By.xpath( "//div[@id='subtitle1']/span" ) ).getText() );
    //mapEngine and service
    assertEquals( "Select map engine:", driver.findElement( By.xpath( "//div[@id='content']/div/div[6]/div/div/div" ) ).getText() );
    assertEquals( "Select tile map service:", driver.findElement( By.xpath( "//div[@id='content']/div/div[6]/div/div[2]/div" ) ).getText() );
    //Zoom
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='testTileServices']/div/div[5]/div" ) );
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='testTileServices']/div/div[5]/div[2]" ) );
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='testTileServices']/div/div[5]/div[3]" ) );
    //Scale
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='testTileServices']/div/div[8]/div" ) );
    assertEquals( "2000 km", driver.findElement( By.xpath( "//div[@id='testTileServices']/div/div[8]/div" ) ).getText() );
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='testTileServices']/div/div[8]/div[2]" ) );
    assertEquals( "1000 mi", driver.findElement( By.xpath( "//div[@id='testTileServices']/div/div[8]/div[2]" ) ).getText() );
    //Check if we are using OpenLayers
    String mapId = driver.findElement( By.xpath( "//div[@id='testTileServices']/div" ) ).getAttribute( "id" );
    assertTrue( mapId.contains( "OpenLayers" ) );
    Select mapEngine = new Select( driver.findElement( By.xpath( "//div[@id='selectMapEngineObj']/select" ) ) );
    Select mapService = new Select( driver.findElement( By.xpath( "//div[@id='selectTileObj']/select" ) ) );
    assertEquals( "openlayers", mapEngine.getFirstSelectedOption().getText() );
    assertEquals( "mapquest-sat", mapService.getFirstSelectedOption().getText() );

    //## Step2
    mapEngine.selectByValue( "google" );
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//div[@id='testTileServices']/div[@class='gm-style']" ) );
    //Image with Google (left down corner)
    assertNotNull( driver.findElement( By.xpath( "//div[@id='testTileServices']/div/div[2]/a/div/img" ) ) );
    //Text 'Termos de Utilização (righ down corner)
    String strMap = "Map";
    String strTerms = "Terms of Use";
    String strSat = "Satellite";

    assertEquals( strTerms, driver.findElement( By.xpath( "//div[@id='testTileServices']/div/div[6]/div[2]/a" ) ).getText() );
    //check if we have mapquest-sat/Mapa/Satelite
    assertEquals( "mapquest-sat", driver.findElement( By.xpath( "//div[@id='testTileServices']/div/div[9]/div/div" ) ).getText() );
    assertEquals( strMap, driver.findElement( By.xpath( "//div[@id='testTileServices']/div/div[9]/div[2]/div" ) ).getText() );
    assertEquals( strSat, driver.findElement( By.xpath( "//div[@id='testTileServices']/div/div[9]/div[3]/div" ) ).getText() );

    //## Step3
    mapService.selectByValue( "mapbox-world-dark" );
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    wait.until( ExpectedConditions.invisibilityOfElementWithText( By.xpath( "//div[@id='testTileServices']/div/div[9]/div/div" ), "mapquest-sat" ) );
    //Image with Google (left down corner)
    assertNotNull( driver.findElement( By.xpath( "//div[@id='testTileServices']/div/div[2]/a/div/img" ) ) );
    //Text 'Termos de Utilização (righ down corner)
    assertEquals( strTerms, driver.findElement( By.xpath( "//div[@id='testTileServices']/div/div[6]/div[2]/a" ) ).getText() );
    //check if we have mapbox-world-dark/Mapa/Satelite
    assertEquals( "mapbox-world-dark", driver.findElement( By.xpath( "//div[@id='testTileServices']/div/div[9]/div/div" ) ).getText() );
    assertEquals( strMap, driver.findElement( By.xpath( "//div[@id='testTileServices']/div/div[9]/div[2]/div" ) ).getText() );
    assertEquals( strSat, driver.findElement( By.xpath( "//div[@id='testTileServices']/div/div[9]/div[3]/div" ) ).getText() );
  }

  /**
   * ############################### Test Case 4 ###############################
   *
   * Test Case Name:
   *    Map with Markers based on Long/lat.
   * Description:
   *    Along validate the contents of the map we are going to validate the
   *    markers, if they displayed the correct contents.
   * Steps:
   *    1. Check the data exist
   *    2. Chick in each marker
   */
  @Test
  public void tc4_MapWithMarkersBasedLongLat_MarkersShouldDisplayCorrectContents() {
    this.log.info( "tc4_MapWithMarkersBasedLongLat_MarkersShouldDisplayCorrectContents" );
    /*
     * ## Step 1
     */
    WebElement marker1 = this.elemHelper.FindElement( driver, By.xpath( "//div[@id='testWithMarker']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image']" ) );
    WebElement marker2 = this.elemHelper.FindElement( driver, By.xpath( "//div[@id='testWithMarker']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image'][2]" ) );
    WebElement marker3 = this.elemHelper.FindElement( driver, By.xpath( "//div[@id='testWithMarker']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image'][3]" ) );
    assertNotNull( marker1 );
    assertNotNull( marker2 );
    assertNotNull( marker3 );
    assertEquals( "/pentaho/api/repos/pentaho-cdf-dd/resources/custom/amd-components/NewMapComponent/images/marker_grey.png", marker1.getAttribute( "xlink:href" ) );
    assertEquals( "/pentaho/api/repos/pentaho-cdf-dd/resources/custom/amd-components/NewMapComponent/images/marker_blue.png", marker2.getAttribute( "xlink:href" ) );
    assertEquals( "/pentaho/api/repos/pentaho-cdf-dd/resources/custom/amd-components/NewMapComponent/images/marker_grey02.png", marker3.getAttribute( "xlink:href" ) );

    /*
     * ## Step 2
     */
    //Zoom in - in order for the elements to be visible
    this.elemHelper.FindElement( driver, By.xpath( "//div[@id='testWithMarker']/div/div[5]/div[3]" ) ).click();
    this.elemHelper.FindElement( driver, By.xpath( "//div[@id='testWithMarker']/div/div[5]/div[3]" ) ).click();

    //Open Marker 1
    marker1.click();
    //Wait for loading disappear
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    String marker1Text = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "HiddenContentCol" ) );
    this.elemHelper.Click( driver, By.cssSelector( "div.olPopupCloseBox" ) );
    assertEquals( "Atelier Graphique", marker1Text );
    //Open Marker 2
    marker2.click();
    //Wait for loading disappear
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    String marker2Text = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "HiddenContentCol" ) );
    this.elemHelper.Click( driver, By.cssSelector( "div.olPopupCloseBox" ) );
    assertEquals( "Australian Collectors, Co.", marker2Text );
    //Open Marker 3
    marker3.click();
    //Wait for loading disappear
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    String marker3Text = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "HiddenContentCol" ) );
    this.elemHelper.Click( driver, By.cssSelector( "div.olPopupCloseBox" ) );
    assertEquals( "Signal Gift Stores", marker3Text );
  }

  /**
   * ############################### Test Case 5 ###############################
   *
   * Test Case Name:
   *    Map with Markers based on city.
   * Description:
   *    Along validate the contents of the map we are going to validate the
   *    markers, if they displayed the correct contents.
   * Steps:
   *    1. Check the data exist
   *    2. Chick in each marker
   */
  @Test
  public void tc5_MapWithMarkersBasedCity_MarkersShouldDisplayCorrectContents() {
    this.log.info( "tc5_MapWithMarkersBasedCity_MarkersShouldDisplayCorrectContents" );
    /*
     * ## Step 1
     */
    //Check if the chart is already rendered
    this.elemHelper.WaitForElementPresence( driver, By.xpath( "//div[@id='testWithGeoLocalization']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image']" ), 90 );
    WebElement marker1 = this.elemHelper.FindElement( driver, By.xpath( "//div[@id='testWithGeoLocalization']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image']" ) );
    WebElement marker2 = this.elemHelper.FindElement( driver, By.xpath( "//div[@id='testWithGeoLocalization']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image'][2]" ) );
    WebElement marker3 = this.elemHelper.FindElement( driver, By.xpath( "//div[@id='testWithGeoLocalization']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image'][3]" ) );
    WebElement marker4 = this.elemHelper.FindElement( driver, By.xpath( "//div[@id='testWithGeoLocalization']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][3]/*[local-name()='g']/*[local-name()='image'][4]" ) );
    assertNotNull( marker1 );
    assertNotNull( marker2 );
    assertNotNull( marker3 );
    assertNotNull( marker4 );
    assertEquals( "/pentaho/api/repos/pentaho-cdf-dd/resources/custom/amd-components/NewMapComponent/images/marker_grey.png", marker1.getAttribute( "xlink:href" ) );
    assertEquals( "/pentaho/api/repos/pentaho-cdf-dd/resources/custom/amd-components/NewMapComponent/images/marker_blue.png", marker2.getAttribute( "xlink:href" ) );
    assertEquals( "/pentaho/api/repos/pentaho-cdf-dd/resources/custom/amd-components/NewMapComponent/images/marker_grey02.png", marker3.getAttribute( "xlink:href" ) );
    assertEquals( "/pentaho/api/repos/pentaho-cdf-dd/resources/custom/amd-components/NewMapComponent/images/marker_orange.png", marker4.getAttribute( "xlink:href" ) );

    /*
     * ## Step 2
     */
    //Zoom in - in order for the elements to be visible
    this.elemHelper.Click( driver, By.xpath( "//div[@id='testWithGeoLocalization']/div/div[5]/div[3]" ) );
    //Open Marker 1
    marker1.click();
    //Wait for loading disappear
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    String marker1Text = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "HiddenContentCol" ) );
    this.elemHelper.Click( driver, By.xpath( "(//div[@class='olPopupCloseBox'])[2]" ) );
    assertEquals( "Atelier Graphique", marker1Text );
    //Open Marker 2
    marker2.click();
    //Wait for loading disappear
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    String marker2Text = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "HiddenContentCol" ) );
    this.elemHelper.Click( driver, By.xpath( "(//div[@class='olPopupCloseBox'])[2]" ) );
    assertEquals( "Australian Collectors, Co.", marker2Text );
    //Open Marker 3
    marker3.click();
    //Wait for loading disappear
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    String marker3Text = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "HiddenContentCol" ) );
    this.elemHelper.Click( driver, By.xpath( "(//div[@class='olPopupCloseBox'])[2]" ) );
    assertEquals( "Signal Gift Stores", marker3Text );
    //Open Marker 4
    marker4.click();
    //Wait for loading disappear
    this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
    String marker4Text = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "HiddenContentCol" ) );
    this.elemHelper.Click( driver, By.xpath( "(//div[@class='olPopupCloseBox'])[2]" ) );
    assertEquals( "La Rochelle Gifts", marker4Text );
  }

  /**
   * ############################### Test Case 6 ###############################
   *
   * Test Case Name:
   *    Map with Shapes
   * Description:
   *    Check if the map has shapes and by click in a known shape must change
   *    its color.
   * Steps:
   *    1. Check the data exist
   *    2. Chick in a shape country
   */
  @Test
  public void tc6_MapWithShapes_ShapesAreClickable() {
    this.log.info( "tc6_MapWithShapes_ShapesAreClickable" );

    //Zoom in the map
    this.elemHelper.FindElement( driver, By.xpath( "//div[12]/div/div/div[5]/div[3]" ) ).click();

    /*
     * ## Step 1
     */
    WebElement shape1 = this.elemHelper.FindElement( driver, By.xpath( "//div[@id='testWithShapes']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][2]/*[local-name()='g']/*[local-name()='path'][2]" ) );
    WebElement shape2 = this.elemHelper.FindElement( driver, By.xpath( "//div[@id='testWithShapes']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][2]/*[local-name()='g']/*[local-name()='path'][10]" ) );
    WebElement shape3 = this.elemHelper.FindElement( driver, By.xpath( "//div[@id='testWithShapes']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][2]/*[local-name()='g']/*[local-name()='path'][13]" ) );
    WebElement shape4 = this.elemHelper.FindElement( driver, By.xpath( "//div[@id='testWithShapes']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][2]/*[local-name()='g']/*[local-name()='path'][14]" ) );
    assertNotNull( shape1 );
    assertNotNull( shape2 );
    assertNotNull( shape3 );
    assertNotNull( shape4 );
    this.elemHelper.WaitForAttributeValue( driver, By.xpath( "//div[@id='testWithShapes']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][2]/*[local-name()='g']/*[local-name()='path'][2]" ), "fill", "rgba(255,8,0,255)", 10 );
    this.elemHelper.WaitForAttributeValue( driver, By.xpath( "//div[@id='testWithShapes']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][2]/*[local-name()='g']/*[local-name()='path'][10]" ), "fill", "rgba(183,212,0,255)", 15 );
    this.elemHelper.WaitForAttributeValue( driver, By.xpath( "//div[@id='testWithShapes']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][2]/*[local-name()='g']/*[local-name()='path'][13]" ), "fill", "rgba(167,202,0,255)", 15 );
    this.elemHelper.WaitForAttributeValue( driver, By.xpath( "//div[@id='testWithShapes']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][2]/*[local-name()='g']/*[local-name()='path'][14]" ), "fill", "rgba(167,202,0,255)", 15 );
    String attrFillShape1 = this.elemHelper.GetAttribute( driver, By.xpath( "//div[@id='testWithShapes']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][2]/*[local-name()='g']/*[local-name()='path'][2]" ), "fill" );
    String attrFillShape2 = this.elemHelper.GetAttribute( driver, By.xpath( "//div[@id='testWithShapes']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][2]/*[local-name()='g']/*[local-name()='path'][10]" ), "fill" );
    String attrFillShape3 = this.elemHelper.GetAttribute( driver, By.xpath( "//div[@id='testWithShapes']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][2]/*[local-name()='g']/*[local-name()='path'][13]" ), "fill" );
    String attrFillShape4 = this.elemHelper.GetAttribute( driver, By.xpath( "//div[@id='testWithShapes']/div/div/div[5]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[local-name()='g'][2]/*[local-name()='g']/*[local-name()='path'][14]" ), "fill" );
    assertEquals( "rgba(255,8,0,255)", attrFillShape1 );
    assertEquals( "rgba(183,212,0,255)", attrFillShape2 );
    assertEquals( "rgba(167,202,0,255)", attrFillShape3 );
    assertEquals( "rgba(167,202,0,255)", attrFillShape4 );

    /*
     * ## Step 2
     */
    //Click in shape4 (England) and check it comes with red.
    String shapeId = shape1.getAttribute( "id" );
    shape1.click();
    //Wait for change color
    this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//*[local-name()='path' and @fill='red']" ) );
    WebElement shapeRed = this.elemHelper.FindElement( driver, By.id( shapeId ) );
    assertEquals( "red", shapeRed.getAttribute( "fill" ) );

    //Related to issue CDE-317
    Actions action = new Actions( driver );
    action.moveToElement( shape2 ).build().perform();
    action.moveToElement( shape1 ).build().perform();
    shapeId = shape1.getAttribute( "id" );
    shapeRed = this.elemHelper.FindElement( driver, By.id( shapeId ) );
    assertEquals( "red", shapeRed.getAttribute( "fill" ) );
  }
}
