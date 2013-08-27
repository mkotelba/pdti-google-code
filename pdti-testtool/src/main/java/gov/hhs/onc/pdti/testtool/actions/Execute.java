package gov.hhs.onc.pdti.testtool.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.support.SoapUIException;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@InterceptorRefs({
    @InterceptorRef("defaultStack"),
    @InterceptorRef("execAndWait")
})
public class Execute extends ActionSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(Execute.class);
    private static final String WSDL_URL_ATTRIBUTE_PARAM_NAME = "wsdlUrl";
    private static final String BASE_DN_STRING_PARAM_NAME = "baseDn";
    private static final String REQUIRED_FIELD_MESSAGE = "This field is required.";
    private static final String SOAPUI_PROJECT_FILE = "soapui-project_hpdplus.xml";
    private static final String URL_PROPERTY = "project.test.server.wsdl.url";
    private static final String BASE_DN_PROPERTY = "project.test.server.dsml.dn.base";
    private static String STATUS_MESSAGE_FRAGMENT = "tests have been executed.";

    private String wsdlUrl;
    private String baseDn;
    private List<String[]> testResults = new ArrayList<String[]>();
    private String status = "no " + STATUS_MESSAGE_FRAGMENT;
    
    @Validations(requiredFields = {
            @RequiredFieldValidator(type = ValidatorType.SIMPLE, fieldName = WSDL_URL_ATTRIBUTE_PARAM_NAME, message = REQUIRED_FIELD_MESSAGE),
            @RequiredFieldValidator(type = ValidatorType.SIMPLE, fieldName = BASE_DN_STRING_PARAM_NAME, message = REQUIRED_FIELD_MESSAGE) })
    public String execute() {
        LOGGER.debug("execute() called...");
        WsdlProject wsdlProject = null;
        try {
            wsdlProject = new WsdlProject(Execute.class.getClassLoader().getResource(SOAPUI_PROJECT_FILE).toString());
        } catch (XmlException | IOException | SoapUIException exception) {
            LOGGER.error(exception.getMessage());
        }
        wsdlProject.setPropertyValue(URL_PROPERTY, getWsdlUrl());
        wsdlProject.setPropertyValue(BASE_DN_PROPERTY, getBaseDn());
        List<TestSuite> testSuites = wsdlProject.getTestSuiteList();
        int numberOfTestCases = countTestCases(testSuites);
        int testCaseCounter = 0;
        for(TestSuite testSuite : testSuites) {
            for(TestCase testCase : testSuite.getTestCaseList()) {
                testCaseCounter++;
                TestCaseRunner testCaseRunner = testCase.run(null, false);
                testResults.add(new String[] {
                            testSuite.getName(), testCase.getName(), testCaseRunner.getStatus().toString()});
                status = testCaseCounter + " of " + numberOfTestCases + " " + STATUS_MESSAGE_FRAGMENT;
            }
        }
        return SUCCESS;
    }
    
    private int countTestCases(final List<TestSuite> testSuites) {
        int numberOfTestCases = 0;
        for(TestSuite testSuite : testSuites) {
            for(TestCase testCase : testSuite.getTestCaseList()) {
                numberOfTestCases++;
            }
        }
        return numberOfTestCases;
    }

    public String getWsdlUrl() {
        return wsdlUrl;
    }
    
    public void setWsdlUrl(final String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }
    
    public String getBaseDn() {
        return baseDn;
    }
    
    public void setBaseDn(final String baseDn) {
        this.baseDn = baseDn;
    }
    
    public List<String[]> getTestResults() {
        return new ArrayList<String[]>(testResults);
    }
    
    public String getStatus() {
        return this.status;
    }

}
