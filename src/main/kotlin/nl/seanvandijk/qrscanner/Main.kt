package nl.seanvandijk.qrscanner

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import nl.seanvandijk.qrscanner.icons.ContentCopy
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection

@Composable
@Preview
fun App() {
    var scanResult by mutableStateOf<QrFileExtractor.ScanResult?>(null)
    var codes by mutableStateOf(listOf<String>())

    MaterialTheme {
        Column(modifier = Modifier.padding(8.dp)) {
            FilePicker {
                scanResult = QrFileExtractor().scanQRsIn(it)
                codes.toMutableList().removeAll { true }
                codes = codes + scanResult!!.codes
            }
            ScanResult(scanResult, codes)

            QrResultList(codes)
        }
    }
}

@Composable
private fun QrResultList(codes: List<String>) {
    Box(
        Modifier.fillMaxSize()
    ) {
        val state = rememberLazyListState()

        SelectionContainer {
            LazyColumn(Modifier.fillMaxSize().padding(end = 12.dp), state) {
                items(codes) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(it)
                        DisableSelection {
                            IconButton(onClick = { setClipboard(it); }) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy QR content",
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            )
        )
    }
}

@Composable
private fun ScanResult(
    scanResult: QrFileExtractor.ScanResult?,
    codes: List<String>
) {
    if (scanResult == null) {
        Text("Nothing scanned yet.")
    }
    scanResult?.let {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Scanned ${it.filesScanned} files, found ${it.codes.size} codes and ${it.duplicates.size} duplicates.")
            Button(onClick = {
                setClipboard(codes.reduce { acc, s -> acc.plus("\n").plus(s) })
            }) {
                Text("Copy all to clipboard")
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

fun setClipboard(s: String) {
    val selection = StringSelection(s)
    val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(selection, selection)
}
