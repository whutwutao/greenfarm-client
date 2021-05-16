package com.example.greenfarm.ui.mine.myFarm.farmer.management.add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.ui.main.MainActivity;
import com.example.greenfarm.utils.HttpUtil;
import com.example.greenfarm.utils.ImageUtil;
import com.example.greenfarm.utils.addressPicker.AddressPickTask;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import okhttp3.Call;
import okhttp3.Response;

public class AddFarmActivity extends AppCompatActivity {

    private EditText etDescription;

    private EditText etAddress;

    private EditText etServiceLife;

    private EditText etArea;

    private EditText etPrice;

    private Uri imageUri;

    private RelativeLayout rlUploadPicture;

    private Button btnAddFarm;

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_farm);

        rlUploadPicture = findViewById(R.id.rl_add_farm_upload_picture);

        rlUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //申请权限部分
                String write= Manifest.permission.WRITE_EXTERNAL_STORAGE;
                String read=Manifest.permission.READ_EXTERNAL_STORAGE;

                final String[] WriteReadPermission = new String[] {write, read};

                int checkWrite= ContextCompat.checkSelfPermission(AddFarmActivity.this,write);
                int checkRead= ContextCompat.checkSelfPermission(AddFarmActivity.this,read);
                int ok= PackageManager.PERMISSION_GRANTED;

                if (checkWrite!= ok && checkRead!=ok){
                    //申请权限，读和写都申请一下，不然容易出问题
                    ActivityCompat.requestPermissions(AddFarmActivity.this,WriteReadPermission,1);
                }else {
                    openAlbum();
                }
            }
        });

        etDescription = findViewById(R.id.et_add_farm_description);
        etAddress = findViewById(R.id.et_add_farm_address);
        etServiceLife = findViewById(R.id.et_add_farm_service_life);
        etArea = findViewById(R.id.et_add_farm_area);
        etPrice = findViewById(R.id.et_add_farm_price);


        imageView = findViewById(R.id.iv_add_farm_picture_to_upload);
        btnAddFarm = findViewById(R.id.btn_add_farm);
        btnAddFarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                imageView.setDrawingCacheEnabled(true);
                Bitmap btp = null;
                if (imageUri == null) {
                    showToast("请选择图片");
                    return;
                }
                try {//获取图像
                    btp = BitmapFactory.decodeStream(AddFarmActivity.this.getContentResolver().openInputStream(imageUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //调整大小
                Bitmap bitmap = ImageUtil.zoomBitmap(btp,120,90);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,90,baos);
                byte[] bytes = baos.toByteArray();
                final Base64.Encoder encoder = Base64.getEncoder();
                String encodePictureText = encoder.encodeToString(bytes);//图片编码成字符串
                imageView.setDrawingCacheEnabled(false);
                HashMap<String,String> dataToServer = new HashMap<>();
                String description = etDescription.getText().toString();
                String address = etAddress.getText().toString();
                String serviceLife = etServiceLife.getText().toString();
                String area = etArea.getText().toString();
                String price = etPrice.getText().toString();
                if (description.isEmpty() || address.isEmpty() || serviceLife.isEmpty() || area.isEmpty() || price.isEmpty()) {
                    showToast("信息填写不全");
                    return;
                }
                dataToServer.put("ownerId", String.valueOf(UserManager.currentUser.getId()));
                dataToServer.put("description",description);
                dataToServer.put("address",address);
                dataToServer.put("serviceLife",serviceLife);
                dataToServer.put("area",area);
                dataToServer.put("price",price);
                dataToServer.put("picture",encodePictureText);
                Gson gson = new Gson();
                String jsonToServer = gson.toJson(dataToServer);
                try {
                    HttpUtil.postWithOkHttp(HttpUtil.getUrl("/addFarm"),jsonToServer,new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            showToast("网络连接错误");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            showToast("添加成功");
                            finish();
                            String body = response.body().toString();
                            Log.d("AddFarmActivity",body);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        //把所有照片显示出来
        intent.setType("image/*");
        startActivityForResult(intent,123);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1&&grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            openAlbum();
        else Toast.makeText(this, "你拒绝了打开相册的权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //当选择完相片，就会回到这里，然后相片的相关信息会保存在data中，后面想办法取出来
        if (requestCode==123){

            //通过getData方法取得它的图片地址，后面的操作都是对这个地址的解析
            Uri uri=data.getData();

            //选择了一张在相册中id为26的照片，它的uri地址如下：
            //uri=content://com.android.providers.media.documents/document/image%3A26

            if (DocumentsContract.isDocumentUri(this,uri)){

                //判断是document类型的图片，所以获取它的doc id
                String docId=DocumentsContract.getDocumentId(uri);//docId=image:26
                //docId是将该资源的关键信息提取出来，比如该资源是一张id为26的image

                //获取它的uri的已解码的授权组成部分，来判断这张图片是在相册文件夹下还是下载文件夹下
                String uri_getAuthority=uri.getAuthority();
                //在相册文件夹的照片标识字段如下
                //uri_getAuthority=com.android.providers.media.documents

                //注意这里的字符串很容易写错，providers和documents都是有带s的
                if ("com.android.providers.media.documents".equals(uri_getAuthority)){
                    //当判断该照片在相册文件夹下时，使用字符串的分割方法split将它id取出来
                    String id=docId.split(":")[1];//id="26"
                    Uri baseUri=Uri.parse("content://media/external/images/media");
                    imageView.setImageURI(Uri.withAppendedPath(baseUri, id));
                    imageUri = Uri.withAppendedPath(baseUri, id);
                    //直接传入Uri地址，该地址为content://media/external/images/media/26

                }
            }

        }
    }

    public void onAddressPicker(View view) {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                showToast("数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                    showToast(province.getAreaName() + city.getAreaName());
                } else {
                    showToast(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    etAddress.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                }
            }
        });
        task.execute("贵州", "毕节", "纳雍");
    }

    private void showToast(String text) {
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
            }
        });
    }

}