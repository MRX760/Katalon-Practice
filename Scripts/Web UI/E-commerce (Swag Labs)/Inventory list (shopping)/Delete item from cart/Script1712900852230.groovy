import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

WebUI.openBrowser('https://www.saucedemo.com/')

WebUI.setText(findTestObject('Object Repository/Page_Swag Labs/Login_page/input_Swag Labs_user-name'), GlobalVariable.username)

WebUI.setText(findTestObject('Object Repository/Page_Swag Labs/Login_page/input_Swag Labs_password'), GlobalVariable.password)

WebUI.click(findTestObject('Object Repository/Page_Swag Labs/Login_page/input_Swag Labs_login-button'))

WebUI.comment('script to add_item, and store the validation value to map')

item_lists = CustomKeywords.'web.swag.add_item'(findTestObject('Page_Swag Labs/Inventory_list/item_list'))

WebUI.comment('Get the item identity - input is the object addresses (array->dict of obj)')

item_identifier = CustomKeywords.'web.swag.getItemIdentifier'(item_lists)

WebUI.click(findTestObject('Page_Swag Labs/Inventory_list/cart'))

WebUI.comment('Verify similarity of items identity. Input is dict of items identity')

CustomKeywords.'web.swag.VerifySimilarity'(findTestObject('Page_Swag Labs/Checkout_cart/cart_item_list'), item_identifier, 
    'add')

WebUI.comment('END OF ADD ITEM TEST CASE')

del_item_identifier = CustomKeywords.'web.swag.DeleteItem'(findTestObject('Page_Swag Labs/Checkout_cart/cart_item_list'))

item_identifier = CustomKeywords.'web.swag.delete_add_identifier'(item_identifier, del_item_identifier)

WebUI.comment('Verify similarity of deleted and remaining items identity on the cart. Input is dict of items identity')

CustomKeywords.'web.swag.VerifySimilarity'(findTestObject('Page_Swag Labs/Checkout_cart/cart_item_list'), item_identifier, 
    'add')

CustomKeywords.'web.swag.VerifySimilarity'(findTestObject('Page_Swag Labs/Checkout_cart/cart_item_list'), del_item_identifier, 
    'del')

