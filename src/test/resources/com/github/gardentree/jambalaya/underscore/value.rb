require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.new.squeeze("underscore.js","this._",[])

_[[1,2,3]].value()
