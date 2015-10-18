package com.dc.sandeep.contactorganizer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dc.sandeep.contactorganizer.com.dc.beans.contact.ContactDetail;
import com.dc.sandeep.contactorganizer.com.dc.beans.contact.com.dc.MyGestureDetector;
import com.dc.sandeep.contactorganizer.databaseImpl.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT = 0,DELETE = 1;

    EditText nametxt,emailTxt,phoneTxt,addressTxt;
    List<ContactDetail> contactDetailsList = new ArrayList<>();
    ListView contactListView;
    ImageView contactImage;
    Uri imageUri = Uri.parse("android.resourse://com.dc.sandeep.contactorganizer/drawable/abc_ic_menu_paste_mtrl_am_alpha");
    private static int contactId;
    DatabaseHandler dbDatabaseHandler;
    int contactSelected;
    ArrayAdapter<ContactDetail> contactDetailArrayAdapter;
    private boolean isEditMode;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nametxt = (EditText)findViewById(R.id.pName);
        emailTxt = (EditText)findViewById(R.id.emailId);
        phoneTxt = (EditText)findViewById(R.id.phoneNo);
        addressTxt = (EditText)findViewById(R.id.address);
        contactListView = (ListView)findViewById(R.id.listView);
        contactImage = (ImageView)findViewById(R.id.imgId);
        dbDatabaseHandler = new DatabaseHandler(getApplicationContext());

       // registerForContextMenu(contactListView);
        contactListView.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(new MyGestureDetector(MainActivity.this));

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
/*
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contactSelected = position;
                Log.e("Position", "" + position);
            }
        });
      */
        contactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                contactSelected = position;
                return false;
            }
        });
        final TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.creatortab);
        tabSpec.setIndicator("Create Contact");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("show");
        tabSpec.setContent(R.id.showContactTab);
        tabSpec.setIndicator("Show");
        tabHost.addTab(tabSpec);

        final Button addContact = (Button)findViewById(R.id.submit);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditMode){
                    ContactDetail newContactDetail = contactDetailsList.get(contactSelected);
                    newContactDetail.setName(String.valueOf(nametxt.getText()));
                    newContactDetail.setPhoneNo(String.valueOf(phoneTxt.getText()));
                    newContactDetail.setEmail(String.valueOf(emailTxt.getText()));
                    newContactDetail.setAddress(String.valueOf(addressTxt.getText()));
                    newContactDetail.setImageuri(imageUri);
                     dbDatabaseHandler.updateContact(newContactDetail);
                    isEditMode = false;
                    addContact.setText("ADD CONTACT");
                    contactDetailArrayAdapter.notifyDataSetChanged();
                    resetAddContactPanel();
                    tabHost.setCurrentTab(1);
                }else{
                    addContact(contactId++, nametxt.getText().toString(), phoneTxt.getText().toString(), emailTxt.getText().toString(), addressTxt.getText().toString(), imageUri);
                    contactDetailArrayAdapter.notifyDataSetChanged();
                }
            }
        });

        nametxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addContact.setEnabled(!nametxt.getText().toString().trim().equals(""));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
            }
        });

        contactDetailsList.addAll(dbDatabaseHandler.getAllContacts());
        populateListView();
        //gestureDetector = new GestureDetector(new MyGestureDetector(this));
    }

    public void onActivityResult(int reqCode,int resCode,Intent data){
        if(resCode == RESULT_OK){
            if(reqCode == 1){
                imageUri = (Uri)data.getData();
                Log.d("Image",imageUri.toString());
                contactImage.setImageURI(data.getData());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public  void addContact(int id,String name,String phone, String email,String address,Uri imageUri){
        ContactDetail contactDetail = new ContactDetail(name,phone,email,address,imageUri);
        contactDetail.setId(id);
        if(!isContactExist(contactDetail)){
            dbDatabaseHandler.insertContact(contactDetail);
            contactDetailsList.add(contactDetail);
            Toast.makeText(getApplicationContext(), "Contact has been created!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Contact already exist", Toast.LENGTH_SHORT).show();
        }

    }

    public void populateListView(){
        contactDetailArrayAdapter = new ContactAdapter();
        contactListView.setAdapter(contactDetailArrayAdapter);
    }
    public class  ContactAdapter extends ArrayAdapter<ContactDetail>{
        public  ContactAdapter(){
            super(MainActivity.this,R.layout.list_contact,contactDetailsList);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null){
                view =  getLayoutInflater().inflate(R.layout.list_contact,parent,false);
            }
            ContactDetail currentContact = contactDetailsList.get(position);
            TextView name = (TextView) view.findViewById(R.id.listContactname);
            name.setText(currentContact.getName());

            TextView phone = (TextView) view.findViewById(R.id.listPhone);
            phone.setText(currentContact.getPhoneNo());

            TextView email = (TextView) view.findViewById(R.id.listEmail);
            email.setText(currentContact.getEmail());

            TextView address = (TextView) view.findViewById(R.id.listAddress);
            address.setText(currentContact.getAddress());

            ImageView savedImage = (ImageView)view.findViewById(R.id.savedImage);
            savedImage.setImageURI(currentContact.getImageuri());

            return view;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderIcon(R.drawable.edit_file);
        menu.setHeaderTitle("Choose Edit Option.");
        menu.add(Menu.NONE, EDIT, 1, "Edit Contact");
        menu.add(Menu.NONE,DELETE,2,"Delete Contact");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case EDIT:
               enableEditMode(contactDetailsList.get(contactSelected));
                break;
            case DELETE:
                dbDatabaseHandler.deleteContact(contactDetailsList.get(contactSelected));
                contactDetailsList.remove(contactSelected);
                contactDetailArrayAdapter.notifyDataSetChanged();
                break;
        }

        return super.onContextItemSelected(item);
    }

    private Boolean isContactExist(ContactDetail contactDetail){
        return dbDatabaseHandler.getContact(contactDetail.getName()) != null ? true : false;
    }

    private void  enableEditMode(ContactDetail contactDetail){
        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setCurrentTab(0);
        nametxt.setText(contactDetail.getName());
        phoneTxt.setText(contactDetail.getPhoneNo());
        emailTxt.setText(contactDetail.getEmail());
        addressTxt.setText(contactDetail.getAddress());
        imageUri = contactDetail.getImageuri();
        contactImage.setImageURI(imageUri);
        Button edit = (Button)findViewById(R.id.submit);
        edit.setText("Update");
        isEditMode = true;
    }

    private void resetAddContactPanel(){
        nametxt.setText("");
        phoneTxt.setText("");
        emailTxt.setText("");
        addressTxt.setText("");
        contactImage.setImageURI(Uri.parse("android.resourse://com.dc.sandeep.contactorganizer/abc_ic_menu_paste_mtrl_am_alpha"));
    }

    public void onLeftSwipe(){
        enableEditMode(contactDetailsList.get(contactSelected));
        Toast.makeText(getApplicationContext(), "Swiped Left!", Toast.LENGTH_SHORT).show();
    }
    public void onRightSwipe(){
        Toast.makeText(getApplicationContext(), "Contact Deleted!", Toast.LENGTH_SHORT).show();
        dbDatabaseHandler.deleteContact(contactDetailsList.get(contactSelected));
        contactDetailsList.remove(contactSelected);
        contactDetailArrayAdapter.notifyDataSetChanged();
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gestureDetector.onTouchEvent(event))
            return true;
        return super.onTouchEvent(event);
    }*/
}
