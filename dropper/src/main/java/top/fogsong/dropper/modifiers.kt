package top.fogsong.dropper

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

/**
 * @author：FogSong
 * @time：2023-02-03  8:34
 */

fun <T> Modifier.draggable(
    ref: DraggableRef<T>,
    enabled: Boolean = true,
    isSetOffset: Boolean = true,
    afterLongPress: Boolean = true,
    zIndex: Float = 100f
) = composed {
    val isDragging = remember { mutableStateOf(false) }
    val totalOffset = ref.totalOffset
    if (enabled) {
        var modifier =
            offset {
                if (isSetOffset)
                    IntOffset(totalOffset.value.x.roundToInt(), totalOffset.value.y.roundToInt())
                else IntOffset.Zero
            }
                .onGloballyPositioned {
                    ref.size = it.size
                    ref.location = it.positionInWindow()
                }
                .pointerInput(Unit) {
                    if (afterLongPress) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                isDragging.value = true
                                ref.onDragStart(it)
                            },
                            onDrag = { _, offset: Offset ->
                                ref.onDrag(offset)
                            },
                            onDragEnd = {
                                ref.checkIsDrop()
                                isDragging.value = false
                            },
                            onDragCancel = {
                                ref.onCancelled()
                                isDragging.value = false
                            }
                        )
                    } else {
                        detectDragGestures(
                            onDragStart = {
                                isDragging.value = true
                                ref.onDragStart(it)

                            },
                            onDrag = { _, offset: Offset ->
                                ref.onDrag(offset)
                            },
                            onDragEnd = {
                                ref.checkIsDrop()
                                isDragging.value = false
                            },
                            onDragCancel = {
                                ref.onCancelled()
                                isDragging.value = false
                            }
                        )
                    }
                }

        if (isDragging.value) {
            modifier = modifier.zIndex(zIndex)
        }
        modifier
    } else this
}


fun <T> Modifier.droppable(ref: DroppableRef<T>, enabled: Boolean = true) = if (enabled) {
    onGloballyPositioned {
        ref.size = it.size
        ref.location = it.positionInWindow()
    }
} else this

