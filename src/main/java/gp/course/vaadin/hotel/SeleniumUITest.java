package gp.course.vaadin.hotel;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import gp.course.vaadin.hotel.servises.CategoryDAOImpl;
import gp.course.vaadin.hotel.servises.HotelDAOImpl;

public class SeleniumUITest extends AbstractUITest {
	
	private HotelDAOImpl hotelDAOImpl = HotelDAOImpl.getInstance();
	private CategoryDAOImpl categoryDAOImpl = CategoryDAOImpl.getInstance();

	@Test
	public void addCategories() throws InterruptedException {
		String[] categoryData = new String[] {
				"Appartments",
				"Family hotel",
				"Middle-range hotels"};
		
		driver.get(BASE_URL);
		Thread.sleep(3000);
		
		WebElement categoryMenu = driver.findElement(By.xpath(
				"//span[@class='v-menubar-menuitem'][./span/@class='v-menubar-menuitem-caption'][contains(./span/text(), 'Category')]"));
		categoryMenu.click();
		Thread.sleep(1000);
		
		long quantityBefore = categoryDAOImpl.count();
		
		for(String category : categoryData) {
			WebElement addButton = driver.findElement(By.xpath(
					"//div[@class='v-slot'][./div/@class='v-button v-widget'][./div/span/span/text()='Add category']"));
			addButton.click();
			Thread.sleep(1000);
			
			driver.findElement(By.xpath(
					"//input[@class='v-textfield v-widget v-required v-textfield-required v-has-width']")).sendKeys(category);
			Thread.sleep(1000);
			
			driver.findElement(By.xpath(
					"//div[@class='v-slot'][./div/@class='v-button v-widget v-has-width'][./div/span/span/text()='Save']")).click();
			Thread.sleep(1000);
		}
		
		long quantityAfter = categoryDAOImpl.count();
		
		Assert.assertEquals(3, quantityAfter - quantityBefore);
	}
	
	@Test
	public void addHotels() throws InterruptedException {
		final String[] hotelData = new String[] {
				"Kong Kham Pheng Guesthouse;1;https://www.booking.com/hotel/la/kong-kham-pheng-guesthouse.en-gb.html;Mixay Village, Paksan district, Bolikhamxay province, 01000 Muang Pakxan, Laos;GuestHouse;15.02.95",
				"Laos Haven Hotel & Spa;3;https://www.booking.com/hotel/la/laos-haven.en-gb.html;047 Ban Viengkeo, Vang Vieng , 01000 Vang Vieng, Laos;Hotel;23.12.04",
				"Lerdkeo Sunset Guesthouse;1;https://www.booking.com/hotel/la/lerdkeo-sunset-guesthouse.en-gb.html;Muang Ngoi Neua,Ban Ngoy-Nua, 01000 Muang Ngoy, Laos;GuestHouse;14.08.14",
				"Luangprabang River Lodge Boutique 1;3;https://www.booking.com/hotel/la/luangprabang-river-lodge.en-gb.html;Mekong River Road, 06000 Luang Prabang, Laos;Hotel;29.01.07",
				"Manichan Guesthouse;2;https://www.booking.com/hotel/la/manichan-guesthouse.en-gb.html;Ban Pakham Unit 4/143, 60000 Luang Prabang, Laos;Hostel;02.11.88"};
		
		driver.get(BASE_URL);
		Thread.sleep(3000);
		
		long quantityBefore = hotelDAOImpl.count();
		
		for (String hotel : hotelData) {
			String[] split = hotel.split(";");
			
			driver.findElement(By.xpath(
				"//div[@class='v-slot'][./div/@class='v-button v-widget'][./div/span/span/text()='Add hotel']"
			)).click();
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
				"//div[@class='v-slot'][./div/@class='v-button v-widget v-has-width'][./div/span/span/text()='Save']"
			)).click();
			Thread.sleep(1000);
		}
		
		long quantityAfter = hotelDAOImpl.count();
		
		Assert.assertEquals(5, quantityAfter - quantityBefore);
	}
}
