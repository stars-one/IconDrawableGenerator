import org.jetbrains.compose.desktop.application.dsl.TargetFormat

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

    implementation ("com.github.stars-one:svg2vector:1.1")

    //implementation("com.squareup.okhttp3:okhttp:4.11.0")

}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "IconDrawableGenerator"
            packageVersion = "1.0.0"

            //开启快捷方式
            windows{
                this.upgradeUuid = "bcfd283d-d6d2-4a73-a4ee-d941572c3864"
                shortcut = true
            }

            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
        }
    }
}
