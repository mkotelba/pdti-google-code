package gov.hhs.onc.pdti.ws.impl;

import gov.hhs.onc.pdti.service.DirectoryService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

public abstract class AbstractProviderInformationDirectory<T, U> {
    @Resource
    private WebServiceContext context;

    protected DirectoryService<T, U> dirService;

    protected abstract void setDirectoryService(DirectoryService<T, U> dirService);
}
