package report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class ExtentTestNGListener implements ITestListener {
	protected static ExtentReports extent;
	private ExtentSparkReporter sparkReporter;

	 @BeforeSuite
	    public void setUp() {
	        sparkReporter = new ExtentSparkReporter("/test-output/spark-report.html");
	        extent = new ExtentReports();
	        extent.attachReporter(sparkReporter);
	    }
    
    @AfterSuite
    public void tearDown() {
        extent.flush();
    }
}
