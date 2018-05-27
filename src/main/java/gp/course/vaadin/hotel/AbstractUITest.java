package gp.course.vaadin.hotel;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AbstractUITest {
	protected WebDriver driver;
	protected static final String BASE_URL = "http://localhost:8080";

	public AbstractUITest() {
		// Optional, if not specified, WebDriver will search your path for
		// chromedriver.
		System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
	}

	@Before
	public void initDriver() throws InterruptedException {
		driver = new ChromeDriver();
	}

	@After
	public void tearDown() throws InterruptedException {
		Thread.sleep(1000); // Let the user actually see something!
		driver.quit();
	}
}
