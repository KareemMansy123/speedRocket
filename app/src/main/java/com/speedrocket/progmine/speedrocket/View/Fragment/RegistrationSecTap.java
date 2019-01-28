package com.speedrocket.progmine.speedrocket.View.Fragment;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.speedrocket.progmine.speedrocket.Model.AppConfig;
import com.speedrocket.progmine.speedrocket.Model.ResultModel;
import com.speedrocket.progmine.speedrocket.Model.UserApi;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.View.Activites.RegistrationCustom;
import com.squareup.picasso.Picasso;
import com.thomashaertel.widget.MultiSpinner;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.speedrocket.progmine.speedrocket.View.Activites.ProfileAccount.GET_FROM_GALLERY;

public class RegistrationSecTap extends Fragment {
    private static final int MODE_PRIVATE = 0;
    private static final int RESULT_OK = -1;
    private static final int REQUEST_CAMERA = 1888;
    private MultiSpinner spinner;
    CircleImageView image ;
    private ArrayAdapter<String> adapter;
    String imagePath ;
    List<String> interstList;
    ArrayAdapter<String> spinnerArrayAdapter;
    StringBuilder chosenInterestList = new StringBuilder();

    EditText name ,email , password ;
    CheckBox policyTermes ;
    TextView policeTermsText ;







    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            if (requestCode == GET_FROM_GALLERY) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }


            /*CircleImageView imageView = (CircleImageView) findViewById(R.id.profile_image_account);
            imageView.setImageBitmap(bitmap);*/


            // uploadProfileImageFast();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        RegistrationCustom.check =1 ;
        RegistrationCustom.buttonVisibalty(true);
        RegistrationCustom.next.setVisibility(View.VISIBLE);
    }
String defultImage = "@drawable/deful_profile_image" ;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view=inflater.inflate(R.layout.registration_sec_tap,
                    container,false);
            image = view.findViewById(R.id.profile_image);


            name  = view.findViewById(R.id.profileName);

            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        RegistrationCustom.secName = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            email  = view.findViewById(R.id.profileEmail);
            email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    RegistrationCustom.secEmail = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            password  = view.findViewById(R.id.reg_password);
            password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    RegistrationCustom.secPassword = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            policyTermes = view.findViewById(R.id.chekTermes);
             policyTermes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                     RegistrationCustom.secAccptTerms = b ;
                 }
             });

            image.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    startActivityForResult(new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            , GET_FROM_GALLERY);
                }
            });

            spinner = (MultiSpinner)view.findViewById(R.id.spinnerMulti);
            adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item);

            spinnerArrayAdapter = new ArrayAdapter<String>(
                    getContext(), R.layout.spinner_item);
             getIntersts();



            return view ;
        }

        public void getIntersts(){
            RegistrationCustom.setAnimation(true);
            Retrofit retrofit =AppConfig.getRetrofit2("https://speed-rocket.com/api/" , getContext());
            final UserApi userApi = retrofit.create(UserApi.class);

            final Call<ResultModel> getInterestConnection = userApi.getUsersInterest(getContext().getSharedPreferences("MyPref", 0).getString("langa",""));

            getInterestConnection.enqueue(new Callback<ResultModel>() {
                @Override
                public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                    try {
                        interstList = response.body().getType();
                        for (int i = 0; i < interstList.size(); i++) {
                            adapter.add(interstList.get(i));
                            spinner.setAdapter(adapter, false, onSelectedListener);
                            spinnerArrayAdapter.add(interstList.get(i));
                            spinnerArrayAdapter.notifyDataSetChanged();

                            Log.i("QP", interstList.get(i));


                            // set initial selection
                            boolean[] selectedItems = new boolean[adapter.getCount()];
                            selectedItems[i] = true; // select second item
                            spinner.setSelected(selectedItems);

                        } // for loop
//                    Toast.makeText(getBaseContext(), "Connection Success\n",
//                            Toast.LENGTH_LONG).show();
                        RegistrationCustom.setAnimation(false);
                    } // try
                    catch (Exception e) {
//                    Toast.makeText(getBaseContext(), "Connection Success\n" +
//                                    "Exception\n" + e.toString(),
//                            Toast.LENGTH_LONG).show();
                    } // catch
                } // onResponse

                @Override
                public void onFailure(Call<ResultModel> call, Throwable t) {

                    Toast.makeText(getContext(), "Connection Faild",
                            Toast.LENGTH_LONG).show();
                } // on Failure
            });
        }

    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items

            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    chosenInterestList.append(adapter.getItem(i)).append(",");
                }
            }
                RegistrationCustom.secIntersts = chosenInterestList.toString();
            Toast.makeText(getContext(), "Selected" +
                            chosenInterestList.toString()
                    , Toast.LENGTH_LONG).show();
        }
    };


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public void onCaptureImageResult(Intent data)
    {
        if (data !=null) {

           /* bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedimage = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/

            Uri imageUri = data.getData();
            image.setImageURI(imageUri);
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.ACTION_IMAGE_CAPTURE};

            Cursor cursor =getContext().getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = getRealPathFromURI(imageUri);


            }
            RegistrationCustom.secImagePath = imagePath ;

            Log.i("QP",imagePath);
        }
    } // onCaptureImageResult

    public void onSelectFromGalleryResult(Intent data)
    {
        if (data != null) {

              /*  bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                encodedimage = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/

            Uri imageUri = data.getData();
            image.setImageURI(imageUri);

            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor =getContext().getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = getRealPathFromURI(imageUri);
            }

            RegistrationCustom.secImagePath = imagePath ;

            // upload image
        }
    } // onSelectFromGalleryResult

}
