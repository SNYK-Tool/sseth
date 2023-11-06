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


public class CheckPoliciesOfDAR {
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
			
			//Policy: Validate DAR Version Uniqueness
			
			String DARVersion="";
			if(ProjectKovairClass.CreateDAR(ProjectName, driver))
			{
				System.out.println("DAR created successfully.");
				KovairGenericClass.idSearch("last1", driver);
				KovairGenericClass.threadSleep("Medium");
				DARVersion= KovairGenericClass.getFieldValueFromViewInEntityListPage("Standard View", "Artifact Version", driver);
			
				boolean addModePolicy1= ProjectKovairClass.DARPolicyValidateDARVersionUniqueness("Add",DARVersion, ProjectName, driver);
				
				if(addModePolicy1)
				{
					PolicyStatus.add("DAR:Validate DAR Version Uniqueness- Add Mode:Passed");
					System.out.println("DARPolicyValidateDARVersionUniqueness Add mode passed");
					flag++;
				}
				else
				{
					PolicyStatus.add("DAR:Validate DAR Version Uniqueness- Add Mode:Failed");
					System.out.println("DARPolicyValidateDARVersionUniqueness Add mode failed");
					GlobalVariables.ExecutionStatus="Failed";
					GlobalVariables.ErrDesc="DAR:Validate DAR Version Uniqueness- Add Mode:Failed";
				}
			}
			if(ProjectKovairClass.CreateDAR(ProjectName, driver))
			{
				System.out.println("DAR created successfully.");

				boolean editModePolicy1= ProjectKovairClass.DARPolicyValidateDARVersionUniqueness("Edit",DARVersion, ProjectName, driver);

				if(editModePolicy1)
				{
					PolicyStatus.add("DAR:Validate DAR version Uniqueness- Edit Mode:Passed");
					System.out.println("DARPolicyValidateDARVersionUniqueness Edit mode passed");
					flag++;
				}
				else
				{
					PolicyStatus.add("DAR:Validate DAR Version Uniqueness- Edit Mode:Failed");
					System.out.println("DARPolicyValidateDARVersionUniqueness Edit mode failed");
					GlobalVariables.ExecutionStatus="Failed";
					GlobalVariables.ErrDesc="DAR:Validate DAR Version Uniqueness- Edit Mode:Failed";
				}
			}
			
			//Policy: Update project Phase Version		
			KovairGenericClass.selectWorkspace(GlobalVariables.WorkspaceName, driver);
			boolean Policy2= ProjectKovairClass.DARPolicyUpdatePhaseVersion(ProjectName, driver);
			if(Policy2)
			{
				PolicyStatus.add("DAR:Update project Phase Version:Passed");
				System.out.println("DARPolicyUpdatePhaseVersion passed");
				flag++;
			}
			else
			{
				PolicyStatus.add("DAR:Update project Phase Version:Failed");
				System.out.println("DARPolicyUpdatePhaseVersion failed");
				GlobalVariables.ExecutionStatus="Failed";
				GlobalVariables.ErrDesc="DAR:Update project Phase Version:Failed";
			}
			
			//Policy: Update Title - When Record Modified
			KovairGenericClass.selectWorkspace(GlobalVariables.WorkspaceName, driver);
			if(ProjectKovairClass.CreateDAR(ProjectName, driver))
			{
				System.out.println("DAR created successfully.");
			}
			String ArtifactVersionUpdated = KovairGenericClass.createRandomArtifactVersion();
			boolean Policy3= ProjectKovairClass.DARPolicyUpdateTitleWhenRecordModified(ArtifactVersionUpdated, ProjectName, driver);
			
			if(Policy3)
			{
				PolicyStatus.add("DAR:Update Title - When Record Modified:Passed");
				System.out.println("DARPolicyUpdateTitleWhenRecordModified passed");
				flag++;
			}
			else
			{
				PolicyStatus.add("DAR:Update Title - When Record Modified:Failed");
				System.out.println("DARPolicyUpdateTitleWhenRecordModified failed");
				GlobalVariables.ExecutionStatus="Failed";
				GlobalVariables.ErrDesc="DAR:Update Title - When Record Modified:Failed";
			}
			
			
			//Policy: Update Title - When Linked to Project Phase		
			boolean Policy4= ProjectKovairClass.DARPolicyUpdateTitleWhenLinkedtoProjectPhase(ProjectName, driver);
			if(Policy4)
			{
				PolicyStatus.add("DAR:Update Title - When Linked to Project Phase:Passed");
				System.out.println("DARPolicyUpdateTitleWhenLinkedtoProjectPhase passed");
				flag++;
			}
			else
			{
				PolicyStatus.add("DAR:Update Title - When Linked to Project Phase:Failed");
				System.out.println("DARPolicyUpdateTitleWhenLinkedtoProjectPhase failed");
				GlobalVariables.ExecutionStatus="Failed";
				GlobalVariables.ErrDesc="DAR:Update Title - When Linked to Project Phase:Failed";
			}
			
