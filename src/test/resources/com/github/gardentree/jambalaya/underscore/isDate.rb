require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
jambalaya = Jambalaya.new
_ = jambalaya.squeeze("underscore.js","this._")

_.isDate(jambalaya.fry("new Date();"))