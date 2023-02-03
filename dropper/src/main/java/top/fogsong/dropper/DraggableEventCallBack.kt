package top.fogsong.dropper

import androidx.compose.ui.geometry.Offset

/**
 * @author：FogSong
 * @time：2023-02-03  8:26
 */
data class DraggableEventCallBack(
    val onDrag: (Offset) -> Unit = {},
    val onDragStart: (Offset) -> Unit = {},
    val onDropped: () -> Unit = {},
    val onCancelled: () -> Unit = {}
)