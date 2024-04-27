package com.norm.myinfinityloader

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Glow(
    val radius: Dp = 8.dp,
    val xShifting: Dp = 0.dp,
    val yShifting: Dp = 0.dp,
)


@Composable
fun InfinityLoader(
    modifier: Modifier,
    brush: Brush,
    duration: Int = 3_000,
    strokeWidth: Dp = 4.dp,
    strokeCap: StrokeCap = StrokeCap.Round,
    glow: Glow? = null,
    placeholderColor: Color? = null,
) {
    val infiniteTransition = rememberInfiniteTransition("PathTransition")

    val pathCompletion by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing)
        ),
        label = "PathCompletion"
    )

    Canvas(modifier) {
        val path = createPath(size.width, size.height)
        val pathSegment = calculatePathSegment(path, pathCompletion)
        val paint = setupPaint(strokeWidth, strokeCap, brush)

        glow?.applyToPaint(paint, this)

        placeholderColor?.let { color ->
            drawPathPlaceholder(path, strokeWidth, strokeCap, color)
        }

        drawPathSegment(pathSegment, paint)
    }
}

private fun createPath(width: Float, height: Float): Path {
    return Path().apply {
        moveTo((width / 2), (height / 2))
        cubicTo(
            x1 = width, y1 = 0f,
            x2 = width, y2 = height,
            x3 = (width / 2), (height / 2)
        )
        cubicTo(
            x1 = 0f, y1 = 0f,
            x2 = 0f, y2 = height,
            x3 = (width / 2), (height / 2)
        )
    }
}

private fun calculatePathSegment(path: Path, pathCompletion: Float): Path {
    val pathMeasure = PathMeasure().apply {
        setPath(path, false)
    }
    val pathSegment = Path()

    val stopDistance = when {
        (pathCompletion < 1f) -> (pathCompletion * pathMeasure.length)
        else -> pathMeasure.length
    }

    val startDistance = when {
        (pathCompletion > 1f) -> ((pathCompletion - 1f) * pathMeasure.length)
        else -> 0f
    }

    pathMeasure.getSegment(startDistance, stopDistance, pathSegment, true)
    return pathSegment
}

private fun DrawScope.setupPaint(
    strokeWidth: Dp,
    strokeCap: StrokeCap,
    brush: Brush,
): Paint {
    return Paint().apply paint@{
        this@paint.isAntiAlias = true
        this@paint.style = PaintingStyle.Stroke
        this@paint.strokeWidth = strokeWidth.toPx()
        this@paint.strokeCap = strokeCap

        brush.applyTo(size, this@paint, 1f)
    }
}

private fun Glow.applyToPaint(paint: Paint, density: Density) = with(density) {
    val frameworkPaint = paint.asFrameworkPaint()
    frameworkPaint.setShadowLayer(
        /* radius = */ radius.toPx(),
        /* dx = */ xShifting.toPx(),
        /* dy = */ yShifting.toPx(),
        /* shadowColor = */ android.graphics.Color.WHITE
    )
}

private fun DrawScope.drawPathPlaceholder(
    path: Path,
    strokeWidth: Dp,
    strokeCap: StrokeCap,
    placeholderColor: Color
) {
    drawPath(
        path = path,
        color = placeholderColor,
        style = Stroke(
            width = strokeWidth.toPx(),
            cap = strokeCap
        )
    )
}

private fun DrawScope.drawPathSegment(pathSegment: Path, paint: Paint) {
    drawIntoCanvas { canvas ->
        canvas.drawPath(pathSegment, paint)
    }
}