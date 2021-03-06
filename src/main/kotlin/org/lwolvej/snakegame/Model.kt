package org.lwolvej.snakegame

import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque

//点，x和y表示
data class Point(val x: Int, val y: Int)

//食物，使用一个格点表示
data class Food(var point: Point = Point(WIDTH / 2, HEIGHT / 2))

//方向，四个方向
enum class Direction {
    UP, DOWN, LEFT, RIGHT;
}

//蛇
data class Snake(
    var direction: Direction = Direction.RIGHT, //方向，默认为右边
    val tail: Deque<Point> = ConcurrentLinkedDeque(), //处头部格点之外的其他点的队列，双端队列
    var isCollidedWithWall: Boolean = false, //判断是否碰到边界
    var headLocation: Point = Point(BLOCK_SIZE, BLOCK_SIZE) // 头部格点所在位置
)

//重置食物
fun Food.foodRest() = run { point = Point(WIDTH / 2, HEIGHT / 2) }

//是否碰到食物
fun Food.touchFood(snake: Snake) = snake.headLocation == point

fun Food.addFood() {
    //设置食物随机出现在各点
    val random = Random()

    var y = Math.round(random.nextInt(HEIGHT).toDouble() / BLOCK_SIZE) * BLOCK_SIZE
    var x = Math.round(random.nextInt(WIDTH).toDouble() / BLOCK_SIZE) * BLOCK_SIZE

    if (y >= HEIGHT) {
        y -= BLOCK_SIZE
    }
    if (x >= WIDTH) {
        x -= BLOCK_SIZE
    }
    //替换成新的food
    point = Point(x.toInt(), y.toInt())
}

//更新snake
fun Snake.snakeUpdate() {
    //如果长度不是空的，那么就需要删除最后的一个点和添加头部之后点到最前面
    if (tail.isNotEmpty()) {
        tail.removeLast()
        tail.offerFirst(Point(headLocation.x, headLocation.y))
    }
    //判断方向，作出不同反应
    when (direction) {
        Direction.UP -> {
            //向上的话：首先更新头部点(y--)，之后判断是否<0(碰到边界).如果是则设置为true，并更新头格点y为0（为了不让他显示出界），下面几个相同的效果
            headLocation = Point(headLocation.x, headLocation.y - BLOCK_SIZE)
            if (headLocation.y < 0) {
                isCollidedWithWall = true
                headLocation = Point(headLocation.x, 0)
            }
        }
        Direction.RIGHT -> {
            headLocation = Point(headLocation.x + BLOCK_SIZE, headLocation.y)
            if (headLocation.x >= WIDTH) {
                isCollidedWithWall = true
                headLocation = Point(WIDTH - BLOCK_SIZE, headLocation.y)
            }
        }
        Direction.LEFT -> {
            headLocation = Point(headLocation.x - BLOCK_SIZE, headLocation.y)
            if (headLocation.x < 0) {
                isCollidedWithWall = true
                headLocation = Point(0, headLocation.y)
            }
        }
        Direction.DOWN -> {
            headLocation = Point(headLocation.x, headLocation.y + BLOCK_SIZE)
            if (headLocation.y >= HEIGHT) {
                isCollidedWithWall = true
                headLocation = Point(headLocation.x, HEIGHT - BLOCK_SIZE)
            }
        }
    }
}

//判断是否碰自己
fun Snake.isTouchTheTail() = tail.contains(headLocation)

//添加长度添加新的格点到队列中
fun Snake.addTail() = tail.offerFirst(Point(headLocation.x, headLocation.y))

