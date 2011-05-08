require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.squeeze("underscore.js","this._",[])

_.reduce([1,2,3],lambda {|memo,number,index,context|
  memo + number
},0);