package org.lwolvej.snakegame

import java.util.*

//点，x和y表示
data class Point(val x: Int, val y: Int)

//食物，使用一个格点表示
data class Food(val point: Point)

//方向，四个方向
enum class Direction {
    UP, DOWN, LEFT, RIGHT;
}

//蛇
data class Snake(
    var direction: Direction = Direction.RIGHT, //方向，默认为右边
    val point: Point = Point(0, 0), //所在点，默认0，0
    val tail: MutableList<Point> = ArrayList(), //处头部格点之外的其他点的队列
    var isCollidedWithWall: Boolean = false, //判断是否碰到边界
    var headLocation: Point = Point(0, 0), // 头部格点所在位置
    val width: Int, //地图的宽度
    val height: Int, //地图的长度
    val blockSize: Int //单个格点的大小
)

//格点
data class Grid(
    val height: Int, //地图高度
    val width: Int, //地图宽度
    val pixelsPerSquare: Int, //单个格点的大小
    var food: Food = Food(Point(width / 2, height / 2)) //食物所在位置
)

//重置食物
fun Grid.foodRest() {
    food = Food(Point(width / 2, height / 2))
}

//是否碰到食物
fun Grid.touchFood(snake: Snake) = snake.headLocation == food.point

fun Grid.addFood() {
    //设置食物随机出现在各点
    val random = Random()
    var y = Math.round(random.nextInt(width).toDouble() / pixelsPerSquare) * pixelsPerSquare
    var x = Math.round(random.nextInt(height).toDouble() / pixelsPerSquare) * pixelsPerSquare

    if (y >= height) {
        y = (height - 3).toLong()
    } else if (y < 0) {
        y = (height + 3).toLong()
    }
    if (x >= width) {
        x = (width - 3).toLong()
    } else if (x < 0) {
        x = (width + 3).toLong()
    }
    //替换成新的food
    food = Food(Point(x.toInt(), y.toInt()))
}

//更新snake
fun Snake.snakeUpdate() {
    //如果长度不是空的，那么就需要删除最后的一个点和添加头部之后点到最前面
    if (tail.isNotEmpty()) {
        tail.removeAt(tail.size - 1)
        tail.add(0, Point(headLocation.x, headLocation.y))
    }
    //判断方向，作出不同反应
    when (direction) {
        Direction.UP -> {
            //向上的话：首先更新头部点(y--)，之后判断是否<0(碰到边界).如果是则设置为true，并更新头格点y为0（为了不让他显示出界），下面几个相同的效果
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
        //遍历，碰到队列中节点，则表示碰到自己
        if (headLocation == element) {
            isTouch = true
            break
        }
    }
    return isTouch
}

//添加长度
fun Snake.addTail() {
    //添加新的格点到队列中
    tail.add(0, Point(headLocation.x, headLocation.y))
}
