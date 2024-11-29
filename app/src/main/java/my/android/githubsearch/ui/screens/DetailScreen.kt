package my.android.githubsearch.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import my.android.githubsearch.R
import my.android.githubsearch.data.model.Contributor
import my.android.githubsearch.data.model.Repository
import my.android.githubsearch.ui.screens.components.sampleRepository
import my.android.githubsearch.ui.theme.StarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailScreen(
    repository: Repository,
    onBackClick: () -> Unit,
    onViewOnGithubClick: () -> Unit,
    contributors: List<Contributor>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        repository.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            RepositoryHeader(repository)
            RepositoryDescription(repository)
            RepositoryStats(repository)
            ContributorsSection(contributors)
            RepositoryActions(onViewOnGithubClick)
        }
    }
}

@Composable
private fun RepositoryHeader(repository: Repository) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = repository.owner.avatar_url,
            contentDescription = "Owner Avatar",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = repository.full_name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "@${repository.owner.login}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RepositoryDescription(repository: Repository) {
    var isExpanded by remember { mutableStateOf(false) }

    val shouldShowToggleButton = remember(repository.description) {
        !repository.description.isNullOrBlank() && repository.description.length > 100
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = repository.description ?: "No description provided",
            style = MaterialTheme.typography.bodyLarge,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.animateContentSize()
        )

        if (shouldShowToggleButton) {
            Spacer(modifier = Modifier.height(4.dp))

            TextButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = if (isExpanded) "Show less" else "Show more",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Composable
private fun RepositoryStats(repository: Repository) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatItem(
                icon = Icons.Filled.Star,
                value = repository.stargazers_count,
                label = "Stars",
                tint = StarColor
            )
            StatItem(
                icon = painterResource(id = R.drawable.ic_fork),
                value = repository.forks_count,
                label = "Forks"
            )
            StatItem(
                icon = painterResource(id = R.drawable.ic_watcher),
                value = repository.watchers_count,
                label = "Watchers"
            )
            StatItem(
                icon = painterResource(id = R.drawable.ic_issue),
                value = repository.open_issues_count,
                label = "Issues",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun StatItem(
    icon: Any,
    value: Int,
    tint: Color = MaterialTheme.colorScheme.primary,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        when (icon) {
            is ImageVector -> {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(24.dp)
                )
            }
            is Painter -> {
                Image(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(tint)
                )
            }
            else -> {
                throw IllegalArgumentException("Icon must be of type ImageVector or Painter")
            }
        }

        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ContributorsSection(contributors: List<Contributor>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Top Contributors",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(contributors) { contributor ->
                ContributorItem(contributor)
            }
        }
    }
}

@Composable
private fun ContributorItem(contributor: Contributor) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        AsyncImage(
            model = contributor.avatar_url,
            contentDescription = "Contributor Avatar",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = contributor.login,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "${contributor.contributions}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun RepositoryActions(onViewOnGithubClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = onViewOnGithubClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_github_black),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("View on GitHub")
        }
    }
}

val contributors = listOf(
    Contributor(
        avatar_url = "",
        html_url = "https://github.com/ayush19sinha",
        login = "Ayush",
        contributions = 7
    ),
    Contributor(
        avatar_url = "",
        html_url = "https://github.com/ayush19sinha",
        login = "Rahul Raj",
        contributions = 10
    ),
    Contributor(
        avatar_url = "",
        html_url = "https://github.com/ayush19sinha",
        login = "Shubham Gupta",
        contributions = 7
    ),
    Contributor(
        avatar_url = "",
        html_url = "https://github.com/ayush19sinha",
        login = "Ayush Sinha",
        contributions = 7
    )
)

@Preview
@Composable
private fun DetailScreenPreview() {
    RepositoryDetailScreen(onBackClick = {}, repository = sampleRepository,
        contributors = contributors, onViewOnGithubClick = {})
}

