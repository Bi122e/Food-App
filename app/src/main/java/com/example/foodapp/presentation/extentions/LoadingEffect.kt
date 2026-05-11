package com.example.foodapp.presentation.extensions

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun Modifier.shimmerEffect(): Modifier = composed {

    val transition = rememberInfiniteTransition(label = "")

    val shimmerTranslate = transition.animateFloat(
        initialValue = -300f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val baseColor = MaterialTheme.colorScheme.surfaceVariant
    val highlightColor = MaterialTheme.colorScheme.surface

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                baseColor,
                highlightColor.copy(alpha = 0.8f),
                baseColor
            ),
            start = Offset(shimmerTranslate.value, 0f),
            end = Offset(shimmerTranslate.value + 300f, 0f)
        )
    )
}

@Composable
fun theme() {
    lightColorScheme(
        primary = Color.Black
    )
}


class GreenRaceShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val path = Path().apply {

            val w = size.width
            val h = size.height

            moveTo(0f, h)

            // góc trái dưới
            lineTo(0f, 40f)

            // bo góc trái trên
            quadraticTo(
                0f, 0f,
                40f, 0f
            )

            // cạnh trên
            lineTo(w - 70f, 0f)

            // cong phải trên
            cubicTo(
                w - 20f, 0f,
                w - 40f, h * 0.6f,
                w - 70f, h
            )

            // cạnh dưới
            lineTo(0f, h)

            close()
        }

        return Outline.Generic(path)
    }
}

 @Composable
fun Modifier.pulseSkeleton(
//    lightColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
//    lightColor: Color = MaterialTheme.colorScheme.outlineVariant,
     lightColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 1f),
//    darkColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
//    darkColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    darkColor: Color =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    ,
    duration: Int = 1500
): Modifier = composed { //modifier co state/animation -> composed
//    val colorLight = MaterialTheme.colorScheme.surfaceVariant
//    val colorDark  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)

    val color by rememberInfiniteTransition(label = "pulse").animateColor(
        initialValue = lightColor,
        targetValue = darkColor,
        animationSpec = infiniteRepeatable(
//            animation = keyframes {
//                durationMillis = 5000 // Tăng tổng chu kỳ lên 5s để mọi thứ diễn ra khoan thai
//
//                val smoothEasing = EaseInOutSine // Đường cong hình sin là tự nhiên nhất cho nhịp thở
//
////                // NGHỈ TẠI SÁNG (0ms - 500ms)
////                // Giữ màu sáng trong 0.5s để tạo điểm neo lúc reset
////                lightColor at 500 with smoothEasing
////
////                // CHUYỂN TỪ SÁNG SANG TỐI (500ms - 2500ms)
////                //  2 giây để chuyển màu
////                darkColor at 2500 with smoothEasing
////
////                // NGHỈ TẠI TỐI (2500ms - 3000ms)
////                // Nghỉ 0.5s tại điểm tối
////                darkColor at 3000 with smoothEasing
////
////                //CHUYỂN TỪ TỐI VỀ SÁNG (3000ms - 5000ms)
////                // 2 giây nữa để quay về màu sáng
////                lightColor at duration with smoothEasing
//                //   SÁNG (0ms - 10%)
//                lightColor at (duration * 0.1f).toInt() with smoothEasing
//
//                // CHUYỂN TỪ SÁNG SANG TỐI (10% - 50%)
//                darkColor at (duration * 0.5f).toInt() with smoothEasing
//
//                //  TỐI (50% - 60%) ---
//                darkColor at (duration * 0.6f).toInt() with smoothEasing
//
//                // CHUYỂN TỪ TỐI VỀ SÁNG (60% - 100%)
//                lightColor at duration with smoothEasing
//            },

//            repeatMode = RepeatMode.Restart
            animation = tween(
                durationMillis = duration,
                // Đường cong EaseInOutQuart: Cực kỳ bẹt ở hai đầu 0 và 1
                // Nó sẽ dừng rất lâu ở lightColor rồi mới bắt đầu biến đổi,
                // và dừng rất lâu ở darkColor trước khi quay đầu.
//                easing = CubicBezierEasing(0.77f, 0f, 0.175f, 1f)
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse // Đảo chiều mượt mà, không reset giật c
        ),
        label = "skeletonColor"
    )

    background(color) //lấy màu nền hiện tại và vẽ lên, -> bg vẽ lại (recom) liên tục
}

@Composable
fun Modifier.shimmerSkeleton(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateX = transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "translateX"
    )

    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.surfaceVariant,
    )

    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateX.value - 300f, 0f),
            end = Offset(translateX.value + 300f, 0f)
        )
    )
}

@Composable
fun CustomTheme1(
    content: @Composable () -> Unit
) {
    val lightColors = lightColorScheme(
        primary = Color(0xFF4CAF50),
        background = Color.White,
        surface = Color.White,
        onSurface = Color.Black
    )

    val darkColors = darkColorScheme(
        primary = Color(0xFF81C784),
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        onSurface = Color.White
    )

    val darkTheme = isSystemInDarkTheme()// lay theme cua system android

    val colors =
        if (darkTheme) {
            darkColors
        } else {
            lightColors
        }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
