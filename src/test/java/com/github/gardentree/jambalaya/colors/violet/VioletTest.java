package com.github.gardentree.jambalaya.colors.violet;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.jruby.RubyFloat;
import org.jruby.RubyObject;
import org.jruby.RubyString;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Test;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.github.gardentree.colors.azure.AzureRuntime;
import com.github.gardentree.colors.crimson.CrimsonRuntime;
import com.github.gardentree.colors.violet.Violet;
import com.github.gardentree.utilities.Entirety;
import com.github.gardentree.utilities.Resource;


/**
 * @author garden_tree
 * @since 2011/05/06
 */
public class VioletTest {
	private final AzureRuntime		m_azure		= AzureRuntime.newInstance();
	private final CrimsonRuntime	m_crimson	= CrimsonRuntime.newInstance();
	private final Violet			m_target	= new Violet(m_azure,m_crimson);
	private final ThreadContext		m_context	= m_crimson.getNativeRuntime().getCurrentContext();

	///////////////////////////////////////////////////////////////////////////deriveCrimson
	@Test
	public void deriveCrimsonFromForString() {
		final IRubyObject actual = m_target.deriveCrimsonFrom(m_azure.evaluate("'violet'").getNativeObject());
		assertEquals(RubyString.class	,actual.getClass());
		assertEquals("violet"			,actual.asJavaString());
	}
	@Test
	public void deriveCrimsonFromForNumber() {
		final IRubyObject actual = m_target.deriveCrimsonFrom(m_azure.evaluate("1").getNativeObject());

		assertEquals(RubyFloat.class	,actual.getClass());
		assertEquals("1.0"				,actual.toString());
	}
	@Test
	public void deriveCrimsonFromForObject() {
		final IRubyObject actual = m_target.deriveCrimsonFrom(m_azure.evaluate("(function(){return {one: 1, 2: 'two'};})()").getNativeObject());

		assertEquals(RubyObject.class	,actual.getClass());
		assertEquals("1.0"				,actual.callMethod(m_context,"one").toString());
		assertEquals("two"				,actual.callMethod(m_context,"2").toString());
	}
	@Test
	public void deriveCrimsonFromForFunction() {
		final IRubyObject actual = m_target.deriveCrimsonFrom(m_azure.evaluate("(function(){return function(){return 'foo';}})()").getNativeObject());

		assertEquals(RubyObject.class	,actual.getClass());
		assertEquals("foo"				,actual.callMethod(m_context,"[]").toString());
	}
	@Test
	public void deriveCrimsonFromForPrototype() throws IOException, URISyntaxException {
		final IRubyObject actual = m_target.deriveCrimsonFrom(m_azure.evaluate(Entirety.getFromFile(Resource.getUrl("prototype.js"))).getNativeObject());

		assertEquals(RubyObject.class	,actual.getClass());
		assertEquals("c"				,actual.callMethod(m_context,"constructor").toString());
		assertEquals("a"				,actual.callMethod(m_context,"anomalous").toString());
		assertEquals("p"				,actual.callMethod(m_context,"proto").toString());
	}

	///////////////////////////////////////////////////////////////////////////deriveAzure
	@Test
	public void deriveAzureFromForArray() {
		final Object actual = m_target.deriveAzureFrom(m_crimson.evaluate("[1,2,3]"));

		assertEquals(NativeArray.class			,actual.getClass());
		assertArrayEquals(new Object[]{1D,2D,3D},(Object[])m_azure.deriveJavaFrom(actual));
	}
	@Test
	public void deriveAzureFromForHash() {
		final Object actual = m_target.deriveAzureFrom(m_crimson.evaluate("{'one' => 1,2 => 'two'}"));

		assertTrue(actual instanceof ScriptableObject);
		final ScriptableObject object = (ScriptableObject)actual;
		final Scriptable scope = m_azure.getNativeScope();
		assertEquals(1L		,object.get("one",scope));
		assertEquals("two"	,object.get("2",scope));
	}
	@Test
	public void deriveAzureFromForNumber() {
		final Object actual = m_target.deriveAzureFrom(m_crimson.evaluate("1"));

		assertEquals(1L,actual);
	}
	@Test
	public void deriveAzureFromForObject() throws IOException, URISyntaxException {
		final Object actual = m_target.deriveAzureFrom(m_crimson.evaluate(Entirety.getFromFile(Resource.getUrl("object.rb"))));

		assertTrue(actual instanceof ScriptableObject);
		final ScriptableObject object = (ScriptableObject)actual;
		final Scriptable scope = m_azure.getNativeScope();
		assertEquals("f",((Function)object.get("function",scope)).call(m_azure.getNativeContext(),m_azure.getNativeScope(),object,new Object[0]));
	}
}
