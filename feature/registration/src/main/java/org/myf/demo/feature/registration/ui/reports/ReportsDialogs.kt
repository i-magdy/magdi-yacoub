package org.myf.demo.feature.registration.ui.reports

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.myf.demo.ui.R as uiResource

@Composable
fun ReportsDeleteDialog(
    modifier: Modifier = Modifier,
    viewModel: ReportsViewModel
){
    val uiState = viewModel.uiState.collectAsState()
    if (uiState.value.deleteFile != null){
        Dialog(
            onDismissRequest = { viewModel.removeDeleteFileObserver() },
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

                        Text(
                            text = uiState.value.fileName,
                            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle
                        )
                        Spacer(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .weight(1f)
                        )
                        Icon(
                            modifier = Modifier.clickable { viewModel.removeDeleteFileObserver() },
                            painter = painterResource(id = uiResource.drawable.ic_close),
                            contentDescription = ""
                        )
                    }
                    Spacer(
                        modifier = modifier.padding(top = 16.dp)
                    )
                    Row{
                        Checkbox(
                            checked = uiState.value.isDeleteFileConfirmed,
                            onCheckedChange = { viewModel.confirmDeleteFile()}
                        )
                        Text(
                            text = stringResource(id = uiResource.string.delete_report_message),
                            modifier = modifier.padding(start = 8.dp, end = 16.dp)
                                .clickable { viewModel.confirmDeleteFile() }
                        )
                    }
                    Spacer(
                        modifier = modifier.padding(top = 16.dp)
                    )
                    Row (
                        modifier = modifier
                            .wrapContentWidth()
                            .align(Alignment.End)
                    ){
                        TextButton(
                            enabled = uiState.value.isDeleteFileConfirmed,
                            modifier = Modifier.weight(1f,false),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.error,
                                containerColor = Color.Transparent
                            ),
                            onClick = { if (uiState.value.isDeleteFileConfirmed) viewModel.deleteFile() }
                        ) {
                            Text(text = stringResource(id = uiResource.string.delete_title))
                        }
                        TextButton(onClick = { viewModel.removeDeleteFileObserver() }) {
                            Text(text = stringResource(id = uiResource.string.cancel_title))
                        }
                    }

                }

            }
        }
    }
}