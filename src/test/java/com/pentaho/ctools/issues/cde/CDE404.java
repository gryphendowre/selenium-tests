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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import com.pentaho.ctools.utils.ElementHelper;
import com.pentaho.selenium.BaseTest;

/**
 * The script is testing the issue:
 * - http://jira.pentaho.com/browse/CDE-404
 *
 * and the automation test is described:
 * - http://jira.pentaho.com/browse/QUALITY-971
 *
 * NOTE
 * To test this script it is required to have CDE plugin installed.
 *
 * Naming convention for test:
 *  'tcN_StateUnderTest_ExpectedBehavior'
 *
 */
public class CDE404 extends BaseTest {
	// Access to wrapper for webdriver
	private final ElementHelper elemHelper = new ElementHelper();
	// Log instance
	private final Logger log = LogManager.getLogger( CDE404.class );

	/**
	 * ############################### Test Case 1 ###############################
	 *
	 * Test Case Name:
	 *    Able to edit external resource from a dashbord belonging to a plugin with upper-case letter on the name
	 *
	 * Description:
	 *    The test pretends validate the CDE-404 issue, so user is able to edit a external resource when
	 *    accessing it from a dashboard that belongs to a plugin with upper case letter on the name.
	 *
	 * Steps:
	 *    1. Open CDE404's dashboard in edit mode.
	 *    2. Select external file as resource
	 *    3. Select file
	 *    4. Edit resource and assert elements on external editor
	 */
	@Test
	public void tc01_ExternalResources_PluginDashboard() {
		this.log.info( "tc01_ExternalResources_PluginDashboard" );

		/*
		 * ## Step 1
		 */
		//Open plugin dashboard
		driver.get( baseUrl + "plugin/pentaho-cdf-dd/api/renderer/edit?absolute=false&inferScheme=false&file=Test.wcdf&path=%2FCDE404%2Fdashboards%2F&solution=system&mode=edit" );
		//wait for invisibility of waiting pop-up
		this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ), 60 );

		WebElement element = this.elemHelper.FindElement( driver, By.xpath( "//a[@title='Save as Template']" ) );
		assertNotNull( element );
		element = this.elemHelper.FindElement( driver, By.xpath( "//a[@title='Apply Template']" ) );
		assertNotNull( element );
		element = this.elemHelper.FindElement( driver, By.xpath( "//a[@title='Add Resource']" ) );
		assertNotNull( element );
		element = this.elemHelper.FindElement( driver, By.xpath( "//a[@title='Add FreeForm']" ) );
		assertNotNull( element );
		element = this.elemHelper.FindElement( driver, By.xpath( "//a[@title='Add Row']" ) );
		assertNotNull( element );
		this.elemHelper.Click( driver, By.xpath( "//a[@title='Add Resource']" ) );

		/*
		 * ## Step 2
		 */
		element = this.elemHelper.FindElement( driver, By.xpath( "//select[@id='resourceType']" ) );
		assertNotNull( element );
		Select select = new Select( this.elemHelper.FindElement( driver, By.xpath( "//select[@id='resourceType']" ) ) );
		select.selectByValue( "Css" );
		element = this.elemHelper.FindElement( driver, By.xpath( "//select[@id='resourceSource']" ) );
		assertNotNull( element );
		Select select1 = new Select( this.elemHelper.FindElement( driver, By.xpath( "//select[@id='resourceSource']" ) ) );
		select1.selectByValue( "file" );
		this.elemHelper.Click( driver, By.xpath( "//button[@id='popup_state0_buttonOk']" ) );

		/*
		 * ## Step 3
		 */
		element = this.elemHelper.FindElement( driver, By.xpath( "//button[@class='cdfdd-resourceFileExplorerRender']" ) );
		assertNotNull( element );
		this.elemHelper.Click( driver, By.xpath( "//button[@class='cdfdd-resourceFileExplorerRender']" ) );
		element = this.elemHelper.FindElement( driver, By.id( "container_id" ) );
		assertNotNull( element );
		element = this.elemHelper.FindElement( driver, By.xpath( "//a[@rel='static/']" ) );
		assertNotNull( element );
		this.elemHelper.Click( driver, By.xpath( "//a[@rel='static/']" ) );
		element = this.elemHelper.FindElement( driver, By.xpath( "//a[@rel='static/system/']" ) );
		assertNotNull( element );
		this.elemHelper.Click( driver, By.xpath( "//a[@rel='static/system/']" ) );
		element = this.elemHelper.FindElement( driver, By.xpath( "//a[@rel='static/system/css/']" ) );
		assertNotNull( element );
		this.elemHelper.Click( driver, By.xpath( "//a[@rel='static/system/css/']" ) );
		element = this.elemHelper.FindElement( driver, By.xpath( "//a[@rel='static/system/css/cpk.css']" ) );
		assertNotNull( element );
		this.elemHelper.Click( driver, By.xpath( "//a[@rel='static/system/css/cpk.css']" ) );
		this.elemHelper.Click( driver, By.xpath( "//button[@id='popup_browse_buttonOk']" ) );

		/*
		 * ## Step 4
		 */
		element = this.elemHelper.FindElement( driver, By.xpath( "//button[@class='cdfddInput']" ) );
		assertNotNull( element );
		this.elemHelper.Click( driver, By.xpath( "//button[@class='cdfddInput']" ) );
		WebElement elementframe = this.elemHelper.FindElement( driver, By.xpath( "//iframe" ) );
		WebDriver frame = this.elemHelper.SwitchToFrame( driver, elementframe );
		element = this.elemHelper.FindElement( frame, By.xpath( "//span[@id='infoArea']" ) );
		assertNotNull( element );
		String pathText = this.elemHelper.WaitForElementPresentGetText( frame, By.xpath( "//span[@id='infoArea']" ) );
		assertEquals( "/system/CDE404/static/system/css/cpk.css", pathText );
		String lineText = this.elemHelper.WaitForElementPresentGetText( frame, By.xpath( "//pre[@id='editArea']/div[2]/div/div[3]/div/span" ) );
		assertEquals( "/* This Source Code Form is subject to the terms of the Mozilla Public", lineText );
		lineText = this.elemHelper.WaitForElementPresentGetText( frame, By.xpath( "//pre[@id='editArea']/div[2]/div/div[3]/div[2]/span" ) );
		assertEquals( "* License, v. 2.0. If a copy of the MPL was not distributed with this file,", lineText );
		lineText = this.elemHelper.WaitForElementPresentGetText( frame, By.xpath( "//pre[@id='editArea']/div[2]/div/div[3]/div[3]/span" ) );
		assertEquals( "* You can obtain one at http://mozilla.org/MPL/2.0/. */", lineText );
	}
}
