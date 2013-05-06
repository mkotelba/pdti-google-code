package gov.hhs.onc.pdti.ldap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("providerInformationDirectoryLdapConnConfig")
@Scope("prototype")
public class ProviderInformationDirectoryLdapConnectionConfig extends LdapConnectionConfig {
    public final static String DEFAULT_LDAP_HOST_NAME = "localhost";

    public boolean isAnonymous() {
        return !StringUtils.isBlank(this.getName());
    }

    @Override
    public String toString() {
        StrBuilder builder = new StrBuilder();
        builder.appendWithSeparators(
                ArrayUtils.toArray("host=" + this.getLdapHost(), "port=" + this.getLdapPort(),
                        "bindDn=" + this.getName()), ",");

        return builder.toString();
    }

    @Override
    public String getDefaultLdapHost() {
        return DEFAULT_LDAP_HOST_NAME;
    }

    @Override
    public String getLdapHost() {
        return StringUtils.defaultIfBlank(super.getLdapHost(), this.getDefaultLdapHost());
    }

    @Override
    public int getLdapPort() {
        int ldapPort = super.getLdapPort();

        return (ldapPort != 0) ? ldapPort : super.getDefaultLdapPort();
    }
}
