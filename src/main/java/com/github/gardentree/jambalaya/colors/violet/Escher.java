package com.github.gardentree.jambalaya.colors.violet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jruby.RubyClass;
import org.jruby.runtime.Arity;
import org.jruby.runtime.Block;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.callback.Callback;
import org.mozilla.javascript.Scriptable;

import com.github.gardentree.colors.crimson.CrimsonClass;
import com.github.gardentree.colors.crimson.CrimsonObject;
import com.github.gardentree.colors.crimson.CrimsonRuntime;

/**
 * @author garden_tree
 * @since 2011/05/15
 */
public class Escher {
	private final CrimsonObject m_crimson;
	private final Scriptable m_source;
	private final Set<String> m_methods = new HashSet<String>();
	private final Map<String,IRubyObject> m_fields = new HashMap<String,IRubyObject>();

	public Escher(final CrimsonObject crimson,final Scriptable source) {
		m_crimson = crimson;
		m_source = source;
	}

	public static Escher newInstance(final CrimsonRuntime runtime,final Scriptable source) {
		final RubyClass base = RubyClass.newClass(runtime.getNativeRuntime(),runtime.getNativeRuntime().getObject());
		base.setBaseName("Escher");

//		base.defineMethod("[]=",new Callback() {
//			@Override
//			public Arity getArity() {
//				return Arity.ONE_REQUIRED;
//			}
//
//			@Override
//			public IRubyObject execute(IRubyObject recv,IRubyObject[] args,Block block) {
//
//				return null;
//			}
//		});

		return new Escher(new CrimsonClass(base).newInstance(),source);
	}

	public IRubyObject getNativeObject() {
		return m_crimson.getNativeObject();
	}

	public void defineSpecialMethod(final String name,final Callback callback) {
		m_crimson.getMetaClass().defineMethod(name,callback);
	}

	public void defineMethod(final String name,final Callback callback) {
		m_methods.add(name);
		m_crimson.getMetaClass().defineMethod(name,callback);
	}

	public void defineField(final String name,final IRubyObject value) {
		m_fields.put(name,value);
		m_crimson.getMetaClass().defineMethod(name,new Callback() {
			@Override
			public Arity getArity() {
				return Arity.NO_ARGUMENTS;
			}
			@Override
			public IRubyObject execute(IRubyObject recv,IRubyObject[] args,Block block) {
				return m_fields.get(name);
			}
		});
		m_crimson.getMetaClass().defineMethod(name + "=",new Callback() {
			@Override
			public Arity getArity() {
				return Arity.ONE_REQUIRED;
			}
			@Override
			public IRubyObject execute(IRubyObject recv,IRubyObject[] args,Block block) {
				return m_fields.put(name,args[0]);
			}
		});
	}

	public Map<String,IRubyObject> getFields() {
		return new HashMap<String,IRubyObject>(m_fields);
	}

	public Scriptable getSource() {
		return m_source;
	}
}
