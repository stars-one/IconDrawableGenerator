package site.starsone.drawablegenerator.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import site.starsone.drawablegenerator.CacheUtil

@Composable
fun SettingPage() {
    var size by remember { mutableStateOf(CacheUtil.getCacheSize()) }

    Surface {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Button(onClick = {
                CacheUtil.cleanCache()
                size = CacheUtil.getCacheSize()
            }){
                Text("清理缓存")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(size)
        }
    }
}