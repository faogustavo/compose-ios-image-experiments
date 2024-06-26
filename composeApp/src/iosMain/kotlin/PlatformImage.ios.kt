import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.layout.ContentScale
import helpers.rememberUIImage
import helpers.toSkiaImage
import kotlinx.cinterop.ExperimentalForeignApi
import org.jetbrains.skia.ColorType
import platform.UIKit.UIColor
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentMode

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformImage(
    image: Image,
    tint: Color?,
    contentScale: ContentScale,
    modifier: Modifier,
) {
    val uiImage = rememberUIImage(image, tint != null)

    UIKitView(
        modifier = modifier,
        factory = {
            UIImageView(uiImage).apply {
                userInteractionEnabled = false
            }
        },
        update = {
            it.image = uiImage
            it.contentMode =
                when (contentScale) {
                    ContentScale.Inside -> UIViewContentMode.UIViewContentModeScaleAspectFill
                    ContentScale.FillBounds -> UIViewContentMode.UIViewContentModeScaleToFill
                    ContentScale.Crop -> UIViewContentMode.UIViewContentModeCenter
                    else -> UIViewContentMode.UIViewContentModeScaleAspectFit
                }

            if (tint != null) {
                it.tintColor =
                    UIColor(
                        red = tint.red.toDouble(),
                        green = tint.green.toDouble(),
                        blue = tint.blue.toDouble(),
                        alpha = tint.alpha.toDouble(),
                    )
            }
        },
        interactive = false,
    )
}

@Composable
actual fun PlatformResourceImage(
    image: Image,
    tint: Color?,
    contentScale: ContentScale,
    modifier: Modifier,
    colorType: ColorType?,
) {
    val uiImage = rememberUIImage(image, asTemplate = false)
    val painter =
        remember(uiImage) {
            uiImage
                ?.toSkiaImage(colorType)
                ?.toComposeImageBitmap()
                ?.let(::BitmapPainter)
        } ?: run {
            Text("Failed to load image", color = LocalContentColor.current)
            return
        }

    androidx.compose.foundation.Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
        colorFilter = tint?.let(ColorFilter::tint),
    )
}
