package gov.hhs.onc.pdti.ws.handler;

public interface DirectoryRequestCacheDescriptor {
    public String toCacheBuilderSpecString();

    public int getCleanUpInterval();

    public void setCleanUpInterval(int cleanUpInterval);

    public int getConcurrencyLevel();

    public void setConcurrencyLevel(int concurrencyLevel);

    public String getExpireAfterAccess();

    public void setExpireAfterAccess(String expireAfterAccess);

    public String getExpireAfterWrite();

    public void setExpireAfterWrite(String expireAfterWrite);

    public int getInitialCapacity();

    public void setInitialCapacity(int initialCapacity);

    public int getMaximumSize();

    public void setMaximumSize(int maximumSize);

    public int getMaximumWeight();

    public void setMaximumWeight(int maximumWeight);

    public String getRefreshAfterWrite();

    public void setRefreshAfterWrite(String refreshAfterWrite);

    public String getRefreshInterval();

    public void setRefreshInterval(String refreshInterval);

    public boolean isSoftValues();

    public void setSoftValues(boolean softValues);

    public boolean isWeakKeys();

    public void setWeakKeys(boolean weakKeys);

    public boolean isWeakValues();

    public void setWeakValues(boolean weakValues);
}
