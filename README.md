#JavaScriptをRubyの中に

##目的
JavaScriptプログラム(ブラウザ等の実行環境に依存しない処理)のテストする事です。
実行環境依存の処理については、Mockを作成する事によって回避可能です。
まずは、TitaniumMobileアプリケーションのテストを可能にする事を目指します。

##underscore_spec.rbについて
本アプリケーションを作成する為の、土台にしました。
JRuby & RSpecのインストールの後、以下のコマンドで確認出来ます。

<pre>
set CLASSPATH=distribute\jambalaya-0.1.jar
jruby -S rspec underscore_spec.rb
</pre>

現在の結果は、以下の通りです
*85 examples, 0 failures, 4 pending