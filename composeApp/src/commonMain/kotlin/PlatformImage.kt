import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.skia.ColorType

@Composable
expect fun PlatformImage(
    image: Image,
    tint: Color? = null,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier = Modifier,
)

@Composable
expect fun PlatformResourceImage(
    image: Image,
    tint: Color? = null,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier = Modifier,
    colorType: ColorType? = null,
)

sealed class Image {
    data class Asset(
        val name: String,
    ) : Image()

    data class SystemImage(
        val name: String,
    ) : Image()
}
