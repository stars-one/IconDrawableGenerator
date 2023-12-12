package com.example.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.common.toast.ComposeToastContainer

@Composable
fun App() {
    var selectIndex by remember { mutableStateOf(0) }

    MaterialTheme() {
        ComposeToastContainer  {

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





