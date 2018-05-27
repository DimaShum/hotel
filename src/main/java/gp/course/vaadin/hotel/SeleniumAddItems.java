package gp.course.vaadin.hotel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class SeleniumAddItems {
	
	protected  WebDriver driver;
	protected static final String BASE_URL = "http://localhost:8080";
	
	public SeleniumAddItems() {
		System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
	}

	public static void main(String[] args) throws InterruptedException {
		SeleniumAddItems selenium = new SeleniumAddItems();
		selenium.run();
	}
	
	public void run() throws InterruptedException {
		driver = new ChromeDriver();
		
		addCategories();
		addHotels();
		
		driver.quit();
	}
	
	public void addCategories() throws InterruptedException {
		String[] categoryData = new String[] {
				"Guest house",
				"Family hotel",
				"Middle-range hotels"};
		
		driver.get(BASE_URL);
		Thread.sleep(3000);
		
		WebElement categoryMenu = driver.findElement(By.xpath(
				"//span[@class='v-menubar-menuitem'][./span/@class='v-menubar-menuitem-caption'][contains(./span/text(), 'Category')]"));
		categoryMenu.click();
		Thread.sleep(1000);
		
		for(String category : categoryData) {
			WebElement addButton = driver.findElement(By.xpath(
					"//div[@class='v-slot'][./div/@class='v-button v-widget'][contains(./div/span/span/text(), 'Add category')]"));
			addButton.click();
			Thread.sleep(1000);
			
			driver.findElement(By.xpath(
					"//input[@class='v-textfield v-widget v-required v-textfield-required v-has-width']")).sendKeys(category);
			Thread.sleep(1000);
			
			driver.findElement(By.xpath(
					"//div[@class='v-slot'][./div/@class='v-button v-widget'][contains(./div/span/span/text(), 'Save')]")).click();
			Thread.sleep(1000);
		}
	}
	
	public void addHotels() throws InterruptedException {
		final String[] hotelData = new String[] {
				"Kong Kham Pheng Guesthouse;1;https://www.booking.com/hotel/la/kong-kham-pheng-guesthouse.en-gb.html;Mixay Village, Paksan district, Bolikhamxay province, 01000 Muang Pakxan, Laos;Guest house;15.02.95",
				"Laos Haven Hotel & Spa;3;https://www.booking.com/hotel/la/laos-haven.en-gb.html;047 Ban Viengkeo, Vang Vieng , 01000 Vang Vieng, Laos;Hotel;23.12.04",
				"Lerdkeo Sunset Guesthouse;1;https://www.booking.com/hotel/la/lerdkeo-sunset-guesthouse.en-gb.html;Muang Ngoi Neua,Ban Ngoy-Nua, 01000 Muang Ngoy, Laos;Guest house;14.08.14",
				"Luangprabang River Lodge Boutique 1;3;https://www.booking.com/hotel/la/luangprabang-river-lodge.en-gb.html;Mekong River Road, 06000 Luang Prabang, Laos;Family hotel;29.01.07",
				"Manichan Guesthouse;2;https://www.booking.com/hotel/la/manichan-guesthouse.en-gb.html;Ban Pakham Unit 4/143, 60000 Luang Prabang, Laos;Middle-range hotels;02.11.88"};
		
		driver.get(BASE_URL);
		Thread.sleep(3000);
		
		for (String hotel : hotelData) {
			String[] split = hotel.split(";");
			
			driver.findElement(By.xpath(
					"//div[@class='v-slot'][./div/@class='v-button v-widget'][contains(./div/span/span/text(), 'Add hotel')]")).click();
			Thread.sleep(1000);
			
			driver.findElement(By.xpath("//tbody/tr[1]/td[3]/input")).sendKeys(split[0]);
			driver.findElement(By.xpath("//tbody/tr[2]/td[3]/input")).sendKeys(split[3]);
			driver.findElement(By.xpath("//tbody/tr[3]/td[3]/input")).sendKeys(split[1]);
			driver.findElement(By.xpath("//tbody/tr[4]/td[3]/div/input")).sendKeys(split[5]);
			
			Select categories = new Select(driver.findElement(By.xpath("//tbody/tr[5]/td[3]/div/select")));
			categories.selectByVisibleText(split[4]);
			
			driver.findElement(By.xpath("//tbody/tr[7]/td[3]/input")).sendKeys(split[2]);
			Thread.sleep(1000);
			
			driver.findElement(By.xpath(
					"//div[@class='v-slot'][./div/@class='v-button v-widget'][contains(./div/span/span/text(), 'Save')]")).click();
			Thread.sleep(1000);
		}
	}
}
