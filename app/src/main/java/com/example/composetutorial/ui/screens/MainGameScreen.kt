package com.example.composetutorial.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composetutorial.R
import com.example.composetutorial.maingame.*
import com.example.composetutorial.ui.screens.ui.theme.ComposeTutorialTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGameScreen(modifier: Modifier = Modifier, mainGameViewModel: MainGameViewModel) {
    val TAG = "MainGameScreen"
    val currentGameState by mainGameViewModel.currentGameState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    println("Current state in compose: $currentGameState")
    ComposeTutorialTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = androidx.compose.material3.MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(
                                modifier = Modifier,
                                onClick = {  },
                            ) {
                                Icon(Icons.Filled.ArrowBack, "backIcon")
                            }
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary),
                        title = {
                            Text(
                                stringResource(id = R.string.login_welcome),
                                style = MaterialTheme.typography.h6)
                        })
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) {contentPadding ->
                Box(modifier = Modifier.padding(contentPadding)){
                    Column(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Definition(modifier = Modifier,
                            mainGameViewModel = mainGameViewModel,
                            defin1 = currentGameState.definition1,
                            defin2 = currentGameState.definition2,
                            showDefinition2 = currentGameState.showDefinition2)

                        AnswerRow(modifier = Modifier,
                            answer = currentGameState.userInputList
                        )
                        Row(
                            modifier = Modifier.padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(modifier = Modifier.padding(horizontal = 4.dp),
                                onClick = {
                                    mainGameViewModel.checkUserAnswer()
                                    Log.d(TAG, "updated char click : ${currentGameState.userInputList} ")
                                }) {
                                Text(text = "Submit")
                            }
                            Button(modifier = Modifier.padding(horizontal = 4.dp),
                                onClick = {
                                    mainGameViewModel.clearUserInput()
                                    Log.d(TAG, "updated char clear : ${currentGameState.userInputList} ")
                                }) {
                                Icon(Icons.Filled.Backspace, "backIcon")
                            }
                        }
                        CharMesh(modifier = Modifier,
                            charList = currentGameState.hintList,
                            mainGameViewModel = mainGameViewModel,
                            gridSize = 5)
                    }
                }
                LaunchedEffect(Unit ) {
                    mainGameViewModel.validationEvent.collect { event ->
                        when(event) {
                            is ValidationEvent.Success -> {
                                snackbarHostState.showSnackbar(message = "Correct Answer")

                            }
                            is ValidationEvent.Failed -> {
                                snackbarHostState.showSnackbar(message = "Wrong Answer")
                            }
                        }
                    }
                }
            }

        }
    }

}

@Composable
fun AnswerRow(modifier: Modifier, answer: List<HintChar>){
    val scrollState = rememberScrollState()
    println("Scroll position ${scrollState.value}")
    Row(modifier = modifier.horizontalScroll(scrollState), horizontalArrangement = Arrangement.SpaceEvenly) {
        for (char in answer){
            SingleCharHolder(modifier = modifier, char = char.char.toString())
        }
    }
}

@Composable
fun SingleCharHolder(modifier: Modifier, char: String, ){
    Box(modifier = modifier
        .padding(8.dp)
        .size(48.dp)
        .clip(CircleShape)
        .background(androidx.compose.material3.MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center) {
        Text(modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
            text = char,
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary)

    }
}

@Composable
fun Definition(modifier: Modifier,
               mainGameViewModel: MainGameViewModel,
               defin1: String, defin2: String?,
               showDefinition2: Boolean){
    Column(modifier = modifier
        .padding(8.dp, 4.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier
            .padding(8.dp, 4.dp)
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(8.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
            )) {
            Text(modifier = Modifier.padding(8.dp), text = defin1, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
        }
        defin2?.let {
            Divider(Modifier.padding(2.dp, 8.dp, 2.dp, 8.dp), thickness = 1.dp)
            BlurTextWithButton(modifier = modifier, text = defin2, showDefinition2 = showDefinition2, onClick = {
                println("Additional hint requested")
                mainGameViewModel.getAdditionalHint()
            })
        }
    }
}

@Composable
fun BlurTextWithButton( modifier: Modifier, text: String, showDefinition2: Boolean, onClick: ()-> Unit) {
    println("Should show definition2: $showDefinition2")
    Box(modifier = Modifier
        .padding(8.dp, 4.dp)
        .fillMaxWidth()
        .background(
            shape = RoundedCornerShape(8.dp),
            color = androidx.compose.material3.MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
        )) {
        Text(modifier = Modifier
            .padding(8.dp)
            .defaultMinSize(minHeight = ButtonDefaults.MinHeight + 8.dp)
            .graphicsLayer(renderEffect = if (!showDefinition2) BlurEffect(8f, 8f) else null), text = text)
        if (!showDefinition2){
            Button(modifier = Modifier
                .padding(4.dp)
                .align(Alignment.Center), onClick = onClick) {
                Text(text = "Check another definition", fontStyle = FontStyle.Italic)
            }
        }
    }
}

@Composable
fun CharMesh(modifier: Modifier, mainGameViewModel: MainGameViewModel?, charList: List<HintChar>, gridSize: Int){
    //charList must be of max size 25 (5*5 grid)
    LazyVerticalGrid(columns = GridCells.Fixed(gridSize),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ){
        items(charList){ item: HintChar ->
            CharBlock(
                modifier = modifier,
                mainGameViewModel = mainGameViewModel,
                hintChar = item,)
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun CharBlock(modifier: Modifier, mainGameViewModel: MainGameViewModel?, hintChar: HintChar,){
    Log.d("CharBlock", "hintChar: $hintChar")
    val bgColor = if (hintChar.isSelected){
        androidx.compose.material3.MaterialTheme.colorScheme.tertiary
    }else{
        androidx.compose.material3.MaterialTheme.colorScheme.secondary
    }
    Box(modifier = modifier
        .padding(4.dp)
        .size(56.dp)
        .clip(RoundedCornerShape(35,))
        .background(
            color = if (hintChar.char == null) androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(
                alpha = 0f
            ) else bgColor
        )
        .clickable(enabled = hintChar.char != null, onClick = {
            if (hintChar.char != null && hintChar.isClickable) {
                mainGameViewModel?.onHintCharClick(hintChar.char, hintChar.index)
            }
        })){
        if (hintChar.char != null){
            Text(modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
                text = hintChar.char.toString(),
                textAlign = TextAlign.Center,
                fontSize = 32.sp)
        }
    }

}

@Preview
@Composable
fun Preview(){
    BlurTextWithButton(modifier = Modifier, "What is the opposite of hide? ; divulge · disclose ; uncloak · bare ; unearth · betray ; lay bare · expose to view ; bring to light · give away.", false, {})
}