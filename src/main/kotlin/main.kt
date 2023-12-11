import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.File

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    val ffmpegFile = File(System.getProperty("compose.application.resources.dir")).resolve("remixicon_v3.7.0.json.html")

    println(ffmpegFile.path)

    MaterialTheme {
        Button(
            onClick = { text = "Hello, Desktop!" },
            modifier = Modifier.testTag("button")
        ) {
            Text(text)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
