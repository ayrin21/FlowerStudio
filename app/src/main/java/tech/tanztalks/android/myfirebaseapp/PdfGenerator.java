package tech.tanztalks.android.myfirebaseapp;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenerator {
    private Context context;

    public PdfGenerator(Context context) {
        this.context = context;
    }

    public void generatePdf(RegisteredUser user) {
        // Create a new PDF document
        PdfDocument document = new PdfDocument();

        // Create a page and set its attributes
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        // Set the font size and style for the header
        paint.setTextSize(24);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // Draw the header
        canvas.drawText("Flower Studio", 50, 50, paint);
        canvas.drawText("Personal Information", 50, 100, paint);

        // Set the font size and style for the user's information
        paint.setTextSize(18);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // Draw the user's information one after another
        int y = 150; // Initial y-coordinate for the first line of user's information
        canvas.drawText("Name: " + user.getFullName(), 50, y, paint);
        y += 50; // Increment y-coordinate for the next line
        canvas.drawText("Mobile: " + user.getMobile(), 50, y, paint);
        y += 50;
        canvas.drawText("Gender: " + user.getGender(), 50, y, paint);

        // Finish the page
        document.finishPage(page);

        // Save the PDF file
        String fileName = user.getFullName() + ".pdf";
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "PDF generated successfully for " + user.getFullName(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to generate PDF for " + user.getFullName(), Toast.LENGTH_SHORT).show();
        }

        // Close the document
        document.close();
    }
}
