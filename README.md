#JavaScriptをRubyの中に
Ruby上でJavaScriptを動作させます。

##使用例：[RSpec](http://relishapp.com/rspec)で[Underscore.js](http://documentcloud.github.com/underscore/)をテストする。
<pre>
var stooges = [{name : 'curly', age : 25}, {name : 'moe', age : 21}, {name : 'larry', age : 23}];
var youngest = _(stooges).chain()
  .sortBy(function(stooge){ return stooge.age; })
  .map(function(stooge){ return stooge.name + ' is ' + stooge.age; })
  .first()
  .value();
=> "moe is 21"
</pre>
上記の様にJavaScriptで書かれた使用例が、以下の様にRSpecでテスト出来ます。
<pre>
describe 'chain' do
  subject {
    stooges = [{'name' => 'curly','age' => 25},{'name' => 'moe','age' => 21},{'name' => 'larry','age'=> 23}];
    @_[stooges].chain.
      sortBy{|stooge,index,context|stooge.age}.
      map{|stooge,index,context|stooge.name + ' is ' + stooge.age.to_s}.
      first.
      value
  }
  it {should == "moe is 21"}
end
</pre>
詳細は[underscore_spec.rb](/gardentree/jambalaya/blob/master/underscore_spec.rb)を参照して下さい。
(現状の結果:85 examples, 0 failures, 4 pending)

また、以下のコマンドでテストが実行出来ます。
(要JRubyのインストール)

<pre>
jruby --1.9 -S gem install jambalaya-rspec

jruby -S rspec underscore_spec.rb
</pre>
