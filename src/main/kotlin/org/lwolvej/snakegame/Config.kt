package org.lwolvej.snakegame

import javafx.scene.paint.Color

const val WIDTH = 400

const val HEIGHT = 400

const val BUTTON_WIDTH = 100.0

const val BUTTON_HEIGHT = 36.0

const val BLOCK_SIZE = 20

const val UPDATE_PERIOD = 100L

const val UPDATE_DELAY = UPDATE_PERIOD

const val BUTTON_NAME = "开始!"

const val STAGE_NAME = "贪吃蛇"

const val TIMER_NAME = "lwolvej"

val SNAKE_COLOR = Color.rgb(122, 193, 218)!!

val BLOCK_COLOR = Color.rgb(245, 245, 245)!!

val FOOD_COLOR = Color.rgb(247, 163, 111)!!

val LINE_COLOR = Color.rgb(233, 230, 239)!!

val TEXT_COLOR = Color.BLACK!!

val text = { str: Int -> "游戏结束!得分:$str!" }
