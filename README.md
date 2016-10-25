开干开干

准备开发一个NEJ工程的Intellij插件。

## 期望运行平台

* WebStorm
* Intellij IDEA ultimate

## 期望功能

* 能够解析NEJ.define，解析pro、pool等参数(前提要求，一个工程中只能有一个引入NEJ文件), 使之能够go to definition 和 declartion(已实现)

* 解析路径，写NEJ.define是，之后的[]中出入路径能够自动提示路径(已实现)

* # 函数解析(先做这个吧)

* 因为使用了NEJ之后，在html中找到的所有class和id都失效了，这里需要更正

* 实现live template

* 实现find usage

* 实现find usage之后，应该就能够实现rename这个refactor了

* 实现event函数提示，这里所有的component实例都是new出来的，组件和工程代码的交互靠事件监听，所以我应该在组件中保存它所抛出的事件，然后在工程中提示

* NEJ库解析，帮助提示NEJ的方法（这个可能已经被实现了）

## 二期功能

实现Regular模板引擎

* 提供一个更改模板起始标识
* go to declaration实现
* 模板->implement
* implement->模板
* 组件名称自动补全
* 模板与js的整合,提供，this.click的跳转

## 三期功能

* 整合Regular模板引擎插件和NEJ插件，串联起来
* 整合NEI进来，工程规范->live template


所以首先是切图去，只有深入业务才能发现业务需要


## 另外
NEJ打包功能，增加文件名,方便查找
