package helpers

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFRelease
import platform.CoreGraphics.CGColorSpaceCreateDeviceCMYK
import platform.CoreGraphics.CGColorSpaceCreateDeviceGray
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGDataProviderCopyData
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageCreateCopyWithColorSpace
import platform.CoreGraphics.CGImageGetAlphaInfo
import platform.CoreGraphics.CGImageGetBytesPerRow
import platform.CoreGraphics.CGImageGetDataProvider
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
internal fun UIImage.toSkiaImage(colorType: ColorType? = null): Image? {
    val imageRef =
        CGImageCreateCopyWithColorSpace(this.CGImage, CGColorSpaceCreateDeviceRGB())
            ?: CGImageCreateCopyWithColorSpace(this.CGImage, CGColorSpaceCreateDeviceCMYK())
            ?: CGImageCreateCopyWithColorSpace(this.CGImage, CGColorSpaceCreateDeviceGray())
            ?: return null

    val width = CGImageGetWidth(imageRef).toInt()
    val height = CGImageGetHeight(imageRef).toInt()

    val bytesPerRow = CGImageGetBytesPerRow(imageRef)
    val data = CGDataProviderCopyData(CGImageGetDataProvider(imageRef))
    val bytePointer = CFDataGetBytePtr(data)
    val length = CFDataGetLength(data)
    val alphaInfo = CGImageGetAlphaInfo(imageRef)

    val alphaType =
        when (alphaInfo) {
            CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst, CGImageAlphaInfo.kCGImageAlphaPremultipliedLast -> ColorAlphaType.PREMUL
            CGImageAlphaInfo.kCGImageAlphaFirst, CGImageAlphaInfo.kCGImageAlphaLast -> ColorAlphaType.UNPREMUL
            CGImageAlphaInfo.kCGImageAlphaNone, CGImageAlphaInfo.kCGImageAlphaNoneSkipFirst, CGImageAlphaInfo.kCGImageAlphaNoneSkipLast -> ColorAlphaType.OPAQUE
            else -> ColorAlphaType.UNKNOWN
        }

    val byteArray =
        ByteArray(length.toInt()) { index ->
            bytePointer!![index].toByte()
        }
    CFRelease(data)
    CFRelease(imageRef)

    val colorTypes =
        if (colorType != null) {
            listOf(colorType)
        } else {
            listOf(
                ColorType.RGBA_8888,
                ColorType.A16_UNORM,
            )
        }

    return colorTypes.firstNotNullOfOrNull { localColorType ->
        makeRaster(width, height, localColorType, alphaType, byteArray, bytesPerRow.toInt())
    }
}

private fun makeRaster(
    width: Int,
    height: Int,
    colorType: ColorType,
    alphaType: ColorAlphaType,
    byteArray: ByteArray,
    bytesPerRow: Int,
) = runCatching {
    Image.makeRaster(
        imageInfo =
            ImageInfo(
                width = width,
                height = height,
                colorType = colorType,
                alphaType = alphaType,
            ),
        bytes = byteArray,
        rowBytes = bytesPerRow,
    )
}.getOrNull()
