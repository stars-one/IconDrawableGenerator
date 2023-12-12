import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.common.App


fun main() = application {
    Window(onCloseRequest = ::exitApplication,
            title = "Android图标生成器",
        state = rememberWindowState(width = 1680.dp, height = 800.dp)
    ) {
        App()
    }
}
