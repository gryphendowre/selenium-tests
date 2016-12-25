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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.CoreMatchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.pentaho.ctools.utils.ElementHelper;
import com.pentaho.ctools.utils.PageUrl;
import com.pentaho.selenium.BaseTest;

/**
 * Testing the functionalities related with AddinReference.
 *
 * Naming convention for test: 'tcN_StateUnderTest_ExpectedBehavior'
 *
 */
public class AddinReference extends BaseTest {
	// Access to wrapper for webdriver
	private final ElementHelper elemHelper = new ElementHelper();
	// Log instance
	private final Logger log = LogManager.getLogger( AddinReference.class );

	/**
	 * ############################### Test Case 0 ###############################
	 *
	 * Test Case Name:
	 * 		Open Sample Page
	 */
	@Test
	public void tc00_OpenSamplePage_Display() {
		this.log.info( "tc00_OpenSamplePage_Display" );
		// The URL for the AddinReference under CDF samples
		// This sample is in: Public/plugin-samples/CDE/CDE Reference
		driver.get( PageUrl.ADDIN_REFERENCE );

		// NOTE - we have to wait for loading disappear
		this.elemHelper.WaitForElementInvisibility( driver, By.cssSelector( "div.blockUI.blockOverlay" ) );
	}

	/**
	 * ############################### Test Case 1 ###############################
	 *
	 * Test Case Name:
	 * 		Page Content
	 *
	 * Description:
	 * 		The test case pretends to validate the display contents on the page.
	 *
	 * Steps:
	 * 		1. Check display texts
	 */
	@Test
	public void tc01_PageContent_ContentDisplayed() {
		this.log.info( "tc01_PageContent_CachedQueries" );

		/*
		 * ## Step 1
		 */
		// Check title
		wait.until( ExpectedConditions.titleContains( "AddIn Reference" ) );
		assertEquals( "AddIn Reference", driver.getTitle() );

		// Check subtitle
		String textSubTitle1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='Title']/span" ) );
		assertEquals( "AddIns reference", textSubTitle1 );

