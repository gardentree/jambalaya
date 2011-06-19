package com.github.gardentree.colors.azure;

import org.mozilla.javascript.BaseFunction;

/**
 * @author garden_tree
 * @since 2011/04/30
 */
public class AzureFunction extends AzureObject<BaseFunction> {
	private final AzureObject<?> m_self;

	public AzureFunction(final BaseFunction object,final AzureObject<?> self,final AzureRuntime runtime) {
		super(object,runtime);

		m_self = self;
	}

	public String getName() {
		return getNativeObject().getFunctionName();
	}
	public int getArity() {
		return getNativeObject().getArity();
	}
	public Object call(final Object[] arguments) {
		final AzureRuntime runtime = getNativeRuntime();

		return getNativeObject().call(runtime.getNativeContext(),runtime.getNativeScope(),m_self.getNativeObject(),arguments);
	}
}
