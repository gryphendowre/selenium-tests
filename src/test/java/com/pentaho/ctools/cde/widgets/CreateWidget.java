package com.pentaho.ctools.cde.widgets;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.Test;

import com.pentaho.ctools.utils.ElementHelper;
import com.pentaho.gui.web.ctools.cde.utils.Widgets;

/**
 * NOTE - The test was created regarding issue CDF-318
 */
public class CreateWidget {
	// Instance of the driver (browser emulator)
	private WebDriver driver;
	// Instance to be used on wait commands
	private Wait<WebDriver> wait;
	// The base url to be append the relative url in test
	private String baseUrl;
	// The name for the widget to be created
	private String widgetName = "dummyCreateWidget";
	// The name for the parameter to be added
	private String paramName = "paramCreateWidget";
	// The wrap class of Webdriver.
	private final ElementHelper elemHelper = new ElementHelper();

	@Test
	public void testCreateWidget() throws Exception {
		//Step 0 - Delete the widget
		Widgets widgets = new Widgets();
		widgets.RemoveWidgetByName( this.driver, this.widgetName );

		//Create widget with specific parameter
		this.driver = widgets.CreateWidgetWithParameter( this.driver, this.widgetName, this.paramName );
	}

	@Test
	public void checkExistentWidgetAndParameter() throws Exception {
		//Resuming Steps
		// 1. open the widget
		// 2. check if the parameter exist in settings
		// 3. check if the widget exist in 'widgets' at Component Layout
		Widgets widgets = new Widgets();
		this.driver = widgets.OpenWidgetEditMode( this.driver, this.wait, this.baseUrl, this.widgetName );

		//Step 3 - Check if the parameter exist in 'Settings'
		//Move to the iframe
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//div[@id='solutionNavigatorAndContentPanel']/div[4]/table/tbody/tr[2]/td/div/div/table/tbody/tr/td/iframe" ) ) );
		WebElement frameCDEDashboard = this.driver.findElement( By.xpath( "//div[@id='solutionNavigatorAndContentPanel']/div[4]/table/tbody/tr[2]/td/div/div/table/tbody/tr/td/iframe" ) );
		this.elemHelper.SwitchToFrame( driver, frameCDEDashboard );
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//div[@class='layoutPanelButton']" ) ) );
		assertTrue( this.driver.findElement( By.xpath( "//div[@class='layoutPanelButton']" ) ).isEnabled() );
		//Press 'Settings'
		this.driver.findElement( By.xpath( "//div[@id='headerLinks']/div[5]/a" ) ).click();
		this.wait.until( ExpectedConditions.visibilityOfAllElementsLocatedBy( By.xpath( "//div[@id='popup']" ) ) );
		assertNotNull( this.driver.findElement( By.xpath( "//span[@id='widgetParameters']/div/input" ) ) );
		//The parameter MUST be equal to the one set
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//span[@id='widgetParameters']/div/span" ) ) );
		assertEquals( this.driver.findElement( By.xpath( "//span[@id='widgetParameters']/div/span" ) ).getText(), this.paramName );
		//Press on the check box
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//span[@id='widgetParameters']/div/input" ) ) );
		assertTrue( this.driver.findElement( By.xpath( "//span[@id='widgetParameters']/div/input" ) ).isSelected() );
		//Press button 'Cancel'
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//div[@class='popupbuttons']/button[@id='popup_state0_buttonCancel']" ) ) );
		this.driver.findElement( By.xpath( "//div[@class='popupbuttons']/button[@id='popup_state0_buttonCancel']" ) ).click();

		//Step 4 - Click on Component Panel and check if the widget is listed
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//div[@class='componentsPanelButton']" ) ) );
		this.driver.findElement( By.xpath( "//div[@class='componentsPanelButton']" ) ).click();
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//div[@id='cdfdd-components-palletePallete']/div[9]/h3/span" ) ) );
		this.driver.findElement( By.xpath( "//div[@id='cdfdd-components-palletePallete']/div[9]/h3/span" ) ).click();
		//Getting the element where the widget created is displayed
		WebElement listOfWidgets = this.driver.findElement( By.xpath( "//div[@id='cdfdd-components-palletePallete']/div[9]/div" ) );
		//Check if the widget created is listed
		WebElement theWidgetCreated = listOfWidgets.findElement( By.xpath( "//a[@class='tooltip' and contains(text(),'" + this.widgetName + "')]" ) );
		assertNotNull( theWidgetCreated );
		assertEquals( theWidgetCreated.getText(), this.widgetName );

		//Step 5 - Click in the widget created and check if the widget is add at Components column and Properties
		theWidgetCreated.click();
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//table[@id='table-cdfdd-components-components']/tbody/tr[4]/td" ) ) );
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//table[@id='table-cdfdd-components-properties']/tbody/tr[2]/td" ) ) );
		assertEquals( this.driver.findElement( By.xpath( "//table[@id='table-cdfdd-components-components']/tbody/tr[4]/td" ) ).getText(), this.widgetName + " Widget" );
		assertEquals( this.driver.findElement( By.xpath( "//table[@id='table-cdfdd-components-properties']/tbody/tr[2]/td" ) ).getText(), "Parameter " + this.paramName );
	}
}
