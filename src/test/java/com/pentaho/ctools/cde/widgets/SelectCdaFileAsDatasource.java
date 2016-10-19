package com.pentaho.ctools.cde.widgets;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.pentaho.ctools.utils.ElementHelper;
import com.pentaho.gui.web.ctools.cde.utils.Widgets;

/**
 * NOTE - The test was created regarding issue CDE-140
 */
public class SelectCdaFileAsDatasource {
	// Instance of the driver (browser emulator)
	private WebDriver driver;
	// Instance to be used on wait commands
	private final Wait<WebDriver> wait = null;
	// The base url to be append the relative url in test
	private final String baseUrl = null;
	// Access to wrapper for webdriver
	private final ElementHelper elemHelper = new ElementHelper();
	// The name for the widget to be created
	private final String widgetName = "dummyWidgetSelectCdaDatasource";

	/**
	 * Where we do stuff (like: clean, prepare data) before start testing.
	 */
	@BeforeClass
	public void setUpClass() {
		Widgets widgets = new Widgets();
		// ##Step 0 - Delete the widget
		widgets.RemoveWidgetByName( this.driver, this.widgetName );
	}

	/**
	 * ############################### Test Case 1 ###############################
	 *
	 * Test Case Name:
	 *    Select CDA File As Datasource
	 * Description:
	 *    When we insert a CDA file as data source, the path shall be a full path.
	 * Steps:
	 *    1. Create a widget
	 *    2. Go to the widget and assign a data source file (CDA)
	 *    3. Go back the widget and check if persist the data source field
	 *
	 * @throws Exception
	 */
	@Test
	public void tc1_SelectCdaFileAsDatasource_PathOfCdaFileCorrect() throws Exception {
		Widgets widgets = new Widgets();

		// ##Step 1 - Create widget with specific parameter
		widgets.CreateWidget( this.driver, this.widgetName );

		// ##Step 2 - Access the widget
		this.driver = widgets.OpenWidgetEditMode( this.driver, this.wait, this.baseUrl, this.widgetName );

		// ##Step 3 - Click in Datasources panel and add a CDA Datasource
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//div[@id='solutionNavigatorAndContentPanel']/div[4]/table/tbody/tr[2]/td/div/div/table/tbody/tr/td/iframe" ) ) );
		WebElement frameCDEDashboard = this.driver.findElement( By.xpath( "//div[@id='solutionNavigatorAndContentPanel']/div[4]/table/tbody/tr[2]/td/div/div/table/tbody/tr/td/iframe" ) );
		this.elemHelper.SwitchToFrame( driver, frameCDEDashboard );
		// Click in Datasources panel
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//div[@class='datasourcesPanelButton']" ) ) );
		this.driver.findElement( By.xpath( "//div[@class='datasourcesPanelButton']" ) ).click();
		// Go to Community Data Access (left panel)
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//div[@id='cdfdd-datasources-palletePallete']/div[2]/h3/span" ) ) );
		this.driver.findElement( By.xpath( "//div[@id='cdfdd-datasources-palletePallete']/div[2]/h3/span" ) ).click();
		// Click in 'CDA Data Source'
		final WebElement elementListedOthers = this.driver.findElement( By.xpath( "//div[@id='cdfdd-datasources-palletePallete']/div[2]/div" ) );
		elementListedOthers.findElement( By.xpath( "//a[@title='CDA Data Source']" ) ).click();

		// ##Step 4 - Add the cda file
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.id( "table-cdfdd-datasources-properties" ) ) );
		// Click in the property to add the file
		this.elemHelper.WaitForElementVisibility( this.driver, By.xpath( "//table[@id='table-cdfdd-datasources-properties']/tbody/tr[4]/td[2]/button" ) );
		this.driver.findElement( By.xpath( "//table[@id='table-cdfdd-datasources-properties']/tbody/tr[4]/td[2]/button" ) ).click();
		assertNotNull( this.elemHelper.WaitForElementVisibility( this.driver, By.id( "popupbox" ) ) );
		assertNotNull( this.elemHelper.WaitForElementVisibility( this.driver, By.id( "popup_state_browse" ) ) );
		// Click in 'Public'
		this.wait.until( ExpectedConditions.visibilityOfAllElementsLocatedBy( By.id( "container_id" ) ) );
		final WebElement listFolders = this.driver.findElement( By.xpath( "//div[@id='container_id']" ) );
		listFolders.findElement( By.xpath( "//a[@rel='public/']" ) ).click();
		// Click in 'plugin-samples'
		this.wait.until( ExpectedConditions.visibilityOfAllElementsLocatedBy( By.id( "container_id" ) ) );
		assertNotNull( this.elemHelper.WaitForElementVisibility( this.driver, By.xpath( "//a[@rel='public/plugin-samples/']" ) ) );
		this.driver.findElement( By.xpath( "//a[@rel='public/plugin-samples/']" ) ).click();
		// Click in 'cda'
		assertNotNull( this.elemHelper.WaitForElementVisibility( this.driver, By.xpath( "//a[@rel='public/plugin-samples/cda/']" ) ) );
		this.driver.findElement( By.xpath( "//a[@rel='public/plugin-samples/cda/']" ) ).click();
		// Click in 'cdafiles'
		assertNotNull( this.elemHelper.WaitForElementVisibility( this.driver, By.xpath( "//a[@rel='public/plugin-samples/cda/cdafiles/']" ) ) );
		this.driver.findElement( By.xpath( "//a[@rel='public/plugin-samples/cda/cdafiles/']" ) ).click();
		// Select a file
		assertNotNull( this.elemHelper.WaitForElementVisibility( this.driver, By.xpath( "//a[@rel='public/plugin-samples/cda/cdafiles/compoundJoin.cda']" ) ) );
		this.driver.findElement( By.xpath( "//a[@rel='public/plugin-samples/cda/cdafiles/compoundJoin.cda']" ) ).click();
		// Click OK
		this.wait.until( ExpectedConditions.visibilityOfAllElementsLocatedBy( By.id( "popup_browse_buttonOk" ) ) );
		this.driver.findElement( By.id( "popup_browse_buttonOk" ) ).click();
		this.elemHelper.WaitForElementInvisibility( this.driver, By.id( "popup_browse_buttonOk" ) );
		// SAVE the widget
		this.elemHelper.WaitForElementVisibility( this.driver, By.xpath( "//div[@id='headerLinks']/div[2]/a" ) );
		this.driver.findElement( By.xpath( "//div[@id='headerLinks']/div[2]/a" ) ).click();
		this.elemHelper.WaitForElementInvisibility( this.driver, By.id( "notifyBar" ) );

		// ## Step 5 - Check if the file persist after SAVE widget
		this.driver = widgets.OpenWidgetEditMode( this.driver, this.wait, this.baseUrl, this.widgetName );
		// Open
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//div[@id='solutionNavigatorAndContentPanel']/div[4]/table/tbody/tr[2]/td/div/div/table/tbody/tr/td/iframe" ) ) );
		frameCDEDashboard = this.driver.findElement( By.xpath( "//div[@id='solutionNavigatorAndContentPanel']/div[4]/table/tbody/tr[2]/td/div/div/table/tbody/tr/td/iframe" ) );
		this.elemHelper.SwitchToFrame( driver, frameCDEDashboard );
		// Click in Datasources panel
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//div[@class='datasourcesPanelButton']" ) ) );
		this.driver.findElement( By.xpath( "//div[@class='datasourcesPanelButton']" ) ).click();
		// Click in CDA Data Source in the list of Datasources
		// Expand group
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.id( "table-cdfdd-datasources-datasources" ) ) );
		this.driver.findElement( By.xpath( "//table[@id='table-cdfdd-datasources-datasources']/tbody/tr/td/span" ) ).click();
		// Click in CDA Data Source
		this.wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//table[@id='table-cdfdd-datasources-datasources']/tbody/tr[2]" ) ) );
		this.driver.findElement( By.xpath( "//table[@id='table-cdfdd-datasources-datasources']/tbody/tr[2]/td" ) ).click();

		/*#######################################
		  EXPECT RESULT:
		  Path shall start with '/'
		 #######################################*/
		assertEquals( "Path", this.driver.findElement( By.xpath( "//table[@id='table-cdfdd-datasources-properties']/tbody/tr[4]/td" ) ).getText() );
		assertEquals( "/public/plugin-samples/cda/cdafiles/compoundJoin.cda", this.driver.findElement( By.xpath( "//table[@id='table-cdfdd-datasources-properties']/tbody/tr[4]/td[2]/div" ) ).getText() );
	}
}
