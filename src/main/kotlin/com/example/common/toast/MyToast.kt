package com.example.common.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MyToast(isShow: Boolean, text: String, icon: ImageVector) {
    if (isShow) {
        val alig = Alignment.BottomCenter
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = alig) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xff011627))
                    .padding(8.dp)
            ) {
                Icon(icon, "", tint = Color.White)
                Spacer(Modifier.width(16.dp))
                Text(text, color = Color.White)
            }
        }
    }
}

@Composable
fun MyToastLayout(isShow: Boolean, text: String, icon: ImageVector, content: @Composable BoxScope.() -> Unit) {
    Box {
        Box(content = content)
        MyToast(isShow, text, icon)
    }
}

fun CoroutineScope.showToast(delayTime: Long = 1000, action: (Boolean) -> Unit) {
    launch {
        action.invoke(true)
        delay(1000)
        action.invoke(false)
    }
}

fun showToast(scope: CoroutineScope, action: (Boolean) -> Unit) {
    scope.launch {
        action.invoke(true)
        delay(1000)
        action.invoke(false)
    }
}