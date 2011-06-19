require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')

jambalaya = Jambalaya.new
_ = jambalaya.squeeze("underscore.js","this._")

jambalaya.fry("(function(){ return _.toArray(arguments).slice(0); })(1,2,3)")