require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.new.squeeze("underscore.js","this._",[])

function = lambda {|greeting|greeting + ': ' + self.name}
function = _.bind(function,{'name' => 'moe'},'hi')
function[]