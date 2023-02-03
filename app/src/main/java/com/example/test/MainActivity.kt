package com.example.test

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.test.ui.theme.TestTheme
import kotlinx.coroutines.launch
import top.fogsong.dropper.*
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    val tag = "aaaa"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dropper by remember { mutableStateOf(Dropper<String>()) }
            var color by remember {
                mutableStateOf(Color.Blue)
            }
            val dropRef = dropper.createDroppableRef(DroppableEventCallBack(
                onEnter = { name, info ->
                    Log.d(tag, "enter: $name, info: $info")
                    color = Color.Red
                },
                onDrop = { name, info ->
                    Log.d(tag, "drop: $name, info: $info")
                    color = Color.Black
                },
                onLeave = { name ->
                    Log.d(tag, "leave: $name")
                    color = Color.Blue
                }
            ))
            val offset = remember {
                Animatable(Offset.Zero, Offset.VectorConverter)
            }
            val scope = rememberCoroutineScope()
            val dragRef = dropper.createDraggableRef("name", "info", DraggableEventCallBack(
                onDrag = {
                    scope.launch { offset.snapTo(offset.value + it) }
                },
                onDragStart = {
                    scope.launch { offset.snapTo(offset.value + it) }
                },
                onDropped = {
                    scope.launch { offset.snapTo(Offset.Zero) }
                },
                onCancelled = {
                    scope.launch { offset.animateTo(Offset.Zero) }
                }
            ))
            TestTheme {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .offset {
                                offset.value.let {
                                    IntOffset(x = it.x.roundToInt(), y = it.y.roundToInt())
                                }
                            }
                            .draggable(dragRef, afterLongPress = false, isSetOffset = false)
                            .background(Color.Yellow)
                    )
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(color)
                            .droppable(dropRef)
                    ) {

                    }
                }
            }
        }
    }
}
