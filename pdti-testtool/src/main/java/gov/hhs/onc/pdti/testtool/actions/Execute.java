package gov.hhs.onc.pdti.testtool.actions;

import gov.hhs.onc.pdti.testtool.soapui.TestExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Execute extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(Execute.class);
    
    private String wsdlUrl;
    private String baseDn;
    private String testResult;
    
    public String execute() {
        LOGGER.debug("execute() called...");
        TestExecutor testExecutor = new TestExecutor();
        testResult = testExecutor.executeTests(wsdlUrl, baseDn);
        return SUCCESS;
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
    
    public String getTestResult() {
        return testResult;
    }
    
    public void setTestResult(final String testResult) {
        this.testResult = testResult;
    }

}
