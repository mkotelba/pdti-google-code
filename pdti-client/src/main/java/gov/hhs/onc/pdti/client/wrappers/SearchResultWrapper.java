package gov.hhs.onc.pdti.client.wrappers;

import java.util.List;
import java.util.Map;

public class SearchResultWrapper {
    
    private String dn;
    private Map<String, List<String>> attributes;
    
    public String getDn() {
        return dn;
    }
    
    public Map<String, List<String>> getAttributes() {
        return attributes;
    }
    
    public void setDn(String dn) {
        this.dn = dn;
    }
    
    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }
    
}