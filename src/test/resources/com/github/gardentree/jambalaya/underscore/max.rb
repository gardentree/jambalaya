require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.new.squeeze("underscore.js","this._",[])

stooges = [{'name' => 'moe','age' => 40},{'name' => 'larry','age' => 50},{'name' => 'curly','age' => 60}]
result = _.max(stooges) {|stooge,index,context|
  stooge.age
}.to_hash
