package site.starsone.drawablegenerator.page

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.isCtrlPressed
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import site.starsone.drawablegenerator.view.DropBoxPanel

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun SvgToDrawablePage() {
    val txt by remember { mutableStateOf("") }

    Column {
        Box(Modifier.size(200.dp).background(Color.LightGray)){

        }

        Box {
            Text("hello")
        }
    }



    //DropBoxPanel(Modifier.size(200.dp).background(Color.LightGray))
}