package com.example.waitinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waitinglist.Data.WaitListContract;
import com.example.waitinglist.Data.WaitListDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private GuestAdapter guestAdapter;
    private ImageView listImage;
    private Cursor cursor;
    private WaitListDBHelper waitListDBHelper;
    private static final String TAG = "MainActivity";
    private Dialog dialog;
    private EditText addEditText;
    private EditText addSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler_view);
        listImage = findViewById(R.id.main_image);

        waitListDBHelper = new WaitListDBHelper(getApplicationContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));

        cursor = waitListDBHelper.getGuests();
        guestAdapter = new GuestAdapter(getApplicationContext(), cursor);
        recyclerView.setAdapter(guestAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });
        if(cursor.getCount() != 0){
            listImage.setVisibility(View.GONE);
        }else{
            listImage.setVisibility(View.VISIBLE);
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                waitListDBHelper.remove(id);
                guestAdapter.swapCursor(waitListDBHelper.getGuests());

                if(waitListDBHelper.getGuests().getCount() == 0){
                    listImage.setVisibility(View.VISIBLE);
                }
            }
        }).attachToRecyclerView(recyclerView);

    }


    public void showAddDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_guest_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //Custom Toast
        final View layout = getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_group));
        TextView toastView = findViewById(R.id.toast_text);
        final Toast toast = new Toast(getApplicationContext());


        //Handle Dialog Buttons
        Button addButton = dialog.findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditText = dialog.findViewById(R.id.add_guest_name);
                addSize = dialog.findViewById(R.id.add_party_size);
                String name = addEditText.getText().toString();
                String size = addSize.getText().toString();
                if (name.length() != 0 && size.length() != 0) {
                    waitListDBHelper.addGuest(name, size);
                    listImage.setVisibility(View.GONE);
                    guestAdapter.swapCursor(waitListDBHelper.getGuests());
                    dialog.dismiss();
                }else{
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
            }
        });

        Button backButton = dialog.findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
    }
}

