# NEJ support for Intellij IDEA ultimate/ WebStorm

## 如何使用

首先找到定义了define.js的html文件，如![](/doc/images/find_definejs.jpg)

其中的代码行
`<script src="/src/lib/nej/src/define.js?pro=/src/module/&3rd=/src/lib/&rot=/src/"></script>`
表明我们需要配置pro、3rd、rot这几个参数(具体哪些根据自己的定义)。
打开`Preserence`,进入`Other Settings -> NEJ setting`. 将这里的参数配置进去。值得注意的是，大多数情况下，不能直接将define.js后面的路径直接拷贝到配置中，这是因为NEJ还依赖于release.conf里面的配置文件进行静态文件路径配置。这里，插件统一以当前工程的路径为根路径。如在我的工程中，src文件的路径为./public/src/，则最终的配置如下所示:
![](/doc/images/nej_setting.jpg)。
### 重要
如果define.js后面的参数没有配置lib, 则还得在nej中配置该路径，否则无法解析base/element这类nej的部件，该路径指向nej库下的src。如上图所示。
之后我们就能使用了路径跳转的功能,如下:
![](http://g.recordit.co/k1fKfCTLmV.gif)

## 路径自动补全

该插件提供了针对define的自动路径补全功能。如下图所示:
![](/doc/images/filePathAutoComplete.gif)
