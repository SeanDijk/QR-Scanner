package nl.seanvandijk.qrscanner

import boofcv.factory.fiducial.FactoryFiducial
import boofcv.io.image.UtilImageIO
import boofcv.kotlin.loadImage
import boofcv.struct.image.GrayU8
import boofcv.struct.image.ImageType
import java.io.File
import kotlin.collections.HashSet


class QrFileExtractor {

    private val detector = FactoryFiducial.qrcode(null, GrayU8::class.java)
    private val result: ScanResult = ScanResult()

    fun scanQRsIn(files: Collection<File>): ScanResult {
        files.forEach { scanQRsIn(it) }
        return result
    }

    fun scanQRsIn(file: File): ScanResult {
        if (file.isDirectory) {
            println("is directory")
            val walk = file.walk()
            walk.onFail { it, ioException ->
                println("walk failed for $it: $ioException")
            }
            walk.iterator().forEach {
                manageFile(it)
            }
        } else {
            manageFile(file)
        }
        return result;
    }

    private fun manageFile(file: File) {
        if (file.isFile && UtilImageIO.isImage(file)) {
            println("$file is image")
            // Get the codes
            val image = file.absoluteFile.loadImage(ImageType.SB_U8)
            detector.process(image)
            val codes = detector.detections.map { it.message }

            // Check if we find any duplicates with previous values
            val duplicates: MutableSet<String> = HashSet(codes)
            duplicates.retainAll(result.codes)
            result.duplicates.addAll(duplicates)

            // Add the codes the the result object.
            result.codes.addAll(codes)
            result.filesScanned++
        }
    }

    data class ScanResult(
        var filesScanned: Int = 0,
        val codes: MutableSet<String> = mutableSetOf(),
        val duplicates: MutableSet<String> = mutableSetOf()
    )

}
