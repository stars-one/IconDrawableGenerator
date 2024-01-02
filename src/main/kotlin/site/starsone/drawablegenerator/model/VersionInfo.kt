package site.starsone.drawablegenerator.model

data class VersionInfo(
    val appName: String, // Android
    val author: String, // stars-one
    val blogUrl: String, // www.cnblogs.com/stars-one
    val desc: String, // 用于生成android开发可用的xml矢量图标文件
    val githubUrl: String, // https://github.com/Stars-One/NovelDownloader-Kotlin
    val icon: String, // img/icon.png
    val qq: String, // 1053894518
    val version: String, // 1.0.1
    val versionCode: Int // 1
){
    constructor() : this("","","","","","","","",0)
}