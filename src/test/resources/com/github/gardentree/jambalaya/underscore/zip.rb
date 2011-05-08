require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.squeeze("underscore.js","this._",[])

_.zip(['moe','larry','curly'],[30,40,50],[true,false,false])
