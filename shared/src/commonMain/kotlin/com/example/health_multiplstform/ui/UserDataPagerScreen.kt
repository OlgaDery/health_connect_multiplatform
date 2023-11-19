import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import com.example.health_multiplstform.ui.AutoSlidingCarousel
import com.example.health_multiplstform.ui.HealthValues
import com.example.health_multiplstform.ui.Images
import com.example.health_multiplstform.ui.PagerViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PagerMainContent(
    index: Int, aggregatedData: HealthValues) {

    Column (
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(24.dp)
        ) {
            Text(
                text =
                when (index) {
                    0 -> {"Steps made today: "}
                    1 -> {"Avg heart rate today: "}
                    else -> {"Records collected today: "}
                },
                modifier = Modifier.wrapContentHeight()

            )

            Text(
                text =
                when (index) {
                    0 -> {(aggregatedData).steps.toString()}
                    1 -> {(aggregatedData).heartRate.toString()}
                    else -> {(aggregatedData).total.toString()}
                },
                modifier = Modifier.wrapContentHeight()
            )
        }
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            val imageType =
                when (index) {
                    0 -> {Images.Steps}
                    1 -> {Images.HeartRate}
                    else -> {Images.Total}
                }

            Image(painterResource("skincare.png"), null, contentScale = ContentScale.Crop)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarouselWithHealthMetrics(viewModel: PagerViewModel){

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        3
    }

    Surface(
        modifier = Modifier.fillMaxHeight()
    ) {
        AutoSlidingCarousel(
            itemsCount = 3,
            pagerState = pagerState,
            itemContent = {index ->
                PagerMainContent(
                    index = index,
                    aggregatedData = viewModel.healthDataValue.collectAsState(HealthValues(0, 0f, 0)).value
                )
            }
        )
    }

}
