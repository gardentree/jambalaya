require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.new.squeeze("underscore.js","this._",[])

result = []
result << _[[1,2,3,200]].chain().
  select(lambda {|number,index,context|number % 2 == 0}).
  tap(){|value|result << value}.
  map(lambda {|number,index,context| number * number}).
  value()
