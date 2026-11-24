package com.evolyn.api.Common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.interceptor.InterceptorBinding;

/**
 * Put on a Data Service REST client interface: its calls are paced and retried on 429.
 * See DataServiceRetryInterceptor.
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DataServiceRetry {
}
