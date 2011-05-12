require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.new.squeeze("underscore.js","this._",[])

actual = []
_.each({'one' => 1,'two' => 2,'three' => 3}){|number,key,content|
  actual << number
}
actual
