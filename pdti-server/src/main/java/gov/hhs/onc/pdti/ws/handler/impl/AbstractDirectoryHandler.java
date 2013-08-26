package gov.hhs.onc.pdti.ws.handler.impl;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import gov.hhs.onc.pdti.DirectoryType;
import gov.hhs.onc.pdti.DirectoryTypeId;
import gov.hhs.onc.pdti.jaxb.DirectoryJaxb2Marshaller;
import gov.hhs.onc.pdti.ws.handler.DirectoryHandler;
import gov.hhs.onc.pdti.ws.handler.DirectoryHandlerException;
import gov.hhs.onc.pdti.ws.handler.DirectoryRequestCacheDescriptor;
import gov.hhs.onc.pdti.ws.handler.DuplicateRequestIdException;
import java.util.Date;
import java.util.concurrent.Semaphore;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

public abstract class AbstractDirectoryHandler<T, U> implements DirectoryHandler<T, U>, RemovalListener<String, Date> {
    protected final static Semaphore DIR_REQ_CACHE_LOCK = new Semaphore(1, true);

    protected static Cache<String, Date> dirReqCache;

    @Autowired
    protected DirectoryJaxb2Marshaller dirJaxb2Marshaller;

    @Autowired
    @DirectoryType(DirectoryTypeId.MAIN)
    protected DirectoryRequestCacheDescriptor dirReqCacheDesc;

    protected Class<T> reqClass;
    protected Class<U> respClass;

    private final static Logger LOGGER = Logger.getLogger(AbstractDirectoryHandler.class);

    protected AbstractDirectoryHandler(Class<T> reqClass, Class<U> respClass) {
        this.reqClass = reqClass;
        this.respClass = respClass;
    }

    public synchronized void registerRequest(String reqId) throws DirectoryHandlerException {
        if (StringUtils.isBlank(reqId)) {
            return;
        }

        this.checkDirectoryRequestCache();

        Date reqDate;

        if ((reqDate = dirReqCache.getIfPresent(reqId)) != null) {
            throw new DuplicateRequestIdException("Duplicate directory request ID (" + reqId + ") detected - already registered at: " + reqDate);
        }

        reqDate = new Date();

        dirReqCache.put(reqId, reqDate);

        LOGGER.trace("Registered directory request (requestId=" + reqId + ", requestDate=" + reqDate + ").");
    }

    public synchronized void releaseRequest(String reqId) {
        if (StringUtils.isBlank(reqId)) {
            return;
        }

        this.checkDirectoryRequestCache();

        dirReqCache.invalidate(reqId);
    }

    public synchronized void cleanUpRequests() {
        this.checkDirectoryRequestCache();

        CacheStats dirReqCacheStats = dirReqCache.stats();

        dirReqCache.cleanUp();

        if (dirReqCacheStats.evictionCount() > 0) {
            LOGGER.trace("Cleaned up directory request cache: " + dirReqCacheStats);
        }

        // Resetting directory request cache statistics
        dirReqCacheStats.minus(dirReqCacheStats);
    }

    public synchronized void onRemoval(RemovalNotification<String, Date> notification) {
        LOGGER.trace("Released directory request (requestId=" + notification.getKey() + ", requestDate=" + notification.getValue() + ") from registry: cause="
                + notification.getCause());
    }

    @Override
    public boolean handleMessage(LogicalMessageContext logicalMsgContext) {
        try {
            if (this.isRequest(logicalMsgContext)) {
                this.registerRequest(this.getRequestId(logicalMsgContext, logicalMsgContext.getMessage(), this.reqClass));
            }
        } catch (RuntimeException e) {
            if (e.getClass().getAnnotation(SoapFault.class) != null) {
                throw e;
            } else {
                // TODO: improve error handling
                LOGGER.error("Unable to handle directory message in context: {" + StringUtils.join(logicalMsgContext) + "}", e);

                return false;
            }
        } catch (Throwable th) {
            // TODO: improve error handling
            LOGGER.error("Unable to handle directory message in context: {" + StringUtils.join(logicalMsgContext) + "}", th);

            return false;
        }

        return true;
    }