			//Policy: Freeze Approved/Expired Records
			
			boolean FreezeApprovedPolicy5= ProjectKovairClass.DARPolicyFreezeApprovedExpiredRecords(ProjectName,"Approved", driver);
			if(FreezeApprovedPolicy5)
			{
				PolicyStatus.add("DAR:Freeze Approved/Expired Records-Aproved:Passed");
				System.out.println("DARPolicyFreezeApprovedExpiredRecords-Approved passed");
				flag++;
			}
			else
			{
				PolicyStatus.add("DAR:Freeze Approved/Expired Records-Approved:Failed");
				System.out.println("DARPolicyFreezeApprovedExpiredRecords-Approved failed");
				GlobalVariables.ExecutionStatus="Failed";
				GlobalVariables.ErrDesc="DAR:Freeze Approved/Expired Records-Approved:Failed";
			}
			
			boolean FreezeExpiredPolicy5= ProjectKovairClass.DARPolicyFreezeApprovedExpiredRecords(ProjectName,"Expired", driver);			
			if(FreezeExpiredPolicy5)
			{
				PolicyStatus.add("DAR:Freeze Approved/Expired Records-Expired:Passed");
				System.out.println("DARPolicyFreezeApprovedExpiredRecords-Expired passed");
				flag++;
			}
			else
			{
				PolicyStatus.add("DAR:Freeze Approved/Expired Records-Expired:Failed");
				System.out.println("DARPolicyFreezeApprovedExpiredRecords-Expired failed");
				GlobalVariables.ExecutionStatus="Failed";
				GlobalVariables.ErrDesc="DAR:Freeze Approved/Expired Records-Expired:Failed";
			}
			
			//Policy: Populate CSE, SSE(Linked to Project Phase)
			
			boolean Policy6= ProjectKovairClass.DARPolicyPopulateCSE_SSELinkedToProjectPhase(ProjectName, driver);
			if(Policy6)
			{
				PolicyStatus.add("DAR:Populate CSE SSE (linked to Project Phase):Passed");
				System.out.println("DARPolicyPopulateCSE_SSELinkedToProjectPhase passed");
				flag++;
			}
			else
			{
				PolicyStatus.add("DAR:Populate CSE SSE (linked to Project Phase):Failed");
				System.out.println("DARPolicyPopulateCSE_SSELinkedToProjectPhase failed");
				GlobalVariables.ExecutionStatus="Failed";
				GlobalVariables.ErrDesc="DAR:Populate CSE SSE (linked to Project Phase):Failed";
			}
			
			
			//Policy: Update status and unfreeze
			
			if(ProjectKovairClass.CreateDAR(ProjectName, driver))
			{
				boolean unfreezePolicy7= ProjectKovairClass.DARPolicyUpdateStatusAndUnfreeze(ProjectName,"UnfreezeItem", driver);
				if(unfreezePolicy7)
				{
					PolicyStatus.add("DAR:Update status and unfreeze-unfreeze Item-Self:Passed");
					System.out.println("Unfreeze Item self passed");
					flag++;
				}
				else
				{
					PolicyStatus.add("DAR:Update status and unfreeze-unfreeze Item-Self:Failed");
					System.out.println("Unfreeze Item self failed");
					GlobalVariables.ExecutionStatus="Failed";
					GlobalVariables.ErrDesc="DAR:Update status and unfreeze-unfreeze Item-Self:Failed";
				}
				
				boolean updateStatusPolicy7= ProjectKovairClass.DARPolicyUpdateStatusAndUnfreeze(ProjectName,"UpdateStatus", driver);
				if(updateStatusPolicy7)
				{
					PolicyStatus.add("DAR:Update status and unfreeze-Update Status-Self:Passed");
					System.out.println("Update Status-Self passed");
					flag++;
				}
				else
				{
					PolicyStatus.add("DAR:Update status and unfreeze-Update Status-Self:Failed");
					System.out.println("Update Status-Self failed");
					GlobalVariables.ExecutionStatus="Failed";
					GlobalVariables.ErrDesc="DAR:Update status and unfreeze-Update Status-Self:Failed";
				}
				
				boolean linkProjectPolicy7= ProjectKovairClass.DARPolicyUpdateStatusAndUnfreeze(ProjectName,"LinkProject", driver);
				if(linkProjectPolicy7)
				{
					PolicyStatus.add("DAR:Update status and unfreeze-Link Project:Passed");
					System.out.println("Link Project passed");
					flag++;
				}
				else
				{
					PolicyStatus.add("DAR:Update status and unfreeze-Link Project:Failed");
					System.out.println("Link Project failed");
					GlobalVariables.ExecutionStatus="Failed";
					GlobalVariables.ErrDesc="DAR:Update status and unfreeze-Link Project:Failed";
				}
			}
			else
			{
				System.out.println("DAR Creation failed.");
				GlobalVariables.ExecutionStatus="Failed";
				GlobalVariables.ErrDesc="DAR:Update status and unfreeze:Failed";
			}
			
