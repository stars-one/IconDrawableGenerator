package com.example.common.toast

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@Composable
fun Title(text:String) {
    Surface{
        Row {
            Icon(Icons.Default.Search,"")
            Text(text)
        }
    }
}


public interface ToastData {
    public val message: String
    public val icon: ImageVector?
    public val animationDuration: StateFlow<Int?>
    public val type: ToastModel.Type?
    public suspend fun run(accessibilityManager: AccessibilityManager?)
    public fun pause()
    public fun resume()
    public fun dismiss()
    public fun dismissed()
}

data class ToastModel(
    val message: String,
    val type: Type
){
    enum class Type {
        Normal, Success, Info, Warning, Error,
    }
}

private data class ColorData(
    val backgroundColor: Color,
    val textColor: Color,
    val iconColor: Color,
    val icon: ImageVector? = null,
)

@Composable
public fun Toast(
    toastData: ToastData,
) {

    val animateDuration by toastData.animationDuration.collectAsState()

    val colorData = when (toastData.type) {
        ToastModel.Type.Normal -> ColorData(
            backgroundColor = MaterialTheme.colorScheme.background,
            textColor = MaterialTheme.colorScheme.onPrimary,
            iconColor = MaterialTheme.colorScheme.onPrimary,
            icon = Icons.Rounded.Notifications,
        )

        ToastModel.Type.Success -> ColorData(
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            textColor = MaterialTheme.colorScheme.onErrorContainer,
            iconColor = MaterialTheme.colorScheme.onErrorContainer,
            icon = Icons.Rounded.Check,
        )

        ToastModel.Type.Info -> ColorData(
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            textColor = MaterialTheme.colorScheme.onErrorContainer,
            iconColor = MaterialTheme.colorScheme.onErrorContainer,
            icon = Icons.Rounded.Info,

            )

        ToastModel.Type.Warning -> ColorData(
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            textColor = MaterialTheme.colorScheme.onErrorContainer,
            iconColor = MaterialTheme.colorScheme.onErrorContainer,
            icon = Icons.Rounded.Warning,

            )

        ToastModel.Type.Error -> ColorData(
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            textColor = MaterialTheme.colorScheme.onErrorContainer,
            iconColor = MaterialTheme.colorScheme.onErrorContainer,
            icon = Icons.Rounded.Warning,
        )

        else -> ColorData(
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            textColor = MaterialTheme.colorScheme.onErrorContainer,
            iconColor = MaterialTheme.colorScheme.onErrorContainer,
            icon = Icons.Rounded.Notifications,
        )
    }
    val icon = toastData.icon ?: colorData.icon
    key(toastData) {
        Toast(
            message = toastData.message,
            icon = icon,
            backgroundColor = colorData.backgroundColor,
            iconColor = colorData.iconColor,
            textColor = colorData.textColor,
            animateDuration = animateDuration,
            onPause = toastData::pause,
            onResume = toastData::resume,
            onDismissed = toastData::dismissed,
        )

    }
}

@Composable
private fun Toast(
    message: String,
    icon: ImageVector?,
    backgroundColor: Color,
    iconColor: Color,
    textColor: Color,
    animateDuration: Int? = 0,
    onPause: () -> Unit = {},
    onResume: () -> Unit = {},
    onDismissed: () -> Unit = {},
) {
    val roundedValue = 26.dp
    Surface(
        modifier = Modifier
            .padding(18.dp)
            .widthIn(max = 520.dp)
            .fillMaxWidth()
            .toastGesturesDetector(onPause, onResume, onDismissed),
        color = backgroundColor,
        shape = RoundedCornerShape(roundedValue),
        elevation = 2.dp,
    ) {
        val progress = remember { Animatable(0f) }
        LaunchedEffect(animateDuration) {
            // Do not run animation when animations are turned off.

//            if (coroutineContext.durationScale == 0f) return@LaunchedEffect

            if (animateDuration == null) {
                progress.stop()
            } else {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = animateDuration,
                        easing = EaseOut,
                    ),
                )
            }
        }

        val color = LocalContentColor.current
        Row(
            Modifier
                .drawBehind {
                    val fraction = progress.value * size.width
                    drawRoundRect(
                        color = color,
                        size = Size(width = fraction, height = size.height),
                        cornerRadius = CornerRadius(roundedValue.toPx()),
                        alpha = 0.1f,
                    )
                }
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (icon != null) {
                Icon(
                    icon,
                    contentDescription = null,
                    Modifier.size(24.dp),
                    tint = iconColor
                )
            }
            Title(message)
        }
    }
}