    @Override
    public boolean handleFault(LogicalMessageContext logicalMsgContext) {
        LOGGER.trace("Handling directory fault: {" + StringUtils.join(logicalMsgContext) + "}");

        return true;
    }

    @Override
    public void close(MessageContext msgContext) {
        LOGGER.trace("Closing directory handler message context: {" + StringUtils.join(msgContext) + "}");

        this.cleanUpRequests();
    }

    protected synchronized void checkDirectoryRequestCache() {
        try {
            DIR_REQ_CACHE_LOCK.acquire();

            dirReqCache = (dirReqCache != null) ? dirReqCache : this.createCache();
        } catch (InterruptedException e) {
            LOGGER.error("Directory request handler thread interrupted.", e);

            Thread.currentThread().interrupt();
        } finally {
            DIR_REQ_CACHE_LOCK.release();
        }
    }

    protected synchronized Cache<String, Date> createCache() {
        Cache<String, Date> dirReqCache = CacheBuilder.from(dirReqCacheDesc.toCacheBuilderSpecString()).recordStats().removalListener(this).build();

        LOGGER.debug("Directory request cache initialized: " + dirReqCache);

        return dirReqCache;
    }

    protected Object getProperty(MessageContext msgContext, Scope scope, String name) {
        return this.hasProperty(msgContext, scope, name) ? msgContext.get(name) : null;
    }

    protected Object removeProperty(MessageContext msgContext, Scope scope, String name) {
        return this.hasProperty(msgContext, scope, name) ? msgContext.remove(name) : null;
    }

    protected void setProperty(MessageContext msgContext, Scope scope, String name, Object value) {
        msgContext.setScope(name, scope);
        msgContext.put(name, value);
    }

    protected boolean hasProperty(MessageContext msgContext, Scope scope, String name) {
        return msgContext.containsKey(name) && msgContext.getScope(name).equals(scope);
    }

    protected boolean isRequest(LogicalMessageContext logicalMsgContext) {
        return !this.isResponse(logicalMsgContext);
    }

    protected boolean isResponse(LogicalMessageContext logicalMsgContext) {
        return logicalMsgContext.containsKey(MessageContext.MESSAGE_OUTBOUND_PROPERTY)
                && BooleanUtils.toBoolean(ObjectUtils.toString(logicalMsgContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)));
    }

    protected <V> V getPayload(LogicalMessageContext logicalMsgContext, LogicalMessage logicalMsg, Class<V> payloadMsgClass) throws DirectoryHandlerException {
        try {
            Object payload = logicalMsg.getPayload(dirJaxb2Marshaller.getJaxbContext());

            if (payload == null) {
                throw new DirectoryHandlerException("Directory message payload is empty: {" + StringUtils.join(logicalMsgContext) + "}");
            }

            Class<?> payloadClass = payload.getClass();

            if (JAXBElement.class.isAssignableFrom(payloadClass)) {
                payloadClass = ((JAXBElement<?>) payload).getDeclaredType();
                payload = ((JAXBElement<?>) payload).getValue();
            }

            if (!payloadMsgClass.isAssignableFrom(payloadClass)) {
                throw new DirectoryHandlerException("Directory message payload is an unknown type (expected=" + payloadMsgClass.getName() + "): "
                        + payloadClass.getName());
            }

            return payloadMsgClass.cast(payload);
        } catch (Throwable th) {
            // TODO: improve error handling
            throw new DirectoryHandlerException(th);
        }
    }

    protected abstract <V> String getRequestId(LogicalMessageContext logicalMsgContext, LogicalMessage logicalMsg, Class<V> payloadClass)
            throws DirectoryHandlerException;
}
