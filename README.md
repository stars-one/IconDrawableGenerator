
# Android图标生成器

一款Android开发者的工具软件,可以快速生成android开发需要使用的xml矢量图标文件

## 起因

个人开发Android app过程中,经常会使用到一些矢量图标,经常重复性复制svg文件,然后使用Android Studio转换,但是Android Studio还不支持批量转换,且每次操作总感觉有些繁琐

恰巧找到了svg转换为xml的代码实例,于是就用上新版本的compose-multiplatform实现了个桌面端软件,也算试手一下

## 软件截图

![](https://img2024.cnblogs.com/blog/1210268/202401/1210268-20240103143833652-251587451.png)

![](https://img2024.cnblogs.com/blog/1210268/202401/1210268-20240103144235939-754101879.png)

![](https://img2024.cnblogs.com/blog/1210268/202401/1210268-20240103143845742-1738448581.png)

![](https://img2024.cnblogs.com/blog/1210268/202401/1210268-20240103143854082-725375603.png)

## 软件获取

见release页面,选择自己平台的对应安装包安装

> **PS:** window系统不要将安装到C盘,否则会出现打开失败问题!!

## 软件使用

目前是使用了RemixIcon作为图标库,支持搜索,选中某个图标后,可以导出png或者xml文件,这里我是直接将导出的文件直接复制到了剪切板,之后只需要到Android Studio,在指定drawable文件夹粘贴即可

除此之外,如果使用了是其他图标库,还可以直接复制svg代码或者将svg文件转为xml文件


## 项目说明

如果想要升级remixicon图标版本,需要修改下面的2个文件,并将代码中的RemixIconDataUtil里的jsonName改为下面的新版本的json名称即可(不需要common开头)

- `common/remixicon_v4.0.0.json`是从官网上下载的,为了规范,请以版本名结尾

![](https://img2023.cnblogs.com/blog/1210268/202312/1210268-20231218104014035-701783491.png)

- `remixicon_tags.json`是https://github.com/Remix-Design/RemixIcon/blob/master/tags.json的数据,也需要更新

# 致谢

- 图标来源[Remix-Design/RemixIcon: Open source neutral style icon system](https://github.com/Remix-Design/RemixIcon)
- Toast组件实现参考[GangJust/AdbHelper: Android开发者必备的Adb便携工具](https://github.com/GangJust/AdbHelper)
- svg转换代码参考[stars-one/svg2vector: svg文件转为android可用的xml矢量图标的工具库](https://github.com/stars-one/svg2vector)
- action打包脚本参考[SpotiFlyer/.github/workflows/build-release-binaries.yml at main · Shabinder/SpotiFlyer](https://github.com/Shabinder/SpotiFlyer/blob/main/.github/workflows/build-release-binaries.yml)

## Compose Multiplatform官方教程文档参考

- [Learn about other cases for using the Compose Multiplatform UI framework](https://github.com/JetBrains/compose-multiplatform#readme)
- [Complete more Compose Multiplatform tutorials](https://github.com/JetBrains/compose-multiplatform/blob/master/tutorials/README.md)
- [Explore more advanced Compose Multiplatform example projects](https://github.com/JetBrains/compose-multiplatform/blob/master/examples/README.md)
