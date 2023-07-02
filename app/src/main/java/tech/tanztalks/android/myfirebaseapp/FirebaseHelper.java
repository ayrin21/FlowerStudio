package tech.tanztalks.android.myfirebaseapp;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {
    private Context context;
    private PdfGenerator pdfGenerator;

    public FirebaseHelper(Context context) {
        this.context = context;
        pdfGenerator = new PdfGenerator(context);
    }

    public void fetchUsers() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Registered Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate over the users
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve the user's information
                    RegisteredUser user = userSnapshot.getValue(RegisteredUser.class);
                    if (user != null) {
                        // Generate PDF for the user
                        pdfGenerator.generatePdf(user);
                    }
                }
                Toast.makeText(context, "PDFs generated for all users", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if necessary
                Toast.makeText(context, "Failed to fetch user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
