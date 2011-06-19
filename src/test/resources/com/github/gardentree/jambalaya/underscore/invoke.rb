require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.new.squeeze("underscore.js","this._")

_.invoke([[5,1,7],[3,2,1]],'sort')
