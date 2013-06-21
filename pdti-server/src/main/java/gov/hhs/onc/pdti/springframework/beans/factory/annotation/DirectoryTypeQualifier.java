package gov.hhs.onc.pdti.springframework.beans.factory.annotation;

import gov.hhs.onc.pdti.data.DirectoryType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Qualifier;

@Inherited
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
public @interface DirectoryTypeQualifier {
    DirectoryType value() default DirectoryType.IHE;
}
