package com.dharbor;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ImageToPdfConverter {

    public static void main(String arg[]) throws Exception {
        convertImageToPdf("D:\\tmp\\input", "D:\\tmp\\output", "output.pdf");
    }

    public static void convertImageToPdf(final String fileInputPath, final String fileOuputPath,
                                         final String outputFileName) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(new File(fileOuputPath, outputFileName)));
        document.open();

//        List<File> files = Files.list(Paths.get(fileInputPath)).map(Path::toFile).collect(Collectors.toList());

        File directory = new File(fileInputPath);
        File[] files1 = directory.listFiles();
        Arrays.sort(files1, Comparator.comparingLong(File::lastModified));

        List<File> files = Arrays.asList(files1);

        files.forEach(f -> {
            document.newPage();

            Image image = null;
            try {
                image = Image.getInstance(new File(fileInputPath, f.getName()).getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }

            image.setAlignment(Element.ALIGN_CENTER);

            float imageWidth = image.getWidth();

            if (imageWidth > document.getPageSize().getWidth()) {
                int indentation = 0;
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()
                        - indentation) / image.getWidth()) * 100;
                image.scalePercent(scaler);
            }

            try {
                document.add(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        document.close();
    }
}
