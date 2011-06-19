package com.github.gardentree.colors.azure;

import org.mozilla.javascript.ScriptableObject;

/**
 * @author garden_tree
 * @since 2011/05/13
 */
public class AzureBasicObject extends AzureObject<ScriptableObject> {
	public AzureBasicObject(final ScriptableObject object,final AzureRuntime runtime) {
		super(object,runtime);
	}

	public void setProperty(final String name,final Azure<?> azure) {
		getNativeObject().defineProperty(name,azure.getNativeObject(),ScriptableObject.PERMANENT);
	}
}
