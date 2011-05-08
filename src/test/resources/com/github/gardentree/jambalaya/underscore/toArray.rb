require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')

jambalaya = Jambalaya.new
_ = jambalaya.mix("underscore.js","this._",[])

jambalaya.evaluate("(function(){ return _.toArray(arguments).slice(0); })(1,2,3)")