package com.sotiemgiat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sotiemgiat.ui.SoGiatApp
import com.sotiemgiat.ui.theme.SoGiatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoGiatTheme {
                SoGiatApp()
            }
        }
    }
}
