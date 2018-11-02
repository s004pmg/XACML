/*
 * 
 * Copyright (C) 2013-2018 AT&T Intellectual Property.
 *
 * SPDX-License-Identifier: MIT
 *
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.StatusCode;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;

/**
 * PolicySetIdReference extends {@link com.att.research.xacmlatt.pdp.policy.PolicyIdReferenceBase} for
 * {@link com.att.research.xacmlatt.pdp.PolicySet} objects to implement the <code>ensureReferencee</code>
 * method to find <code>PolicySet</code>s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class PolicySetIdReference extends PolicyIdReferenceBase<PolicySet> {

	public PolicySetIdReference(PolicySet policySetParent, StatusCode statusCodeIn, String statusMessageIn) {
		super(policySetParent, statusCodeIn, statusMessageIn);
	}
	
	public PolicySetIdReference(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public PolicySetIdReference(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}
	
	public PolicySetIdReference(PolicySet policySetParent) {
		super(policySetParent);
	}

	public PolicySetIdReference() {
	}

	@Override
	protected PolicySet ensureReferencee(EvaluationContext evaluationContext) throws EvaluationException {
		if (this.getReferencee() == null) {
			PolicyFinderResult<PolicySet> policyFactoryResult	= evaluationContext.getPolicySet(this.getIdReferenceMatch());
			if (policyFactoryResult.getStatus() == null || policyFactoryResult.getStatus().isOk()) {
				this.setReferencee(policyFactoryResult.getPolicyDef());
			}
		}
		return this.getReferencee();
	}

}
