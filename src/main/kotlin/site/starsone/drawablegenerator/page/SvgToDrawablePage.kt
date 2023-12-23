package site.starsone.drawablegenerator.page

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import site.starsone.drawablegenerator.toast.ComposeToast
import site.starsone.drawablegenerator.util.CommonUtil

import site.starsone.drawablegenerator.view.DropBoxPanel
import site.starsone.drawablegenerator.view.MyCard

@Preview
@Composable
fun SvgToDrawablePage() {
    var txt by remember { mutableStateOf("") }

    Column(modifier = Modifier.width(450.dp).padding(16.dp)) {
        MyCard("svg代码转换") {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(txt, onValueChange = {
                txt = it

                runCatching {
                    val file = CommonUtil.svgToXml(it, 24)
                    CommonUtil.copyFiles(file)
                    ComposeToast.show("转换文件成功,已复制输出文件到剪切板,可以直接粘贴!")
                }
            },
                modifier = Modifier.fillMaxWidth().requiredHeight(300.dp),
                placeholder = {
                    Text("输入svg代码后自动转换")
                })
        }

        Spacer(modifier = Modifier.height(16.dp))

        MyCard("svg文件批量转换为xml") {
            Spacer(modifier = Modifier.height(8.dp))

            DropBoxPanel(Modifier.size(200.dp).border(1.dp, Color.Black)) {
                val files = it.map {
                    CommonUtil.svgToXml(it, 24)
                }
                CommonUtil.copyFiles(files)
                ComposeToast.show("转换文件成功,已复制输出文件到剪切板,可以直接粘贴!")
            }
        }
    }

}

