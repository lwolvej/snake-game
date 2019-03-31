package org.lwolvej.snakegame

import javafx.scene.paint.Color

//地图宽度
const val WIDTH = 400

//地图高度
const val HEIGHT = 400

//按钮宽度
const val BUTTON_WIDTH = 100.0

//按钮高度
const val BUTTON_HEIGHT = 36.0

//单个格点的大小
const val BLOCK_SIZE = 20

//更新的频率，也可以认为蛇的速度
const val UPDATE_PERIOD = 100L

//更新的延迟，同上的效果
const val UPDATE_DELAY = UPDATE_PERIOD

//按钮名称
const val BUTTON_NAME = "开始!"

//界面名称
const val STAGE_NAME = "贪吃蛇"

//Timer的名称
const val TIMER_NAME = "lwolvej"

//蛇的颜色
val SNAKE_COLOR = Color.rgb(122, 193, 218)!!

//格点颜色
val BLOCK_COLOR = Color.rgb(245, 245, 245)!!

//食物颜色
val FOOD_COLOR = Color.rgb(247, 163, 111)!!

//线的颜色
val LINE_COLOR = Color.rgb(233, 230, 239)!!

//字体颜色
val TEXT_COLOR = Color.BLACK!!

//结束字体，用了一下lambda
val text = { str: Int -> "游戏结束!得分:$str!" }
