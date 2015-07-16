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
package org.pentaho.ctools.issues.cdf;

import static org.junit.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.pentaho.ctools.suite.CToolsTestSuite;
import org.pentaho.ctools.utils.ElementHelper;
import org.pentaho.ctools.utils.ScreenshotTestRule;

/**
 * The script is testing the issue:
 * - http://jira.pentaho.com/browse/CDF-501
 *
 * and the automation test is described:
 * - http://jira.pentaho.com/browse/QUALITY-1109
 *
 * NOTE
 * To test this script it is required to have CDF plugin installed.
 *
 * Naming convention for test:
 *  'tcN_StateUnderTest_ExpectedBehavior'
 *
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class CDF501 {
  // Instance of the driver (browser emulator)
  private final WebDriver driver = CToolsTestSuite.getDriver();
  // The base url to be append the relative url in test
  private final String baseUrl = CToolsTestSuite.getBaseUrl();
  //Access to wrapper for webdriver
  private final ElementHelper elemHelper = new ElementHelper();
  // Log instance
  private final Logger log = LogManager.getLogger( CDF501.class );
  // Getting screenshot when test fails
  @Rule
  public ScreenshotTestRule screenshotTestRule = new ScreenshotTestRule( this.driver );

  /**
   * ############################### Test Case 1 ###############################
   *
   * Test Case Name:
   *    Assert that parameters passed on the URL are received by the dashboard
   *
   * Description:
   *    When adding paramType=test to the URL Dashboards.context.params.type is created with the value "test".
   *    When adding type=test to the URL Dashboards.getQueryParameter("type") is created with the value "test"
   *
   * Steps:
   *    1. Open created sample adding both parameters to the URL
   *    2. Assert text on alert and click OK
   *    3. Assert text on alert and click OK
   *
   */
  @Test( timeout = 120000 )
  public void tc1_CdfDashboardUrl_ParamSuccessfull() {
    this.log.info( "tc1_CdfDashboardUrl_ParamSuccessfull" );

    /*
     * ## Step 1
     */
    //Open Created sample with params on the URL
    this.driver.get( this.baseUrl + "api/repos/%3Apublic%3AIssues%3ACDF%3ACDF-501%3Aurl_param.wcdf/generatedContent?paramtype=success&type=awesome" );

    /*
     * ## Step 2
     */
    String alertMessage = this.elemHelper.WaitForAlertReturnConfirmationMsg( this.driver );
    assertEquals( "Direct Access: success", alertMessage );

    /*
     * ## Step 3
     */
    String alertMessage1 = this.elemHelper.WaitForAlertReturnConfirmationMsg( this.driver );
    assertEquals( "Function: awesome", alertMessage1 );

  }
}