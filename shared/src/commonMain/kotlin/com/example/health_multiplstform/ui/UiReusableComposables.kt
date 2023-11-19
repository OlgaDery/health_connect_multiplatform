package com.example.health_multiplstform.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.health_multiplstform.models.HealthDataProvider
import com.example.health_multiplstform.ui.UiComposables.Companion.PAGER
import com.example.health_multiplstform.ui.UiComposables.Companion.SELECTED_PROGRESS_DOT
import com.example.health_multiplstform.ui.UiComposables.Companion.UNSELECTED_PROGRESS_DOT

class UiComposables {
    companion object {
        const val SELECTED_PROGRESS_DOT = "selected_dot"
        const val UNSELECTED_PROGRESS_DOT = "unselected_dot"
        const val PAGER = "pager"
    }
}

@Composable
fun <T> HealthRecordListItem(data: T, modifier: Modifier = Modifier) {
    data as RecordToDisplay

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically

    ) {

        val header = "Record received at ${data.time}"

        Column (modifier = Modifier
            .wrapContentHeight()
            .weight(8F)
        ){
            Text(
                text = header,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Temperature: ${data.temperature}",
                Modifier.padding(3.dp)
            )
            Text(
                "Heart rate: ${data.heartBeat}",
                Modifier.padding(3.dp)
            )
            Text(
                "Steps: ${data.steps}",
                Modifier.padding(3.dp)
            )
            Text(
                text = data.locationRecord?.let { "Location: ${data.locationRecord.latitude} and ${data.locationRecord.longitude}" }
                    ?: kotlin.run { "No relevant location found" },
                Modifier.padding(3.dp)
            )
        }
        Column (
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End

        ){
            val icon = if (data.healthDataProvider == HealthDataProvider.PlatformHealthProvider) {
                Icons.Default.Share
            } else Icons.Default.AddCircle
             Icon(imageVector = icon, contentDescription = "", tint = MaterialTheme.colors.primaryVariant)
        }
    }
}

val MyCustomTypography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )
)

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}
@Composable
fun DotsIndicatorLayout(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = Color.Blue,
    unSelectedColor: Color = Color.LightGray,
    dotSize: Dp
) {
    val description = "some description"
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 0.dp, vertical = 20.dp)
            .semantics { contentDescription = description },
        horizontalArrangement = Arrangement.Center

    ) {
        items(totalDots) { index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize,
                modifier = Modifier.semantics {
                    contentDescription = if (index == selectedIndex) SELECTED_PROGRESS_DOT else UNSELECTED_PROGRESS_DOT
                }
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 15.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoSlidingCarousel(
    pagerState: PagerState,
    itemsCount: Int,
    itemContent: @Composable (index: Int) -> Unit,
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    LazyColumn (verticalArrangement = Arrangement.Center) {
        item {
            HorizontalPager(
                modifier =
                Modifier
                    .wrapContentWidth()
                    .semantics {
                        contentDescription = PAGER
                    },
                state = pagerState,
                pageSpacing = 0.dp,
                userScrollEnabled = true,
                reverseLayout = false,
                contentPadding = PaddingValues(0.dp),
                beyondBoundsPageCount = 0,
                pageSize = PageSize.Fill,
                flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                key = null,
                pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
                    Orientation.Horizontal
                ),
                pageContent = {
                    itemContent(it)
                }
            )
        }
        item {
            Column (
                modifier = Modifier
                    .padding(bottom = 25.dp)
                    .wrapContentHeight(),

                verticalArrangement = Arrangement.Center

            ) {
                DotsIndicatorLayout(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    totalDots = itemsCount,
                    selectedIndex = if (isDragged) pagerState.currentPage else pagerState.targetPage,
                    dotSize = 12.dp
                )
            }
        }
    }
}

@Composable
fun <T> RecyclerViewScreen(modifier: Modifier, list: State<List<T>>) {

    // for better performance each item should have a key
    LazyColumn(modifier = modifier) {
        itemsIndexed(
            items = list.value

        ){ idx, row ->
            HealthRecordListItem(row)
        }
    }
}

val md_theme_light_primary = Color(0xFF006C4C)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_secondary = Color(0xFF4D6357)
val md_theme_light_onSecondary = Color(color = 0xFFD3D0D0)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFBFDF9)
val md_theme_light_onBackground = Color(0xFF191C1A)
val md_theme_light_surface = Color(0xFFFBFDF9)
val md_theme_light_onSurface = Color(0xFF191C1A)


private val LightColors = lightColors(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface
)

@Composable
fun MyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = LightColors,
        typography = MyCustomTypography,
        content = content
    )
}