			//Policy: Push approved records to CI
			if(ProjectKovairClass.CreateDAR(ProjectName, driver))
			{
				boolean Policy8= ProjectKovairClass.DARPolicyPushApprovedRecordsToCI(ProjectName, driver);
				if(Policy8)
				{
					PolicyStatus.add("DAR:Push Approved Records To CI:Passed");
					System.out.println("DARPolicyPushApprovedRecordsToCI passed");
					flag++;
				}
				else
				{
					PolicyStatus.add("DAR:Push Approved Records To CI:Failed");
					System.out.println("DARPolicyPushApprovedRecordsToCI failed");
					GlobalVariables.ExecutionStatus="Failed";
					GlobalVariables.ErrDesc="DAR:Push Approved Records To CI:Failed";
				}
			}
			else
			{
				System.out.println("DAR Creation failed.");
				GlobalVariables.ExecutionStatus="Failed";
				GlobalVariables.ErrDesc="DAR:Push Approved Records To CI:Failed";
			}
			
			
			//Policy: Update Title When Project Phase Link is Replaced
			KovairGenericClass.selectWorkspace(GlobalVariables.WorkspaceName, driver);
			boolean Policy9= ProjectKovairClass.DARPolicyUpdateTitleWhenProjectPhaseLinkisReplaced(ProjectName, driver);
			if(Policy9)
			{
				PolicyStatus.add("DAR:Update Title When Project Phase Link is Replaced:Passed");
				System.out.println("DARPolicyUpdateTitleWhenProjectPhaseLinkisReplaced passed");
				flag++;
			}
			else
			{
				PolicyStatus.add("DAR:Update Title When Project Phase Link is Replaced:Failed");
				System.out.println("DARPolicyUpdateTitleWhenProjectPhaseLinkisReplaced failed");
				GlobalVariables.ExecutionStatus="Failed";
				GlobalVariables.ErrDesc="DAR:Update Title When Project Phase Link is Replaced:Failed";
			}
			
			//Policy: Update CI Item status to expired
			KovairGenericClass.selectWorkspace(GlobalVariables.WorkspaceName, driver);
			boolean Policy8= ProjectKovairClass.DARPolicyUpdateCIStatus(ProjectName, driver);
			if(Policy8)
			{
				PolicyStatus.add("DAR:Update CI Item status :Passed");
				System.out.println("DARPolicyUpdateCIStatus passed");
				flag++;
			}
			else
			{
				PolicyStatus.add("DAR:Update CI Item status:Failed");
				System.out.println("DARPolicyUpdateCIStatus failed");
				GlobalVariables.ExecutionStatus="Failed";
				GlobalVariables.ErrDesc="DAR:Update CI Item status:Failed";
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
			if(flag==14)
			{
			    System.out.println("All Policies Of DAR Passed");
			    GlobalVariables.ExecutionStatus="Passed";
			}
			else
			{
			    System.out.println("CheckPoliciesOfDAR Failed");
			    GlobalVariables.ExecutionStatus="Failed";
			}	
			driver.quit();
		}
		catch(Exception ex)
		{
			System.out.println("Exception Message: "+ex.getMessage());
			org.testng.Assert.fail("Exception Message: "+ex.getMessage());
		}	
	}
	@AfterTest
	public void afterTest() {
		System.out.println("***********After Test Start**********");
		try{
			String TCName = "Check Policies Of DAR";
			String TCID = "CheckPoliciesOfDAR";
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
