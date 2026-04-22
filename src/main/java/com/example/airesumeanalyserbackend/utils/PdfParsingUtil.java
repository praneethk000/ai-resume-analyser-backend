package com.example.airesumeanalyserbackend.utils;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

public class PdfParsingUtil {
    public static String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String parsedText = stripper.getText(document);

            return parsedText.replaceAll("\\r\\n|\\r|\\n", " ").trim();
        }
    }
}
