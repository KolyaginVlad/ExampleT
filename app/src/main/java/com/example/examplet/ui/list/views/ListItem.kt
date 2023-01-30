package com.example.examplet.ui.list.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.examplet.ui.list.ItemState

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ListItem(
    data: ItemState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                modifier = Modifier
                    .fillMaxSize(0.2f)
                    .height(100.dp)
                    .padding(4.dp),
                model = data.imageUrl,
                contentDescription = null
            ) {
                it.fitCenter().centerCrop()
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = data.subTitle,
                    style = MaterialTheme.typography.subtitle1.copy(Color.Gray)
                )
            }
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = "${data.price}$",
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Preview
@Composable
private fun ListItemPreview() {
    ListItem(
        data = ItemState(
            title = "title",
            subTitle = "subTitle",
            price = 500.5f,
            imageUrl = "https://media.baamboozle.com/uploads/images/351028/1642483532_406091_url.jpeg"
        )
    )
}