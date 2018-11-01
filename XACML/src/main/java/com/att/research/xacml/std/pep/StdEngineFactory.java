/*
 * 
 * Copyright (C) 2013-2018 AT&T Intellectual Property.
 *
 * SPDX-License-Identifier: MIT
 *
 */
package com.att.research.xacml.std.pep;

import java.util.Properties;

import com.att.research.xacml.api.pep.PEPEngine;
import com.att.research.xacml.api.pep.PEPEngineFactory;
import com.att.research.xacml.api.pep.PEPException;

public class StdEngineFactory extends PEPEngineFactory {

	public StdEngineFactory() {
	}

	@Override
	public PEPEngine newEngine() throws PEPException {
		return new StdEngine();
	}

	@Override
	public PEPEngine newEngine(Properties properties) throws PEPException {
		return new StdEngine(properties);
	}

}