private fun Modifier.toastGesturesDetector(
    onPause: () -> Unit,
    onResume: () -> Unit,
    onDismissed: () -> Unit,
): Modifier = composed {
    val offsetY = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }

    pointerInput(Unit) {
        val decay = splineBasedDecay<Float>(this)
        coroutineScope {
            while (true) {
                awaitPointerEventScope {
                    // Detect a touch down event.
                    val down = awaitFirstDown()
                    onPause()
                    val pointerId = down.id

                    val velocityTracker = VelocityTracker()
                    // Stop any ongoing animation.
                    launch(start = CoroutineStart.UNDISPATCHED) {
                        offsetY.stop()
                        alpha.stop()
                    }

                    verticalDrag(pointerId) { change ->
                        onPause()
                        // Update the animation value with touch events.
                        val changeY = (offsetY.value + change.positionChange().y).coerceAtMost(0f)
                        launch {
                            offsetY.snapTo(changeY)
                        }
                        if (changeY == 0f) {
                            velocityTracker.resetTracking()
                        } else {
                            velocityTracker.addPosition(
                                change.uptimeMillis,
                                change.position,
                            )
                        }
                    }

                    onResume()
                    // No longer receiving touch events. Prepare the animation.
                    val velocity = velocityTracker.calculateVelocity().y
                    val targetOffsetY = decay.calculateTargetValue(
                        offsetY.value,
                        velocity,
                    )
                    // The animation stops when it reaches the bounds.
                    offsetY.updateBounds(
                        lowerBound = -size.height.toFloat() * 3,
                        upperBound = size.height.toFloat(),
                    )
                    launch {
                        if (velocity >= 0 || targetOffsetY.absoluteValue <= size.height) {
                            // Not enough velocity; Slide back.
                            offsetY.animateTo(
                                targetValue = 0f,
                                initialVelocity = velocity,
                            )
                        } else {
                            // The element was swiped away.
                            launch { offsetY.animateDecay(velocity, decay) }
                            launch {
                                alpha.animateTo(targetValue = 0f, animationSpec = tween(300))
                                onDismissed()
                            }
                        }
                    }
                }
            }
        }
    }
        .offset {
            IntOffset(0, offsetY.value.roundToInt())
        }
        .alpha(alpha.value)
}



@Stable
class ToastUIState {
    private val mutex = Mutex()

    public var currentData: ToastData? by mutableStateOf(null)
        private set

    public suspend fun show(
        message: String,
        icon: ImageVector? = null,
    ): Unit = mutex.withLock {
        try {
            return suspendCancellableCoroutine { cont ->
                currentData = ToastDataImpl(
                    message,
                    icon,
                    cont,
                )
            }
        } finally {
            currentData = null
        }
    }

    public suspend fun show(
        toastModel: ToastModel
    ): Unit = mutex.withLock {
        try {
            return suspendCancellableCoroutine { cont ->
                currentData = ToastDataImpl(
                    toastModel.message,
                    null,
                    cont,
                    toastModel.type
                )
            }
        } finally {
            currentData = null
        }
    }



    @Stable
    private class ToastDataImpl(
        override val message: String,
        override val icon: ImageVector?,
        private val continuation: CancellableContinuation<Unit>,
        override val type: ToastModel.Type? = ToastModel.Type.Normal,
    ) : ToastData {
        private var elapsed = 0L
        private var started = 0L
        private var duration = 0L
        private val _state = MutableStateFlow<Int?>(null)
        override val animationDuration: StateFlow<Int?> = _state.asStateFlow()

        override suspend fun run(accessibilityManager: AccessibilityManager?) {
            duration = durationTimeout(
                hasIcon = icon != null,
                accessibilityManager = accessibilityManager,
            )

            // Accessibility decided to show forever
            // Let's await explicit dismiss, do not run animation.
            if (duration == Long.MAX_VALUE) {
                delay(duration)
                return
            }

            resume()
            supervisorScope {
                launch {
                    animationDuration.collectLatest { duration ->
                        val animationScale = coroutineContext.durationScale
                        if (duration != null) {
                            started = System.currentTimeMillis()
                            // 关闭动画后，只需显示、等待和隐藏即可。
                            val finalDuration = when (animationScale) {
                                0f -> duration.toLong()
                                else -> (duration.toLong() * animationScale).roundToLong()
                            }
                            delay(finalDuration)
                            this@launch.cancel()
                        } else {
                            elapsed += System.currentTimeMillis() - started
                            delay(Long.MAX_VALUE)
                        }
                    }
                }
            }
        }

        override fun pause() {
            _state.value = null
        }

        override fun resume() {
            val remains = (duration - elapsed).toInt()
            if (remains > 0) {
                _state.value = remains
            } else {
                dismiss()
            }
        }

        override fun dismiss() {
            _state.value = 0
        }

        override fun dismissed() {
            if (continuation.isActive) {
                continuation.resume(Unit)
            }
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun ToastUI(
    hostState: ToastUIState,
    modifier: Modifier = Modifier,
    toast: @Composable (ToastData) -> Unit = { Toast(it) },
) {
    val accessibilityManager = LocalAccessibilityManager.current
    val currentData = hostState.currentData ?: return
    //震动
    val feedback = LocalHapticFeedback.current
    key(currentData) {
        var state by remember { mutableStateOf(false) }
        val transition = updateTransition(targetState = state, label = "toast")

        LaunchedEffect(Unit) {
            state = true
            currentData.run(accessibilityManager)
            state = false
//            feedback.vibration()
        }

        transition.AnimatedVisibility(
            visible = { it },
            modifier = modifier,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
        ) {
            toast(currentData)
        }

        // Await dismiss animation and dismiss the Toast completely.
        // This animation workaround instead of nulling the toast data is to prevent
        // relaunching another Toast when the dismiss animation has not completed yet.
        LaunchedEffect(state, transition.currentState, transition.isRunning) {
            if (!state && !transition.currentState && !transition.isRunning) {
                currentData.dismissed()
//                feedback.vibration()

            }
        }
    }
}

internal fun durationTimeout(
    hasIcon: Boolean,
    accessibilityManager: AccessibilityManager?,
): Long {
    val timeout = 3000L
    if (accessibilityManager == null) return timeout
    return accessibilityManager.calculateRecommendedTimeoutMillis(
        originalTimeoutMillis = timeout,
        containsIcons = hasIcon,
        containsText = true,
        containsControls = false,
    )
}

internal val CoroutineContext.durationScale: Float
    get() {
        val scale = this[MotionDurationScale]?.scaleFactor ?: 1f
        check(scale >= 0f)
        return scale
    }
