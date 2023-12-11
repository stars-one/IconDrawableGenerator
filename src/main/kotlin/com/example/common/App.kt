package com.example.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.common.toast.MyToastLayout

@Composable
fun App() {
    var selectIndex by remember { mutableStateOf(0) }

    val scope = rememberCoroutineScope()

    var showTip by remember { mutableStateOf(false) }
    var tipText by remember { mutableStateOf("") }


    MaterialTheme() {
        MyToastLayout(showTip, tipText, icon = Icons.Default.Face) {

            Row {
                LeftMenuNav { selectIndex = it }

                Surface() {

                    when (selectIndex) {
                        0 -> PageEnvInstall()
                    }
                }
            }
        }
    }
}





