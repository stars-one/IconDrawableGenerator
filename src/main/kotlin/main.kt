import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import site.starsone.drawablegenerator.page.App


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        //应用名称
        title = "Android图标生成器",
        //设置宽高和居中
        state = rememberWindowState(size = DpSize(1680.dp, 800.dp), position = WindowPosition(Alignment.Center))
    ) {
        App()
    }
}
