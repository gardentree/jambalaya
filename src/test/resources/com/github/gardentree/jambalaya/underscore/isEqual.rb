require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')

jambalaya = Jambalaya.new
_ = jambalaya.squeeze("underscore.js","this._")

moe   = jambalaya.fry("(function(){ return {name: 'moe',luckyNumbers: [13,27,34]}})()")
clone = jambalaya.fry("(function(){ return {name: 'moe',luckyNumbers: [13,27,34]}})()")

_.isEqual(moe,clone)