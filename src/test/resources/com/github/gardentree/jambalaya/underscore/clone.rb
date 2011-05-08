require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.squeeze("underscore.js","this._",[])

_.clone({'name' => 'moe'}).to_hash
