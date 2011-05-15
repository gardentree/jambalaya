package com.github.gardentree.jambalaya;


import java.io.File;

import org.jruby.Ruby;
import org.jruby.runtime.builtin.IRubyObject;

import com.github.gardentree.colors.azure.Azure;
import com.github.gardentree.colors.azure.AzureRuntime;
import com.github.gardentree.colors.azure.Logger;
import com.github.gardentree.colors.crimson.CrimsonRuntime;
import com.github.gardentree.jambalaya.colors.violet.Violet;
import com.github.gardentree.utilities.Entirety;

/**
 * @author garden_tree
 * @since 2011/04/29
 */
public class Jambalaya {
	private final Violet m_violet = new Violet(AzureRuntime.newInstance(),new CrimsonRuntime(Ruby.newInstance()));
	private final AzureRuntime m_azure = m_violet.getAzure();
	private final IRubyObject	m_top;
	public Jambalaya() {
		m_top = m_violet.deriveCrimsonFrom(m_azure.getTopObject().getNativeObject());
	}

	public IRubyObject fry(final String script) {
		final Azure azure = m_azure.evaluate(script);

		return m_violet.deriveCrimsonFrom(azure.getNativeObject());
	}
//	public void mix(final String name,final IRubyObject object) {
//		final Object azure = m_violet.deriveAzureFrom(object);
//		m_azure.getTopObject().setProperty(name,m_azure.wrap(azure));
//	}
	public IRubyObject squeeze(final String pathname,final String specifier,final String[] scripts) throws Exception {
		///////////////////////////////
		m_azure.getNativeScope().put("print",m_azure.getNativeScope(),new Logger("print",m_azure.getNativeScope()));
		///////////////////////////////

		for (final String script:scripts) {
			m_violet.getCrimson().evaluate(script);
		}

		try {
			final StringBuilder builder = new StringBuilder();
			builder.append("(function(exports){");
			builder.append(Entirety.getFromFile(new File(pathname).toURI().toURL()));
			builder.append(String.format("return %s;",specifier));
			builder.append("})({})");

			final Azure object = m_azure.evaluate(builder);
			return m_violet.deriveCrimsonFrom(object.getNativeObject());
//			if (object instanceof AzureObject) {
//				final JavaScriptObject javascript = new JavaScriptObject((AzureObject)object,m_violet);
//				return javascript.define(scripts);
//			}


//			return JavaScriptObject.define((AzurePrimitiveObject)object);
		}
		catch (final Exception cause) {
			throw new SilenceException(cause);
		}
//		finally {
//			Context.exit();
//		}
	}

	public IRubyObject top() {
		return m_top;
	}
}
