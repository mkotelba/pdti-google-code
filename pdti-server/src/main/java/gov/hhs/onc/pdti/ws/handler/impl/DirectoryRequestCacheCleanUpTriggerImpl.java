package gov.hhs.onc.pdti.ws.handler.impl;


import gov.hhs.onc.pdti.DirectoryType;
import gov.hhs.onc.pdti.DirectoryTypeId;
import gov.hhs.onc.pdti.ws.handler.DirectoryRequestCacheCleanUpTrigger;
import gov.hhs.onc.pdti.ws.handler.DirectoryRequestCacheDescriptor;
import java.util.Date;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TriggerContext;
import org.springframework.stereotype.Component;

@Component("dirReqCacheCleanUpTrigger")
@Scope("singleton")
public class DirectoryRequestCacheCleanUpTriggerImpl implements DirectoryRequestCacheCleanUpTrigger {
    @Autowired
    @DirectoryType(DirectoryTypeId.MAIN)
    private DirectoryRequestCacheDescriptor dirReqCacheDesc;

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        long dirReqCacheCleanUpInterval = this.dirReqCacheDesc.getCleanUpInterval();

        return (dirReqCacheCleanUpInterval > 0) ? new Date(ObjectUtils.defaultIfNull(triggerContext.lastScheduledExecutionTime(), new Date()).getTime()
                + dirReqCacheCleanUpInterval) : null;
    }
}
