/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.XACML1;
import com.att.research.xacml.api.XACML2;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.datatypes.DataTypeAnyURI;
import com.att.research.xacml.std.datatypes.DataTypeBoolean;
import com.att.research.xacml.std.datatypes.DataTypeDate;
import com.att.research.xacml.std.datatypes.DataTypeDateTime;
import com.att.research.xacml.std.datatypes.DataTypeDouble;
import com.att.research.xacml.std.datatypes.DataTypeInteger;
import com.att.research.xacml.std.datatypes.DataTypeRFC822Name;
import com.att.research.xacml.std.datatypes.DataTypeTime;
import com.att.research.xacml.std.datatypes.DataTypeX500Name;
import com.att.research.xacml.std.datatypes.DataTypeXPathExpression;

public class JsonRequestTranslatorTest {
	private static final Logger logger	= LoggerFactory.getLogger(JsonRequestTranslatorTest.class);

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();	
	
	@Test
	public void test81() throws Exception {
		//
		// Read it from the file
		//
		Request request = JsonRequestTranslator.load(new File("src/test/resources/Request-8.1.json"));
		validate81Request(request);

		//
		// Convert to string
		//
		String strJson = JsonRequestTranslator.toString(request, true);
		validate81Json(strJson);
		
		//
		// Read it in again
		//
		request = JsonRequestTranslator.load(strJson);
		validate81Request(request);
		//
		// Convert to string again
		//
		strJson = JsonRequestTranslator.toString(request, false);
		validate81Json(strJson);
		
	}
	
	private void validate81Json(String strJson) {
		logger.info("{}", strJson);

		// @formatter:off
		assertThat(strJson)
			.contains("\"Andreas\"")
			.contains("\"Gamla Stan\"")
			.contains("\"http://example.com/buy\"")
			.contains("123.34")
			.contains("\"SEK\"");
        // @formatter:on
	}
	
