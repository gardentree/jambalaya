package com.github.gardentree.jambalaya;


import java.io.File;

import org.jruby.Ruby;
import org.jruby.runtime.builtin.IRubyObject;

import com.github.gardentree.colors.azure.Azure;
import com.github.gardentree.colors.azure.AzureRuntime;
import com.github.gardentree.colors.azure.Logger;
import com.github.gardentree.colors.azure.function.Require;
import com.github.gardentree.colors.crimson.CrimsonRuntime;
import com.github.gardentree.jambalaya.colors.violet.Violet;

/**
 * @author garden_tree
 * @since 2011/04/29
 */
public class Jambalaya {
	private final Violet m_violet = new Violet(AzureRuntime.newInstance(),new CrimsonRuntime(Ruby.newInstance()));
	private final AzureRuntime m_azure = m_violet.getAzure();
	private IRubyObject	m_top;

	public Jambalaya() {
		m_azure.getNativeScope().put("print",m_azure.getNativeScope(),new Logger("print",m_azure.getNativeScope()));

		Require.setup(m_azure,new File("lib").getAbsolutePath());
//		m_azure.getNativeScope().put("require",m_azure.getNativeScope(),new Require("require",m_azure.getNativeScope()));
	}

	public IRubyObject fry(final String script,final String... crimsons) {
		final Azure azure = m_azure.evaluate(script);
		for (final String crimson:crimsons) {
			m_violet.getCrimson().evaluate(crimson);
		}

		return m_violet.deriveCrimsonFrom(azure.getNativeObject());
	}
	public IRubyObject squeeze(final String pathname,final String specifier,final String... scripts) {

		for (final String script:scripts) {
			m_violet.getCrimson().evaluate(script);
		}

		return m_violet.deriveCrimsonFrom(m_azure.require(pathname,specifier));
	}

	public IRubyObject top() {
		if (m_top == null) {
			m_top = m_violet.deriveCrimsonFrom(m_azure.getTopObject().getNativeObject());
		}

		return m_top;
	}
}
