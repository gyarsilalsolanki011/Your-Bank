package com.gyarsilalsolanki011.bankingapp.ui.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.databinding.ActivityAboutUsBinding;
import com.gyarsilalsolanki011.bankingapp.databinding.ActivityContactUsBinding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

public class ContactUsActivity extends AppCompatActivity {
    private ActivityContactUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Toolbar
        setSupportActionBar(binding.toolbar);

        // Enable Back Button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Drawable navIcon = binding.toolbar.getNavigationIcon();
        if (navIcon != null) {
            navIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        getSupportActionBar().setTitle("Contact Us");

        // Open Email
        binding.email.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:support@yourbank.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Customer Inquiry");
            startActivity(intent);
        });

        // Call Support
        binding.phone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+917620824421"));
            startActivity(intent);
        });

        // Open Social Media Links
        binding.facebook.setOnClickListener(v -> openLink("https://facebook.com/YourBank"));

        binding.twitter.setOnClickListener(v -> openLink("https://twitter.com/YourBank"));

        binding.linkedin.setOnClickListener(v -> openLink("https://linkedin.com/company/YourBank"));

        binding.whatsapp.setOnClickListener(v -> {
            String uriText = "https://api.whatsapp.com/send/"+"?phone="+"7909519946"+"&text="+
                    convertToUrlFormat("Hello Your Bank team, Please approve my internet banking request")+"&type=phone_number&app_absent=0";
            openLink(uriText);
        });

        // Send Message to whatsapp
        binding.sendMessage.setOnClickListener(v -> {
            if (binding.messageInput.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Please Enter the message.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Handle back button
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public String convertToUrlFormat(String inputText) {
        try {
            return URLEncoder.encode(inputText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return inputText; // Return original text in case of error
        }
    }
}