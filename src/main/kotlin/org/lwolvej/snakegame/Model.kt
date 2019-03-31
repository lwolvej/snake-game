package org.lwolvej.snakegame

import java.util.*

data class Point(val x: Int, val y: Int)

data class Food(val point: Point)

enum class Direction {
    UP, DOWN, LEFT, RIGHT;
}

data class Snake(
    var direction: Direction = Direction.RIGHT,
    val point: Point = Point(0, 0),
    val tail: MutableList<Point> = ArrayList(),
    var isCollidedWithWall: Boolean = false,
    var headLocation: Point = Point(0, 0),
    val width: Int,
    val height: Int,
    val blockSize: Int
)

data class Grid(
    val height: Int,
    val width: Int,
    val pixelsPerSquare: Int,
    var food: Food = Food(Point(width / 2, height / 2))
)

//重置食物
fun Grid.foodRest() {
    food = Food(Point(width / 2, height / 2))
}

//是否碰到食物
fun Grid.touchFood(snake: Snake) = snake.headLocation == food.point

fun Grid.addFood() {
    val random = Random()
    val y = Math.round(random.nextInt(width).toDouble() / pixelsPerSquare) * pixelsPerSquare
    val x = Math.round(random.nextInt(height).toDouble() / pixelsPerSquare) * pixelsPerSquare

    food = Food(Point(x.toInt(), y.toInt()))
}

//更新snake
fun Snake.snakeUpdate() {
    if (tail.isNotEmpty()) {
        tail.removeAt(tail.size - 1)
        tail.add(0, Point(headLocation.x, headLocation.y))
    }
    when (direction) {
        Direction.UP -> {
            headLocation = Point(headLocation.x, headLocation.y - blockSize)
            if (headLocation.y < 0) {
                isCollidedWithWall = true
                headLocation = Point(headLocation.x, 0)
            }
        }
        Direction.RIGHT -> {
            headLocation = Point(headLocation.x + blockSize, headLocation.y)
            if (headLocation.x >= width) {
                isCollidedWithWall = true
                headLocation = Point(width - blockSize, headLocation.y)
            }
        }
        Direction.LEFT -> {
            headLocation = Point(headLocation.x - blockSize, headLocation.y)
            if (headLocation.x < 0) {
                isCollidedWithWall = true
                headLocation = Point(0, headLocation.y)
            }
        }
        Direction.DOWN -> {
            headLocation = Point(headLocation.x, headLocation.y + blockSize)
            if (headLocation.y >= height) {
                isCollidedWithWall = true
                headLocation = Point(headLocation.x, height - blockSize)
            }
        }
    }
}

//判断是否碰自己
fun Snake.isTouchTheTail(): Boolean {
    var isTouch = false
    for (element in tail) {
        if (headLocation == element) {
            isTouch = true
            break
        }
    }
    return isTouch
}

//添加长度
fun Snake.addTail() {
    tail.add(0, Point(headLocation.x, headLocation.y))
}
