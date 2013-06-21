package gov.hhs.onc.pdti.springframework.beans.factory.config;

import java.beans.PropertyEditorSupport;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("dnPropEditor")
@Scope("singleton")
public class DnPropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            this.setValue(new Dn(text));
        } catch (LdapInvalidDnException e) {
            throw new IllegalArgumentException("Unable to set Distinguished Name (DN) property from string: " + text, e);
        }
    }
}
