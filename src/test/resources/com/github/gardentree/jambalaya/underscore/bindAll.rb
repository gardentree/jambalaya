require 'java'
include_class('com.github.gardentree.jambalaya.Jambalaya')
_ = Jambalaya.squeeze("underscore.js","this._",[])

buttonView = {
  'label'   => 'underscore',
  'onClick' => lambda { 'clicked: ' + label },
  'onHover' => lambda { 'hovering: ' + label }
}
buttonView = _.bindAll(buttonView)

"#{buttonView.onClick}&#{buttonView.onHover}"