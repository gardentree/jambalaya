require 'java'
java_import 'com.github.gardentree.jambalaya.Jambalaya'

jambalaya = Jambalaya.new

class Box
  def initialize
    @string = 'abcde'
  end
end

jambalaya.top[:box] = Box.new

script = "target/test-classes/com/github/gardentree/jambalaya/colors/violet/sync.js"
target = jambalaya.squeeze(script,"exports")
target.first