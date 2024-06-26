package helpers

import Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSBundle
import platform.UIKit.UIImage
import platform.UIKit.UIImageRenderingMode

@Composable
fun rememberUIImage(
    image: Image,
    asTemplate: Boolean,
) = remember(image, asTemplate) {
    var uiImage =
        when (image) {
            is Image.Asset ->
                UIImage.imageNamed(
                    image.name,
                    inBundle = NSBundle.mainBundle,
                    withConfiguration = null,
                )

            is Image.SystemImage -> UIImage.systemImageNamed(image.name)
        }

    if (uiImage != null && asTemplate) {
        uiImage = uiImage.imageWithRenderingMode(UIImageRenderingMode.UIImageRenderingModeAlwaysTemplate)
    }

    uiImage
}
