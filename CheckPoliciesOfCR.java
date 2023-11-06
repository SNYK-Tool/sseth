package testCases;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import globals.GlobalVariables;
import modules.KovairGenericClass;
import modules.ProjectKovairClass;


public class CheckPoliciesOfCR {
	WebDriver driver;
	@SuppressWarnings("deprecation")
	@Test
	public void CheckPoliciesOfDAR1() {

		try
		{
			GlobalVariables.GlobalWaitTime = 120;
			System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\src\\test\\resources\\Chromedriver\\chromedriver.exe");
			ChromeOptions capability = new ChromeOptions();
			//capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capability.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
			capability.addArguments("--remote-allow-origins=*");
			WebDriver driver = new ChromeDriver(capability);

			String uid = "";
			String pwd = "";
			int flag=0;
			
			uid = KovairGenericClass.getLoginInfoProperty("ProjectLeader");
			pwd = KovairGenericClass.getLoginInfoProperty("GlobalPassword");
			String ProjectName=KovairGenericClass.getLoginInfoProperty("Workspace");
			System.out.println(uid);
			System.out.println(pwd);

			ArrayList<String> PolicyStatus = new ArrayList<String>();

			ProjectKovairClass.Initialize(driver);
			KovairGenericClass.login(uid, pwd, driver);
			
			String PL= KovairGenericClass.getLoginInfoProperty("ProjectLeader");
			String PLusername = KovairGenericClass.getUserFullName(PL);
			String PLrank=KovairGenericClass.getUserRank(PL);
			String PLuserfullname=PLrank+" "+PLusername;
			System.out.println(PLuserfullname);

			KovairGenericClass.selectWorkspace(GlobalVariables.WorkspaceName, driver);
			try
			{
				String adminControlXpath = KovairGenericClass.getObjectFrmRepository("EndUser", "enduser.changeadminstrative.control");

				if(KovairGenericClass.waitForElementVisible(driver, adminControlXpath, 30)) 
				{
					WebElement adminControl = driver.findElement(By.xpath(KovairGenericClass.getObjectFrmRepository("EndUser", "enduser.changeadminstrative.control")));
					adminControl.click();
				}
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			
			//Policy: Link Project.
			boolean Policy1= ProjectKovairClass.CRPolicyLinkProject(ProjectName, uid, driver);
			if(Policy1)
			{
				PolicyStatus.add("CR:Link Project :Passed");
				System.out.println("CRPolicyLinkProject passed");
				flag++;
			}
			else
			{
				PolicyStatus.add("CR:Link Project:Failed");
				System.out.println("CRPolicyLinkProject failed");
				GlobalVariables.ExecutionStatus="Failed";
				GlobalVariables.ErrDesc="CRPolicyLinkProject failed";
			}
			
			
			//Policy: Update PL and Phase Version.
			KovairGenericClass.selectWorkspace(GlobalVariables.WorkspaceName, driver);
			boolean Policy2= ProjectKovairClass.CRPolicyUpdatePLandPhaseVersion(ProjectName, uid, driver);
			if(Policy2)
			{
				PolicyStatus.add("CR:Update PL and Phase Version:Passed");
				System.out.println("CRPolicyUpdatePLandPhaseVersion passed");
				flag++;
			}
			else
			{
				PolicyStatus.add("CR:Update PL and Phase Version:Failed");
				System.out.println("CRPolicyUpdatePLandPhaseVersion failed");
				GlobalVariables.ExecutionStatus="Failed";
				GlobalVariables.ErrDesc="CRPolicyUpdatePLandPhaseVersion failed";
			}
			
			//Policy: Update Expected Date of Completion
			KovairGenericClass.selectWorkspace(GlobalVariables.WorkspaceName, driver);
			boolean Policy3= ProjectKovairClass.CRPolicyUpdatePLandPhaseVersion(ProjectName, uid, driver);
			if(Policy3)
			{
				PolicyStatus.add("CR:Update Expected Date of Completion:Passed");
				System.out.println("CRPolicyUpdatePLandPhaseVersion passed");
				flag++;
			}
			else
			{
				PolicyStatus.add("CR:Update Expected Date of Completion:Failed");
				System.out.println("CRPolicyUpdateExpectedDateOfCompletion failed");
				GlobalVariables.ExecutionStatus="Failed";
				GlobalVariables.ErrDesc="CRPolicyUpdateExpectedDateOfCompletion failed";
			}
			
			GlobalVariables.ResultDescription="";
			System.out.println("==========================:Result:==================================");
			for(int j=0;j<PolicyStatus.size();j++)
			{
				System.out.println("Result:"+PolicyStatus.get(j));
				GlobalVariables.ResultDescription+=PolicyStatus.get(j)+",";
			}
			System.out.println("==========================:Result:==================================");	

			System.out.println("Flag :"+flag);
			if(flag==3)
			{
			    System.out.println("All Policies Of CR Passed");
			    GlobalVariables.ExecutionStatus="Passed";
			}
			else
			{
			    System.out.println("CheckPoliciesOfCR Failed");
			    GlobalVariables.ExecutionStatus="Failed";
			}	
			driver.quit();
		}
		catch(Exception ex)
		{
			System.out.println("Exception Message: "+ex.getMessage());
			org.testng.Assert.fail("Exception Message: "+ex.getMessage());
			GlobalVariables.ExecutionStatus="Failed";
		}	
	}
	
	@AfterTest
	public void afterTest() {
		System.out.println("***********After Test Start**********");
		try{
			String TCName = "Check Policies Of CR";
			String TCID = "CheckPoliciesOfCR";
			GlobalVariables.SrlNo++;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("MMddHHmmss");
			Date date = new Date();
			int RunID = Integer.parseInt(dateFormatter.format(date));
			System.out.println("Srl No: " + GlobalVariables.SrlNo);
			System.out.println("Run ID: " + RunID);
			System.out.println("Test Case ID: " + TCID);
			System.out.println("Test Case Name: " + TCName);
			System.out.println("Execution Status: " + GlobalVariables.ExecutionStatus);
			System.out.println("Error Description: " + GlobalVariables.ErrDesc);
			System.out.println("ResultDescription: "+GlobalVariables.ResultDescription);
			KovairGenericClass.insertTCResultInDB(RunID, GlobalVariables.SrlNo, TCID, TCName,GlobalVariables.ExecutionStatus, GlobalVariables.ErrDesc,GlobalVariables.ResultDescription);
			System.out.println("Test result inserted into the database successfully.");

		} catch (Exception e) {
			System.out.println("Error occurred while inserting the test result into the database. " + e.getMessage());

		}
		System.out.println("***********After Test End**********");
	}
}
