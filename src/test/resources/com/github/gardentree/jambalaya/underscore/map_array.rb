require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.squeeze("underscore.js","this._",[])

_.map([2,3,4]){|number,index,object|
  number * 2
}