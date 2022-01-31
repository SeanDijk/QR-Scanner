package nl.seanvandijk.qrscanner

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog
import java.io.File
import java.net.URLConnection

@Composable
fun FilePicker(
    allowMultiSelection: Boolean = true,
    text: String = "Select file" + if (allowMultiSelection) "(s)" else "",
    onSelect: (files: Set<File>) -> Unit
) {
    Button(onClick = {
        onSelect(
            openFileDialog(
                ComposeWindow(),
                "Pick a file or multiple files",
                listOf("image/"),
                allowMultiSelection
            )
        )
    }) {
        Text(text)
    }
}


fun openFileDialog(
    window: ComposeWindow,
    title: String,
    mimeTypes: List<String>,
    allowMultiSelection: Boolean = true
): Set<File> {
    val files = FileDialog(window, title, FileDialog.LOAD).apply {
        isMultipleMode = allowMultiSelection

        // linux only
        setFilenameFilter { _, name ->
            name == null || mimeTypes.any {
                URLConnection.guessContentTypeFromName(name)?.startsWith(it) ?: false
            }
        }

        isVisible = true
    }.files
    return files.toSet()
}
