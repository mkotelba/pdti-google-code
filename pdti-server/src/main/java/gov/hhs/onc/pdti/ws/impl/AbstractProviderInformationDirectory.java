package gov.hhs.onc.pdti.ws.impl;

import gov.hhs.onc.pdti.service.DirectoryService;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractProviderInformationDirectory {
    @Resource
    private WebServiceContext context;

    @Autowired
    protected ObjectFactory objectFactory;

    @Autowired
    protected DirectoryService dirService;
}
