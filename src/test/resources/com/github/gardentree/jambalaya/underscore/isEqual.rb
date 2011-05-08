require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')

jambalaya = Jambalaya.new
_ = jambalaya.mix("underscore.js","this._",[])

moe   = jambalaya.evaluate("(function(){ return {name: 'moe',luckyNumbers: [13,27,34]}})()")
clone = jambalaya.evaluate("(function(){ return {name: 'moe',luckyNumbers: [13,27,34]}})()")

_.isEqual(moe,clone)