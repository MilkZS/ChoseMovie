# ChoseMovie

## App实现的功能介绍
1. App可以从网站themoviedb.org中读取电影的相关信息。
2. 应用打开后，能够以网格排列展示电影海报。
3. 使用户能够通过设置更改海报排列顺序。
     * 热门程度
     * 评分高低
4. 使用户能够点按海报转到详情页面，了解电影的更多信息。
     * 电影名称 
      * 电影海报  
      * 剧情简介 
      * 用户评分 
      * 发布日期  



## 关于 themoviedb.org Api_key
1. 使用者需要自己注册request一个themoviedb.org 的 api_key。
2. 将申请到的api_key放入代码中。
      * 路径为：app/src/main/java/com/example/android/chosemovie/data/BaseDataInfo.java 
      * 放置在变量myPrivateKey后。



## 注意
* 代码中除了主要部分代码，还有一部分多余代码是为接下来的项目进展做准备，并不影响程序的正常运行。使用者不需要过多的考虑其作用。
* 多余的内部类：PrevueMovieTask
