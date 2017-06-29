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
package com.pentaho.ctools.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;

public class ElementHelper {
  // Retry
  private final static int RETRY_15 = 15;
  // Log instance
  private final Logger log = LogManager.getLogger( ElementHelper.class );
  // Counter for number of retries
  private int clickRetry = RETRY_15;

  /**
   * This method shall wait for the title and return it.
   *
   * @param driver
   * @param url - URL to access
   */
  public void Get( final WebDriver driver, final String url ) {
    this.log.debug( "Get(Main)::Enter" );

    driver.get( url );

    String complete = "complete";
    String state = ( (JavascriptExecutor) driver ).executeScript( "return document.readyState" ).toString();
    this.log.debug( "Page state: " + state );
    for ( int i = 0; i < 20; i++ ) {
      if ( !state.equalsIgnoreCase( complete ) ) {
        try {
          Thread.sleep( 500 );
        } catch ( InterruptedException e ) {
          e.printStackTrace();
        }
        state = ( (JavascriptExecutor) driver ).executeScript( "return document.readyState" ).toString();
        this.log.debug( "Page state: " + state );
      } else {
        break;
      }
    }

    this.log.debug( "Get(Main)::Exit" );
  }

  /**
   * This method shall wait for the title and return it.
   *
   * @param driver
   * @param title
   * @return
   */
  public String WaitForTitle( final WebDriver driver, final String title ) {
    this.log.debug( "WaitForTitle(Main)::Enter" );
    String returnTitle = this.WaitForTitle( driver, title, 30, 150 );
    this.log.debug( "WaitForTitle(Main)::Exit" );
    return returnTitle;
  }

  /**
   * This method shall wait for the title and return it. The developer can specify the timeout and pollingTime.
   *
   *
   * @param driver
   * @param title
   * @param timeout
   * @param pollingTime
   * @return
   */
  public String WaitForTitle( final WebDriver driver, final String title, final long timeout, final long pollingTime ) {
    this.log.debug( "WaitForTitle::Enter" );
    String returnTitle = "";
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );

    ExecutorService executor = null;

    try {

      class RunnableObject implements Runnable {

        private Boolean textIsEquals;

        public Boolean isTextEquals() {
          return this.textIsEquals;
        }

        @Override
        public void run() {
          Wait<WebDriver> wait = new FluentWait<>( driver ).withTimeout( timeout, TimeUnit.SECONDS ).pollingEvery( pollingTime, TimeUnit.MILLISECONDS );

          // Wait for element visible
          this.textIsEquals = wait.until( new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply( WebDriver d ) {
              String currentTitle = driver.getTitle();
              return Boolean.valueOf( currentTitle != null && currentTitle.contains( title ) );
            }
          } );
        }
      }

      RunnableObject r = new RunnableObject();