		// Check subtitle
		String textSubTitle2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='DescriptionBody']/div" ) );
		assertEquals( "AddIn Implementation", textSubTitle2 );

		// Check subtitle
		String textSubTitle3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='DescriptionBody']/div[2]" ) );
		assertEquals( "Setting options", textSubTitle3 );

		// Check subtitle
		String textSubTitle4 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='DescriptionBody']/div[3]" ) );
		assertEquals( "Setting defaults", textSubTitle4 );

		// Check subtitle
		String textSubTitle5 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='DescriptionBody']/div[4]" ) );
		assertEquals( "Implementation arguments", textSubTitle5 );

		// Check subtitle
		String textSubTitle6 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='DescriptionBody']/div[5]" ) );
		assertEquals( "Calling AddIns from components", textSubTitle6 );

		// Check subtitle
		String textSubTitle7 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='SupportedAddinsTitle']" ) );
		assertEquals( "Supported AddIns - Table \"colType\"", textSubTitle7 );
	}

	/**
	 * ############################### Test Case 2 ###############################
	 *
	 * Test Case Name:
	 * 		Sparkline
	 *
	 * Description:
	 * 		The test case pretends to validate the sparkline sample.
	 *
	 * Steps:
	 * 		1. Check if the sample for 'sparkline' has its contents present
	 * 		2. Order the table
	 */
	@Test
	public void tc02_Sparkline_SampleWorks() {
		this.log.info( "tc02_Sparkline_SampleWorks" );

		/*
		 * ## Step 1
		 */
		String sampleTitle = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "SparklineTitle" ) );
		assertEquals( "sparkline", sampleTitle );
		// Check the hyperlink
		WebElement elemUrl = this.elemHelper.FindElement( driver, By.linkText( "JQuery Sparkline plugin" ) );
		String attrHref = elemUrl.getAttribute( "href" );
		assertEquals( "http://www.omnipotent.net/jquery.sparkline/", attrHref );

		// Check Defaults
		String defaultText = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='SparklineDescHtml']/blockquote/pre" ) );
		assertEquals( "defaults: { type: 'line' }", defaultText );

		// Check Rows
		String row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='SparklineObjTable']/tbody/tr/td" ) );
		String row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='SparklineObjTable']/tbody/tr[2]/td" ) );
		assertEquals( "Row 1", row1 );
		assertEquals( "Row 2", row2 );

		// Check column Default Sparklin
		WebElement chartRow1Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='SparklineObjTable']/tbody/tr[1]/td[2]/canvas" ) );
		WebElement chartRow1Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='SparklineObjTable']/tbody/tr[1]/td[3]/canvas" ) );
		WebElement chartRow2Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='SparklineObjTable']/tbody/tr[2]/td[2]/canvas" ) );
		WebElement chartRow2Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='SparklineObjTable']/tbody/tr[2]/td[3]/canvas" ) );
		assertNotNull( chartRow1Col1 );
		assertNotNull( chartRow1Col2 );
		assertNotNull( chartRow2Col1 );
		assertNotNull( chartRow2Col2 );

		/*
		 * ## Step 3
		 */
		// Check ordering
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='SparklineObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='SparklineObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='SparklineObjTable']/thead/tr/th[1][@class='column0 string sorting_desc']" ) );
		row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='SparklineObjTable']/tbody/tr/td" ) );
		row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='SparklineObjTable']/tbody/tr[2]/td" ) );
		assertEquals( "Row 2", row1 );
		assertEquals( "Row 1", row2 );
	}

	/**
	 * ############################### Test Case 3 ###############################
	 *
	 * Test Case Name:
	 * 		pvSparkline
	 *
	 * Description:
	 * 		The test case pretends to validate the pvSparkline sample.
	 *
	 * Steps:
	 * 		1. Check if the sample for 'pvSparkline' has its contents present
	 * 		2. Order the table
	 */
	@Test
	public void tc03_pvSparkline_SampleWorks() {
		this.log.info( "tc03_pvSparkline_SampleWorks" );

		/*
		 * ## Step 1
		 */
		// Check sample title
		String sampleTitle = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "PvSparklineTitle" ) );
		assertEquals( "pvSparkline", sampleTitle );

		// Check Defaults
		String defaultText = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='PvSparklineDesc']/blockquote/pre" ) );
		assertEquals( "defaults: { height: 10, strokeStyle: \"#000\", lineWidth: 1, width: undefined, canvasMargin: 2 }", defaultText );

		// Check Rows
		String row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='PvSparklineObjTable']/tbody/tr/td" ) );
		String row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='PvSparklineObjTable']/tbody/tr[2]/td" ) );
		assertEquals( "Row 1", row1 );
		assertEquals( "Row 2", row2 );

		// Check column Default Sparklin
		WebElement chartRow1Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='PvSparklineObjTable']/tbody/tr[1]/td[2]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='g']/*[name()='path']" ) );
		WebElement chartRow1Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='PvSparklineObjTable']/tbody/tr[1]/td[3]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='g']/*[name()='path']" ) );
		WebElement chartRow2Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='PvSparklineObjTable']/tbody/tr[2]/td[2]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='g']/*[name()='path']" ) );
		WebElement chartRow2Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='PvSparklineObjTable']/tbody/tr[2]/td[3]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='g']/*[name()='path']" ) );
		assertNotNull( chartRow1Col1 );
		assertNotNull( chartRow1Col2 );
		assertNotNull( chartRow2Col1 );
		assertNotNull( chartRow2Col2 );

		/*
		 * ## Step 2
		 */
		// Check ordering
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='PvSparklineObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='PvSparklineObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='PvSparklineObjTable']/thead/tr/th[1][@class='column0 string sorting_desc']" ) );
		row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='PvSparklineObjTable']/tbody/tr/td" ) );
		row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='PvSparklineObjTable']/tbody/tr[2]/td" ) );
		assertEquals( "Row 2", row1 );
		assertEquals( "Row 1", row2 );

		// Check attribute 'd'
		chartRow2Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='PvSparklineObjTable']/tbody/tr[2]/td[2]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='g']/*[name()='path']" ) );
		String row1Col1ChartDataActual = chartRow2Col1.getAttribute( "d" );
		String row1Col1ChartDataExpected1 = "M0,7.428571428571429";
		String row1Col1ChartDataExpected2 = ",0.8571428571428577";
		assertThat( "Display tooltip: " + row1Col1ChartDataActual, row1Col1ChartDataActual, CoreMatchers.containsString( row1Col1ChartDataExpected1 ) );
		assertThat( "Display tooltip: " + row1Col1ChartDataActual, row1Col1ChartDataActual, CoreMatchers.containsString( row1Col1ChartDataExpected2 ) );
	}

	/**
	 * ############################### Test Case 4 ###############################
	 *
	 * Test Case Name:
	 * 		DataBar
	 *
	 * Description:
	 * 		The test case pretends to validate the dataBar sample.
	 *
	 * Steps:
	 * 		1.Check if the sample for 'dataBar' has its contents present
	 * 		2. Order the table
	 */
	@Test
	public void tc04_dataBar_SampleWorks() {
		this.log.info( "tc04_dataBar_SampleWorks" );

		/*
		 * ## Step 1
		 */
		// Check sample title
		String sampleTitle = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "DataBarTitle" ) );
		assertEquals( "dataBar", sampleTitle );

		// Check Defaults
		String defaultText = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='DataBarDesc']/blockquote/pre" ) );
		assertEquals( "defaults: { widthRatio:1, height: 10, startColor: \"#55A4D6\", endColor: \"#448FC8\", stroke: null, max: undefined, min: undefined, absValue: true, includeValue: false, valueFormat: function(v, format, st) { return \"\" + sprintf(format,v) + \"\"; } }", defaultText );

		// Check Rows
		String row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr/td" ) );
		String row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[2]/td" ) );
		String row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[3]/td" ) );
		assertEquals( "Row 1", row1 );
		assertEquals( "Row 2", row2 );
		assertEquals( "Row 3", row3 );

		// Check column Default barData
		WebElement chartRow1Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[1]/td[2]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='rect']" ) );
		WebElement chartRow1Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[1]/td[3]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='rect']" ) );
		WebElement chartRow2Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[2]/td[2]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='rect']" ) );
		WebElement chartRow2Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[2]/td[3]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='rect']" ) );
		WebElement chartRow3Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[3]/td[2]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='rect']" ) );
		WebElement chartRow3Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[3]/td[3]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='rect']" ) );
		assertNotNull( chartRow1Col1 );
		assertNotNull( chartRow1Col2 );
		assertNotNull( chartRow2Col1 );
		assertNotNull( chartRow2Col2 );
		assertNotNull( chartRow3Col1 );
		assertNotNull( chartRow3Col2 );

		// check values in column 1
		String chartRow1Col1Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[1]/td[2]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='rect']/*[name()='title']" ) );
		String chartRow2Col1Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[2]/td[2]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='rect']/*[name()='title']" ) );
		assertEquals( "Value: 26", chartRow1Col1Value );
		assertEquals( "Value: 30", chartRow2Col1Value );

		// check values in column 2
		String chartRow2Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[2]/td[3]/div/span" ) );
		String chartRow3Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[3]/td[3]/div/span" ) );
		assertEquals( "77.0", chartRow2Col2Value );
		assertEquals( "-65.0", chartRow3Col2Value );

		/*
		 * ## Step 2
		 */
		// Check ordering
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='DataBarObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='DataBarObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='DataBarObjTable']/thead/tr/th[1][@class='column0 string sorting_desc']" ) );
		row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr/td" ) );
		row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[2]/td" ) );
		row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[3]/td" ) );
		assertEquals( "Row 3", row1 );
		assertEquals( "Row 2", row2 );
		assertEquals( "Row 1", row3 );
		// check values in column 1
		chartRow1Col1Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[1]/td[2]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='rect']/*[name()='title']" ) );
		chartRow2Col1Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[2]/td[2]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='rect']/*[name()='title']" ) );
		assertEquals( "Value: 14.5", chartRow1Col1Value );
		assertEquals( "Value: 30", chartRow2Col1Value );
		// check values in column 2
		chartRow2Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[2]/td[3]/div/span" ) );
		chartRow3Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[3]/td[3]/div/span" ) );
		assertEquals( "77.0", chartRow2Col2Value );
		assertEquals( "50.0", chartRow3Col2Value );

		// Check ordering - Custom barData
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='DataBarObjTable']/thead/tr/th[3]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='DataBarObjTable']/thead/tr/th[3][@class='column2 dataBar sorting_asc']" ) );
		// check values in column 1
		chartRow1Col1Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[1]/td[2]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='rect']/*[name()='title']" ) );
		chartRow2Col1Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[2]/td[2]/div/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='rect']/*[name()='title']" ) );
		assertEquals( "Value: 14.5", chartRow1Col1Value );
		assertEquals( "Value: 26", chartRow2Col1Value );
		// check values in column 2
		chartRow2Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[2]/td[3]/div/span" ) );
		chartRow3Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='DataBarObjTable']/tbody/tr[3]/td[3]/div/span" ) );
		assertEquals( "50.0", chartRow2Col2Value );
		assertEquals( "77.0", chartRow3Col2Value );
	}

	/**
	 * ############################### Test Case 5 ###############################
	 *
	 * Test Case Name:
	 * 		trendArrow
	 *
	 * Description:
	 * 		The test case pretends to validate the trendArrow sample.
	 *
	 * Steps:
	 * 		1. Check if the sample for 'trendArrow' has its contents present
	 * 		2. Order the table
	 */
	@Test
	public void tc05_trendArrow_SampleWorks() {
		this.log.info( "tc05_trendArrow_SampleWorks" );

		/*
		 * ## Step 1
		 */
		// Check sample title
		String sampleTitle = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "TrendArrowTitle" ) );
		assertEquals( "trendArrow", sampleTitle );

		// Check Defaults
		String defaultText = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='TrendArrowDesc']/blockquote/pre" ) );
		assertEquals( "defaults: { includeValue: false, good: true, valueFormat: function(v,format,st) { return sprintf(format,v); } }", defaultText );

		// Check Rows
		String row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr/td" ) );
		String row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[2]/td" ) );
		String row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[3]/td" ) );
		assertEquals( "Row 1", row1 );
		assertEquals( "Row 2", row2 );
		assertEquals( "Row 3", row3 );

		// Check column Default trendArrow
		WebElement chartRow1Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[1]/td[2]/div[@class='trend up good']" ) );
		WebElement chartRow1Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[1]/td[3]/div[2][@class='trend up bad']" ) );
		WebElement chartRow2Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[2]/td[2]/div[@class='trend down good']" ) );
		WebElement chartRow2Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[2]/td[3]/div[2][@class='trend up bad']" ) );
		WebElement chartRow3Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[3]/td[2]/div[@class='trend down good']" ) );
		WebElement chartRow3Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[3]/td[3]/div[2][@class='trend down bad']" ) );
		assertNotNull( chartRow1Col1 );
		assertNotNull( chartRow1Col2 );
		assertNotNull( chartRow2Col1 );
		assertNotNull( chartRow2Col2 );
		assertNotNull( chartRow3Col1 );
		assertNotNull( chartRow3Col2 );

		// Check column value for 'Custom trendArrow'
		String chartRow1Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[1]/td[3]/div" ) );
		String chartRow2Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[2]/td[3]/div" ) );
		String chartRow3Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[3]/td[3]/div" ) );
		assertEquals( chartRow1Col2Value, "50.00€" );
		assertEquals( chartRow2Col2Value, "77.00€" );
		assertEquals( chartRow3Col2Value, "-65.00€" );

		/*
		 * ## Step 2
		 */
		// Check ordering
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='TrendArrowObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='TrendArrowObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='TrendArrowObjTable']/thead/tr/th[1][@class='column0 string sorting_desc']" ) );
		row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr/td" ) );
		row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[2]/td" ) );
		row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[3]/td" ) );
		assertEquals( "Row 3", row1 );
		assertEquals( "Row 2", row2 );
		assertEquals( "Row 1", row3 );

		// Check column value for 'Custom trendArrow'
		chartRow1Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[1]/td[3]/div" ) );
		chartRow2Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[2]/td[3]/div" ) );
		chartRow3Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[3]/td[3]/div" ) );
		assertEquals( "-65.00€", chartRow1Col2Value );
		assertEquals( "77.00€", chartRow2Col2Value );
		assertEquals( "50.00€", chartRow3Col2Value );

		// Check ordering - Custom trendArrow
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='TrendArrowObjTable']/thead/tr/th[3]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='TrendArrowObjTable']/thead/tr/th[3][@class='column2 trendArrow sorting_asc']" ) );
		// Check column value for 'Custom trendArrow'
		row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr/td" ) );
		row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[2]/td" ) );
		row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[3]/td" ) );
		assertEquals( "Row 3", row1 );
		assertEquals( "Row 1", row2 );
		assertEquals( "Row 2", row3 );
		chartRow1Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[1]/td[3]/div" ) );
		chartRow2Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[2]/td[3]/div" ) );
		chartRow3Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='TrendArrowObjTable']/tbody/tr[3]/td[3]/div" ) );
		assertEquals( "-65.00€", chartRow1Col2Value );
		assertEquals( "50.00€", chartRow2Col2Value );
		assertEquals( "77.00€", chartRow3Col2Value );
	}

	/**
	 * ############################### Test Case 6 ###############################
	 *
	 * Test Case Name:
	 *    hyperlink
	 *
	 * Description:
	 *    The test case pretends to validate the hyperlink sample.
	 *
	 * Steps:
	 *    1. Check if the sample for 'hyperlink' has its contents present 2. Order the table
	 */
	@Test
	public void tc06_hyperlink_SampleWorks() {
		this.log.info( "tc06_hyperlink_SampleWorks" );

		/*
		 * ## Step 1
		 */
		// Check sample title
		String sampleTitle = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "HyperlinkTitle" ) );
		assertEquals( "hyperlink", sampleTitle );

		// Check Defaults
		String defaultText = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='HyperlinkDesc']/blockquote/pre" ) );
		assertEquals( "defaults: { openInNewTab: true, prependHttpIfNeeded: true, regexp: null }", defaultText );

		// Check Rows
		String row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr/td" ) );
		String row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[2]/td" ) );
		String row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[3]/td" ) );
		assertEquals( "Row 1", row1 );
		assertEquals( "Row 2", row2 );
		assertEquals( "Row 3", row3 );

		// Check column Default trendArrow
		WebElement chartRow1Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[1]/td[2]/a" ) );
		WebElement chartRow1Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[1]/td[3]" ) );
		WebElement chartRow2Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[2]/td[2]/a" ) );
		WebElement chartRow2Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[2]/td[3]/a" ) );
		WebElement chartRow3Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[3]/td[2]/a" ) );
		WebElement chartRow3Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[3]/td[3]/a" ) );
		assertNotNull( chartRow1Col1 );
		assertNotNull( chartRow1Col2 );
		assertNotNull( chartRow2Col1 );
		assertNotNull( chartRow2Col2 );
		assertNotNull( chartRow3Col1 );
		assertNotNull( chartRow3Col2 );

		// Check column value for 'Custom hyperlink'
		String urlRow1Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[1]/td[3]" ) );
		String urlRow2Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[2]/td[3]/a" ) );
		String urlRow3Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[3]/td[3]/a" ) );
		assertEquals( "www.webdetails.pt", urlRow1Col2Value );
		assertEquals( "http://ctools.webdetails.org", urlRow2Col2Value );
		assertEquals( "cde.webdetails.org", urlRow3Col2Value );
		String urlRow1Col1AttrHref = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[1]/td[2]/a" ) ).getAttribute( "href" );
		String urlRow2Col1AttrHref = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[2]/td[2]/a" ) ).getAttribute( "href" );
		String urlRow3Col1AttrHref = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[3]/td[2]/a" ) ).getAttribute( "href" );
		assertEquals( "http://www.webdetails.org/", urlRow1Col1AttrHref );
		assertEquals( "http://ctools.webdetails.org/", urlRow2Col1AttrHref );
		assertEquals( "http://cdf.webdetails.org/", urlRow3Col1AttrHref );

		/*
		 * ## Step 2
		 */
		// Check ordering
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='HyperlinkObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='HyperlinkObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='HyperlinkObjTable']/thead/tr/th[1][@class='column0 string sorting_desc']" ) );
		row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr/td" ) );
		row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[2]/td" ) );
		row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[3]/td" ) );
		assertEquals( "Row 3", row1 );
		assertEquals( "Row 2", row2 );
		assertEquals( "Row 1", row3 );
		// Check column value for 'Custom hyperlink'
		urlRow1Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[1]/td[3]/a" ) );
		urlRow2Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[2]/td[3]/a" ) );
		urlRow3Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[3]/td[3]" ) );
		assertEquals( "cde.webdetails.org", urlRow1Col2Value );
		assertEquals( "http://ctools.webdetails.org", urlRow2Col2Value );
		assertEquals( "www.webdetails.pt", urlRow3Col2Value );

		// Check ordering - Custom hyperlink
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='HyperlinkObjTable']/thead/tr/th[3]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='HyperlinkObjTable']/thead/tr/th[3][@class='column2 hyperlink sorting_asc']" ) );
		// Check column value for 'Custom hyperlink'
		row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr/td" ) );
		row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[2]/td" ) );
		row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[3]/td" ) );
		assertEquals( "Row 3", row1 );
		assertEquals( "Row 2", row2 );
		assertEquals( "Row 1", row3 );
		// Check column value for 'Custom hyperlink'
		urlRow1Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[1]/td[3]/a" ) );
		urlRow2Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[2]/td[3]/a" ) );
		urlRow3Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='HyperlinkObjTable']/tbody/tr[3]/td[3]" ) );
		assertEquals( "cde.webdetails.org", urlRow1Col2Value );
		assertEquals( "http://ctools.webdetails.org", urlRow2Col2Value );
		assertEquals( "www.webdetails.pt", urlRow3Col2Value );
	}

	/**
	 * ############################### Test Case 7 ###############################
	 *
	 * Test Case Name:
	 *    Circle
	 *
	 * Description:
	 *    The test case pretends to validate the Circle sample.
	 *
	 * Steps:
	 *    1. Check if the sample for 'Circle' has its contents present
	 *    2. Order the table
	 */
	@Test
	public void tc07_Circle_SampleWorks() {
		this.log.info( "tc07_Circle_SampleWorks" );

		/*
		 * ## Step 1
		 */
		// Check sample title
		String sampleTitle = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "CircleTitle" ) );
		assertEquals( "Circle", sampleTitle );

		// Check Defaults
		String defaultText = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='CircleDesc']/blockquote/pre" ) );
		assertEquals( "defaults: { canvasSize: 10, radius: 4, color: 'black', title: function(st) {return \"Value: \" + st.value;} }", defaultText );

		// Check Rows
		String row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr/td" ) );
		String row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[2]/td" ) );
		String row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[3]/td" ) );
		String row4 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[4]/td" ) );
		String row5 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[5]/td" ) );
		assertEquals( "Row 1", row1 );
		assertEquals( "Row 2", row2 );
		assertEquals( "Row 3", row3 );
		assertEquals( "Row 4", row4 );
		assertEquals( "Row 5", row5 );

		// Check column Default circle
		WebElement chartRow1Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[1]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']" ) );
		WebElement chartRow1Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[1]/td[3]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']" ) );
		WebElement chartRow2Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[2]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']" ) );
		WebElement chartRow2Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[2]/td[3]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']" ) );
		WebElement chartRow3Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[3]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']" ) );
		WebElement chartRow3Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[3]/td[3]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']" ) );
		WebElement chartRow4Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[4]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']" ) );
		WebElement chartRow4Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[4]/td[3]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']" ) );
		WebElement chartRow5Col1 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[5]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']" ) );
		WebElement chartRow5Col2 = this.elemHelper.FindElement( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[5]/td[3]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']" ) );
		assertNotNull( chartRow1Col1 );
		assertNotNull( chartRow1Col2 );
		assertNotNull( chartRow2Col1 );
		assertNotNull( chartRow2Col2 );
		assertNotNull( chartRow3Col1 );
		assertNotNull( chartRow3Col2 );
		assertNotNull( chartRow4Col1 );
		assertNotNull( chartRow4Col2 );
		assertNotNull( chartRow5Col1 );
		assertNotNull( chartRow5Col2 );

		// Check column values
		String circleRow1Col1Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[1]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		String circleRow3Col1Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[3]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		String circleRow3Col2Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[3]/td[3]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		String circleRow4Col2Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[4]/td[3]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		String circleRow5Col2Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[5]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		assertEquals( "Value: 1.26", circleRow1Col1Value );
		assertEquals( "Value: 14.5", circleRow3Col1Value );
		assertEquals( "Value: 50.49", circleRow3Col2Value );
		assertEquals( "Value: 70", circleRow4Col2Value );
		assertEquals( "Value: 14.5", circleRow5Col2Value );

		/*
		 * ## Step 2
		 */
		// Check ordering
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='CircleObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='CircleObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='CircleObjTable']/thead/tr/th[1][@class='column0 string sorting_desc']" ) );
		row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr/td" ) );
		row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[2]/td" ) );
		row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[3]/td" ) );
		row4 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[4]/td" ) );
		row5 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[5]/td" ) );
		assertEquals( "Row 5", row1 );
		assertEquals( "Row 4", row2 );
		assertEquals( "Row 3", row3 );
		assertEquals( "Row 2", row4 );
		assertEquals( "Row 1", row5 );
		// Check column values
		circleRow1Col1Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[1]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		circleRow3Col1Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[3]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		circleRow3Col2Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[3]/td[3]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		circleRow4Col2Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[4]/td[3]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		circleRow5Col2Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[5]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		assertEquals( "Value: 14.5", circleRow1Col1Value );
		assertEquals( "Value: 14.5", circleRow3Col1Value );
		assertEquals( "Value: 50.49", circleRow3Col2Value );
		assertEquals( "Value: 30", circleRow4Col2Value );
		assertEquals( "Value: 1.26", circleRow5Col2Value );

		// Check ordering - Custom circle
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='CircleObjTable']/thead/tr/th[3]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='CircleObjTable']/thead/tr/th[3][@class='column2 circle sorting_asc']" ) );
		// Check column values
		circleRow1Col1Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[1]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		circleRow3Col1Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[3]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		circleRow3Col2Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[3]/td[3]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		circleRow4Col2Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[4]/td[3]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		circleRow5Col2Value = this.elemHelper.GetTextElementInvisible( driver, By.xpath( "//table[@id='CircleObjTable']/tbody/tr[5]/td[2]/*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']/*[name()='circle']/*[name()='title']" ) );
		assertEquals( "Value: 1.26", circleRow1Col1Value );
		assertEquals( "Value: 14.5", circleRow3Col1Value );
		assertEquals( "Value: 50.49", circleRow3Col2Value );
		assertEquals( "Value: 70", circleRow4Col2Value );
		assertEquals( "Value: 14.5", circleRow5Col2Value );
	}

	/**
	 * ############################### Test Case 8 ###############################
	 *
	 * Test Case Name:
	 *    formattedText
	 *
	 * Description:
	 *    The test case pretends to validate the formattedText sample.
	 *
	 * Steps:
	 *    1. Check if the sample for 'formattedText' has its contents present
	 *    2. Order the table
	 */
	@Test
	public void tc08_FormattedText_SampleWorks() {
		this.log.info( "tc08_FormattedText_SampleWorks" );

		/*
		 * ## Step 1
		 */
		// Check sample title
		String sampleTitle = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "textFormatTitle" ) );
		assertEquals( "formattedText", sampleTitle );

		// Check Defaults
		String defaultText = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='textFormatDesc']/blockquote/pre" ) );
		assertEquals( "defaults: { textFormat: function(v, st) {return st.colFormat ? sprintf(st.colFormat,v) : v;} }", defaultText );

		// Check Rows
		String row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr/td" ) );
		String row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[2]/td" ) );
		String row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[3]/td" ) );
		assertEquals( "Row 1", row1 );
		assertEquals( "Row 2", row2 );
		assertEquals( "Row 3", row3 );

		// Check column value
		String textRow1Col1Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[1]/td[2]" ) );
		String textRow1Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[1]/td[3]/span" ) );
		String textRow2Col1Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[2]/td[2]" ) );
		String textRow2Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[2]/td[3]/span" ) );
		String textRow3Col1Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[3]/td[2]" ) );
		String textRow3Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[3]/td[3]/span" ) );
		assertEquals( "1.26%", textRow1Col1Value );
		assertEquals( "0.21", textRow1Col2Value );
		assertEquals( "33.00%", textRow2Col1Value );
		assertEquals( "30.25", textRow2Col2Value );
		assertEquals( "14.50%", textRow3Col1Value );
		assertEquals( "50.49", textRow3Col2Value );

		/*
		 * ## Step 2
		 */
		// Check ordering
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='textFormatObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='textFormatObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='textFormatObjTable']/thead/tr/th[1][@class='column0 string sorting_desc']" ) );
		// Check Rows
		row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr/td" ) );
		row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[2]/td" ) );
		row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[3]/td" ) );
		assertEquals( "Row 3", row1 );
		assertEquals( "Row 2", row2 );
		assertEquals( "Row 1", row3 );
		// Check column value
		textRow1Col1Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[1]/td[2]" ) );
		textRow1Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[1]/td[3]/span" ) );
		textRow2Col1Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[2]/td[2]" ) );
		textRow2Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[2]/td[3]/span" ) );
		textRow3Col1Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[3]/td[2]" ) );
		textRow3Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[3]/td[3]/span" ) );
		assertEquals( "14.50%", textRow1Col1Value );
		assertEquals( "50.49", textRow1Col2Value );
		assertEquals( "33.00%", textRow2Col1Value );
		assertEquals( "30.25", textRow2Col2Value );
		assertEquals( "1.26%", textRow3Col1Value );
		assertEquals( "0.21", textRow3Col2Value );

		// Check ordering - Custom hyperlink
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='textFormatObjTable']/thead/tr/th[3]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='textFormatObjTable']/thead/tr/th[3][@class='column2 formattedText sorting_asc']" ) );
		// Check Rows
		row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr/td" ) );
		row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[2]/td" ) );
		row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[3]/td" ) );
		assertEquals( "Row 1", row1 );
		assertEquals( "Row 2", row2 );
		assertEquals( "Row 3", row3 );
		// Check column value
		textRow1Col1Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[1]/td[2]" ) );
		textRow1Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[1]/td[3]/span" ) );
		textRow2Col1Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[2]/td[2]" ) );
		textRow2Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[2]/td[3]/span" ) );
		textRow3Col1Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[3]/td[2]" ) );
		textRow3Col2Value = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='textFormatObjTable']/tbody/tr[3]/td[3]/span" ) );
		assertEquals( "1.26%", textRow1Col1Value );
		assertEquals( "0.21", textRow1Col2Value );
		assertEquals( "33.00%", textRow2Col1Value );
		assertEquals( "30.25", textRow2Col2Value );
		assertEquals( "14.50%", textRow3Col1Value );
		assertEquals( "50.49", textRow3Col2Value );
	}

	/**
	 * ############################### Test Case 9 ###############################
	 *
	 * Test Case Name:
	 *    cccBulletChart
	 *
	 * Description:
	 *    The test case pretends to validate the cccBulletChart sample.
	 *
	 * Steps:
	 *    1. Check if the sample for 'cccBulletChart' has its contents present
	 *    2. Order the table
	 */
	@Test
	public void tc09_CCCBulletChart_SampleWorks() {
		this.log.info( "tc09_CCCBulletChart_SampleWorks" );

		/*
		 * ## Step 1
		 */
		// Check sample title
		String sampleTitle = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "bulletChartTitle" ) );
		assertEquals( "cccBulletChart", sampleTitle );

		// Check Defaults
		String defaultText = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='bulletChartDesc']/blockquote/pre" ) );
		assertEquals( "defaults: { height: 40, animate: false, orientation: \"horizontal\", bulletSize: 16, bulletSpacing: 150, bulletMargin: 5, bulletRanges: [30,80,100], extensionPoints: { \"bulletMarker_shape\":\"triangle\", \"bulletTitle_textStyle\":\"green\", \"bulletMeasure_fillStyle\":\"black\", \"bulletRuleLabel_font\":\"8px sans-serif\", \"bulletRule_height\": 5 } }", defaultText );

		// Check Rows
		String row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='bulletChartObjTable']/tbody/tr/td" ) );
		String row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='bulletChartObjTable']/tbody/tr[2]/td" ) );
		String row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='bulletChartObjTable']/tbody/tr[3]/td" ) );
		assertEquals( "Row 1", row1 );
		assertEquals( "Row 2", row2 );
		assertEquals( "Row 3", row3 );

		// Check column value
		WebElement chartRow1Col1 = this.elemHelper.FindElement( driver, By.cssSelector( "table#bulletChartObjTable.tableComponent.compact.dataTable tbody tr.odd td.column1.cccBulletChart span svg g g g g g g:nth-child(2) rect" ) );
		WebElement chartRow1Col2 = this.elemHelper.FindElement( driver, By.cssSelector( "table#bulletChartObjTable.tableComponent.compact.dataTable tbody tr.odd td.column2.cccBulletChart span svg g g g g g g:nth-child(2) rect" ) );
		WebElement chartRow2Col1 = this.elemHelper.FindElement( driver, By.cssSelector( "table#bulletChartObjTable.tableComponent.compact.dataTable tbody tr.even td.column1.cccBulletChart span svg g g g g g g:nth-child(2) rect" ) );
		WebElement chartRow2Col2 = this.elemHelper.FindElement( driver, By.cssSelector( "table#bulletChartObjTable.tableComponent.compact.dataTable tbody tr.even td.column2.cccBulletChart span svg g g g g g g:nth-child(2) rect" ) );
		WebElement chartRow3Col1 = this.elemHelper.FindElement( driver, By.cssSelector( "table#bulletChartObjTable.tableComponent.compact.dataTable tbody tr.odd:nth-child(3) td.column1.cccBulletChart span svg g g g g g g:nth-child(2) rect" ) );
		WebElement chartRow3Col2 = this.elemHelper.FindElement( driver, By.cssSelector( "table#bulletChartObjTable.tableComponent.compact.dataTable tbody tr.odd:nth-child(3) td.column2.cccBulletChart span svg g g g g g g:nth-child(2) rect" ) );
		assertNotNull( chartRow1Col1 );
		assertNotNull( chartRow1Col2 );
		assertNotNull( chartRow2Col1 );
		assertNotNull( chartRow2Col2 );
		assertNotNull( chartRow3Col1 );
		assertNotNull( chartRow3Col2 );

		/*
		 * ## Step 2
		 */
		// Check ordering
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='bulletChartObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='bulletChartObjTable']/thead/tr/th[1]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='bulletChartObjTable']/thead/tr/th[1][@class='column0 String sorting_desc']" ) );
		// Check Rows
		row1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='bulletChartObjTable']/tbody/tr/td" ) );
		row2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='bulletChartObjTable']/tbody/tr[2]/td" ) );
		row3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='bulletChartObjTable']/tbody/tr[3]/td" ) );
		assertEquals( "Row 3", row1 );
		assertEquals( "Row 2", row2 );
		assertEquals( "Row 1", row3 );
		// Check column value
		chartRow1Col1 = this.elemHelper.FindElement( driver, By.cssSelector( "table#bulletChartObjTable.tableComponent.compact.dataTable tbody tr.odd td.column1.cccBulletChart span svg g g g g g g:nth-child(2) rect" ) );
		chartRow1Col2 = this.elemHelper.FindElement( driver, By.cssSelector( "table#bulletChartObjTable.tableComponent.compact.dataTable tbody tr.odd td.column2.cccBulletChart span svg g g g g g g:nth-child(2) rect" ) );
		chartRow2Col1 = this.elemHelper.FindElement( driver, By.cssSelector( "table#bulletChartObjTable.tableComponent.compact.dataTable tbody tr.even td.column1.cccBulletChart span svg g g g g g g:nth-child(2) rect" ) );
		chartRow2Col2 = this.elemHelper.FindElement( driver, By.cssSelector( "table#bulletChartObjTable.tableComponent.compact.dataTable tbody tr.even td.column2.cccBulletChart span svg g g g g g g:nth-child(2) rect" ) );
		chartRow3Col1 = this.elemHelper.FindElement( driver, By.cssSelector( "table#bulletChartObjTable.tableComponent.compact.dataTable tbody tr.odd:nth-child(3) td.column1.cccBulletChart span svg g g g g g g:nth-child(2) rect" ) );
		chartRow3Col2 = this.elemHelper.FindElement( driver, By.cssSelector( "table#bulletChartObjTable.tableComponent.compact.dataTable tbody tr.odd:nth-child(3) td.column2.cccBulletChart span svg g g g g g g:nth-child(2) rect" ) );
		assertNotNull( chartRow1Col1 );
		assertNotNull( chartRow1Col2 );
		assertNotNull( chartRow2Col1 );
		assertNotNull( chartRow2Col2 );
		assertNotNull( chartRow3Col1 );
		assertNotNull( chartRow3Col2 );
	}

	/**
	 * ############################### Test Case 10 ###############################
	 *
	 * Test Case Name:
	 *    groupHeaders
	 *
	 * Description:
	 *    The test case pretends to validate the groupHeaders sample.
	 *
	 * Steps:
	 *    1. Check if the sample for 'groupHeaders' has its contents present
	 *    2. Order the table
	 */
	@Test
	public void tc10_GroupHeaderst_SampleWorks() {
		this.log.info( "tc10_GroupHeaderst_SampleWorks" );

		/*
		 * ## Step 1
		 */
		// Check sample title
		String sampleTitle = this.elemHelper.WaitForElementPresentGetText( driver, By.id( "groupHeadersTitle" ) );
		assertEquals( "groupHeaders", sampleTitle );

		// Check Defaults
		String defaultText = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//div[@id='groupHeadersDesc']/blockquote/pre" ) );
		assertEquals( "defaults: { hide: true }", defaultText );

		// Check Rows
		String value1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[2]/td" ) );
		String value2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[5]/td" ) );
		String value3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[7]/td[3]" ) );
		String value4 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[7]/td[4]" ) );
		String value5 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[9]/td[3]" ) );
		String value6 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[9]/td[4]" ) );
		String value7 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[17]/td[3]" ) );
		String value8 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[17]/td[4]" ) );
		String value9 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[31]/td" ) );
		assertEquals( "AAA", value1 );
		assertEquals( "aaa", value2 );
		assertEquals( "-26", value3 );
		assertEquals( "50", value4 );
		assertEquals( "90", value5 );
		assertEquals( "-50", value6 );
		assertEquals( "6", value7 );
		assertEquals( "-32", value8 );
		assertEquals( "ccc", value9 );

		/*
		 * ## Step 2
		 */
		// Check ordering
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='groupHeadersObjTable']/thead/tr/th[3]" ) );
		this.elemHelper.ClickJS( driver, By.xpath( "//table[@id='groupHeadersObjTable']/thead/tr/th[3]" ) );
		this.elemHelper.WaitForElementPresenceAndVisible( driver, By.xpath( "//table[@id='groupHeadersObjTable']/thead/tr/th[3][@class='column2 sorting_desc']" ) );
		// Check Rows
		value1 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[2]/td" ) );
		value2 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[5]/td" ) );
		value3 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[7]/td[3]" ) );
		value4 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[7]/td[4]" ) );
		value5 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[9]/td[3]" ) );
		value6 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[9]/td[4]" ) );
		value7 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[17]/td[3]" ) );
		value8 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[17]/td[4]" ) );
		value9 = this.elemHelper.WaitForElementPresentGetText( driver, By.xpath( "//table[@id='groupHeadersObjTable']/tbody/tr[31]/td" ) );
		assertEquals( "AAA", value1 );
		assertEquals( "aaa", value2 );
		assertEquals( "90", value3 );
		assertEquals( "-50", value4 );
		assertEquals( "-26", value5 );
		assertEquals( "50", value6 );
		assertEquals( "6", value7 );
		assertEquals( "-32", value8 );
		assertEquals( "ccc", value9 );
	}
}
