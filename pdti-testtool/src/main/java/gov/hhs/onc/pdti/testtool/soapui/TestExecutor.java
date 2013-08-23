package gov.hhs.onc.pdti.testtool.soapui;

import java.util.Map;

import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepResult;
import com.eviware.soapui.model.testsuite.TestAssertion;
import com.eviware.soapui.model.testsuite.TestStepResult.TestStepStatus;
import com.eviware.soapui.tools.SoapUITestCaseRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestExecutor {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TestExecutor.class);
    private static final String SOAPUI_PROJECT_FILE = "soapui-project_hpdplus.xml";
    private static final String URL_PROPERTY = "project.test.server.wsdl.url=";
    private static final String BASE_DN_PROPERTY = "project.test.server.dsml.dn.base=";
    
    public String executeTests(final String wsdlUrlToTest, final String baseDn) {
        LOGGER.info("wsdlUrlToTest = " + wsdlUrlToTest);
        SoapUITestCaseRunner runner = new SoapUITestCaseRunner();
        runner.setProjectFile(TestExecutor.class.getClassLoader().getResource(SOAPUI_PROJECT_FILE).toString());
        runner.setProjectProperties(new String[] { URL_PROPERTY + wsdlUrlToTest, BASE_DN_PROPERTY + baseDn});
        try {
            runner.run();
        } catch(Exception exception) {
            LOGGER.error("Unable to run SoapUI test case(s).", exception);
        }
        StringBuffer stringBuffer = new StringBuffer();
        Map<TestAssertion, WsdlTestStepResult> assertionResultsMap = runner.getAssertionResults();
        for(TestAssertion testAssertion : assertionResultsMap.keySet()) {
            String assertionName = testAssertion.getName();
            WsdlTestStepResult wsdlTestStepResult = assertionResultsMap.get(testAssertion);
            TestStepStatus testStepStatus = wsdlTestStepResult.getStatus();
            stringBuffer.append("assertion name = " + assertionName + "=, testStepStatus = " + testStepStatus + "\n");
        }
        return stringBuffer.toString();
    }

}
