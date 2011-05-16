require 'java'
java_import 'com.github.gardentree.jambalaya.Jambalaya'

jambalaya = Jambalaya.new
jambalaya.top[:document] = Object.new

class Location
  def initialize
    @hash = '#123,456,789'
  end
end
jambalaya.top[:location] = Location.new

target = jambalaya.squeeze("target/test-classes/com/github/gardentree/jambalaya/colors/violet/map.js","wand",[])
target.getDataFromHash.to_hash
