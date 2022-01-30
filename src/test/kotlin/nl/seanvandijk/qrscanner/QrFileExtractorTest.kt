package nl.seanvandijk.qrscanner

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.io.path.Path
import kotlin.io.path.absolute

internal class QrFileExtractorTest {

    @Test
    fun `scanQRsIn for single file`() {
        val given = Path("./src/test/resources/qrcodes.png").absolute().normalize().toFile()

        val result = QrFileExtractor().scanQRsIn(given)

        assertEquals(result, QrFileExtractor.ScanResult(
            1,
            mutableSetOf(
                "TDW-BNPX-C96-2VY",
                "MCX-LBCN-V7N-RZZ",
                "TLG-9PZC-9ZX-WL7",
                "TLD-VB4D-KCQ-X7V",
                "CZJ-MVLN-9T4-VGN",
                "C2B-RCBX-ZBQ-QP4",
                "TQQ-HKTC-G4V-WWH",
                "CX2-DBV9-29L-M7H"
            ),
            mutableSetOf()
        ))
    }

    @Test
    fun `scanQRsIn for directory`() {
        val given = Path("./src/test/resources/directoryForTest").absolute().normalize().toFile()

        val result = QrFileExtractor().scanQRsIn(given)

        assertEquals(result, QrFileExtractor.ScanResult(
            2,
            mutableSetOf(
                "TDW-BNPX-C96-2VY",
                "MCX-LBCN-V7N-RZZ",
                "TLG-9PZC-9ZX-WL7",
                "TLD-VB4D-KCQ-X7V",
                "CZJ-MVLN-9T4-VGN",
                "C2B-RCBX-ZBQ-QP4",
                "TQQ-HKTC-G4V-WWH",
                "CX2-DBV9-29L-M7H"
            ),
            mutableSetOf(
                "TDW-BNPX-C96-2VY",
                "MCX-LBCN-V7N-RZZ",
                "TLG-9PZC-9ZX-WL7",
                "TLD-VB4D-KCQ-X7V",
                "CZJ-MVLN-9T4-VGN",
                "C2B-RCBX-ZBQ-QP4",
                "TQQ-HKTC-G4V-WWH",
                "CX2-DBV9-29L-M7H"
            )
        ))
    }
}
