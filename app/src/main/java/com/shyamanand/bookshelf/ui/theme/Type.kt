package com.shyamanand.bookshelf.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.shyamanand.bookshelf.R

val BebasNeue = FontFamily(
    Font(R.font.bebasneue_regular)
)

val NanumGothic = FontFamily(
    Font(R.font.nanumgothic_regular),
    Font(R.font.nanumgothic_bold, FontWeight.W700),
    Font(R.font.nanumgothic_extrabold, FontWeight.W800)
)

// Set of Material typography styles to start with
val BookshelfTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W600,
        fontSize = 30.sp
    ),
    displayMedium = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp
    ),
    displaySmall = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = NanumGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = NanumGothic,
        fontWeight = FontWeight.W700,
        fontSize = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp
    )
)