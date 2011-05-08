package com.github.gardentree.jambalaya;

import org.mozilla.javascript.Scriptable;

/**
 * @author garden_tree
 * @since 2011/05/07
 */
public interface AzureCallback {

	/**
	 * @param self TODO
	 * @param arguments
	 * @return
	 */
	Object call(Scriptable self, Object[] arguments);
}
