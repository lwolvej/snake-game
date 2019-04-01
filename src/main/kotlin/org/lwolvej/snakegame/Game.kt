package org.lwolvej.snakegame

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.input.KeyCode
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Stage
import java.util.*

class Game : Application() {

    lateinit var context: GraphicsContext

    lateinit var snake: Snake

    lateinit var grid: Grid

    lateinit var animationTimer: AnimationTimer

    var timer: Timer? = null

    private lateinit var timerTask: TimerTask

    var inProgress = false

    var gameOver = false

    private var paused = false

    override fun start(primaryStage: Stage?) {

        //初始化格点
        grid = Grid(
            width = WIDTH,
            height = HEIGHT,
            pixelsPerSquare = BLOCK_SIZE
        )

        //初始化蛇
        snake = Snake(
            width = WIDTH,
            height = HEIGHT,
            blockSize = BLOCK_SIZE
        )

        //初始化蛇的头结点
        snake.headLocation = Point(
            x = BLOCK_SIZE,
            y = BLOCK_SIZE
        )

        val canvas = Canvas(WIDTH.toDouble(), HEIGHT.toDouble())
        context = canvas.graphicsContext2D

        //button的设置
        val button = Button().apply {
            text = BUTTON_NAME // 名称
            minHeight = BUTTON_HEIGHT  //最小高度
            minWidth = BUTTON_WIDTH //最小长度
            //设置按钮动作
            setOnAction {
                //开始游戏
                inProgress = true
                //游戏结束为false
                gameOver = false
                //设置游戏开始不可见
                isVisible = false
                //如果timer为null将其初始化
                timer ?: apply {
                    timerTask = createTask()
                    timer = Timer()
                    timer?.scheduleAtFixedRate(timerTask, UPDATE_DELAY, UPDATE_PERIOD)
                    animationTimer.start()
                }
            }
        }

        //vBox（放置button）的设置
        val vBox = VBox().apply {
            prefWidthProperty().bind(canvas.widthProperty())
            prefHeightProperty().bind(canvas.heightProperty())
            alignment = Pos.CENTER
            children.add(button)
        }

        //将画布和vBox加入root
        val root = Group().apply {
            children.addAll(canvas, vBox)
        }

        //设置scene
        val myScene = Scene(root).apply {
            setOnKeyPressed {
                //判断键盘的按键
                when (it.code) {
                    //如果为上或者w，就往上走，如果此时蛇的方向是向下，则不允许。为啥？自己去掉if然后试试看
                    KeyCode.UP, KeyCode.W -> {
                        if (snake.direction != Direction.DOWN) {
                            snake.direction = Direction.UP
                        }
                    }
                    KeyCode.DOWN, KeyCode.S -> {
                        if (snake.direction != Direction.UP) {
                            snake.direction = Direction.DOWN
                        }
                    }
                    KeyCode.LEFT, KeyCode.A -> {
                        if (snake.direction != Direction.RIGHT) {
                            snake.direction = Direction.LEFT
                        }
                    }
                    KeyCode.RIGHT, KeyCode.D -> {
                        if (snake.direction != Direction.LEFT) {
                            snake.direction = Direction.RIGHT
                        }
                    }
                    //如果按了其他按键则暂停
                    else -> {
                        if (paused) {
                            timerTask = createTask()
                            timer = Timer()
                            timer?.scheduleAtFixedRate(timerTask, UPDATE_DELAY, UPDATE_PERIOD)
                            paused = false
                        } else {
                            timer?.cancel()
                            timer = null
                            paused = true
                        }
                    }
                }
            }
        }

        //绘制地图
        drawGrid()

        //展示stage
        primaryStage?.apply {
            title = STAGE_NAME
            scene = myScene
        }?.show()

        animationTimer = object : AnimationTimer() {
            override fun handle(now: Long) {
                if (inProgress) {
                    drawGrid()
                    drawSnake()
                    drawFood()
                } else if (gameOver) {
                    animationTimer.stop()
                    showEndGameAlert()
                    button.isVisible = true
                    grid.foodRest()
                    snake = Snake(
                        width = WIDTH,
                        height = HEIGHT,
                        blockSize = BLOCK_SIZE
                    )
                    snake.headLocation = Point(
                        x = BLOCK_SIZE,
                        y = BLOCK_SIZE
                    )
                }
            }
        }
        animationTimer.start()

        timerTask = createTask()
        timer = Timer()
        timer?.scheduleAtFixedRate(timerTask, UPDATE_DELAY, UPDATE_PERIOD)
    }

    override fun stop() {
        timer?.cancel()
        timer = null
    }

}

//绘制食物
private fun Game.drawFood() {
    context.fill = FOOD_COLOR
    context.fillRect(
        grid.food.point.x.toDouble(),
        grid.food.point.y.toDouble(),
        BLOCK_SIZE.toDouble(),
        BLOCK_SIZE.toDouble()
    )
}

//绘制蛇
private fun Game.drawSnake() {
    context.fill = SNAKE_COLOR
    context.fillRect(
        snake.headLocation.x.toDouble(),
        snake.headLocation.y.toDouble(),
        BLOCK_SIZE.toDouble(),
        BLOCK_SIZE.toDouble()
    )
    snake.tail.forEach {
        context.fillRect(
            it.x.toDouble(),
            it.y.toDouble(),
            BLOCK_SIZE.toDouble(),
            BLOCK_SIZE.toDouble()
        )
    }
}

//绘制地图
private fun Game.drawGrid() {
    context.fill = BLOCK_COLOR
    context.fillRect(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())

    context.stroke = LINE_COLOR
    context.lineWidth = 0.5

    for (element in 0 until WIDTH step BLOCK_SIZE) {
        context.strokeLine(element.toDouble(), 0.0, element.toDouble(), element + HEIGHT.toDouble())
    }

    for (element in 0 until HEIGHT step BLOCK_SIZE) {
        context.strokeLine(0.0, element.toDouble(), element + HEIGHT.toDouble(), element.toDouble())
    }
}

//展示结束的弹窗
private fun Game.showEndGameAlert() {
    val text = text(snake.tail.size + 1)
    val textWidth = getTextWidth(text)
    context.fill = TEXT_COLOR
    context.fillText(text, (WIDTH / 2) - (textWidth / 2), HEIGHT / 2 - 48.toDouble())
}

//结束游戏
private fun Game.endGame() {
    timer?.cancel()
    timer = null
    inProgress = false
    gameOver = true
}

//获取结束文字的宽度
private fun getTextWidth(string: String): Double {
    val text = Text(string)
    Scene(Group(text))
    text.applyCss()
    return text.layoutBounds.width
}

//创建任务
private fun Game.createTask() = object : TimerTask() {
    override fun run() {
        //如果在游戏中
        if (inProgress) {
            //更新蛇
            snake.snakeUpdate()
            //如果蛇碰到墙或者碰到自己, 结束游戏
            if (snake.isCollidedWithWall || snake.isTouchTheTail()) endGame()
            //如果蛇碰到食物：添加格点，场景添加食物
            if (grid.touchFood(snake)) {
                snake.addTail()
                grid.addFood()
            }
        }
    }
}

