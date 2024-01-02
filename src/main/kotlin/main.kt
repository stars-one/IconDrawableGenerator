import AppInfoUtil.versionInfo
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.google.gson.Gson
import site.starsone.drawablegenerator.model.VersionInfo
import site.starsone.drawablegenerator.page.App
import java.io.File


fun main() = application {

    val versionInfo = AppInfoUtil.versionInfo
    Window(
        icon = painterResource("logo.png"),
        onCloseRequest = ::exitApplication,
        //应用名称
        title = "${versionInfo.appName}${versionInfo.version} by ${versionInfo.author}",
        //设置宽高和居中
        state = rememberWindowState(size = DpSize(1200.dp, 800.dp), position = WindowPosition(Alignment.Center))
    ) {
        App()
    }
}

object AppInfoUtil{
    val versionInfo by lazy {
        //读取版本信息
        val json = this::class.java.getResource("/desc.json")?.readText()
        Gson().fromJson(json, VersionInfo::class.java)
    }
}
