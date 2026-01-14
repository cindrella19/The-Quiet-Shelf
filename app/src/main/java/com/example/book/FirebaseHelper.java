package com.example.book;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {

    private static FirebaseFirestore db;
    private static FirebaseAuth auth;

    // ===============================
    // 1️⃣ INIT (CALL ONCE)
    // ===============================
    public static void init() {
        if (db != null && auth != null) return;

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings =
                new FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(true)
                        .build();

        db.setFirestoreSettings(settings);
    }

    // ===============================
    // 2️⃣ AUTH HELPERS
    // ===============================
    public static boolean isLoggedIn() {
        return auth != null && auth.getCurrentUser() != null;
    }

    @Nullable
    public static String getUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public static FirebaseAuth getAuth() {
        return auth;
    }

    // ===============================
    // 3️⃣ FIRESTORE REFERENCES
    // ===============================
    private static CollectionReference getBooksCollection() {
        String userId = getUserId();
        if (userId == null) return null;

        return db.collection("users")
                .document(userId)
                .collection("books");
    }

    private static DocumentReference getProfileDocument() {
        String userId = getUserId();
        if (userId == null) return null;

        return db.collection("users")
                .document(userId)
                .collection("profile")
                .document("info");
    }

    // ===============================
    // 4️⃣ SAVE BOOK (EVALUATION IMPORTANT)
    // ===============================
    public static void saveBook(Book book) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || book == null) return;

        Map<String, Object> data = new HashMap<>();
        data.put("title", book.getTitle());
        data.put("vibe1", book.getVibe1());
        data.put("vibe2", book.getVibe2());
        data.put("vibe3", book.getVibe3());
        data.put("quote", book.getQuote());
        data.put("rating", book.getRating());
        data.put("createdAt", FieldValue.serverTimestamp());

        db.collection("users")
                .document(user.getUid())
                .collection("books")
                .add(data)
                .addOnFailureListener(e ->
                        Log.e("Firebase", "Book save failed", e)
                );
    }

    // ===============================
    // 5️⃣ SYNC ROOM → FIRESTORE
    // ===============================
    public static void syncBookToFirestore(Book book) {
        CollectionReference booksRef = getBooksCollection();
        if (booksRef == null || book == null) return;

        String cloudId = String.valueOf(book.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("cloudId", cloudId);
        data.put("title", book.getTitle());
        data.put("vibes", Arrays.asList(
                book.getVibe1(),
                book.getVibe2(),
                book.getVibe3()
        ));
        data.put("quote", book.getQuote());
        data.put("rating", book.getRating());
        data.put("updatedAt", FieldValue.serverTimestamp());

        booksRef.document(cloudId)
                .set(data)
                .addOnFailureListener(e ->
                        Log.e("Firebase", "Book sync failed", e)
                );
    }

    // ===============================
    // 6️⃣ DELETE BOOK
    // ===============================
    public static void deleteBookFromFirestore(int bookId) {
        CollectionReference booksRef = getBooksCollection();
        if (booksRef == null) return;

        booksRef.document(String.valueOf(bookId)).delete();
    }

    // ===============================
    // 7️⃣ PROFILE SAVE / UPDATE
    // ===============================
    public static void updateProfile(
            String displayName,
            String email,
            Runnable onSuccess
    ) {
        DocumentReference profileRef = getProfileDocument();
        if (profileRef == null) return;

        Map<String, Object> data = new HashMap<>();
        data.put("displayName", displayName);
        data.put("email", email);
        data.put("updatedAt", FieldValue.serverTimestamp());

        profileRef.set(data)
                .addOnSuccessListener(unused -> {
                    if (onSuccess != null) onSuccess.run();
                })
                .addOnFailureListener(e ->
                        Log.e("Firebase", "Profile update failed", e)
                );
    }

    // ===============================
    // 8️⃣ CLOUD → LOCAL SYNC
    // ===============================
    public static void fetchAndSync(Context context, Runnable onComplete) {
        CollectionReference booksRef = getBooksCollection();

        if (booksRef == null) {
            if (onComplete != null) onComplete.run();
            return;
        }

        booksRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                AppDatabase localDb = AppDatabase.getInstance(context);
                QuerySnapshot snapshot = task.getResult();

                for (DocumentSnapshot doc : snapshot.getDocuments()) {
                    syncDocToLocal(doc, localDb);
                }
            }

            if (onComplete != null) onComplete.run();
        });
    }

    // ===============================
    // 9️⃣ SINGLE DOC → ROOM
    // ===============================
    private static void syncDocToLocal(
            DocumentSnapshot doc,
            AppDatabase localDb
    ) {
        try {
            String cloudId = doc.getString("cloudId");
            String title = doc.getString("title");
            List<String> vibes = (List<String>) doc.get("vibes");
            String quote = doc.getString("quote");
            Long ratingLong = doc.getLong("rating");

            if (cloudId == null || title == null || vibes == null || vibes.size() < 3) {
                return;
            }

            int rating = ratingLong != null ? ratingLong.intValue() : 0;

            Book existing = localDb.bookDao().getBookByCloudId(cloudId);

            if (existing == null) {
                Book book = new Book(
                        title,
                        vibes.get(0),
                        vibes.get(1),
                        vibes.get(2),
                        quote,
                        rating
                );

                book.setCloudId(cloudId);
                localDb.bookDao().insertBook(book);
            }

        } catch (Exception e) {
            Log.e("Firebase", "Local sync failed", e);
        }
    }
}
