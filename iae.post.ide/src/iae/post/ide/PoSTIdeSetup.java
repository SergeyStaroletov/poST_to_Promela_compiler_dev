/*
 * generated by Xtext 2.26.0
 */
package iae.post.ide;

import com.google.inject.Guice;
import com.google.inject.Injector;
import iae.post.PoSTRuntimeModule;
import iae.post.PoSTStandaloneSetup;
import org.eclipse.xtext.util.Modules2;

/**
 * Initialization support for running Xtext languages as language servers.
 */
public class PoSTIdeSetup extends PoSTStandaloneSetup {

	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new PoSTRuntimeModule(), new PoSTIdeModule()));
	}
	
}
