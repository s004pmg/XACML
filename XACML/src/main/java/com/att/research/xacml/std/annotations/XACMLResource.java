/*
 * 
 * Copyright (C) 2013-2018 AT&T Intellectual Property.
 *
 * SPDX-License-Identifier: MIT
 *
 */
package com.att.research.xacml.std.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface XACMLResource {
	String	category() default "urn:oasis:names:tc:xacml:3.0:attribute-category:resource";
	String	attributeId() default "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
	String	datatype() default XACMLRequest.nullString;
	String	issuer() default XACMLRequest.nullString;
	String	id() default XACMLRequest.nullString;
	boolean includeInResults() default false;
}
