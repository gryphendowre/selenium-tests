/*!*****************************************************************************
 *
 * Selenium Tests For CTools
 *
 * Copyright (C) 2002-2014 by Pentaho : http://www.pentaho.com
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
package org.pentaho.ctools.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class ElementHelper {
  //Log instance
  private static Logger log = LogManager.getLogger(ElementHelper.class);

  /**
   * This method shall check if the element to search is displayed and is
   * enabled, if not, wait until reach 20000 (20sec).
   *
   * @param driver
   *          The access to browser.
   * @param path
   *          DOM element to search.
   */
  public static boolean IsElementDisplayed(WebDriver driver, By path) {
    boolean elementDisplayed = false;

    for (int i = 0; i < 100; i++) {
      try {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        WebElement element = driver.findElement(path);
        if (element != null) {
          if (element.isDisplayed() && element.isEnabled()) {
            elementDisplayed = true;
            break;
          }
        }
      } catch (NoSuchElementException ex) {
        System.out.println(ex.getMessage());
      }

      try {
        Thread.sleep(200);
      } catch (InterruptedException ex) {
        System.out.println("Exception timeout");
      }
    }

    return elementDisplayed;
  }

  /**
   * This method shall verify if the element exist in DOM, if not exist wait the
   * amount of time specified.
   *
   * @param driver
   *          The access to browser.
   * @param timeToWaitSec
   *          The amount of time to wait for the element.
   * @param path
   *          The element path in DOM (css, id, xpath)
   * @return true Element exist in DOM. false Element does not exist.
   */
  public static boolean IsElementPresent(WebDriver driver, long timeToWaitSec, By path) {
    boolean elementPresent = true;

    try {
      driver.manage().timeouts().implicitlyWait(timeToWaitSec, TimeUnit.SECONDS);
      driver.findElement(path);
    } catch (NoSuchElementException ex) {
      elementPresent = false;
    }

    // Restore to initial value
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    return elementPresent;
  }

  /**
   * TODO
   *
   * @param driver
   * @param maxTimeToWaitSec
   * @param path
   */
  public static void WaitForElementNotPresent(WebDriver driver, long maxTimeToWaitSec, By path) {
    if (maxTimeToWaitSec > 0) {
      int timeToWaitSec = 2;

      for (int i = 0; i < maxTimeToWaitSec; i = i + timeToWaitSec) {
        Boolean isPresent = IsElementPresent(driver, timeToWaitSec, path);
        if (isPresent == false) {
          break;
        }
      }
    }
  }

  /**
   * TODO
   *
   * @param driver
   * @param wait
   * @param locator
   * @return
   */
  public static boolean IsElementInvisible(WebDriver driver, By locator) {
    boolean bElementInvisible = false;
    try {
      driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
      driver.findElement(locator);
    } catch (NoSuchElementException s) {
      log.error("NuSuchElement - got it. Locator: " + locator.toString());
      bElementInvisible = true;
    }

    if (!bElementInvisible) {
      Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS);

      driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
      bElementInvisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    return bElementInvisible;
  }

  /**
   * TODO
   *
   * @param driver
   * @param locator
   * @return
   */
  public static WebElement IsElementVisible(WebDriver driver, By locator) {
    WebElement element = null;
    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS).pollingEvery(200, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);

    //Wait for element presence
    wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    return element;
  }

  /**
   * TODO
   *
   * @param driver
   * @param locator
   * @return
   */
  public static boolean IsElementPresent(WebDriver driver, By locator) {
    boolean isElementPresent = false;
    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS).pollingEvery(200, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);

    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

    WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    if (element != null) {
      isElementPresent = true;
    }
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    return isElementPresent;
  }

  /**
   * This method works as a wrapper for findElement method of WebDriver.
   * So, in same cases, we may have the issue 'Stale Element Reference', i.e.,
   * the element is not ready in DOM. Hence, to prevent exception, we develop
   * a function that is the same of findElement but avoid this exception.
   *
   * @param driver
   * @param locator
   * @return
   */
  public static WebElement FindElement(WebDriver driver, By locator) {
    log.debug("Enter:FindElement");
    try {
      IsElementVisible(driver, locator);
      log.debug("Element is visble");
      List<WebElement> listElements = driver.findElements(locator);
      if (listElements.size() > 0) {
        WebElement element = listElements.get(0);
        if (element.isDisplayed() && element.isEnabled()) {
          log.debug("return element found it");
          return element;
        } else {
          log.warn("Trying again! Displayed:" + element.isDisplayed() + " Enabled:" + element.isEnabled() + " Locator: " + locator.toString());
          return FindElement(driver, locator);
        }
      } else {
        log.warn("Trying obtain! Locator: " + locator.toString());
        return null;
      }
    } catch (StaleElementReferenceException s) {
      log.error("Stale - got one. Locator: " + locator.toString());
      return FindElement(driver, locator);
    } catch (ElementNotVisibleException v) {
      log.error("NotVisible - got one. Locator: " + locator.toString());
      return IsElementVisible(driver, locator);
    }
  }

  /**
   * This method works as a wrapper for findElement method of WebDriver.
   * So, in same cases, we may have the issue 'Stale Element Reference', i.e.,
   * the element is not ready in DOM. Hence, to prevent exception, we develop
   * a function that is the same of findElement but avoid this exception.
   *
   * @param driver
   * @param locator
   * @return
   */
  public static WebElement FindElementInvisble(WebDriver driver, By locator) {
    log.debug("Enter:FindElement");
    try {
      IsElementPresent(driver, locator);
      log.debug("Element is visble");
      List<WebElement> listElements = driver.findElements(locator);
      if (listElements.size() > 0) {
        WebElement element = listElements.get(0);
        if (element.isEnabled()) {
          log.debug("return element found it");
          return element;
        } else {
          log.warn("Trying again! Enabled:" + element.isEnabled() + " Locator: " + locator.toString());
          return FindElement(driver, locator);
        }
      } else {
        log.warn("Trying obtain! Locator: " + locator.toString());
        return null;
      }
    } catch (StaleElementReferenceException s) {
      log.error("Stale - got one. Locator: " + locator.toString());
      return FindElement(driver, locator);
    } catch (ElementNotVisibleException v) {
      log.error("NotVisible - got one. Locator: " + locator.toString());
      return IsElementVisible(driver, locator);
    } catch (TimeoutException te) {
      log.error("TimeoutException - got one. Locator: " + locator.toString());
      log.error(te.getMessage());
      log.debug("Trying again.");
      return driver.findElement(locator);
    }
  }

  /**
   *
   * @param driver
   * @param locator
   * @return
   */
  public static String GetText(WebDriver driver, By locator) {
    String text = "";
    try {
      text = FindElement(driver, locator).getText();
    } catch (StaleElementReferenceException e) {
      log.debug("Got stale");
      text = FindElement(driver, locator).getText();
    }
    return text;
  }

  /**
   *
   * @param driver
   * @param locator
   * @return
   */
  public static String GetTextElementInvisible(WebDriver driver, By locator) {
    String text = "";
    try {
      WebElement element = FindElementInvisble(driver, locator);
      text = ((JavascriptExecutor) driver).executeScript("return arguments[0].textContent", element).toString();
    } catch (StaleElementReferenceException e) {
      log.debug("Got stale");
      text = FindElementInvisble(driver, locator).getText();
    } catch (Exception e) {
      log.debug("Exception: " + e.getMessage());
    }

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
  public static void WaitForTextPresent(WebDriver driver, By locator, String textToWait) {
    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(200, TimeUnit.MILLISECONDS);

    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, textToWait));
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  /**
   * This method wait for text to be present and if present return the text.
   *
   * @param driver
   * @param locator
   * @param textToWait
   * @return
   */
  public static String WaitForText(WebDriver driver, By locator, String textToWait) {
    String strText = "";

    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(200, TimeUnit.MILLISECONDS);

    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    boolean found = wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, textToWait));
    if (found) {
      strText = GetText(driver, locator);
    }
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    return strText;
  }

  /**
   *
   * @param driver
   * @param locator
   */
  public static void WaitForInvisible(WebDriver driver, By locator) {
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

    for (int i = 0; i < 1000; i++) {
      try {
        driver.findElement(locator);
      } catch (NoSuchElementException s) {
        log.error("NuSuchElement - got it. Locator: " + locator.toString());
        break;
      } catch (StaleElementReferenceException s) {
        log.error("Stale - got it. Locator: " + locator.toString());
      }
    }

    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  /**
   * The function will search for the element and then click on it.
   *
   * @param driver
   * @param locator
   */
  public static void Click(WebDriver driver, By locator) {
    WebElement element = FindElement(driver, locator);
    if (element != null) {
      try {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
      } catch (WebDriverException wde) {
        if (wde.getMessage().contains("arguments[0].click is not a function")) {
          element.click();
        }
      }
    } else {
      log.entry("Element is null " + locator.toString());
    }
  }
}