	private void validate81Request(Request request) throws DataTypeException {
        // @formatter:off

		assertThat(request).isNotNull()
        	.hasFieldOrPropertyWithValue("combinedDecision", false)
        	.hasFieldOrPropertyWithValue("returnPolicyIdList", false)
        	;
        
        assertThat(request.getRequestAttributes()).isNotNull().hasSize(3);
        assertThat(request.getRequestAttributesIncludedInResult()).isEmpty();;
        assertThat(request.getMultiRequests()).isEmpty();
        assertThat(request.getRequestDefaults()).isNull();

        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT, 
        		XACML1.ID_SUBJECT_SUBJECT_ID, 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "Andreas"))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT, 
        		new IdentifierImpl("location"), 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "Gamla Stan"))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_ACTION, 
        		XACML1.ID_ACTION_ACTION_ID, 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_ANYURI, 
        						DataTypeAnyURI.newInstance().convert("http://example.com/buy")))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
        		new IdentifierImpl("book-title"), 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "Learn German in 90 days"))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
        		new IdentifierImpl("currency"), 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "SEK"))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
        		new IdentifierImpl("price"), 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_DOUBLE, 123.34))
        		)).isTrue();
                 
        // @formatter:on
	}

    @Test
    public void test83() throws Exception {
        Request request = JsonRequestTranslator.load(new File("src/test/resources/Request-8.3.json"));       
		validate83Request(request);

		//
		// Convert to string
		//
		String strJson = JsonRequestTranslator.toString(request, true);
		validate83Json(strJson);
		
		//
		// Read it in again
		//
		request = JsonRequestTranslator.load(strJson);
		validate83Request(request);
		//
		// Convert to string again
		//
		strJson = JsonRequestTranslator.toString(request, false);
		validate83Json(strJson);
    }
    
    private void validate83Json(String strJson) {
		logger.info("{}", strJson);
		
        // @formatter:off
		assertThat(strJson)
			.contains("\"Alice\"")
			.contains("\"record\"")
			.contains("\"126\"")
			.contains("\"125\"")
			.contains("\"view\"")
			.contains("\"edit\"")
			.contains("\"delete\"")
			.contains("\"MultiRequests\"")
			.contains("\"s1\"")
			.contains("\"a1\"")
			.contains("\"r1\"")
			.contains("\"a2\"")
			;
        // @formatter:on
    }
    
    private void validate83Request(Request request) {
    	
        // @formatter:off
        assertThat(request).isNotNull()
        	.hasFieldOrPropertyWithValue("combinedDecision", false)
        	.hasFieldOrPropertyWithValue("returnPolicyIdList", false)
        	;
        
        assertThat(request.getRequestAttributes()).isNotNull().hasSize(6);
        assertThat(request.getRequestAttributesIncludedInResult()).isNotNull().hasSize(5);
        assertThat(request.getMultiRequests()).hasSize(2);
        assertThat(request.getRequestDefaults()).isNull();

        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT, 
        		new IdentifierImpl("com.acme.user.employeeId"), 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "Alice"))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
        		new IdentifierImpl("com.acme.object.objectType"), 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "record"))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
        		new IdentifierImpl("com.acme.record.recordId"), 
        		null, 
        		true, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "126"))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
        		new IdentifierImpl("com.acme.record.recordId"), 
        		null, 
        		true, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "125"))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_ACTION, 
        		new IdentifierImpl("com.acme.action.actionId"), 
        		null, 
        		true, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "view"))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_ACTION, 
        		new IdentifierImpl("com.acme.action.actionId"), 
        		null, 
        		true, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "edit"))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_ACTION, 
        		new IdentifierImpl("com.acme.action.actionId"), 
        		null, 
        		true, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "delete"))
        		)).isTrue();

        // @formatter:on
    }

    @Test
    public void testAll() throws Exception {
    	Request request;
    	try (FileInputStream is = new FileInputStream(new File("src/test/resources/Request-All.json"))) { 
    		request = JsonRequestTranslator.load(is);
    	}
        validateAll(request);
		//
		// Convert to string
		//
		String strJson = JsonRequestTranslator.toString(request, true);
		validateAllJson(strJson);
		
		//
		// Read it in again
		//
		request = JsonRequestTranslator.load(strJson);
		validateAll(request);

		//
		// Convert to string again
		//
		strJson = JsonRequestTranslator.toString(request, false);
		validateAllJson(strJson);
    }
    
    private void validateAll(Request request) throws DataTypeException {
        // @formatter:off
        assertThat(request).isNotNull()
        	.hasFieldOrPropertyWithValue("combinedDecision", false)
        	.hasFieldOrPropertyWithValue("returnPolicyIdList", true)
        	;
        
        assertThat(request.getRequestAttributes()).isNotNull().hasSize(8);
        assertThat(request.getRequestAttributesIncludedInResult()).isNotNull().hasSize(1);
        assertThat(request.getMultiRequests()).isEmpty();
        assertThat(request.getRequestDefaults()).isNull();

        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT, 
        		XACML1.ID_SUBJECT_SUBJECT_ID, 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "Andreas"))
        		)).isTrue();
        
        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT, 
        		XACML1.ID_SUBJECT_SUBJECT_ID_QUALIFIER, 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "Member"))
        		)).isTrue();
        
        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT, 
        		XACML1.ID_SUBJECT_KEY_INFO, 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "key123"))
        		)).isTrue();
        
        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT, 
        		XACML1.ID_SUBJECT_AUTHENTICATION_TIME, 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_TIME, DataTypeTime.newInstance().convert("08:19:52-05:00")))
        		)).isTrue();
        
        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_RECIPIENT_SUBJECT, 
        		new IdentifierImpl("id:boolean"), 
        		null, 
        		true, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_BOOLEAN, DataTypeBoolean.newInstance().convert(false)))
        		)).isTrue();
        
        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_RECIPIENT_SUBJECT, 
        		new IdentifierImpl("id:integer"), 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_INTEGER, DataTypeInteger.newInstance().convert(5)))
        		)).isTrue();
        
        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_RECIPIENT_SUBJECT, 
        		new IdentifierImpl("id:double"), 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_DOUBLE, DataTypeDouble.newInstance().convert(8.888)))
        		)).isTrue();
        
        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_INTERMEDIARY_SUBJECT, 
        		new IdentifierImpl("id:x500"), 
        		null, 
        		false, 
        		Arrays.asList(new StdAttributeValue<>(XACML3.ID_DATATYPE_X500NAME, 
        					DataTypeX500Name.newInstance().convert("cn=Julius Hibbert,o=Medico Corp, c=US")))
        		)).isTrue();
        
        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_INTERMEDIARY_SUBJECT, 
        		new IdentifierImpl("id:rfc822"), 
        		null, 
        		false, 
        		Arrays.asList(new StdAttributeValue<>(XACML3.ID_DATATYPE_RFC822NAME, 
        					DataTypeRFC822Name.newInstance().convert("j_hibbert@MEDICO.COM")))
        		)).isTrue();
        
        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_INTERMEDIARY_SUBJECT, 
        		new IdentifierImpl("id:xpath"), 
        		null, 
        		false, 
        		Arrays.asList(new StdAttributeValue<>(XACML3.ID_DATATYPE_XPATHEXPRESSION, 
        					DataTypeXPathExpression.newInstance().convert("md:record/md:patient/md:patientDoB")))
        		)).isTrue();
        
        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_ACTION, 
        		XACML3.ID_ACTION_ACTION_ID, 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "read"))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_ACTION, 
        		XACML3.ID_ACTION_IMPLIED_ACTION, 
        		null, 
        		false, 
        		Arrays.asList(new StdAttributeValue<>(XACML3.ID_DATATYPE_ANYURI, 
        					DataTypeAnyURI.newInstance().convert("http://example.com/buy")))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
        		XACML3.ID_RESOURCE_RESOURCE_ID, 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "Learn German in 90 days"))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
        		XACML2.ID_RESOURCE_TARGET_NAMESPACE, 
        		null, 
        		false, 
        		Arrays.asList(
        				new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, "SelfHelp"))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT, 
        		XACML1.ID_ENVIRONMENT_CURRENT_TIME, 
        		null, 
        		false, 
        		Arrays.asList(new StdAttributeValue<>(XACML3.ID_DATATYPE_TIME, 
        				DataTypeTime.newInstance().convert("08:23:47-05:00")))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT, 
        		XACML1.ID_ENVIRONMENT_CURRENT_DATE, 
        		null, 
        		false, 
        		Arrays.asList(new StdAttributeValue<>(XACML3.ID_DATATYPE_DATE, 
        				DataTypeDate.newInstance().convert("20202-01-01")))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT, 
        		XACML1.ID_ENVIRONMENT_CURRENT_DATETIME, 
        		null, 
        		false, 
        		Arrays.asList(new StdAttributeValue<>(XACML3.ID_DATATYPE_DATETIME, 
        				DataTypeDateTime.newInstance().convert("2020-01-01T08:23:47-05:00")))
        		)).isTrue();

        if (! hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_CODEBASE, 
        		new IdentifierImpl("id:code"), 
        		null, 
        		false, 
        		Arrays.asList(new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, 
        				Arrays.asList("image:build", BigInteger.valueOf(5))))
        		) &&
        		! hasAttribute(request, 
                		XACML1.ID_SUBJECT_CATEGORY_CODEBASE, 
                		new IdentifierImpl("id:code"), 
                		null, 
                		false, 
                		Arrays.asList(new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, 
                				Arrays.asList("image:build", "5")))
                		)
        		) {
        	fail("id:code is incorrect");
        }

        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_CODEBASE, 
        		new IdentifierImpl("id:version"), 
        		null, 
        		false, 
        		Arrays.asList(new StdAttributeValue<>(XACML3.ID_DATATYPE_INTEGER, BigInteger.valueOf(999)))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_CODEBASE, 
        		new IdentifierImpl("id:random"), 
        		null, 
        		false, 
        		Arrays.asList(new StdAttributeValue<>(XACML3.ID_DATATYPE_DOUBLE, Double.parseDouble("5.55")))
        		)).isTrue();

        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_CODEBASE, 
        		new IdentifierImpl("id:isSnapshot"), 
        		null, 
        		false, 
        		Arrays.asList(new StdAttributeValue<>(XACML3.ID_DATATYPE_BOOLEAN, Boolean.parseBoolean("false")))
        		)).isTrue();

        /*
        assertThat(hasAttribute(request, 
        		XACML1.ID_SUBJECT_CATEGORY_REQUESTING_MACHINE, 
        		new IdentifierImpl("id:machine"), 
        		null, 
        		false, 
        		Arrays.asList(new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, 
        				Arrays.asList(
        						ImmutableMap.of("rack", "topleft"),
        						ImmutableMap.of("id", BigInteger.valueOf(10))
        						)))
        		)).isTrue();
         */
        // @formatter:on
    }
    
    private void validateAllJson(String strJson) {
        // @formatter:off
		logger.info("{}", strJson);
		
		assertThat(strJson)
			.contains("\"Andreas\"")
			.contains("\"Member\"")
			.contains("\"key123\"")
			.contains("\"08:19:52-05:00\"")
			.contains("\"frontlock\"")
			.contains("\"08:23:48-05:00\"")
			.contains("\"08:20:48-05:00\"")
			.contains("\"127.0.0.1\"")
			.containsIgnoringCase("\"yourhost:0-9\"")
			.containsIgnoringCase("\"cn=Julius Hibbert,o=Medico Corp,c=US\"")
			.containsIgnoringCase("\"j_hibbert@MEDICO.COM\"")
			.contains("\"md:record/md:patient/md:patientDoB\"")
			.contains("\"read\"")
			.contains("\"http://example.com/buy\"")
			.contains("\"Learn German in 90 days\"")
			.contains("\"SelfHelp\"")
			.contains("\"08:23:47-05:00\"")
			.contains("\"20202-01-01\"")
			.contains("\"2020-01-01T08:23:47-05:00\"")
			.contains("\"999\"")
			.contains("\"5.55\"")
			.contains("\"false\"")
			.contains("\"8.888\"")
			.contains("\"5\"");
		
        // @formatter:on
    }
    
   private <T> boolean hasAttribute(Request request, Identifier categoryId, Identifier attributeId, 
		   String issuer, boolean includeInResults, Collection<AttributeValue<T>> expectedValues) {
	   logger.info("Searching for attribute {} in category {}", attributeId, categoryId);

	   Iterator<RequestAttributes> iterAttributes = request.getRequestAttributes(categoryId);
	   while (iterAttributes.hasNext()) {
		   RequestAttributes requestAttributes = iterAttributes.next();
		   Iterator<Attribute> iterAttribute = requestAttributes.getAttributes(attributeId);
		   while (iterAttribute.hasNext()) {
			   Attribute attribute = iterAttribute.next();
			   assertThat(attribute.getCategory()).isEqualTo(categoryId);
			   assertThat(attribute.getAttributeId()).isEqualTo(attributeId);
			   if (attribute.getValues().containsAll(expectedValues)) {
				   return true;
			   }
		   };
	   };
	   return false;
   }

   @Test
   public void testExceptions() {
       assertThatExceptionOfType(JSONStructureException.class).isThrownBy(() -> {
           JsonRequestTranslator.load(new File(folder.getRoot().getAbsolutePath() + "/idontexist.json"));
       });
       assertThatExceptionOfType(JSONStructureException.class).isThrownBy(() -> {
           JsonRequestTranslator.load("iamnot a json string at all");
       });
       assertThatExceptionOfType(JSONStructureException.class).isThrownBy(() -> {
           try (ByteArrayInputStream is = new ByteArrayInputStream("fjskfjdskalfjdkslajdf".getBytes())) {
               JsonRequestTranslator.load(is);
           }
           
       });
   }
}
