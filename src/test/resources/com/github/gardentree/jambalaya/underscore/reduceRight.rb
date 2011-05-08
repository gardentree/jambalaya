require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.squeeze("underscore.js","this._",[])

_.reduceRight([[0,1],[2,3],[4,5]],lambda {|a,b,index,context|
  a.concat(b)
},[]);
