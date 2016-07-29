package com.pentaho.ctools.cdf.tutorials;

import static org.testng.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.pentaho.ctools.utils.ElementHelper;
import com.pentaho.ctools.utils.PageUrl;
import com.pentaho.selenium.BaseTest;

public class TimeplotsDatePickers extends BaseTest {
	// Access to wrapper for webdriver
	private static final ElementHelper elemHelper = new ElementHelper();
	//Log instance
	private static final Logger log = LogManager.getLogger( TimeplotsDatePickers.class );

	/**
	* ############################### Test Case 0 ###############################
	*
	* Description:
	* 	Opens Timeplots and Date Pickers tutorial and check if the page was loaded.
	*/
	@Test
	public void tc0_openTimeplotsDatePickers() {
		log.info( "openTimeplotsDatePickers" );
		
		//Open CDE Tutorials Welcome page 
		elemHelper.Get(driver, PageUrl.CDF_TUTORIAL_TIMEPLOTS_DATE_PICKERS);
		
		//Test if page was loaded
		assertEquals(elemHelper.WaitForElementPresentGetText(driver, By.xpath("//*[@id='title']/h1")),"Timeplots and Date Pickers");
					  
		return;
	}
	
	/**
	   * ############################### Test Case 0 ###############################
	   *
	   * Test Case Name:
	   *    Check Tabs Names
	   *    
	   * Test Case Description:
	   * 	Calls a function that check that the first tab is named "Intro" and the remaining tabs are named accordingly to the step number (e.g. Step1...StepX).
	   * 	The function will call another function that will check if every tab has a right panel with a title similar to the tab name.
	   * Test Steps:
	   * 		1. Call the function checkTabs with the quantity of steps that the sample has.
	   */
	@Test
	public void tc1_checkTabsNames()
	{
		log.info( "checkTabsNames" );
		
		CdfTutorials.checkTabs(4);
	}
	
}