      executor = Executors.newSingleThreadExecutor();
      executor.submit( r ).get( timeout + 2, TimeUnit.SECONDS );
      if ( r.isTextEquals().booleanValue() ) { // If the text is equals then send the text that we wait for.
        returnTitle = title;
        this.log.debug( "Wait for text successful!" );
      }
    } catch ( InterruptedException ie ) {
      this.log.warn( "Interrupted Exception" );
      this.log.warn( ie.toString() );
    } catch ( ExecutionException ee ) {
      if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
        this.log.warn( "WebDriver timeout exceeded!" );
      } else {
        this.log.warn( "Execution Exception" );
        this.log.warn( ee.toString() );
      }
    } catch ( java.util.concurrent.TimeoutException cte ) {
      this.log.warn( "Thread timeout exceeded!" );
      this.log.warn( cte.toString() );
    } catch ( Exception e ) {
      this.log.error( "Exception" );
      this.log.catching( e );
    }

    if ( executor != null ) {
      executor.shutdown();
    }

    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    this.log.debug( "WaitForTitle::Exit" );
    return returnTitle;
  }

  /**
   * This method works as a wrapper for findElement method of WebDriver. So, in same cases, we may have the issue 'Stale
   * Element Reference', i.e., the element is not ready in DOM. Hence, to prevent exception, we develop a function that
   * is the same of findElement but avoid this exception.
   *
   * @param driver
   * @param locator
   * @return
   */
  public WebElement FindElement( final WebDriver driver, final By locator ) {
    this.log.debug( "FindElement::Enter" );
    WebElement element = WaitForElementPresenceAndVisible( driver, locator, 30 );
    this.log.debug( "FindElement::Exit" );
    return element;
  }

  /**
   * This method works as a wrapper for findElement method of WebDriver. So, in same cases, we may have the issue 'Stale
   * Element Reference', i.e., the element is not ready in DOM. Hence, to prevent exception, we develop a function that
   * is the same of findElement but avoid this exception.
   *
   * @param driver
   * @param locator
   * @return
   */
  public WebElement FindElement( final WebDriver driver, final By locator, long timeout ) {
    this.log.debug( "FindElement::Enter" );
    WebElement element = WaitForElementPresenceAndVisible( driver, locator, timeout );
    this.log.debug( "FindElement::Exit" );
    return element;
  }

  /**
   * This method works as a wrapper for findElement method of WebDriver. So, in same cases, we may have the issue 'Stale
   * Element Reference', i.e., the element is not ready in DOM. Hence, to prevent exception, we develop a function that
   * is the same of findElement but avoid this exception.
   *
   * @param driver
   * @param locator
   * @return
   */
  public WebElement FindElementInvisible( WebDriver driver, By locator ) {
    this.log.debug( "FindElementInvisible(Main)::Enter" );
    WebElement element = FindElementInvisible( driver, locator, 30, 150 );
    this.log.debug( "FindElementInvisible(Main)::Exit" );
    return element;
  }

  /**
   * This method works as a wrapper for findElement method of WebDriver. So, in same cases, we may have the issue 'Stale
   * Element Reference', i.e., the element is not ready in DOM. Hence, to prevent exception, we develop a function that
   * is the same of findElement but avoid this exception.
   *
   * @param driver
   * @param locator
   * @return
   */
  public WebElement FindElementInvisible( WebDriver driver, By locator, long timeout ) {
    this.log.debug( "FindElementInvisible(Main2)::Enter" );
    WebElement element = FindElementInvisible( driver, locator, timeout, 150 );
    this.log.debug( "FindElementInvisible(Main2)::Exit" );
    return element;
  }

  /**
   * This method works as a wrapper for findElement method of WebDriver. So, in same cases, we may have the issue 'Stale
   * Element Reference', i.e., the element is not ready in DOM. Hence, to prevent exception, we develop a function that
   * is the same of findElement but avoid this exception.
   *
   * @param driver
   * @param locator
   * @return
   */
  public WebElement FindElementInvisible( WebDriver driver, By locator, long timeout, final long pollingTime ) {
    this.log.debug( "FindElementInvisible::Enter" );
    this.log.debug( "Locator: " + locator.toString() );

    WebElement element = null;
    ExecutorService executor = null;
    RunnableFindElementInvisible r = new RunnableFindElementInvisible( driver, timeout, pollingTime, locator );

    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );

    try {
      executor = Executors.newSingleThreadExecutor();
      executor.submit( r ).get( timeout + 2, TimeUnit.SECONDS );
      element = r.getInvisbleElement();
    } catch ( InterruptedException ie ) {
      this.log.warn( "Interrupted Exception" );
      this.log.warn( ie.toString() );
    } catch ( ExecutionException ee ) {
      if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
        this.log.warn( "WebDriver timeout exceeded! Looking for: " + locator.toString() );
      } else {
        this.log.warn( "Execution Exception" );
        this.log.warn( ee.toString() );
      }
    } catch ( java.util.concurrent.TimeoutException cte ) {
      this.log.warn( "Thread timeout exceeded! Looking for: " + locator.toString() );
      this.log.warn( cte.toString() );
    } catch ( Exception e ) {
      this.log.error( "Exception" );
      this.log.catching( e );
    }

    if ( executor != null ) {
      executor.shutdown();
    }

    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    this.log.debug( "FindElementInvisible::Exit" );
    return element;
  }

  /**
   *
   * @param driver
   * @param locator
   * @return
   */
  public String GetTextElementInvisible( WebDriver driver, By locator ) {
    this.log.debug( "GetTextElementInvisible::Enter" );
    this.log.debug( "Locator: " + locator.toString() );

    String text = "";
    try {
      WebElement element = FindElementInvisible( driver, locator );
      text = ( (JavascriptExecutor) driver ).executeScript( "return arguments[0].textContent", element ).toString();
    } catch ( StaleElementReferenceException e ) {
      this.log.warn( "Stale Element Reference Exception", e );
      text = FindElementInvisible( driver, locator ).getText();
    } catch ( Exception e ) {
      this.log.catching( e );
    }

    this.log.debug( "GetTextElementInvisible::Exit" );
    return text;
  }

  /**
   * This method wait for text to be present.
   *
   * @param driver
   * @param locator
   * @param text
   * @return
   */
  public String WaitForTextPresence( WebDriver driver, By locator, String textToWait ) {
    this.log.debug( "WaitForTextPresence(Main)::Enter" );
    String str = WaitForTextPresence( driver, locator, textToWait, 10 );
    this.log.debug( "WaitForTextPresence(Main)::Exit" );
    return str;
  }

  /**
   * This method wait for text to be present.
   *
   * @param driver
   * @param locator
   * @param text
   * @return
   */
  public String WaitForTextPresence( WebDriver driver, By locator, String textToWait, long timeout ) {
    this.log.debug( "WaitForTextPresence(Main2)::Enter" );
    String str = WaitForTextPresence( driver, locator, textToWait, timeout, 50 );
    this.log.debug( "WaitForTextPresence(Main2)::Exit" );
    return str;
  }

  /**
   * This method wait for text to be present.
   *
   * @param driver
   * @param locator
   * @param text
   * @param timeout
   *          - in seconds
   * @return
   */
  public String WaitForTextPresence( final WebDriver driver, final By locator, final String textToWait,
      final long timeout, final long pollingTime ) {
    this.log.debug( "WaitForTextPresence::Enter" );
    this.log.debug( "Locator: " + locator.toString() );
    String textPresent = "";
    ExecutorService executor = null;
    RunnableWaitForText r = new RunnableWaitForText( driver, timeout, pollingTime, locator, textToWait );
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );

    try {
      executor = Executors.newSingleThreadExecutor();
      executor.submit( r ).get( timeout + 2, TimeUnit.SECONDS );
      if ( r.isTextEquals().booleanValue() ) { // If the text is equals then send the text that we wait for.
        textPresent = textToWait;
        this.log.debug( "Wait for text successful! We got this [" + textPresent + "]." );
      } else {
        textPresent = r.getTextPresent();
        this.log.debug( "No text present. We only found this [" + textPresent + "]." );
      }
    } catch ( InterruptedException ie ) {
      this.log.warn( "Interrupted Exception" );
      this.log.warn( ie.toString() );
    } catch ( ExecutionException ee ) {
      if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
        this.log.warn( "WebDriver timeout exceeded! Looking for: " + locator.toString() );
        textPresent = r.getTextPresent();
      } else {
        this.log.warn( "Execution Exception" );
        this.log.warn( ee.toString() );
      }
    } catch ( java.util.concurrent.TimeoutException cte ) {
      this.log.warn( "Thread timeout exceeded! Looking for: " + locator.toString() );
      this.log.warn( cte.toString() );
    } catch ( Exception e ) {
      this.log.error( "Exception" );
      this.log.catching( e );
    }

    if ( executor != null ) {
      executor.shutdown();
    }

    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    this.log.debug( "WaitForTextPresence::Exit" );
    return textPresent;
  }

  /**
   * This method wait for text to be present but should be different from empty or null.
   *
   * @param driver
   * @param locator
   * @param text
   * @return
   */
  public String WaitForTextDifferentEmpty( WebDriver driver, By locator ) {
    this.log.debug( "WaitForTextDifferentEmpty(Main)::Enter" );
    String str = WaitForTextDifferentEmpty( driver, locator, 10 );
    this.log.debug( "WaitForTextDifferentEmpty(Main)::Exit" );
    return str;
  }

  /**
   * This method wait for text to be present but should be different from empty or null.
   *
   * @param driver
   * @param locator
   * @param text
   * @return
   */
  public String WaitForTextDifferentEmpty( WebDriver driver, By locator, long timeout ) {
    this.log.debug( "WaitForTextDifferentEmpty(Main2)::Enter" );
    String str = WaitForTextDifferentEmpty( driver, locator, timeout, 50 );
    this.log.debug( "WaitForTextDifferentEmpty(Main2)::Exit" );
    return str;
  }

  /**
   * This method wait for text to be present but should be different from empty or null.
   *
   * @param driver
   * @param locator
   * @param timeout
   *          - in seconds
   * @param pollingTime
   * @return
   */
  public String WaitForTextDifferentEmpty( final WebDriver driver, final By locator, final long timeout,
      final long pollingTime ) {
    this.log.debug( "WaitForTextDifferentEmpty::Enter" );
    this.log.debug( "Locator: " + locator.toString() );
    String textPresent = "";
    ExecutorService executor = null;
    RunnableWaitForTextDifferentEmpty r = new RunnableWaitForTextDifferentEmpty( driver, timeout, pollingTime, locator );
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );

    try {
      executor = Executors.newSingleThreadExecutor();
      executor.submit( r ).get( timeout + 2, TimeUnit.SECONDS );
      textPresent = r.getTextPresent();
    } catch ( InterruptedException ie ) {
      this.log.warn( "Interrupted Exception" );
      this.log.warn( ie.toString() );
    } catch ( ExecutionException ee ) {
      if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
        this.log.warn( "WebDriver timeout exceeded! Looking for: " + locator.toString() );
        textPresent = r.getTextPresent();
      } else {
        this.log.warn( "Execution Exception" );
        this.log.warn( ee.toString() );
      }
    } catch ( java.util.concurrent.TimeoutException cte ) {
      this.log.warn( "Thread timeout exceeded! Looking for: " + locator.toString() );
      this.log.warn( cte.toString() );
    } catch ( Exception e ) {
      this.log.error( "Exception" );
      this.log.catching( e );
    }

    if ( executor != null ) {
      executor.shutdown();
    }

    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    this.log.debug( "WaitForTextPresence::Exit" );
    return textPresent;
  }

  /**
   * This method s
   *
   * @param driver
   * @param locator
   * @param text
   */
  public void WaitForTextDifferentOf( WebDriver driver, By locator, String text ) {
    this.log.debug( "WaitForTextDifferentEmpty(Main)::Enter" );
    WaitForTextDifferentOf( driver, locator, text, 10 );
    this.log.debug( "WaitForTextDifferentEmpty(Main)::Exit" );
  }

  /**
   *
   * @param driver
   * @param locator
   * @param text
   * @param timeout
   */
  public void WaitForTextDifferentOf( WebDriver driver, By locator, String text, long timeout ) {
    this.log.debug( "WaitForTextDifferentEmpty(Main2)::Enter" );
    WaitForTextDifferentOf( driver, locator, text, timeout, 50 );
    this.log.debug( "WaitForTextDifferentEmpty(Main2)::Exit" );
  }

  /**
   *
   *
   * @param driver
   * @param locator
   * @param text
   * @param timeout
   * @param pollingTime
   * @return
   */
  public void WaitForTextDifferentOf( final WebDriver driver, final By locator, final String text, final long timeout,
      final long pollingTime ) {
    this.log.debug( "WaitForTextDifferentEmpty::Enter" );
    this.log.debug( "Locator: " + locator.toString() );
    ExecutorService executor = null;
    RunnableWaitForTextDifferentOf r = new RunnableWaitForTextDifferentOf( driver, text, timeout, pollingTime, locator );
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );

    try {
      executor = Executors.newSingleThreadExecutor();
      executor.submit( r ).get( timeout + 2, TimeUnit.SECONDS );
    } catch ( InterruptedException ie ) {
      this.log.warn( "Interrupted Exception" );
      this.log.warn( ie.toString() );
    } catch ( ExecutionException ee ) {
      if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
        this.log.warn( "WebDriver timeout exceeded! Looking for: " + locator.toString() );
      } else {
        this.log.warn( "Execution Exception" );
        this.log.warn( ee.toString() );
      }
    } catch ( java.util.concurrent.TimeoutException cte ) {
      this.log.warn( "Thread timeout exceeded! Looking for: " + locator.toString() );
      this.log.warn( cte.toString() );
    } catch ( Exception e ) {
      this.log.error( "Exception" );
      this.log.catching( e );
    }

    if ( executor != null ) {
      executor.shutdown();
    }

    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    this.log.debug( "WaitForTextPresence::Exit" );
  }

  /**
   * The function will search for the element and then click on it using the Click method of java script.
   *
   * @param driver
   * @param locator
   */
  public void ClickJS( WebDriver driver, By locator ) {
    this.log.debug( "ClickJS::Enter" );
    this.log.debug( "Locator: " + locator.toString() );

    WebElement element = WaitForElementPresenceAndVisible( driver, locator );
    if ( element != null ) {
      try {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript( "arguments[0].click();", element );
      } catch ( WebDriverException wde ) {
        if ( wde.getMessage().contains( "arguments[0].click is not a function" ) ) {
          element.click();
        }
      }
    } else {
      this.log.error( "Element is null " + locator.toString() );
    }

    this.log.debug( "ClickJS::Exit" );
  }

  /**
   * The function will search for the element and then click on it using the Click method of webdriver.
   *
   * @param driver
   * @param locator
   */
  public void Click( WebDriver driver, By locator ) {
    this.log.debug( "Click::Enter" );
    this.log.debug( "Locator: " + locator.toString() );

    try {
      WebElement element = WaitForElementPresenceAndVisible( driver, locator );
      if ( element != null ) {
        element.click();
        this.log.debug( "Click::Exit" );
      } else {
        this.log.error( "Element is null " + locator.toString() );
      }
    } catch ( StaleElementReferenceException e ) {
      this.log.warn( "Stale Element Reference Exception", e );
      Click( driver, locator );
    } catch ( WebDriverException wde ) {
      this.log.warn( "Web Driver Exception", wde );
      if ( this.clickRetry > 0 ) {
        this.clickRetry--;
        try {
          // Delay 100 ms for page loading or any other dom element loaded
          Thread.sleep( 100 );
        } catch ( InterruptedException ie ) {
          this.log.error( "InterruptedException", ie );
        }
        Click( driver, locator );
        this.clickRetry = RETRY_15;
      }
    }
  }

  /**
   * The function will search for the element present (doesn't matter if element is visible or not) and then click on
   * it.
   *
   * @param driver
   * @param locator
   */
  public void ClickElementInvisible( WebDriver driver, By locator ) {
    this.log.debug( "ClickElementInvisible::Enter" );
    this.log.debug( "Locator: " + locator.toString() );

    WebElement element = FindElementInvisible( driver, locator );
    if ( element != null ) {
      try {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript( "arguments[0].click();", element );
      } catch ( WebDriverException wde ) {
        if ( wde.getMessage().contains( "arguments[0].click is not a function" ) ) {
          element.click();
        }
      }
    } else {
      this.log.error( "Element is null " + locator.toString() );
    }

    this.log.debug( "ClickElementInvisible::Exit" );
  }

  /**
   * The function will search for the element present (doesn't matter if element is visible or not) and then click on
   * it.
   *
   * @param driver
   * @param locator
   */
  public void MoveToElementAndClick( WebDriver driver, By locator ) {
    this.log.debug( "MoveToElementAndClick::Enter" );

    try {
      WebElement element = WaitForElementPresenceAndVisible( driver, locator );
      if ( element != null ) {
        Actions builder = new Actions( driver );
        builder.moveToElement( element ).click( element );
        builder.perform();
      } else {
        this.log.error( "Element is null " + locator.toString() );
      }
    } catch ( StaleElementReferenceException sere ) {
      // Repeat it again
      MoveToElementAndClick( driver, locator );
    }

    this.log.debug( "MoveToElementAndClick::Exit" );
  }

  /**
   * This method pretends to check if the element is present, if it doesn't then don't wait, if element is present, wait
   * for its invisibility.
   *
   * @param driver
   * @param locator
   */
  public boolean WaitForElementInvisibility( WebDriver driver, final By locator ) {
    this.log.debug( "WaitForElementInvisibility(Main)::Enter" );
    boolean isElemVisible = WaitForElementInvisibility( driver, locator, 30 );
    this.log.debug( "WaitForElementInvisibility(Main)::Exit" );

    return isElemVisible;
  }

  /**
   * This method pretends to check if the element is present, if it doesn't then don't wait, if element is present, wait
   * for its invisibility. true - element invisible false - element visible
   *
   * @param driver
   * @param locator
   * @param timeout
   */
  public boolean WaitForElementInvisibility( final WebDriver driver, final By locator, final long timeout ) {
    this.log.debug( "WaitForElementInvisibility::Enter" );
    this.log.debug( "Locator: " + locator.toString() );
    boolean isElemVisible = false;
    ExecutorService executor = null;

    try {

      driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );

      try {
        class RunnableObject implements Runnable {

          private Boolean isVisible;

          public Boolean getVisibility() {
            return this.isVisible;
          }

          @Override
          public void run() {
            Wait<WebDriver> wait = new FluentWait<>( driver ).withTimeout( timeout, TimeUnit.SECONDS ).pollingEvery( 50, TimeUnit.MILLISECONDS );

            // Wait for element invisible
            this.isVisible = wait.until( new Function<WebDriver, Boolean>() {

              @Override
              public Boolean apply( WebDriver d ) {
                try {
                  List<WebElement> listElements = d.findElements( locator );
                  if ( listElements.size() > 0 ) {
                    WebElement elem = listElements.get( 0 );
                    return Boolean.valueOf( ( !elem.isDisplayed() ) ? true : false );
                  }
                  // The element does not exit, i.e., is not visible and even present
                  return Boolean.valueOf( true );
                } catch ( StaleElementReferenceException sere ) {
                  return Boolean.valueOf( false );
                }
              }
            } );
          }
        }

        RunnableObject r = new RunnableObject();
        executor = Executors.newSingleThreadExecutor();
        executor.submit( r ).get( timeout + 2, TimeUnit.SECONDS );
        isElemVisible = r.getVisibility().booleanValue();
      } catch ( InterruptedException ie ) {
        this.log.warn( "Interrupted Exception" );
        this.log.warn( ie.toString() );
      } catch ( ExecutionException ee ) {
        if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
          this.log.warn( "WebDriver timeout exceeded! Looking for: " + locator.toString() );
          this.log.warn( ee.toString() );
        } else {
          this.log.warn( "Execution Exception" );
          this.log.warn( ee.toString() );
        }
      } catch ( java.util.concurrent.TimeoutException cte ) {
        this.log.warn( "Thread timeout exceeded! Looking for: " + locator.toString() );
        this.log.warn( cte.toString() );
      } catch ( Exception e ) {
        this.log.error( "Exception" );
        this.log.catching( e );
      }

      if ( executor != null ) {
        executor.shutdown();
      }

      driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    } catch ( Exception ge ) {
      this.log.error( "Exception" );
      this.log.catching( ge );
    }
    this.log.debug( "WaitForElementInvisibility::Exit" );

    return isElemVisible;
  }

  /**
   * This method pretends to check if the element is present, if it doesn't we wait for presence for a specific timeout
   * (input), after this, we will wait for element visible. And, if the element is present then we have to check if is
   * visible if not wait for visibility.
   *
   * The default timeout to wait for elements is 30 seconds.
   *
   * @param driver
   * @param locator
   */
  public WebElement WaitForElementPresenceAndVisible( WebDriver driver, By locator ) {
    this.log.debug( "WaitForElementPresenceAndVisible(Main)::Enter" );
    WebElement element = WaitForElementPresenceAndVisible( driver, locator, 30 );
    this.log.debug( "WaitForElementPresenceAndVisible(Main)::Exit" );
    return element;
  }

  /**
   * This method pretends to check if the element is present, if it doesn't we wait for presence for a specific timeout
   * (input), after this, we will wait for element visible. And, if the element is present then we have to check if is
   * visible if not wait for visibility.
   *
   * @param driver
   * @param locator
   * @param timeout
   */
  public WebElement WaitForElementPresenceAndVisible( final WebDriver driver, final By locator, final long timeout ) {
    this.log.debug( "WaitForElementPresenceAndVisible::Enter" );
    this.log.debug( "Locator: " + locator.toString() );
    WebElement element = null;
    ExecutorService executor = null;
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );

    try {

      class RunnableObject implements Runnable {

        private WebElement theElement;

        public RunnableObject( WebElement theElement ) {
          this.theElement = theElement;
        }

        public WebElement getValue() {
          return this.theElement;
        }

        @Override
        public void run() {
          Wait<WebDriver> wait = new FluentWait<>( driver ).withTimeout( timeout, TimeUnit.SECONDS ).pollingEvery( 50, TimeUnit.MILLISECONDS );

          // Wait for element visible
          this.theElement = wait.until( new Function<WebDriver, WebElement>() {

            @Override
            public WebElement apply( WebDriver d ) {
              try {
                List<WebElement> listElem = d.findElements( locator );
                if ( listElem.size() > 0 ) {
                  WebElement elem = listElem.get( 0 );
                  if ( elem != null && ( ( elem.isEnabled() == true ) && ( elem.isDisplayed() == true ) ) ) {
                    return elem;
                  }
                  return null;
                }
                return null;
              } catch ( StaleElementReferenceException sere ) {
                return null;
              }
            }
          } );
        }
      }

      RunnableObject r = new RunnableObject( element );
      executor = Executors.newSingleThreadExecutor();
      executor.submit( r ).get( timeout + 2, TimeUnit.SECONDS );
      element = r.getValue();
    } catch ( InterruptedException ie ) {
      this.log.warn( "Interrupted Exception" );
      this.log.warn( ie.toString() );
    } catch ( ExecutionException ee ) {
      if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
        this.log.warn( "WebDriver timeout exceeded! Looking for: " + locator.toString() );
      } else {
        this.log.warn( "Execution Exception" );
        this.log.warn( ee.toString() );
      }
    } catch ( java.util.concurrent.TimeoutException cte ) {
      this.log.warn( "Thread timeout exceeded! Looking for: " + locator.toString() );
      this.log.warn( cte.toString() );
    } catch ( Exception e ) {
      this.log.error( "Exception" );
      this.log.catching( e );
    }

    if ( executor != null ) {
      executor.shutdown();
    }

    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    this.log.debug( "WaitForElementPresenceAndVisible::Exit" );
    return element;
  }

  /**
   * This method pretends to check if the element is present, if it doesn't we wait for presence for a specific timeout
   * (30 seconds).
   *
   * @param driver
   * @param locator
   */
  public WebElement WaitForElementPresence( WebDriver driver, By locator ) {
    this.log.debug( "WaitForElementPresence(Main)::Enter" );
    WebElement element = WaitForElementPresence( driver, locator, 30 );
    this.log.debug( "WaitForElementPresence(Main)::Exit" );
    return element;
  }

  /**
   * This method pretends to check if the element is present, if it doesn't we wait for presence for a specific timeout
   * (30 seconds).
   *
   * @param driver
   * @param locator
   */
  public WebElement WaitForElementPresence( WebDriver driver, By locator, long timeout ) {
    this.log.debug( "WaitForElementPresence(Main2)::Enter" );
    WebElement element = WaitForElementPresence( driver, locator, timeout, 50 );
    this.log.debug( "WaitForElementPresence(Main2)::Exit" );
    return element;
  }

  /**
   * This method pretends to check if the element is present, if it doesn't we wait for presence for a specific timeout
   * (input).
   *
   * @param driver
   * @param locator
   */
  public WebElement WaitForElementPresence( final WebDriver driver, final By locator, final long timeout,
      final long pollingTime ) {
    this.log.debug( "WaitForElementPresence::Enter" );
    this.log.debug( "Locator: " + locator.toString() );
    WebElement element = null;
    ExecutorService executor = null;
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );

    try {

      class RunnableObject implements Runnable {

        private WebElement theElement = null;

        public RunnableObject() {
        }

        public WebElement getValue() {
          return this.theElement;
        }

        @Override
        public void run() {
          Wait<WebDriver> wait = new FluentWait<>( driver ).withTimeout( timeout, TimeUnit.SECONDS ).pollingEvery( pollingTime, TimeUnit.MILLISECONDS );

          // Wait for element visible
          this.theElement = wait.until( new Function<WebDriver, WebElement>() {

            @Override
            public WebElement apply( WebDriver d ) {
              try {
                List<WebElement> listElem = d.findElements( locator );
                if ( listElem.size() > 0 ) {
                  WebElement elem = listElem.get( 0 );
                  if ( elem.isEnabled() ) {
                    return elem;
                  }
                  return null;
                }
                return null;
              } catch ( StaleElementReferenceException sere ) {
                return null;
              }
            }
          } );
        }
      }

      RunnableObject r = new RunnableObject();
      executor = Executors.newSingleThreadExecutor();
      executor.submit( r ).get( timeout + 2, TimeUnit.SECONDS );
      element = r.getValue();
    } catch ( InterruptedException ie ) {
      this.log.warn( "Interrupted Exception" );
      this.log.warn( ie.toString() );
    } catch ( ExecutionException ee ) {
      if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
        this.log.warn( "WebDriver timeout exceeded! Looking for: " + locator.toString() );
      } else {
        this.log.warn( "Execution Exception" );
        this.log.warn( ee.toString() );
      }
    } catch ( java.util.concurrent.TimeoutException cte ) {
      this.log.warn( "Thread timeout exceeded! Looking for: " + locator.toString() );
      this.log.warn( cte.toString() );
    } catch ( Exception e ) {
      this.log.error( "Exception" );
      this.log.catching( e );
    }

    if ( executor != null ) {
      executor.shutdown();
    }

    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    this.log.debug( "WaitForElementPresence::Exit" );
    return element;
  }

  /**
   * This method check if the element is present, if don't waits for it. And then returns the textContent. The max
   * timeout set for this function is 5 seconds.
   *
   * NOTE - the method was created to help on the tool tips, when mouse over an element and we want to read the
   * elements.
   *
   * @param driver
   * @param locator
   * @return
   */
  public String WaitForElementPresentGetText( final WebDriver driver, final By locator ) {
    this.log.debug( "WaitForElementPresentGetText::Enter" );
    String text = "";

    WebElement element = WaitForElementPresence( driver, locator, 5, 15 );
    if ( element != null ) {
      try {
        // Cross-browser, see: http://www.quirksmode.org/dom/html/
        text = ( (JavascriptExecutor) driver ).executeScript( "return (arguments[0].innerText == null)?arguments[0].textContent:arguments[0].innerText", element ).toString();
        text = text.replaceAll( "\\xA0", " " );
        text = text.replaceAll( "\\s+", " " );
        text = text.replaceAll( "\\r\\n|\\r|\\n|\\t", "" );
        text = text.trim(); // remove spaces, newlines,...
      } catch ( WebDriverException wde ) {
        this.log.warn( "WebDriver Exception" );
        this.log.warn( wde.toString() );
        text = WaitForElementPresentGetText( driver, locator );
      }
    } else {
      this.log.warn( "Element does not exist! [null element]" );
    }

    this.log.debug( "WaitForElementPresentGetText::Exit" );
    return text;
  }

  /**
   * This method intends to get the value of an input field.
   *
   * @param driver
   * @param locator
   * @return
   */
  public String GetInputValue( WebDriver driver, By locator ) {
    this.log.debug( "GetInputValue::Enter" );
    this.log.debug( "Locator: " + locator.toString() );

    String attrValue = "";
    WebElement element = FindElement( driver, locator );
    if ( element != null ) {
      attrValue = element.getAttribute( "value" );
    }

    this.log.debug( "GetInputValue::Exit" );
    return attrValue;
  }

  /**
   * This method intends to drag and drop an element.
   *
   * @param driver
   * @param from
   * @param to
   * @return
   */
  public void DragAndDrop( WebDriver driver, By from, By to ) {
    this.log.debug( "DragAndDrop::Enter" );
    this.log.debug( "From: " + from.toString() );
    this.log.debug( "To: " + to.toString() );

    WebElement drag = FindElement( driver, from );
    WebElement drop = FindElement( driver, to );
    if ( drag != null && drop != null ) {
      new Actions( driver ).dragAndDrop( drag, drop ).build().perform();
    }

    this.log.debug( "DragAndDrop::exit" );
  }

  /**
   * This method intends to check if two elements overlap or are contained inside each other, returns true if elements
   * don't overlap.
   *
   * @param driver
   * @param element1
   * @param element2
   * @return
   */
  public boolean ElementsNotOverlap( WebDriver driver, By element1, By element2 ) {
    this.log.debug( "ElementsNotOverlap::Enter" );
    this.log.debug( "Locator1: " + element1.toString() );
    this.log.debug( "Locator2: " + element2.toString() );

    WebElement elem1 = FindElement( driver, element1 );
    WebElement elem2 = FindElement( driver, element2 );

    // get borders of first element
    Point firstLocation = elem1.getLocation();
    Dimension firstDimension = elem1.getSize();
    int firstLeft = firstLocation.getX();
    int firstTop = firstLocation.getY();
    int firstRight = firstLeft + firstDimension.getWidth();
    int firstBottom = firstTop + firstDimension.getHeight();
    // get borders of second element
    Point secondLocation = elem2.getLocation();
    Dimension secondDimension = elem2.getSize();
    int secondLeft = secondLocation.getX();
    int secondTop = secondLocation.getY();
    int secondRight = secondLeft + secondDimension.getWidth();
    int secondBottom = secondTop + secondDimension.getHeight();
    this.log.debug( firstTop + " " + firstBottom + " " + firstLeft + " " + firstRight );
    this.log.debug( secondTop + " " + secondBottom + " " + secondLeft + " " + secondRight );
    // if firstElement is either to the left, the right, above or below the second return true
    boolean notIntersected = firstBottom < secondTop || firstTop > secondBottom || firstLeft > secondRight || firstRight < secondLeft;

    this.log.debug( "ElementsNotOverlap::Exit" );
    return notIntersected;
  }

  /**
   * This function shall wait for the new window display. The code check if there is more than one window in the list.
   * In the beginning we only have the main window.
   *
   * @param driver
   */
  public void WaitForNewWindow( WebDriver driver ) {
    this.log.debug( "WaitForNewWindow::Enter" );

    Wait<WebDriver> wait = new FluentWait<>( driver ).withTimeout( 30, TimeUnit.SECONDS ).pollingEvery( 500, TimeUnit.MILLISECONDS );

    wait.until( new ExpectedCondition<Boolean>() {

      @Override
      public Boolean apply( WebDriver d ) {
        return Boolean.valueOf( d.getWindowHandles().size() != 1 );
      }
    } );

    this.log.debug( "WaitForNewWindow::Exit" );
  }

  /**
   * This function shall wait for the alert window to be closed or doesn't exist.
   *
   * @param driver
   */
  public void WaitForAlertNotPresent( WebDriver driver ) {
    this.log.debug( "WaitForAlertNotPresent::Enter" );

    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );
    
    Wait<WebDriver> wait = new FluentWait<>( driver ).withTimeout( 30, TimeUnit.SECONDS ).pollingEvery( 50, TimeUnit.MILLISECONDS );

    wait.until( new ExpectedCondition<Boolean>() {

      @Override
      public Boolean apply( WebDriver d ) {
        Boolean alertExist = Boolean.valueOf( false );
        try {
          d.switchTo().alert();
        } catch ( NoAlertPresentException e ) {
          log.warn("Exception No Alert");
          alertExist= Boolean.valueOf(true);
        }
        return alertExist;
      }
    } );

    this.log.debug( "WaitForAlertNotPresent::Exit" );
  }

  /**
   * The method will wait for the frame to be available to usage. To ensure that we check if an element exist inside
   * (example a new element that refresh the frame).
   *
   * @param driver
   * @param locator
   */
  public void WaitForFrameReady( WebDriver driver, final By locator ) {
    this.log.debug( "WaitForFrameReady::Enter" );
    this.log.debug( "Locator: " + locator.toString() );

    Wait<WebDriver> wait = new FluentWait<>( driver ).withTimeout( 30, TimeUnit.SECONDS ).pollingEvery( 100, TimeUnit.MILLISECONDS );

    wait.until( new ExpectedCondition<Boolean>() {

      @Override
      public Boolean apply( WebDriver d ) {
        Boolean elementExist = Boolean.valueOf( false );
        List<WebElement> listElements = d.findElements( locator );

        if ( listElements.size() > 0 ) {
          elementExist = Boolean.valueOf( true );
        }
        return elementExist;
      }
    } );

    this.log.debug( "WaitForFrameReady::Exit" );
  }

  /**
   *
   *
   * @param driver
   * @param locator
   * @param attributeName
   * @return
   */
  public String GetAttribute( WebDriver driver, By locator, String attributeName ) {
    this.log.debug( "GetAttribute::Enter" );

    String attributeValue = "";
    try {
      WebElement element = WaitForElementPresenceAndVisible( driver, locator, 30 );
      if ( element != null ) {
        attributeValue = element.getAttribute( attributeName );
      } else {
        this.log.warn( "Element is null - could not get attribute value!" );
      }
    } catch ( StaleElementReferenceException e ) {
      this.log.warn( "Stale Element Reference Exception", e );
      attributeValue = GetAttribute( driver, locator, attributeName );
    }

    this.log.debug( "GetAttribute::Exit" );
    return attributeValue;
  }

  /**
   *
   *
   * @param driver
   * @param locator
   * @param attributeName
   * @return
   */
  public String GetAttribute( WebDriver driver, By locator, String attributeName, long timeout ) {
    this.log.debug( "GetAttribute::Enter" );

    String attributeValue = "";
    try {
      WebElement element = WaitForElementPresenceAndVisible( driver, locator, timeout );
      if ( element != null ) {
        attributeValue = element.getAttribute( attributeName );
      } else {
        this.log.warn( "Element is null - could not get attribute value!" );
      }
    } catch ( StaleElementReferenceException e ) {
      this.log.warn( "Stale Element Reference Exception", e );
      attributeValue = GetAttribute( driver, locator, attributeName );
    }

    this.log.debug( "GetAttribute::Exit" );
    return attributeValue;
  }

  /**
   * This method pretends to assert the element is not present and return a boolean = true if it isn't.
   *
   * @param driver
   * @param locator
   * @return
   */
  public boolean WaitForElementNotPresent( final WebDriver driver, final By locator ) {
    this.log.debug( "WaitForElementNotPresent(Main)::Enter" );
    boolean result = WaitForElementNotPresent( driver, locator, 30 );
    this.log.debug( "WaitForElementNotPresent(Main)::Exit" );
    return result;
  }

  /**
   * This method pretends to assert the element is not present and return a boolean = true if it isn't. This method also
   * allow user to set a specify timeout.
   *
   * @param driver
   * @param locator
   * @param timeout
   */
  public boolean WaitForElementNotPresent( final WebDriver driver, final By locator, final long timeout ) {
    this.log.debug( "WaitForElementNotPresent::Enter" );
    Boolean notPresent = Boolean.valueOf( false );
    ExecutorService executor = null;
    this.log.debug( "Locator: " + locator.toString() );
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );

    try {

      class RunnableObject implements Runnable {

        private Boolean NotPresent;

        public RunnableObject( Boolean NotPresent ) {
          this.NotPresent = NotPresent;
        }

        public Boolean getValue() {
          return this.NotPresent;
        }

        @Override
        public void run() {
          Wait<WebDriver> wait = new FluentWait<>( driver ).withTimeout( timeout, TimeUnit.SECONDS ).pollingEvery( 50, TimeUnit.MILLISECONDS );

          // Wait for element visible
          this.NotPresent = wait.until( new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply( WebDriver d ) {
              try {
                List<WebElement> listElem = d.findElements( locator );
                if ( listElem.size() == 0 ) {
                  return Boolean.valueOf( true );
                }
                return Boolean.valueOf( false );
              } catch ( StaleElementReferenceException sere ) {
                return Boolean.valueOf( true );
              }
            }
          } );
        }
      }

      RunnableObject r = new RunnableObject( notPresent );
      executor = Executors.newSingleThreadExecutor();
      executor.submit( r ).get( timeout + 2, TimeUnit.SECONDS );
      notPresent = r.getValue();
    } catch ( InterruptedException ie ) {
      this.log.warn( "Interrupted Exception" );
      this.log.warn( ie.toString() );
    } catch ( ExecutionException ee ) {
      if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
        this.log.warn( "WebDriver timeout exceeded! Looking for: " + locator.toString() );
      } else {
        this.log.warn( "Execution Exception" );
        this.log.warn( ee.toString() );
      }
    } catch ( java.util.concurrent.TimeoutException cte ) {
      this.log.warn( "Thread timeout exceeded! Looking for: " + locator.toString() );
      this.log.warn( cte.toString() );
    } catch ( Exception e ) {
      this.log.error( "Exception" );
      this.log.catching( e );
    }

    if ( executor != null ) {
      executor.shutdown();
    }

    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    this.log.debug( "WaitForElementNotPresent::Exit" );
    return notPresent.booleanValue();
  }

  /**
   * The method pretends to wait for an element reach the expected attribute value. The default timeout is 30 seconds.
   *
   * @param driver
   * @param locator
   * @param attributeName
   * @param attributeValue - attribute value to wait.
   */
  public void WaitForAttributeValue( final WebDriver driver, final By locator, final String attributeName,
      final String attributeValue ) {
    this.log.debug( "WaitForAttributeValue(Main)::Enter" );
    WaitForAttributeValue( driver, locator, attributeName, attributeValue, 30 );
    this.log.debug( "WaitForAttributeValue(Main)::Exit" );
  }

  /**
   * The method pretends to wait for an element reach the expected attribute value, specifying a timeout.
   *
   * @param driver
   * @param locator
   * @param attributeName
   * @param attributeValue
   *          - attribute value to wait.
   */
  public void WaitForAttributeValue( final WebDriver driver, final By locator, final String attributeName,
      final String attributeValue, final long timeout ) {
    this.log.debug( "WaitForAttributeValue::Enter" );
    this.log.debug( "Locator: " + locator.toString() );
    this.log.debug( "Attribute: " + attributeName );
    this.log.debug( "AttributeValue: " + attributeValue );
    ExecutorService executor = null;
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );

    try {

      class RunnableObject implements Runnable {

        @Override
        public void run() {
          Wait<WebDriver> wait = new FluentWait<>( driver ).withTimeout( timeout, TimeUnit.SECONDS ).pollingEvery( 50, TimeUnit.MILLISECONDS );

          // Wait for element visible
          wait.until( new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply( WebDriver d ) {
              try {
                List<WebElement> listElements = d.findElements( locator );
                if ( listElements.size() > 0 ) {
                  WebElement element = listElements.get( 0 );
                  String attrValue = element.getAttribute( attributeName ).toLowerCase();
                  String attrValueFor = attributeValue.toLowerCase();
                  return Boolean.valueOf( attrValue.contains( attrValueFor ) );
                }
                return Boolean.valueOf( false );
              } catch ( StaleElementReferenceException sere ) {
                return Boolean.valueOf( true );
              }
            }
          } );
        }
      }

      RunnableObject r = new RunnableObject();
      executor = Executors.newSingleThreadExecutor();
      executor.submit( r ).get( timeout + 2, TimeUnit.SECONDS );
    } catch ( InterruptedException ie ) {
      this.log.warn( "Interrupted Exception" );
      this.log.warn( ie.toString() );
    } catch ( ExecutionException ee ) {
      if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
        this.log.warn( "WebDriver timeout exceeded! Looking for: " + locator.toString() );
      } else {
        this.log.warn( "Execution Exception" );
        this.log.warn( ee.toString() );
      }
    } catch ( java.util.concurrent.TimeoutException cte ) {
      this.log.warn( "Thread timeout exceeded! Looking for: " + locator.toString() );
      this.log.warn( cte.toString() );
    } catch ( Exception e ) {
      this.log.error( "Exception" );
      this.log.catching( e );
    }

    if ( executor != null ) {
      executor.shutdown();
    }

    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    this.log.debug( "WaitForAttributeValue::Exit" );
  }

  /**
   * The method pretends to wait for an element reach the expected attribute value. The default timeout is 30 seconds.
   *
   * @param driver
   * @param locator
   * @param attributeName
   * @param attributeValue
   *          - attribute value to wait.
   */
  public void WaitForAttributeValueEqualsTo( final WebDriver driver, final By locator, final String attributeName,
      final String attributeValue ) {
    this.log.debug( "WaitForAttributeValue(Main)::Enter" );
    WaitForAttributeValueEqualsTo( driver, locator, attributeName, attributeValue, 30 );
    this.log.debug( "WaitForAttributeValue(Main)::Exit" );
  }

  /**
   * The method pretends to wait for an element reach the expected attribute value, specifying a timeout.
   *
   * @param driver
   * @param locator
   * @param attributeName
   * @param attributeValue
   *          - attribute value to wait.
   */
  public void WaitForAttributeValueEqualsTo( final WebDriver driver, final By locator, final String attributeName,
      final String attributeValue, final long timeout ) {
    this.log.debug( "WaitForAttributeValue::Enter" );
    this.log.debug( "Locator: " + locator.toString() );
    this.log.debug( "Attribute: " + attributeName );
    this.log.debug( "AttributeValue: " + attributeValue );
    ExecutorService executor = null;
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );

    try {

      class RunnableObject implements Runnable {

        @Override
        public void run() {
          Wait<WebDriver> wait = new FluentWait<>( driver ).withTimeout( timeout, TimeUnit.SECONDS ).pollingEvery( 50, TimeUnit.MILLISECONDS );

          // Wait for element visible
          wait.until( new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply( WebDriver d ) {
              try {
                List<WebElement> listElements = d.findElements( locator );
                if ( listElements.size() > 0 ) {
                  WebElement element = listElements.get( 0 );
                  String attrValue = element.getAttribute( attributeName ).toLowerCase();
                  String attrValueFor = attributeValue.toLowerCase();
                  return Boolean.valueOf( attrValue.equals( attrValueFor ) );
                }
                return Boolean.valueOf( false );
              } catch ( StaleElementReferenceException sere ) {
                return Boolean.valueOf( true );
              }
            }
          } );
        }
      }

      RunnableObject r = new RunnableObject();
      executor = Executors.newSingleThreadExecutor();
      executor.submit( r ).get( timeout + 2, TimeUnit.SECONDS );
    } catch ( InterruptedException ie ) {
      this.log.warn( "Interrupted Exception" );
      this.log.warn( ie.toString() );
    } catch ( ExecutionException ee ) {
      if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
        this.log.warn( "WebDriver timeout exceeded! Looking for: " + locator.toString() );
      } else {
        this.log.warn( "Execution Exception" );
        this.log.warn( ee.toString() );
      }
    } catch ( java.util.concurrent.TimeoutException cte ) {
      this.log.warn( "Thread timeout exceeded! Looking for: " + locator.toString() );
      this.log.warn( cte.toString() );
    } catch ( Exception e ) {
      this.log.error( "Exception" );
      this.log.catching( e );
    }

    if ( executor != null ) {
      executor.shutdown();
    }

    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    this.log.debug( "WaitForAttributeValue::Exit" );
  }

  /**
   * This method shall focus on the element and then add the text.
   *
   * @param driver
   * @param locator
   * @param text
   */
  public void ClickAndSendKeys( final WebDriver driver, final By locator, final CharSequence... keysToSend ) {
    this.log.debug( "ClickAndSendKeys::Enter" );

    try {
      WebElement element = WaitForElementPresenceAndVisible( driver, locator );
      if ( element != null ) {
        element.click();
        element.sendKeys( keysToSend );
      } else {
        this.log.error( "Element is null!" );
      }
    } catch ( StaleElementReferenceException e ) {
      this.log.warn( "Stale Element Reference Exception", e );
      ClickAndSendKeys( driver, locator, keysToSend );
    }
    this.log.debug( "ClickAndSendKeys::Exit" );
  }

  /**
   * This method find the element and sendkeys.
   *
   * @param driver
   * @param locator
   * @param text
   */
  public void SendKeys( final WebDriver driver, final By locator, final CharSequence... keysToSend ) {
    this.log.debug( "SendKeys::Enter" );

    try {
      WebElement element = WaitForElementPresenceAndVisible( driver, locator );
      if ( element != null ) {
        element.sendKeys( keysToSend );
      } else {
        this.log.error( "Element is null!" );
      }
    } catch ( StaleElementReferenceException e ) {
      this.log.warn( "Stale Element Reference Exception", e );
      SendKeys( driver, locator, keysToSend );
    }
    this.log.debug( "SendKeys::Exit" );
  }

  /**
   * This method find the element and sendkeys.
   *
   * @param driver
   * @param locator
   * @param text
   */
  public void SendKeysAndSubmit( final WebDriver driver, final By locator, final CharSequence... keysToSend ) {
    this.log.debug( "SendKeysAndSubmit::Enter" );

    try {
      WebElement element = WaitForElementPresenceAndVisible( driver, locator );
      if ( element != null ) {
        element.clear();
        element.sendKeys( keysToSend );
        element.submit();
      } else {
        this.log.error( "Element is null!" );
      }
    } catch ( StaleElementReferenceException e ) {
      this.log.warn( "Stale Element Reference Exception", e );
      SendKeysAndSubmit( driver, locator, keysToSend );
    }
    this.log.debug( "SendKeysAndSubmit::Exit" );
  }

  /**
   * This method find the element and clear the contents in the element (input or textarea).
   *
   * @param driver
   * @param locator
   */
  public void Clear( final WebDriver driver, final By locator ) {
    this.log.debug( "Clear::Enter" );

    try {
      WebElement element = WaitForElementPresenceAndVisible( driver, locator );
      if ( element != null ) {
        element.clear();
      } else {
        this.log.error( "Element is null!" );
      }
    } catch ( StaleElementReferenceException e ) {
      this.log.warn( "Stale Element Reference Exception", e );
      Clear( driver, locator );
    }
    this.log.debug( "SendKeys::Exit" );
  }

  /**
   * This method find the element, clear the field and sendkeys.
   *
   * @param driver
   * @param locator
   * @param text
   */
  public void ClearAndSendKeys( final WebDriver driver, final By locator, final CharSequence... keysToSend ) {
    this.log.debug( "ClearAndSendKeys::Enter" );

    try {
      WebElement element = WaitForElementPresenceAndVisible( driver, locator );
      if ( element != null ) {
        element.clear();
        element.sendKeys( keysToSend );
      } else {
        this.log.error( "Element is null!" );
      }
    } catch ( StaleElementReferenceException e ) {
      this.log.warn( "Stale Element Reference Exception", e );
      SendKeys( driver, locator, keysToSend );
    }
    this.log.debug( "ClearAndSendKeys::Exit" );
  }

  /**
   * This method is a wrap of switchTo method used in WebDriver.
   *
   * @param driver
   * @param nameOrId
   */
  public WebDriver SwitchToFrame( final WebDriver driver, String nameOrId ) {
    WebDriver newframe = null;
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );
    newframe = driver.switchTo().frame( nameOrId );
    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    return newframe;
  }

  /**
   * This method is a wrap of switchTo method used in WebDriver.
   *
   * @param driver
   * @param index
   */
  public WebDriver SwitchToFrame( final WebDriver driver, int index ) {
    WebDriver newframe = null;
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );
    newframe = driver.switchTo().frame( index );
    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    return newframe;
  }

  /**
   * This method is a wrap of switchTo method used in WebDriver.
   *
   * @param driver
   * @param frameElement
   */
  public WebDriver SwitchToFrame( final WebDriver driver, WebElement frameElement ) {
    WebDriver newframe = null;
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );
    newframe = driver.switchTo().frame( frameElement );
    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    return newframe;
  }

  /**
   * This method is a wrap of switchTo method used in WebDriver.
   *
   * @param driver
   */
  public WebDriver SwitchToDefault( final WebDriver driver ) {
    WebDriver newframe = null;
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );
    newframe = driver.switchTo().defaultContent();
    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    return newframe;
  }

  /**
   * This method shall select an element on a drop-down list by Value.
   *
   * @param driver
   * @param locator
   * @param value
   */
  public void SelectByValue( final WebDriver driver, final By locator, final String value ) {
    this.log.debug( "SelectByValue::Enter" );
    this.log.debug( "Locator: " + locator.toString() );

    try {
      WebElement elementSelector = WaitForElementPresence( driver, locator );
      if ( elementSelector != null ) {
        Select list = new Select( elementSelector );
        list.selectByValue( value );
      } else {
        this.log.warn( "The element does not exist [null]. Could perform the select action!" );
      }
    } catch ( StaleElementReferenceException e ) {
      this.log.warn( "Stale Element Reference Exception", e );
      SelectByValue( driver, locator, value );
    }
    this.log.debug( "SelectByValue::Exit" );
  }

  /**
   * This method shall perform the MoveToElement wrap function of WebDriver. We have to do this wrap to avoid
   * StaleElement exceptions.
   *
   * @param driver
   * @param toLocator
   */
  public void MoveToElement( final WebDriver driver, final By toLocator ) {
    this.log.debug( "MoveToElement::Enter" );
    try {
      WebElement element = FindElementInvisible( driver, toLocator );
      if ( element != null ) {
        Actions acts = new Actions( driver );
        acts.moveToElement( element );
        acts.build().perform();
      } else {
        this.log.warn( "Element null!" );
      }
    } catch ( StaleElementReferenceException sere ) {
      this.log.warn( "Stale Element Reference Exception" );
      MoveToElement( driver, toLocator );
    }
    this.log.debug( "MoveToElement::Exit" );
  }

  /**
   * This method shall perform the MoveToElement wrap function of WebDriver. We have to do this wrap to avoid
   * StaleElement exceptions.
   *
   * @param driver
   * @param toLocator
   * @param xOffset
   * @param yOffset
   */
  public void MoveToElement( final WebDriver driver, final By toLocator, final int xOffset, final int yOffset ) {
    this.log.debug( "MoveToElement::Enter" );
    try {
      WebElement element = WaitForElementPresenceAndVisible( driver, toLocator );
      if ( element != null ) {
        Actions acts = new Actions( driver );
        acts.moveToElement( element, xOffset, yOffset );
        acts.build().perform();
      } else {
        this.log.warn( "Element null!" );
      }
    } catch ( StaleElementReferenceException sere ) {
      this.log.warn( "Stale Element Reference Exception" );
      MoveToElement( driver, toLocator, xOffset, yOffset );
    }
    this.log.debug( "MoveToElement::Exit" );
  }

  /**
   * The method shall move mouse over the element and fire the event onclick when clicking on the element.
   *
   * @param driver
   * @param locator
   */
  public void MouseOverElementAndClick( final WebDriver driver, final By locator ) {
    this.log.debug( "MouseOverElementAndClick::Enter" );
    WebElement elementToOver = WaitForElementPresenceAndVisible( driver, locator );
    if ( elementToOver != null ) {
      String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
      String onClickScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('click', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onclick');}";

      JavascriptExecutor js = (JavascriptExecutor) driver;
      js.executeScript( mouseOverScript, elementToOver );
      js.executeScript( onClickScript, elementToOver );
    } else {
      this.log.warn( "Element null!" );
    }
    this.log.debug( "MouseOverElementAndClick::Exit" );
  }

  /**
   * Wait for alert display and return the presented text on the alert window.
   *
   * @param driver
   * @return
   */
  public String WaitForAlertReturnConfirmationMsg( WebDriver driver ) {
    this.log.debug( "WaitForAlertReturnConfirmationMsg::Enter" );
    String confirmationMsg = "";
    Alert alert = WaitForAlert( driver, 10, 15 );

    if ( alert != null ) {
      confirmationMsg = alert.getText();
      alert.accept();
    }

    this.log.debug( "WaitForAlertReturnConfirmationMsg::Exit" );
    return confirmationMsg;
  }

  /**
   * This method shall wait for an alert shows-up.
   *
   * @param driver
   * @param timeout
   * @param pollingTime
   * @return
   */
  public Alert WaitForAlert( final WebDriver driver, final long timeout, final long pollingTime ) {
    this.log.debug( "WaitForAlert::Enter" );
    ExecutorService executor = null;

    Alert alert = null;

    try {
      class CheckForAlert implements Callable<Alert> {

        @Override
        public Alert call() {
          Wait<WebDriver> wait = new FluentWait<>( driver ).withTimeout( timeout, TimeUnit.SECONDS ).pollingEvery( pollingTime, TimeUnit.MILLISECONDS );

          return wait.until( new Function<WebDriver, Alert>() {

            @Override
            public Alert apply( WebDriver driver ) {
              try {
                return driver.switchTo().alert();
              } catch ( NoAlertPresentException e ) {
                e.printStackTrace();
                return null;
              }
            }
          } );
        }
      }

      CheckForAlert call = new CheckForAlert();
      executor = Executors.newSingleThreadExecutor();
      alert = executor.submit( call ).get( timeout + 2, TimeUnit.SECONDS );

    } catch ( InterruptedException ie ) {
      this.log.warn( "Interrupted Exception" );
      this.log.warn( ie.toString() );
    } catch ( ExecutionException ee ) {
      if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
        this.log.warn( "WebDriver timeout exceeded!" );
      } else {
        this.log.warn( "Execution Exception" );
        this.log.warn( ee.toString() );
      }
    } catch ( java.util.concurrent.TimeoutException cte ) {
      this.log.warn( "Thread timeout exceeded!" );
      this.log.warn( cte.toString() );
    } catch ( Exception e ) {
      this.log.error( "Exception" );
      this.log.catching( e );
    }

    if ( executor != null ) {
      executor.shutdown();
    }
    this.log.debug( "WaitForAlert::Exit" );
    return alert;
  }

  /**
   * This method shall select a new window that has opened and return the handle for the parent window.
   *
   * The handle should be stored in a string so we can go back to the parent window afterwards using the
   * SelectParentWindow method
   *
   * @param driver
   * @return parentWindow
   */
  public String SelectNewWindow( final WebDriver driver ) {
    String parentWindowHandle = driver.getWindowHandle();
    Set<String> listWindows = driver.getWindowHandles();
    WaitForNewWindow( driver );
    listWindows = driver.getWindowHandles();
    // Get the windowHandler of the new open window
    Iterator<String> iterWindows = listWindows.iterator();
    while ( iterWindows.hasNext() ) {
      String windowHandle = iterWindows.next();
      if ( windowHandle.equals( parentWindowHandle ) == false ) {
        driver.switchTo().window( windowHandle );
        break;
      }
    }
    return parentWindowHandle;
  }

  /**
   * This method shall close current window and select previous window to carry on testing
   *
   * @param driver
   * @param parentWindow
   */
  public void SelectParentWindow( final WebDriver driver, final String parentWindow ) {
    // Need guarantee we close everything
    WebDriver previewWindow = null;
    String currentWindowHandle = driver.getWindowHandle();
    previewWindow = driver.switchTo().window( currentWindowHandle );
    previewWindow.close();
    driver.switchTo().window( parentWindow );
  }
  
  
  /**
   * This method shall find for elements presence and not for 'presence and visible'.
   *
   * @param driver
   * @param locator
   * @return
   */
  public List<WebElement> FindElementsPresence( final WebDriver driver, final By locator ) {
    this.log.debug( "FindElement::Enter" );
    List<WebElement> elements = WaitForElementsPresence( driver, locator, 30 );
    this.log.debug( "FindElement::Exit" );
    return elements;
  }
  
  /**
   * This method pretends to check if a list of elements is present, if it doesn't we wait for presence for a specific
   * timeout (input). Only element presents, not visible.
   *
   * @param driver
   * @param locator
   * @param timeout
   */
  public List<WebElement> WaitForElementsPresence( final WebDriver driver, final By locator,
      final long timeout ) {
    this.log.debug( "WaitForElementPresenceAndVisible::Enter" );
    this.log.debug( "Locator: " + locator.toString() );
    List<WebElement> elements = null;
    ExecutorService executor = null;
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );

    try {

      class RunnableObject implements Runnable {

        private List<WebElement> theElements;

        public RunnableObject( List<WebElement> theElements ) {
          this.theElements = theElements;
        }

        public List<WebElement> getValue() {
          return this.theElements;
        }

        @Override
        public void run() {
          Wait<WebDriver> wait = new FluentWait<>( driver ).withTimeout( timeout, TimeUnit.SECONDS ).pollingEvery( 50, TimeUnit.MILLISECONDS );

          // Wait for element visible
          this.theElements = wait.until( new Function<WebDriver, List<WebElement>>() {

            @Override
            public List<WebElement> apply( WebDriver d ) {
              try {
                List<WebElement> elems = d.findElements( locator );
                int count = 0;
                for ( int i = 0; i < elems.size(); i++ ) {
                  WebElement elem = elems.get( i );

                  if ( elem != null && elem.isEnabled() ) {
                    count++;
                    continue;
                  }                  
                }
                
                if (count != elems.size()) {
                  return null;
                }
                
                return elems;
              } catch ( StaleElementReferenceException sere ) {
                return null;
              }
            }
          } );
        }
      }

      RunnableObject r = new RunnableObject( elements );
      executor = Executors.newSingleThreadExecutor();
      executor.submit( r ).get( timeout + 2, TimeUnit.SECONDS );
      elements = r.getValue();
    } catch ( InterruptedException ie ) {
      this.log.warn( "Interrupted Exception" );
      this.log.warn( ie.toString() );
    } catch ( ExecutionException ee ) {
      if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
        this.log.warn( "WebDriver timeout exceeded! Looking for: " + locator.toString() );
      } else {
        this.log.warn( "Execution Exception" );
        this.log.warn( ee.toString() );
      }
    } catch ( java.util.concurrent.TimeoutException cte ) {
      this.log.warn( "Thread timeout exceeded! Looking for: " + locator.toString() );
      this.log.warn( cte.toString() );
    } catch ( Exception e ) {
      this.log.error( "Exception" );
      this.log.catching( e );
    }

    if ( executor != null ) {
      executor.shutdown();
    }

    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    this.log.debug( "WaitForElementPresenceAndVisible::Exit" );
    return elements;
  }


  /**
   * This method works as a wrapper for findElements method of WebDriver. So, in same cases, we may have the issue
   * 'Stale Element Reference', i.e., the element is not ready in DOM. Hence, to prevent exception, we develop a
   * function that is the same of findElement but avoid this exception.
   *
   * @param driver
   * @param locator
   * @return
   */
  public List<WebElement> FindElements( final WebDriver driver, final By locator ) {
    this.log.debug( "FindElement::Enter" );
    List<WebElement> elements = WaitForElementsPresenceAndVisible( driver, locator, 30 );
    this.log.debug( "FindElement::Exit" );
    return elements;
  }

  /**
   * This method pretends to check if a list of elements is present, if it doesn't we wait for presence for a specific
   * timeout (input), after this, we will wait for element visible. And, if any elements is present then we have to
   * check if is visible if not wait for visibility.
   *
   * @param driver
   * @param locator
   * @param timeout
   */
  public List<WebElement> WaitForElementsPresenceAndVisible( final WebDriver driver, final By locator,
      final long timeout ) {
    this.log.debug( "WaitForElementPresenceAndVisible::Enter" );
    this.log.debug( "Locator: " + locator.toString() );
    List<WebElement> elements = null;
    ExecutorService executor = null;
    driver.manage().timeouts().implicitlyWait( 0, TimeUnit.SECONDS );

    try {

      class RunnableObject implements Runnable {

        private List<WebElement> theElements;

        public RunnableObject( List<WebElement> theElements ) {
          this.theElements = theElements;
        }

        public List<WebElement> getValue() {
          return this.theElements;
        }

        @Override
        public void run() {
          Wait<WebDriver> wait = new FluentWait<>( driver ).withTimeout( timeout, TimeUnit.SECONDS ).pollingEvery( 50, TimeUnit.MILLISECONDS );

          // Wait for element visible
          this.theElements = wait.until( new Function<WebDriver, List<WebElement>>() {

            @Override
            public List<WebElement> apply( WebDriver d ) {
              try {
                List<WebElement> elems = d.findElements( locator );
                int count = 0;
                log.debug( "TESTING size: " + elems.size() );
                for ( int i = 0; i < elems.size(); i++ ) {
                  WebElement elem = elems.get( i );

                  log.debug( "TESTING elem: " + i );
                  
                  log.debug( "TESTING ena: " + elem.isEnabled() );
                  log.debug( "TESTING dis: " + elem.isDisplayed() );
                  
                  if ( elem != null && ( ( elem.isEnabled() ) && ( elem.isDisplayed() ) ) ) {
                    count++;
                    continue;
                  }                  
                }
                
                log.debug( "TESTING count: " + count );
                log.debug( "TESTING size: " + elems.size() );
                if (count != elems.size()) {
                  return null;
                }
                
                return elems;
              } catch ( StaleElementReferenceException sere ) {
                return null;
              }
            }
          } );
        }
      }

      RunnableObject r = new RunnableObject( elements );
      executor = Executors.newSingleThreadExecutor();
      executor.submit( r ).get( timeout + 2, TimeUnit.SECONDS );
      elements = r.getValue();
    } catch ( InterruptedException ie ) {
      this.log.warn( "Interrupted Exception" );
      this.log.warn( ie.toString() );
    } catch ( ExecutionException ee ) {
      if ( ee.getCause().getClass().getCanonicalName().equalsIgnoreCase( TimeoutException.class.getCanonicalName() ) ) {
        this.log.warn( "WebDriver timeout exceeded! Looking for: " + locator.toString() );
      } else {
        this.log.warn( "Execution Exception" );
        this.log.warn( ee.toString() );
      }
    } catch ( java.util.concurrent.TimeoutException cte ) {
      this.log.warn( "Thread timeout exceeded! Looking for: " + locator.toString() );
      this.log.warn( cte.toString() );
    } catch ( Exception e ) {
      this.log.error( "Exception" );
      this.log.catching( e );
    }

    if ( executor != null ) {
      executor.shutdown();
    }

    driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    this.log.debug( "WaitForElementPresenceAndVisible::Exit" );
    return elements;
  }

}
