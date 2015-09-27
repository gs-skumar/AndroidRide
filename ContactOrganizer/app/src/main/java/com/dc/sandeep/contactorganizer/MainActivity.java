package com.dc.sandeep.contactorganizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dc.sandeep.contactorganizer.com.dc.beans.contact.ContactDetail;
import com.dc.sandeep.contactorganizer.com.dc.beans.contact.TestAbcd;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText nametxt,emailTxt,phoneTxt,addressTxt;
    List<ContactDetail> contactDetailsList = new ArrayList<>();
    ListView contactListView;
    ImageView contactImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestAbcd testAbcd = new TestAbcd();
        nametxt = (EditText)findViewById(R.id.pName);
        emailTxt = (EditText)findViewById(R.id.emailId);
        phoneTxt = (EditText)findViewById(R.id.phoneNo);
        addressTxt = (EditText)findViewById(R.id.address);
        contactListView = (ListView)findViewById(R.id.listView);
        contactImage = (ImageView)findViewById(R.id.imgId);
        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
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
                addContact(nametxt.getText().toString(),phoneTxt.getText().toString(),emailTxt.getText().toString(),addressTxt.getText().toString());
                populateListView();
                Toast.makeText(getApplicationContext(),"Contact has been created!",Toast.LENGTH_SHORT).show();
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
                startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
            }
        });
    }

    public void onActivityResult(int reqCode,int resCode,Intent data){
        if(reqCode == RESULT_OK){
            if(resCode == 1){
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

    public  void addContact(String name,String phone, String email,String address){
        contactDetailsList.add(new ContactDetail(name,phone,email,address));
    }

    public void populateListView(){
        ArrayAdapter<ContactDetail> adapter = new ContactAdapter();
        contactListView.setAdapter(adapter);
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

            return view;
        }
    }
}
