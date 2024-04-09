package web
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import java.util.Random

class swag {
	/**
	 * Click element
	 * @param to Katalon test object
	 */
	private clickElement(TestObject to) {
		try {
			WebElement element = WebUiBuiltInKeywords.findWebElement(to,10);
			KeywordUtil.logInfo("Clicking element")
			element.click()
			KeywordUtil.markPassed("Element has been clicked")
		} catch (WebElementNotFoundException e) {
			KeywordUtil.markFailed("Element not found")
		} catch (Exception e) {
			KeywordUtil.markFailed("Fail to click on element")
		}
	}

	/**
	 * get number of child
	 */
	@Keyword
	public static int getHtmlElementChild(TestObject element) {
		WebElement parent = WebUiBuiltInKeywords.findWebElement(element, 10)
		List<WebElement> childs = parent.findElements(By.xpath("./*"))
		return childs != null ? childs.size() : 0
	}
	
	/**
	 * generating new TestObject object
	 */
	private TestObject GenerateNewObject(TestObject base, String xpath, String name) {
		String baseXpath = base.getProperties().get(0).getValue()
		String updatedXpath = baseXpath + xpath;
		TestObject NewObject = new TestObject(name)
		NewObject.addProperty("xpath", ConditionType.EQUALS, updatedXpath)
//		KeywordUtil.logInfo("New TestObject value: ${NewObject.findPropertyValue("xpath")}")

		return NewObject
	}
	
	/**
	 * Handling to response button, description, name, and price 
	 * arguments => parent, child, type of child property (button,price,name,description)
	 */
	private TestObject getObject(TestObject base, int num, String type) {
		TestObject temp_obj = null
		if ("btn".equalsIgnoreCase(type)) {
			temp_obj = GenerateNewObject(base, "/div["+num+"]/div[2]/div[2]/button", "btn")
		}
		else if ("price".equalsIgnoreCase(type)) {
			temp_obj = GenerateNewObject(base, "/div["+num+"]/div[2]/div[2]/div[1]", "price")
		}
		else if ("name".equalsIgnoreCase(type)) {
			temp_obj = GenerateNewObject(base, "/div["+num+"]/div[2]/div[1]/div[1]", "name")
		}
		else if ("desc".equalsIgnoreCase(type)) {
			temp_obj = GenerateNewObject(base, "/div["+num+"]/div[2]/div[1]/div[1]", "desc")
		}
		return temp_obj
	}
	
	/**
	 * Add items to cart, also count the item
	 * arguments => table/list object 
	 */
	@Keyword
	public List add_item(TestObject object) {
		int child = getHtmlElementChild(object)
		//if child not detected, then fail
		if (child==0 || child==null) {
			KeywordUtil.markFailed("Element has no child")
		}
		else {
			// randomly generate how many item added to cart 	
			def random = new Random()
			int rand = random.nextInt(6) + 1
			KeywordUtil.logInfo("Start Generate Num. Items add: "+rand)
			List num = GenerateNum(rand, child)
			// get item attribute (name, price, description, button)
			KeywordUtil.logInfo("Start Getting Item Attributes. List: "+num)
			def valid_val = GetItemAttributes(object, num)
//			KeywordUtil.logInfo("Printing attrb." + valid_val[0][0]['btn'])
//			if(valid_val.size()>0) PrintAttr(valid_val)
			//click the add button based on sequence
			KeywordUtil.logInfo("Start adding item by clicking button")	
			for (int i=0; i<valid_val.size(); i++) {
				KeywordUtil.logInfo("Adding item number "+(i+1))
				clickElement(valid_val[i][0]["btn"])
			}
			KeywordUtil.logInfo("Finished adding item. Returning values.")
			return valid_val
		}
	}
	
	/**
	 * Add items to cart, also count the item
	 * arguments => object, number of items will be add to cart
	 */
	private List GenerateNum(int threshold, int child) {
		List num = []
		if (threshold > child) KeywordUtil.markFailed("Threshold is bigger than element child")
		else if (threshold == child) {
			for(int i=0;i<child;i++) {
				num<<i+1
			}
		}
		else {
			def random = new Random() 
			while(num.size()!=threshold) {
				int temp = random.nextInt(child) + 1
				if(!(temp in num)) num << temp
			} 
		}
		return num
	}
	
	/**
	 * Get attributes of item
	 * arguments => object, list of child position (item)
	 * =GenerateNum(getHtmlElementChild(object), getHtmlElementChild(object)) => for nums
	 */ 
	private List GetItemAttributes(TestObject object, List nums) {
		def valid_val = []
		def temp = []
		// inserting object to be validated in cart (check if it the same or not)
		for(i in nums) {
			temp = []
			temp << ["btn" : getObject(object, i, "btn")]
			temp << ["price" : getObject(object, i, "price")]
			temp << ["name" : getObject(object, i, "name")]
			temp << ["desc" : getObject(object, i, "desc")]
			valid_val << temp
		}
		return valid_val
	}
	/**
	 * Print list of attributes
	 * arguments => list of attributes
	 */
	private PrintAttr(List attr) {
		KeywordUtil.logInfo("Item Attributes: ")
		for (int i = 0; i<attr.size(); i++) {
			KeywordUtil.logInfo("Item-"+i+1+" name: "+attr[i]["name"])
			KeywordUtil.logInfo("Item-"+i+1+" desc: "+attr[i]["desc"])
			KeywordUtil.logInfo("Item-"+i+1+" price: "+attr[i]["price"])
		}
	}
}