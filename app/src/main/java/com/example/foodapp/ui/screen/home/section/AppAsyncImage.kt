import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.foodapp.R

@Composable
fun AppAsyncImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val isPreview = LocalInspectionMode.current

    if (isPreview) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = R.drawable.pizza1),
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale,
            placeholder = painterResource(R.drawable.ic_loading),
            error = painterResource(R.drawable.error)
        )
    }
}