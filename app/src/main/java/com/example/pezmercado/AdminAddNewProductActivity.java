package com.example.pezmercado;

import android.app.AlertDialog;
//import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String CategoryName, ScientificName, CommonName, LocalName;
    private String ProductForm, DateCaught, PlaceCaught, Availability;
    private String PriceKg, MinimumOrder, InStock;
    private String saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputScientificName, InputCommonName, InputLocalName;
    private EditText InputProductForm, InputDateCaught, InputPlaceCaught, InputAvailability;
    private EditText InputPriceKg, InputMinimumOrder, InputInStock;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    //private ProgressDialog loadingBar;
    private AlertDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputScientificName = (EditText) findViewById(R.id.scientific_name);
        InputCommonName = (EditText) findViewById(R.id.common_name);
        InputLocalName = (EditText) findViewById(R.id.local_name);
        InputProductForm = (EditText) findViewById(R.id.product_form);
        InputDateCaught = (EditText) findViewById(R.id.date_caught);
        InputPlaceCaught = (EditText) findViewById(R.id.place_caught);
        InputAvailability = (EditText) findViewById(R.id.availability);
        InputPriceKg = (EditText) findViewById(R.id.price_kg);
        InputMinimumOrder = (EditText) findViewById(R.id.minimum_order);
        InputInStock = (EditText) findViewById(R.id.in_stock);
        //loadingBar = new ProgressDialog(this);
        loadingBar = new SpotsDialog.Builder().setContext(this).build();

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    
                OpenGallery();
            }

        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }
        });

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData(){
        ScientificName = InputScientificName.getText().toString();
        LocalName = InputLocalName.getText().toString();
        CommonName = InputCommonName.getText().toString();
        ProductForm = InputProductForm.getText().toString();
        DateCaught = InputDateCaught.getText().toString();
        PlaceCaught = InputPlaceCaught.getText().toString();
        Availability = InputAvailability.getText().toString();
        PriceKg = InputPriceKg.getText().toString();
        MinimumOrder = InputMinimumOrder.getText().toString();
        InStock = InputInStock.getText().toString();

        if(ImageUri == null){
            Toast.makeText(this, "Product image is mandatory.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(ScientificName)){
            Toast.makeText(this, "Please write scientific name.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(LocalName)){
            Toast.makeText(this, "Please write local name.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(CommonName)){
            Toast.makeText(this, "Please write common name.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(ProductForm)){
            Toast.makeText(this, "Please write product name.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(DateCaught)){
            Toast.makeText(this, "Please write date caught.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(PlaceCaught)){
            Toast.makeText(this, "Please write place caught.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Availability)){
            Toast.makeText(this, "Please write availability of the product.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(PriceKg)){
            Toast.makeText(this, "Please write price in kg.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(MinimumOrder)){
            Toast.makeText(this, "Please write the minimum order.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(InStock)){
            Toast.makeText(this, "Please write if product is in stock.", Toast.LENGTH_SHORT).show();
        }else{
            StoreProductInformation();
        }
    }

    private void StoreProductInformation(){
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Seller, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filepath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filepath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Product image uploaded successfully.", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewProductActivity.this, "got the product image Url successfully.", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase(){
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("scientificName", ScientificName);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", CategoryName);
        productMap.put("commonName", CommonName);
        productMap.put("localName", LocalName);
        productMap.put("productForm", ProductForm);
        productMap.put("dateCaught", DateCaught);
        productMap.put("placeCaught", PlaceCaught);
        productMap.put("availability", Availability);
        productMap.put("price", PriceKg);
        productMap.put("minOrder", MinimumOrder);
        productMap.put("stock", InStock);

        ProductsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent( AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);

                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "Product is added successfully.", Toast.LENGTH_SHORT).show();
                }else{
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
