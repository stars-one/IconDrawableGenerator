import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.com.google.gson.Gson

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    // Include the Test API
    testImplementation(compose.desktop.uiTestJUnit4)

    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.material3)

    implementation("com.google.code.gson:gson:2.10.1")

    //svg转png
    implementation ("org.apache.xmlgraphics:batik-transcoder:1.14")
    implementation ("org.apache.xmlgraphics:batik-codec:1.14")
    implementation ("commons-codec:commons-codec:1.15")

    //svg转xml的库
    implementation ("org.dom4j:dom4j:2.1.1")
    implementation ("gov.nist.math:jama:1.0.3")

    implementation ("com.github.stars-one:svg2vector:1.2")

    //颜色选择 https://github.com/godaddy/compose-color-picker
    //implementation ("com.godaddy.android.colorpicker:compose-color-picker:0.7.0")
    implementation ("com.godaddy.android.colorpicker:compose-color-picker-jvm:0.7.0")

    //implementation("com.squareup.okhttp3:okhttp:4.11.0")

}

val descJson = """
    {
      "appName": "Android图标生成器",
      "desc":"用于生成android开发可用的xml矢量图标文件",
      "version": "1.0.2",
      "versionCode":1,
      "icon":"img/icon.png",
      "author": "stars-one",
      "githubUrl": "https://github.com/Stars-One/NovelDownloader-Kotlin",
      "blogUrl": "www.cnblogs.com/stars-one",
      "qq": "1053894518"
    }
""".trimIndent()

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
)

compose.desktop {
    val versionInfo = Gson().fromJson(descJson,VersionInfo::class.java)

    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = versionInfo.appName
            packageVersion = versionInfo.version


            version = "v${versionInfo.version}"
            description = versionInfo.desc
            copyright = "© 2023 ${versionInfo.author}. All rights reserved."

            windows{
                this.upgradeUuid = "ce843e03-537e-42c0-9c2e-492d231e6f23" //更新的uuid
                shortcut = true //开启快捷方式
                menuGroup = "start-menu-group" //将程序加入window菜单组
                dirChooser = true
            }

            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
        }
    }
}
