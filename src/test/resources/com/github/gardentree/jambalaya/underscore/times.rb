require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.new.squeeze("underscore.js","this._",[])

count = 0
_[3].times {
  count += + 1
}
count
