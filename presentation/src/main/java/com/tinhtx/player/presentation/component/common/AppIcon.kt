package com.tinhtx.player.presentation.component.common

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinhtx.player.presentation.theme.IconStyle
import com.tinhtx.player.presentation.theme.materialSymbolsOutlined
import com.tinhtx.player.presentation.theme.materialSymbolsRounded
import com.tinhtx.player.presentation.theme.materialSymbolsSharp

@Composable
fun AppIcon(
    iconName: String,
    modifier: Modifier = Modifier,
    style: IconStyle = IconStyle.Outlined,
    size: Dp = 24.dp,
    fill: Boolean = false,
    weight: Int = 400,
    grade: Int = 0,
    tint: Color = LocalContentColor.current
) {
    val fontFamily = when (style) {
        IconStyle.Outlined -> materialSymbolsOutlined
        IconStyle.Rounded -> materialSymbolsRounded
        IconStyle.Sharp -> materialSymbolsSharp
    }

    Text(
        text = iconName,
        modifier = modifier,
        color = tint,
        fontFamily = fontFamily,
        fontSize = size.value.sp
    )
}