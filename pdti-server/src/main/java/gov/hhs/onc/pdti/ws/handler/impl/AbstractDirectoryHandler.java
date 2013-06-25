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
import java.util.Date;
import java.util.concurrent.Semaphore;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public abstract class AbstractDirectoryHandler<T, U> implements DirectoryHandler<T, U>, RemovalListener<MessageContext, Date> {
    protected final static Semaphore DIR_REQ_CACHE_LOCK = new Semaphore(1, true);

    protected static Cache<MessageContext, Date> dirReqCache;

    @Autowired
    protected DirectoryJaxb2Marshaller dirJaxb2Marshaller;

    @Autowired
    @DirectoryType(DirectoryTypeId.MAIN)
    DirectoryRequestCacheDescriptor dirReqCacheDesc;

    private final static Logger LOGGER = Logger.getLogger(AbstractDirectoryHandler.class);

    public synchronized void registerRequest(MessageContext msgContext) throws DirectoryHandlerException {
        this.checkDirectoryRequestCache(msgContext);

        Date reqDate;

        if ((reqDate = dirReqCache.getIfPresent(msgContext)) != null) {
            throw new DirectoryHandlerException("Directory federation loop detected - request (msgContext=" + msgContext + ", requestDate=" + reqDate
                    + ") was already registered.");
        }

        reqDate = new Date();

        dirReqCache.put(msgContext, reqDate);

        LOGGER.trace("Registered directory request (msgContext=" + msgContext + ", requestDate=" + reqDate + ").");
    }

    public synchronized void releaseRequest(MessageContext msgContext) {
        this.checkDirectoryRequestCache(msgContext);

        dirReqCache.invalidate(msgContext);
    }

    @Scheduled(fixedDelayString = "#{ dirReqCacheDescriptor.cleanUpInterval }")
    public synchronized void cleanUpRequests(MessageContext msgContext) {
        this.checkDirectoryRequestCache(msgContext);

        dirReqCache.cleanUp();

        CacheStats dirReqCacheStats = dirReqCache.stats();

        LOGGER.trace("Cleaned up directory request cache: " + dirReqCacheStats);

        // Resetting directory request cache statistics
        dirReqCacheStats.minus(dirReqCacheStats);
    }

    public synchronized void onRemoval(RemovalNotification<MessageContext, Date> notification) {
        LOGGER.trace("Released directory request (msgContext=" + notification.getKey() + ", requestDate=" + notification.getValue() + ") from registry: cause="
                + notification.getCause());
    }

    @Override
    public boolean handleMessage(LogicalMessageContext logicalMsgContext) {
        try {
            if (this.isRequest(logicalMsgContext)) {
                this.registerRequest(logicalMsgContext);
            } else if (this.isResponse(logicalMsgContext)) {
                // TODO: implement
            }
        } catch (Throwable th) {
            // TODO: improve error handling
            LOGGER.error("Unable to handle directory message in context: {" + StringUtils.join(logicalMsgContext) + "}", th);
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

        this.releaseRequest(msgContext);
        this.cleanUpRequests(msgContext);
    }

    protected synchronized void checkDirectoryRequestCache(MessageContext msgContext) {
        try {
            DIR_REQ_CACHE_LOCK.acquire();

            dirReqCache = this.createCache(msgContext);
        } catch (InterruptedException e) {
            LOGGER.error("Directory request handler thread interrupted.", e);

            Thread.currentThread().interrupt();
        } finally {
            DIR_REQ_CACHE_LOCK.release();
        }
    }

    protected synchronized Cache<MessageContext, Date> createCache(MessageContext msgContext) {
        Cache<MessageContext, Date> dirReqCache = CacheBuilder.from(dirReqCacheDesc.toCacheBuilderSpecString()).recordStats().removalListener(this).build();

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
            Object payload = this.dirJaxb2Marshaller.unmarshal(logicalMsg.getPayload());

            if (payload == null) {
                throw new DirectoryHandlerException("Directory message payload is empty: {" + StringUtils.join(logicalMsgContext) + "}");
            }

            Class<?> payloadClass = payload.getClass();

            if (!payloadMsgClass.isAssignableFrom(payloadClass)) {
                throw new DirectoryHandlerException("Directory message payload is an unknown type (expected=" + payloadMsgClass.getName() + "): "
                        + payloadClass.getName());
            }

            return payloadMsgClass.cast(payload);
        } catch (Throwable th) {
            throw new DirectoryHandlerException("");
        }
    }
}
