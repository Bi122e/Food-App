
import android.graphics.BlurMaskFilter
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Date

fun Modifier.coloredShadow(
    colors: List<Color>,
    alpha: Float = 0.25f,
    borderRadius: Dp = 20.dp,
    blurRadius: Dp = 15.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp,
    direction: ShadowDirection = ShadowDirection.TopToBottom
) = this.drawBehind {

    drawIntoCanvas { canvas ->
        val componentPath = Path().apply {
            addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(borderRadius.toPx())
                )
            )
        }

        val spreadPx = spread.toPx()
        val left = 0f - spreadPx + offsetX.toPx()
        val top = 0f - spreadPx + offsetY.toPx()
        val right = size.width + spreadPx + offsetX.toPx()
        val bottom = size.height + spreadPx + offsetY.toPx()

        canvas.save()

        // Đục lỗ vùng dưới component để shadow không bị lem vào trong
        canvas.clipPath(componentPath, clipOp = ClipOp.Difference)

        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint().apply {
            style = android.graphics.Paint.Style.FILL
            isAntiAlias = true

            if (blurRadius.toPx() > 0f) {
                maskFilter = BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL)
            }
        }

        // TỰ ĐỘNG ĐỒNG BỘ: Tính toán lại Alpha cho từng màu
        // Nếu là màu tối (như Color.Black), ta giữ nguyên độ đậm của nó để không bị nền nuốt mất.
        // Nếu là màu sáng/rực rỡ, ta áp dụng alpha vừa phải để nó tỏa ra mịn màng.
        val nativeColors = colors.map { color ->
            // Tính toán độ sáng của màu (Luminance)
            val luminance = 0.2126f * color.red + 0.7152f * color.green + 0.0722f * color.blue

            // Nếu màu quá tối (luminance gần bằng 0), tăng alpha lên để nó hiển thị rõ trên nền tối
            val finalAlpha = if (luminance < 0.2f) {
                (alpha * 2f).coerceAtMost(0.8f)
            } else {
                alpha
            }
            color.copy(alpha = finalAlpha).toArgb()
        }.toIntArray()

        val finalColors = if (nativeColors.size == 1) intArrayOf(nativeColors[0], nativeColors[0]) else nativeColors

        val (startX, startY, endX, endY) = when (direction) {
            ShadowDirection.TopToBottom ->
                ModifierPoints(startX = (left + right) / 2, startY = top, endX = (left + right) / 2, endY = bottom)
            ShadowDirection.BottomToTop ->
                ModifierPoints(startX = (left + right) / 2, startY = bottom, endX = (left + right) / 2, endY = top)
            ShadowDirection.LeftToRight ->
                ModifierPoints(startX = left, startY = (top + bottom) / 2, endX = right, endY = (top + bottom) / 2)
            ShadowDirection.RightToLeft ->
                ModifierPoints(startX = right, startY = (top + bottom) / 2, endX = left, endY = (top + bottom) / 2)
            ShadowDirection.TopLeftToBottomRight ->
                ModifierPoints(startX = left, startY = top, endX = right, endY = bottom)
            ShadowDirection.TopRightToBottomLeft ->
                ModifierPoints(startX = right, startY = top, endX = left, endY = bottom)
        }

        frameworkPaint.shader = LinearGradient(
            startX, startY, endX, endY,
            finalColors,
            null,
            Shader.TileMode.CLAMP
        )

        canvas.drawRoundRect(
            left = left,
            top = top,
            right = right,
            bottom = bottom,
            radiusX = borderRadius.toPx(),
            radiusY = borderRadius.toPx(),
            paint = paint
        )

        canvas.restore()
    }
}

//lap anh
fun Modifier.tiledBackground(
    bitmap: ImageBitmap
) = drawBehind {

    val paint = Paint()

    paint.asFrameworkPaint().shader =
        ImageShader(
            bitmap,
            TileMode.Repeated,
            TileMode.Repeated
        )

    drawRect(
        brush = ShaderBrush(
            ImageShader(
                bitmap,
                TileMode.Repeated,
                TileMode.Repeated
            )
        )
    )
}

fun isSameDay(
    d1: Date?,
    d2: Date?
): Boolean {

    if (d1 == null || d2 == null) return false

    val c1 = Calendar.getInstance().apply {
        time = d1
    }
    val c2 = Calendar.getInstance().apply {
        time = d2
    }

    return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
            c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)

}
private data class ModifierPoints(val startX: Float, val startY: Float, val endX: Float, val endY: Float)

enum class ShadowDirection {
    TopToBottom, BottomToTop, LeftToRight, RightToLeft, TopLeftToBottomRight, TopRightToBottomLeft
}


