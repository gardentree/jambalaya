# coding: utf-8

################################################################################
#
# see also http://documentcloud.github.com/underscore/
#
################################################################################

require 'jambalaya-rspec'

describe 'Underscore.js' do
  before do
    @jambalaya = Jambalaya.new
    @_ = @jambalaya.mix("underscore.js","this._",["require 'rubygems'","require 'rspec'"])
  end

  describe 'Collection Functions (Arrays or Objects)' do
    describe 'each' do
      context 'array' do
        subject {
          actual = []
          @_.each([1,2,3]){|number,key,context|actual << number}
          actual
        }
        it {should == [1,2,3]}
      end
      context 'hash' do
        subject {
          actual = []
          @_.each({'one' => 1,'two' => 2,'three' => 3}){|number,key,context|actual << number}
          actual
        }
        it {should == [1,2,3]}
      end
    end
    describe 'map' do
      context 'array' do
        subject {@_.map([1,2,3]){|number,key,object|number * 3}}
        it {should == [3,6,9]}
      end
      context 'hash' do
        subject {@_.map({'one' => 1,'two' => 2,'three' => 3}){|number,key,object|number * 3}}
        it {should == [3,6,9]}
      end
    end
    describe 'reduce' do
      subject {@_.reduce([1,2,3],lambda {|memo,number,index,context|memo + number},0)}
      it {should == 6}
    end
    describe 'reduceRight' do
      subject {
        list = [[0,1],[2,3],[4,5]]
        @_.reduceRight(list,lambda {|a,b,index,context|a.concat(b)},[])
      }
      it {should == [4,5,2,3,0,1]}
    end
    describe 'detect' do
      subject {@_.detect([1,2,3,4,5,6]){|number,index,context|number % 2 == 0}}
      it {should == 2}
    end
    describe 'select' do
      subject {@_.select([1,2,3,4,5,6]){|number,index,context|number % 2 == 0}}
      it {should == [2,4,6]}
    end
    describe 'reject' do
      subject {@_.reject([1,2,3,4,5,6]){|number,index,context|number % 2 == 0}}
      it {should == [1,3,5]}
    end
    describe 'all' do
      subject {@_.all([true,1,nil,'yes'],@_.method(:identity))}
      it {should be_false}
    end
    describe 'include' do
      subject {@_.include([1,2,3],3)}
      it {should be_true}
    end
    describe 'invoke' do
      subject {@_.invoke([[5,1,7],[3,2,1]],'sort')}
      it {should == [[1,5,7],[1,2,3]]}
    end
    describe 'pluck' do
      subject {
        stooges = [{'name' => 'moe','age' => 40},{'name' => 'larry','age' => 50},{'name' => 'curly','age' => 60}]
        @_.pluck(stooges,'name')
      }
      it {should == ["moe","larry","curly"]}
    end
    describe 'max' do
      subject {
        stooges = [{'name' => 'moe','age' => 40},{'name' => 'larry','age' => 50},{'name' => 'curly','age' => 60}]
        @_.max(stooges) {|stooge,index,context|
          stooge['age']
        }.to_hash
      }
      it {should == {'name' => 'curly','age' => 60}}
    end
    describe 'min' do
      subject {
        numbers = [10,5,100,2,1000]
        @_.min(numbers)
      }
      it {should == 2}
    end
    describe 'sortBy' do
      subject {@_.sortBy([1,2,3,4,5,6]) {|number,index,context|Math.sin(number)}}
      it {should == [5,4,6,3,1,2]}
    end
    describe 'sortedIndex' do
      subject {@_.sortedIndex([10,20,30,40,50],35)}
      it {should == 3}
    end
    describe 'toArray' do
      subject {@jambalaya.evaluate("(function(){ return _.toArray(arguments).slice(0); })(1, 2, 3)")}
      it {should == [1.0,2.0,3.0]}
    end
    describe 'size' do
      subject {@_.size({'one' => 1,'two' => 2,'three' => 3})}
      it {should == 3}
    end
  end

  describe 'Array Functions' do
    describe 'first' do
      subject {@_.first([5,4,3,2,1])}
      it {should == 5}
    end
    describe 'rest' do
      subject {@_.rest([5,4,3,2,1])}
      it {should == [4,3,2,1]}
    end
    describe 'last' do
      subject {@_.last([5,4,3,2,1])}
      it {should == 1}
    end
    describe 'compact' do
      subject {@_.compact([0,1,false,2,'',3])}
      it {should == [1,2,3]}
    end
    describe 'flatten' do
      subject {@_.flatten([1,[2],[3,[[[4]]]]])}
      it {should == [1,2,3,4]}
    end
    describe 'without' do
      subject {@_.without([1,2,1,0,3,1,4],0,1)}
      it {should == [2,3,4]}
    end
    describe 'uniq' do
      subject {@_.uniq([1,2,1,3,1,4])}
      it {should == [1,2,3,4]}
    end
    describe 'intersect' do
      subject {@_.intersect([1,2,3],[101,2,1,10],[2,1])}
      it {should == [1,2]}
    end
    describe 'zip' do
      subject {@_.zip(['moe','larry','curly'],[30,40,50])}  #subject {@_.zip(['moe','larry','curly'],[30,40,50],[true,false,false])}
      it {should == [["moe",30],["larry",40],["curly",50]]} #it {should == [["moe",30,true],["larry",40,false],["curly",50,false]]}
    end
    describe 'indexOf' do
      subject {@_.indexOf([1,2,3],2)}
      it {should == 1}
    end
    describe 'lastIndexOf' do
      subject {@_.lastIndexOf([1,2,3,1,2,3],2)}
      it {should == 4}
    end
    describe 'range' do
      context do
        subject {@_.range(10)}
        it {should == [0,1,2,3,4,5,6,7,8,9]}
      end
      context do
        subject {@_.range(1,11)}
        it {should == [1,2,3,4,5,6,7,8,9,10]}
      end
      context do
        subject {@_.range(0,30,5)}
        it {should == [0,5,10,15,20,25]}
      end
      context do
        subject {@_.range(0,-10,-1)}
        it {should == [0,-1,-2,-3,-4,-5,-6,-7,-8,-9]}
      end
      context do
        subject {@_.range(0)}
        it {should == []}
      end
    end
  end

  describe 'Function (uh, ahem) Functions' do
    describe 'bind' do
      subject {
        function = lambda {|greeting|greeting + ': ' + self.name}
        function = @_.bind(function,{'name' => 'moe'},'hi')
        function[]
      }
      it {should == "hi: moe"}
    end
    describe 'bindAll' do
      subject {
        buttonView = {
          'label'   => 'underscore',
          'onClick' => lambda { 'clicked: ' + label },
          'onHover' => lambda { 'hovering: ' + label }
        }

        buttonView = @_.bindAll(buttonView) #@_.bindAll(buttonView)

        "#{buttonView.onClick}&#{buttonView.onHover}"
      }
      it {should == "clicked: underscore&hovering: underscore"}
    end
    describe 'memoize' do
      subject {
        fibonacci = lambda{|n|n < 2 ? n:fibonacci[n - 1] + fibonacci[n - 2]}
        fastFibonacci = @_.memoize(fibonacci)
        fastFibonacci[7]
      }
      it {should == 13}
    end
    describe 'delay' do
      subject {
        @jambalaya.evaluate("function setTimeout(callback,wait) {callback()}")

        flag = false
        @_.delay(lambda {flag = true}, 50, 'logged later');

        flag
      }
      it {should be_true}
    end
    describe 'defer' do
      subject {
        @jambalaya.evaluate("function setTimeout(callback,wait) {callback()}")

        flag = false
        @_.defer {flag = true}

        flag
      }
      it {should be_true}
    end
    describe 'throttle' do
      subject {
        @jambalaya.evaluate("function setTimeout(callback,wait) {callback()}")

        flag = false
        throttled = @_.throttle(lambda {flag = true},100);
        throttled[]

        flag
      }
      it {should be_true}
    end
    describe 'debounce' do
      subject {
        @jambalaya.evaluate("function setTimeout(callback,wait) {callback()}")
        @jambalaya.evaluate("function clearTimeout(wait) { /* ??? */ }")

        flag = false
        debounce = @_.debounce(lambda {flag = true},100);
        debounce[]

        flag
      }
      it {should be_true}
    end
    describe 'once' do
      subject {
        count = 0
        counter = @_.once {count += 1}
        counter[]
        counter[]
        counter[]
        count
      }
      it {should == 1}
    end
    describe 'after' do
      subject {
        notes = ['one','two','three']

        result = []
        renderNotes = @_.after(notes.length) {result << 'four'}
        @_.each(notes) {|note,index,context|
          result << note
          renderNotes[]
        }
        result
      }
      it {should == ['one','two','three','four']}
    end
    describe 'wrap' do
      subject {
        hello = lambda {|name| "hello: " + name }
        hello = @_.wrap(hello) {|callback|
          "before, " + callback["moe"] + ", after"
        }
        hello[]
      }
      it {should == 'before, hello: moe, after'}
    end
    describe 'compose' do
      subject {
        greet    = lambda {|name| "hi: " + name }
        exclaim  = lambda {|statement| statement + "!" }
        welcome = @_.compose(exclaim, greet);
        welcome['moe'];
      }
      it {should == 'hi: moe!'}
    end
  end

  describe 'Object Functions' do
    describe 'keys' do
      subject {@_.keys({'one' => 1,'two' => 2,'three' => 3})}
      it {should == ["one","two","three"]}
    end
    describe 'values' do
      subject {@_.values({'one' => 1,'two' => 2,'three' => 3})}
      it {should == [1,2,3]}
    end
    describe 'functions' do
      subject {@_.functions(@_)}
      it {should include("each","first","bind","keys","noConflict","[]","VERSION")}
    end
    describe 'extend' do
      subject {@_.extend({'name' => 'moe'},{'age' => 50}).to_hash}
      it {should == {'name' => 'moe','age' => 50}}
    end
    describe 'defaults' do
      subject {
        iceCream = {'flavor' => "chocolate"}
        @_.defaults(iceCream,{'flavor' => "vanilla",'sprinkles' => "lots"}).to_hash
      }
      it {should == {'flavor' => "chocolate",'sprinkles' => "lots"}}
    end
    describe 'clone' do
      subject {@_.clone({'name' => 'moe'}).to_hash}
      it {should == {'name' => 'moe'}}
    end
    describe 'tap' do
      subject {
        result = []
        result << @_[[1,2,3,200]].chain.
          select{|number,index,context|number % 2 == 0}.
          tap{|value|result << value}.
          map{|number,index,context| number * number}.
          value
        }
      it {should == [[2,200],[4,40000]]}
    end
    describe 'isEqual' do
      before do
        @moe   = @jambalaya.evaluate("(function(){ return {name: 'moe',luckyNumbers: [13,27,34]}})()")
        @clone = @jambalaya.evaluate("(function(){ return {name: 'moe',luckyNumbers: [13,27,34]}})()")
      end
      context 'native' do
        subject {@moe == @clone}
        it {should be_false}
      end
      context 'underscore' do
        subject {@_.isEqual(@moe,@clone)}
        it {pending 'Equal!'
          should be_true
        }
      end
    end
    describe 'isEmpty' do
      context do
        subject {@_.isEmpty([1,2,3])}
        it {should be_false}
      end
      context do
        subject {@_.isEmpty({})}
        it {should be_true}
      end
    end
    describe 'isElement' do
      subject {
        element = @jambalaya.evaluate("(function(){ return {nodeType: 1}; })()")
        @_.isElement(element)
      }
      it {pending 'Element?'
        should be_true
      }
    end
    describe 'isArray' do
      context do
        subject {@jambalaya.evaluate("(function(){ return _.isArray(arguments); })();")}
        it {should be_false}
      end
      context do
        subject {@_.isArray([1,2,3])}
        it {should be_true}
      end
    end
    describe 'isArguments' do
      context do
        subject {@jambalaya.evaluate("(function(){return _.isArguments(arguments);})(1,2,3);")}
        it {should be_true}
      end
      context do
        subject {@_.isArguments([1,2,3])}
        it {should be_false}
      end
    end
    describe 'isFunction' do
      subject {
        def function
        end
        @_.isFunction(method(:function))
      }
      it {should be_true}
    end
    describe 'isString' do
      subject {@_.isString("moe")}
      it {should be_true}
    end
    describe 'isNumber' do
      subject {@_.isNumber(8.4 * 5)}
      it {should be_true}
    end
    describe 'isBoolean' do
      subject {@_.isBoolean(nil)}
      it {should be_false}
    end
    describe 'isDate' do
      subject {@_.isDate(@jambalaya.evaluate("new Date();"))}
      it {
        should be_true
      }
    end
    describe 'isRegExp' do
      subject {@_.isRegExp(@jambalaya.evaluate("/moe/;"))}
      it {
        should be_true
      }
    end
    describe 'isNaN' do
      context do
        subject {@_.isNaN(0.0/0)}
        it {should be_true}
      end
      context do
        subject {@jambalaya.evaluate("isNaN(undefined);")}
        it {should be_true}
      end
      context do
        subject {@_.isNaN(@jambalaya.evaluate("undefined"))}
        it {should be_false}
      end
    end
    describe 'isNull' do
      context do
        subject {@_.isNull(nil)}
        it {should be_true}
      end
      context do
        subject {@_.isNull(@jambalaya.evaluate("undefined"))}
        it {should be_false}
      end
    end
    describe 'isUndefined' do
      subject {@_.isUndefined(@jambalaya.evaluate("this.jambalaya"))}
      it {pending 'Undefined?'
        should be_true
      }
    end
  end

  describe 'Utility Functions' do
    describe 'noConflict' do
      subject {
        underscore = @_.noConflict()
        underscore.min([3,2,1])
      }
      it {should == 1}
    end
    describe 'identity' do
      subject {
        moe = {'name' => 'moe'};
        moe === @_.identity(moe).to_hash;
      }
      it {should be_true}
    end
    describe 'times' do
      subject {
        result = []
        @_[3].times {result << 1}
        result
      }
      it {should == [1,1,1]}
    end
    describe 'mixin' do
      subject {
        @_.mixin({
          'capitalize' => lambda{|string|
            string[0,1].upcase + string[1,string.length].downcase
          }
        });
        @_["fabio"].capitalize();
      }
      it {should == "Fabio"}
    end
    describe 'uniqueId' do
      it {
        @_.uniqueId('contact_').should == 'contact_0'
        @_.uniqueId('contact_').should == 'contact_1'
      }
    end
    describe 'template' do
      context 'compiled' do
        subject {
          compiled = @_.template("hello: <%= name %>")
          compiled[{'name' => 'moe'}]
        }
        it {should == "hello: moe"}
      end
      context 'list' do
        subject {
          list = "<% _.each(people, function(name) { %><li><%= name %></li><% }); %>";
          @_.template(list,{'people' => ['moe','curly','larry']});
        }
        it {should == "<li>moe</li><li>curly</li><li>larry</li>"}
      end
      context 'print' do
        subject {
          compiled = @_.template("<% print('Hello ' + epithet); %>");
          compiled[{'epithet' => "stooge"}];
        }
        it {should == "Hello stooge"}
      end
      context 'setting' do
        subject {
          @_.templateSettings = {'interpolate' => /\{\{(.+?)\}\}/}

          template = @_.template("Hello {{ name }}!");
          template[{'name' => "Mustache"}];
        }
        it {pending "templateSettings="
          should == "Hello Mustache!"
        }
      end
    end
  end
  describe 'Chaining' do
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
    describe 'value' do
      subject {@_[[1,2,3]].value}
      it {should == [1,2,3]}
    end
  end
end