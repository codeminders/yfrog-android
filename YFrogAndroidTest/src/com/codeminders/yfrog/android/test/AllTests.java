/**
 * 
 */
package com.codeminders.yfrog.android.test;

import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author idemydenko
 *
 */
public class AllTests extends TestSuite {
	public static Test suite() {
		return new TestSuiteBuilder(AllTests.class).includeAllPackagesUnderHere().build();
	}
}
