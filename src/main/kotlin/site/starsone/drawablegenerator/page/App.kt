package site.starsone.drawablegenerator.page

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import site.starsone.drawablegenerator.view.LeftMenuNav
import site.starsone.drawablegenerator.toast.ComposeToastContainer

@Composable
fun App() {
    var selectIndex by remember { mutableStateOf(1) }

    MaterialTheme() {
        ComposeToastContainer  {

            Row {
                LeftMenuNav { selectIndex = it }

                Surface() {
                    when (selectIndex) {
                        0 -> RemixIconPage()
                        1 -> SvgToDrawablePage()
                    }
                }

            }
        }
    }
}





