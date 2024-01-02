package site.starsone.drawablegenerator.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.awt.Desktop
import java.net.URI

@Composable
fun AboutPage() {
    //读取版本信息
    val versionInfo = AppInfoUtil.versionInfo

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource("logo.png"), null, modifier = Modifier.size(50.dp))
        Text(versionInfo.appName + versionInfo.version)
        Spacer(Modifier.height(8.dp))
        Text(versionInfo.desc)

        Column(Modifier.fillMaxWidth().padding(16.dp)) {

            InfoText("作者", versionInfo.author)
            InfoText("我的博客", versionInfo.blogUrl,true)
            InfoText("软件开源地址", versionInfo.githubUrl,true)
        }

    }
}

@Composable
fun InfoText(title: String, content: String,isClick:Boolean=false) {
    Row {
        Text(title, fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
        Spacer(Modifier.width(16.dp))
        if (isClick) {
            ClickText(content){
                Desktop.getDesktop().browse(URI(content))
            }
        } else {
            Text(content)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClickText(content:String,onclick:()->Unit) {
    Text(content, color = Color.Blue, modifier = Modifier.onClick {
        onclick.invoke()
    }.pointerHoverIcon(PointerIcon.Hand,false))
}