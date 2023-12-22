package site.starsone.drawablegenerator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import site.starsone.drawablegenerator.util.toColor
import site.starsone.drawablegenerator.util.toHexString

@Composable
fun TestColorPicker() {
    var selectColor by remember { mutableStateOf(Color.Black) }

    //ColorPicker(selectColor, onColorChanged = {
    //    selectColor = it
    //})
}

@Composable
fun ColorPicker(color: Color, onColorChanged: (Color) -> Unit,onConfirm:(Boolean)->Unit) {
    var selectColor by remember { mutableStateOf(HsvColor.from(color)) }

    var selectColorHex by remember { mutableStateOf(color.toHexString()) }

    Column(modifier = Modifier.wrapContentSize().background(Color.White, shape = RoundedCornerShape(16.dp)).padding(16.dp)) {
        ClassicColorPicker(modifier = Modifier.size(200.dp, 100.dp), color = selectColor, onColorChanged = {
            selectColor = it
            val composeColor = it.toColor()
            onColorChanged.invoke(composeColor)
            selectColorHex = composeColor.toHexString()
        })

        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.size(200.dp, 100.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(50.dp, 50.dp).background(selectColor.toColor()))
            Spacer(Modifier.width(16.dp))
            OutlinedTextField(selectColorHex, onValueChange = {
                selectColorHex = it
                //这里考虑用户输错了颜色色值,可能会转换失败而出现异常
                val result = runCatching {
                    it.toColor()
                }
                result.getOrNull()?.apply{
                    selectColor = HsvColor.from(this)
                }

            }, placeholder = {Text("十六进制颜色代码(RGBA)")})
        }

        Button(onClick = {onConfirm.invoke(false)}){
            Text("确定")
        }
    }

}