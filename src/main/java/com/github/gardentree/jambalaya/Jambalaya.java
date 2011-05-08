package com.github.gardentree.jambalaya;


import java.io.File;

import org.jruby.Ruby;
import org.jruby.runtime.builtin.IRubyObject;

import com.github.gardentree.color.azure.Azure;
import com.github.gardentree.color.azure.AzureRuntime;
import com.github.gardentree.color.crimson.CrimsonRuntime;
import com.github.gardentree.utilities.Entirety;

/**
 * @author garden_tree
 * @since 2011/04/29
 */
public class Jambalaya {
	private final Violet m_violet = new Violet(AzureRuntime.newInstance(),new CrimsonRuntime(Ruby.newInstance()));
	public Jambalaya() {
		//
	}

	public IRubyObject evaluate(final String script) {
		final Azure azure = m_violet.getAzure().evaluate(script);

		return m_violet.deriveCrimsonFrom(azure.getNativeObject());
	}

	public IRubyObject mix(final String pathname,final String specifier,final String[] scripts) throws Exception {
		return execute(pathname,specifier,scripts);
	}
	public static IRubyObject squeeze(final String pathname,final String specifier,final String[] scripts) throws Exception {
		return new Jambalaya().execute(pathname,specifier,scripts);
	}
	IRubyObject execute(final String pathname,final String specifier,final String[] scripts) throws Exception {
		///////////////////////////////
		m_violet.getAzure().getNativeScope().put("print",m_violet.getAzure().getNativeScope(),new Logger("print",m_violet.getAzure().getNativeScope()));
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

			final Azure object = m_violet.getAzure().evaluate(builder);
			return m_violet.deriveCrimsonFrom(object.getNativeObject());
//			if (object instanceof AzureObject) {
//				final JavaScriptObject javascript = new JavaScriptObject((AzureObject)object,m_violet);
//				return javascript.define(scripts);
//			}


//			return JavaScriptObject.define((AzurePrimitiveObject)object);
		}
		catch (final Exception excepiton) {
			System.out.println(excepiton.getMessage());

			throw excepiton;
		}
//		finally {
//			Context.exit();
//		}
	}
}
