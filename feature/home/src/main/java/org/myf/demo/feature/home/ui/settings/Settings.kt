package org.myf.demo.feature.home.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.myf.demo.feature.home.ui.main.HomeViewModel
import org.myf.demo.ui.R

@Composable
fun HomeSettingDialog(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
){
    val isOpen = viewModel.uiState.collectAsState()
    if (isOpen.value.isDialogOpen && !isOpen.value.isDialogClosed){
        Dialog(
            onDismissRequest = { viewModel.closeHomeSettingDialog() },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = modifier
                            .padding(all = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_language
                            ),
                            contentDescription = stringResource(
                                id = R.string.choose_fav_lang_title
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.home_settings_title),
                            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
                            modifier = modifier.padding(start = 16.dp)
                        )
                        Spacer(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .weight(1f)
                        )
                        Icon(
                            modifier = Modifier.clickable { viewModel.closeHomeSettingDialog() },
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = ""
                        )
                    }
                    Spacer(
                        modifier = modifier.padding(top = 16.dp)
                    )
                    val lang = viewModel.appLanguage.collectAsState()
                    OutlinedButton(
                        onClick = { viewModel.updateAppLanguage("ar") },
                        modifier = modifier
                            .align(
                                Alignment.CenterHorizontally
                            ).fillMaxWidth()
                            .padding(start = 48.dp, end = 48.dp),
                        colors = if (lang.value == "ar") ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ) else ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text(text = stringResource(id = R.string.arabic_title))
                    }
                    OutlinedButton(
                        onClick = { viewModel.updateAppLanguage("en") },
                        modifier = modifier
                            .align(
                                Alignment.CenterHorizontally
                            )
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 32.dp, start = 48.dp, end = 48.dp),
                        colors = if (lang.value == "en") ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ) else ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text(text = stringResource(id = R.string.english_title))
                    }
                }
            }
        }
    }
}