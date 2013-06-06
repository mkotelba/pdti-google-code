package gov.hhs.onc.pdti.service.interceptor.impl;

import gov.hhs.onc.pdti.service.interceptor.DirectoryInterceptor;
import org.springframework.core.annotation.Order;

public class AbstractDirectoryInterceptor implements DirectoryInterceptor {
    @Override
    public int compareTo(DirectoryInterceptor dirInterceptor) {
        return Integer.compare(getOrder(this), getOrder(dirInterceptor));
    }

    private static int getOrder(DirectoryInterceptor dirInterceptor) {
        Order order = dirInterceptor.getClass().getAnnotation(Order.class);

        return (order != null) ? order.value() : -1;
    }
}
