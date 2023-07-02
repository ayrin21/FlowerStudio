package tech.tanztalks.android.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Color;

public class GeneratePdfActivity extends AppCompatActivity {

    private Button generatePdfButton;
    private FirebaseAuth mAuth;
    ActionBar actionBar;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_pdf);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Report");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        generatePdfButton = findViewById(R.id.generatePdfButton);
        mAuth = FirebaseAuth.getInstance();

        generatePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    generatePdf(userId);
                } else {
                    Toast.makeText(GeneratePdfActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void generatePdf(String userId) {
        // Get the user's information from the Firebase database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user exists in the database
                if (dataSnapshot.exists()) {
                    // Retrieve the user's information
                    RegisteredUser user = dataSnapshot.getValue(RegisteredUser.class);
                    if (user != null) {
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
                        // Set the color for the header text (e.g., red)
                        // Set the font size and style for the header
                        paint.setTextSize(36);
                        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

// Set the color for the header text
                        paint.setColor(Color.BLACK);

// Calculate the x-coordinate to center the text
                        float centerX = (canvas.getWidth() - paint.measureText("Flower Studio")) / 2;

// Draw the header text at the center of the canvas
                        canvas.drawText("Flower Studio", centerX, 100, paint);

// Set the font size and style for the section title
                        paint.setTextSize(24);
                        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

// Set the color for the section title
                        paint.setColor(Color.DKGRAY);

// Draw the section title
                        canvas.drawText("Personal Information", 50, 200, paint);

// Set the font size and style for the user's information
                        paint.setTextSize(18);
                        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

// Set the color for the user's information
                        paint.setColor(Color.BLACK);

// Draw the user's information one after another
                        int y = 250; // Initial y-coordinate for the first line of user's information
                        canvas.drawText("Name: " + user.getFullName(), 50, y, paint);
                        y += 50; // Increment y-coordinate for the next line
                        canvas.drawText("Mobile: " + user.getMobile(), 50, y, paint);
                        y += 50;
                        canvas.drawText("Gender: " + user.getGender(), 50, y, paint);
                        y += 50;
                        canvas.drawText("Date of Birth: " + user.getDoB(), 50, y, paint);
                        y += 50;
                        canvas.drawText("Email: " + user.getEmail(), 50, y, paint);

                        // Finish the page
                        document.finishPage(page);

                        // Save the PDF file
                        //File file = new File(Environment.getExternalStorageDirectory(), "UserInfo.pdf");
                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "UserInfo.pdf");

                        try {
                            document.writeTo(new FileOutputStream(file));
                            Toast.makeText(GeneratePdfActivity.this, "PDF generated successfully", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(GeneratePdfActivity.this, "Failed to generate PDF", Toast.LENGTH_SHORT).show();
                        }

                        // Close the document
                        document.close();
                    }
                } else {
                    // User does not exist in the database
                    Toast.makeText(GeneratePdfActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if necessary
                Toast.makeText(GeneratePdfActivity.this, "Failed to fetch user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();//goto previous activity
        return super.onSupportNavigateUp();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate menu items
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // When any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_refresh){
            //Refresh Activity
            startActivity((getIntent()));
            finish();

        } else if(id == R.id.menu_flower_classification){
            Intent intent = new Intent(GeneratePdfActivity.this, FlowerClassification.class);
            startActivity(intent);

        }
        else if(id == R.id.menu_watch_video){
            Intent intent = new Intent(GeneratePdfActivity.this, VideoActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.menu_location){
            Intent intent = new Intent(GeneratePdfActivity.this, LocationActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_reviews){
            Intent intent = new Intent(GeneratePdfActivity.this, AddReviewActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_panel){
            Intent intent = new Intent(GeneratePdfActivity.this, ShowPostActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_user_list){
            Intent intent = new Intent(GeneratePdfActivity.this, UserListActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_library){
            Intent intent = new Intent(GeneratePdfActivity.this, FlowerLibraryActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_flower_dictionary){
            Intent intent = new Intent(GeneratePdfActivity.this, FlowerDicActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_contact_us) {
            Intent intent = new Intent(GeneratePdfActivity.this, ContactUs.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_know_plant) {
            Intent intent = new Intent(GeneratePdfActivity.this, GraphQLMain.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_report) {
            Intent intent = new Intent(GeneratePdfActivity.this, GeneratePdfActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.menu_sign_out){
            authProfile.signOut();
            Toast.makeText(GeneratePdfActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(GeneratePdfActivity.this, MainActivity.class);

            // Clear stack to prevent user coming back to UserActivity on pressing back button signing out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Closer UserActivity after signing out
        } else{
            Toast.makeText(GeneratePdfActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}