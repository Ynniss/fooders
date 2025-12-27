package com.esgi.fooders.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.esgi.fooders.data.remote.responses.RankingResponse.RankingData
import com.esgi.fooders.data.remote.responses.UserSuccessResponse.Succes
import com.esgi.fooders.ui.components.FoodersMediumTopAppBar
import com.esgi.fooders.ui.components.LoadingIndicator
import com.esgi.fooders.ui.components.OverflowMenu
import com.esgi.fooders.ui.components.OverflowMenuItem
import com.esgi.fooders.ui.theme.FoodersCustomShapes
import com.esgi.fooders.ui.theme.FoodersTheme
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userSuccess by viewModel.userSuccessEvent.observeAsState()
    val ranking by viewModel.rankingEvent.observeAsState()
    val username by viewModel.dataStoreManager.usernameFlow.collectAsState(initial = "")

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getUserSuccess()
        viewModel.getRanking()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FoodersMediumTopAppBar(
                title = "Profil",
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                    OverflowMenu(
                        items = listOf(
                            OverflowMenuItem("Deconnexion", onLogout)
                        )
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Profile Header
            ProfileHeader(
                username = username,
                modifier = Modifier.padding(16.dp)
            )

            // Tabs
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                    text = { Text("Succes") },
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = null) }
                )
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                    text = { Text("Classements") },
                    icon = { Icon(Icons.Default.Leaderboard, contentDescription = null) }
                )
            }

            // Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> SuccessTab(
                        successes = userSuccess?.data?.success ?: emptyList(),
                        isLoading = userSuccess == null
                    )
                    1 -> RankingTab(
                        ranking = ranking,
                        isLoading = ranking == null
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(
    username: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = FoodersCustomShapes.Card,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                FoodersTheme.colors.gradientStart,
                                FoodersTheme.colors.gradientEnd
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = username.ifEmpty { "Utilisateur" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Contributeur Fooders",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun SuccessTab(
    successes: List<Succes>,
    isLoading: Boolean
) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
    } else if (successes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Aucun succes pour le moment",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(successes) { success ->
                SuccessItem(success = success)
            }
        }
    }
}

@Composable
fun SuccessItem(
    success: Succes,
    modifier: Modifier = Modifier
) {
    val isCompleted = success.status.lowercase() == "completed" ||
            success.status.lowercase() == "complete"

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = FoodersCustomShapes.Card,
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (isCompleted) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = success.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = success.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isCompleted) {
                Box(
                    modifier = Modifier
                        .clip(FoodersCustomShapes.Chip)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Complete",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun RankingTab(
    ranking: com.esgi.fooders.data.remote.responses.RankingResponse.RankingResponse?,
    isLoading: Boolean
) {
    var selectedFilter by remember { mutableIntStateOf(0) }
    val filters = listOf("Photographes", "Redacteurs", "Scanners")

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEachIndexed { index, filter ->
                    FilterChip(
                        selected = selectedFilter == index,
                        onClick = { selectedFilter = index },
                        label = { Text(filter) }
                    )
                }
            }

            // Ranking List
            val rankingData = when (selectedFilter) {
                0 -> ranking?.photoRanking ?: emptyList()
                1 -> ranking?.textRanking ?: emptyList()
                2 -> ranking?.scanRanking ?: emptyList()
                else -> emptyList()
            }

            if (rankingData.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aucun classement disponible",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(rankingData.take(10)) { data ->
                        RankingItem(
                            rankingData = data,
                            position = rankingData.indexOf(data) + 1,
                            statType = selectedFilter
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun RankingItem(
    rankingData: RankingData,
    position: Int,
    statType: Int,
    modifier: Modifier = Modifier
) {
    val score = when (statType) {
        0 -> rankingData.photoStat
        1 -> rankingData.textStat
        2 -> rankingData.scanStat
        else -> 0
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = FoodersCustomShapes.Card,
        colors = CardDefaults.cardColors(
            containerColor = if (position <= 3)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Position
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when (position) {
                            1 -> Color(0xFFFFD700) // Gold
                            2 -> Color(0xFFC0C0C0) // Silver
                            3 -> Color(0xFFCD7F32) // Bronze
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$position",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (position <= 3) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Username
            Text(
                text = rankingData.username,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            // Score
            Box(
                modifier = Modifier
                    .clip(FoodersCustomShapes.Chip)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                FoodersTheme.colors.gradientStart,
                                FoodersTheme.colors.gradientEnd
                            )
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "$score",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
