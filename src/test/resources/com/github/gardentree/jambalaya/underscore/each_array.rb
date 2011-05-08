require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.squeeze("underscore.js","this._",[])

actual = []
_.each([1,2,3]){|number,index,object|
  actual << number
}
actual