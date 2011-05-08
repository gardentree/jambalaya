require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
jambalaya = Jambalaya.new
_ = jambalaya.mix("underscore.js","this._",[])

_.isUndefined(jambalaya.evaluate("this.jambalaya"))