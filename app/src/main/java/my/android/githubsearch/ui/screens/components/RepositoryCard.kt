package my.android.githubsearch.ui.screens.components

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import getLanguageColor
import my.android.githubsearch.R
import my.android.githubsearch.data.model.Repository
import my.android.githubsearch.data.model.RepositoryOwner
import my.android.githubsearch.ui.theme.StarColor
import my.android.githubsearch.utils.formatCount

@Composable
fun RepositoryCard(
    repository: Repository,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
                    model = remember(context, repository.owner.avatar_url) {
                        ImageRequest.Builder(context)
                            .data(repository.owner.avatar_url)
                            .crossfade(true)
                            .build()
                    },
                    placeholder = painterResource(R.drawable.ic_github),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = repository.full_name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "@${repository.owner.login}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = repository.description ?: "No description available",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            RepositoryMetaData(
                language = repository.language ?: "Unknown",
                starCount = repository.stargazersCount,
                forkCount = repository.forksCount,
                context = context
            )
        }
    }
}

@Composable
private fun RepositoryMetaData(
    language: String,
    starCount: Int,
    forkCount: Int,
    context: Context
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val languageColor = getLanguageColor(context, language)
            Canvas(modifier = Modifier.size(12.dp)) {
                drawCircle(color = Color(languageColor))
            }

            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = language,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Star count",
                tint = StarColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = formatCount(starCount),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_fork),
                contentDescription = "Fork count",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = formatCount(forkCount),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

val sampleRepository = Repository(
    id = 123456,
    name = "LearnKotlin",
    full_name = "ayush/learn-kotlin",
    description = "This is the best repository to learn Kotlin.",
    htmlUrl = "https://github.com/user/awesome-repo",
    stargazersCount = 1500,
    language = "Kotlin",
    forksCount = 250,
    updated_at = "24/11/24",
    isOfflineCache = false,
    owner = RepositoryOwner(
        avatar_url = "https://avatars.githubusercontent.com/u/143383811?v=4",
        html_url = "https://github.com/ayush19sinha",
        login = "Ayush"
    )
)

@Preview
@Composable
private fun RepositoryCardPreview() {
    RepositoryCard(repository = sampleRepository) {

    }
}