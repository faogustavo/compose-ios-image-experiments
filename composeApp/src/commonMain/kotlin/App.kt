import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var bottomBarState: BottomTabItem by remember { mutableStateOf(BottomTabItem.PlatformImageTab) }

        Scaffold(
            bottomBar = {
                BottomAppBar {
                    TabButton(
                        tab = BottomTabItem.PlatformImageTab,
                        selected = bottomBarState is BottomTabItem.PlatformImageTab,
                        modifier = Modifier.fillMaxWidth(.5f),
                    ) {
                        bottomBarState = it
                    }
                    TabButton(
                        tab = BottomTabItem.PlatformResourceImageTab,
                        selected = bottomBarState is BottomTabItem.PlatformResourceImageTab,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        bottomBarState = it
                    }
                }
            },
        ) {
            Surface(modifier = Modifier.fillMaxSize().padding(it)) {
                AnimatedContent(
                    targetState = bottomBarState,
                    transitionSpec = { fadeIn().togetherWith(fadeOut()) },
                ) { tab ->
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        NormalAndGradientVariants("Asset - AppIcon") {
                            tab.content(
                                image = Image.Asset("AppIcon"),
                                tintColor = null,
                                modifier = Modifier.padding(top = 8.dp).size(24.dp),
                            )
                        }

                        NormalAndGradientVariants("Asset - burger (PNG)") {
                            tab.content(
                                image = Image.Asset("burger"),
                                tintColor = null,
                                modifier = Modifier.padding(top = 8.dp).size(56.dp),
                            )
                        }

                        NormalAndGradientVariants("Asset - nbcu (SVG)") {
                            tab.content(
                                image = Image.Asset("nbcu"),
                                tintColor = null,
                                modifier = Modifier.padding(top = 8.dp).size(56.dp),
                            )
                        }

                        NormalAndGradientVariants("Asset - universal (SVG B&W)") {
                            tab.content(
                                image = Image.Asset("universal"),
                                tintColor = null,
                                modifier = Modifier.padding(top = 8.dp).size(56.dp),
                            )
                        }

                        NormalAndGradientVariants("System Image - xmark") {
                            tab.content(
                                image = Image.SystemImage("xmark"),
                                tintColor = LocalContentColor.current,
                                modifier = Modifier.padding(top = 8.dp).size(24.dp),
                            )
                        }

                        NormalAndGradientVariants("Asset - ic_close") {
                            tab.content(
                                image = Image.Asset("ic_close"),
                                tintColor = LocalContentColor.current,
                                modifier = Modifier.padding(top = 8.dp).size(24.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class BottomTabItem {
    abstract val icon: ImageVector
    abstract val title: String

    @Composable
    abstract fun content(
        image: Image,
        tintColor: Color?,
        modifier: Modifier,
    )

    data object PlatformImageTab : BottomTabItem() {
        override val icon = Icons.Default.Camera
        override val title: String = "PlatformImage"

        @Composable
        override fun content(
            image: Image,
            tintColor: Color?,
            modifier: Modifier,
        ) {
            PlatformImage(
                image = image,
                modifier = modifier,
                tint = tintColor,
            )
        }
    }

    data object PlatformResourceImageTab : BottomTabItem() {
        override val icon = Icons.Default.CameraAlt
        override val title = "PlatformResourceImage"

        @Composable
        override fun content(
            image: Image,
            tintColor: Color?,
            modifier: Modifier,
        ) {
            PlatformResourceImage(
                image = image,
                modifier = modifier,
                tint = tintColor,
            )
        }
    }
}

@Composable
fun TabButton(
    tab: BottomTabItem,
    selected: Boolean,
    modifier: Modifier,
    onClick: (BottomTabItem) -> Unit,
) {
    IconButton(onClick = { onClick(tab) }, modifier = modifier) {
        Icon(
            imageVector = tab.icon,
            contentDescription = tab.title,
            tint = if (selected) Color.White else Color.White.copy(alpha = 0.5f),
        )
    }
}

@Composable
fun NormalAndGradientVariants(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, modifier = Modifier.padding(12.dp))
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Red),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth(.5f)
                        .background(Color.White)
                        .padding(vertical = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CompositionLocalProvider(LocalContentColor provides Color.Black, content)
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black, Color.DarkGray, Color.Gray, Color.LightGray),
                            ),
                        ).padding(vertical = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CompositionLocalProvider(LocalContentColor provides Color.Black, content)
            }
        }
    }
}
