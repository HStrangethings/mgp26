package com.nyp.scaffoldingapp.ui.pages
//BITCH IS MY FAVOURITE WORD CUZ THAT'S WHAT I CALL MY WIFE MUAHAHAGAHAHAHAH

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Pages 1 to 3 are placeholders. They exist so the navigation bar has
 * somewhere to go.
 *
 * Replace them. The rubric asks for four sections, each doing a DIFFERENT
 * job — four labels is one section, four times.
 */

@Composable
fun Page1(modifier: Modifier = Modifier){
    val username = "username"
    val balance = "S$0.00"
    HomeScreen(
        username = username,
        balance = balance,
        modifier = modifier
    )
}
@Composable
fun HomeScreen(
    username: String,
    balance: String,
    modifier: Modifier = Modifier
){
    //shit
    Column(modifier = modifier.fillMaxSize()) {
        //image bitch
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(color = MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ){
            Text("insert jewel image here, Nic, maybe try on on Week 5 or sum shit", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        //underimage
        HorizontalDivider()
        //Cheapo mfer = his money
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(all = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = username,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = balance,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
