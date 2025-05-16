package com.kacperkk.postsapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kacperkk.postsapp.model.posts.PostWithUser

@Composable
fun PostListItem(
    postWithUser: PostWithUser,
    onPostClick: () -> Unit,
    onUserClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onPostClick)
            .padding(16.dp)
    ) {
        Text(
            text = postWithUser.post.title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "👤 ${postWithUser.user.name}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .clickable(onClick = onUserClick)
                .padding(top = 4.dp)
        )
    }
}
