package com.example.greenfarm.ui.mine.productManage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.management.UserManager;
import com.example.greenfarm.pojo.Category;
import com.example.greenfarm.ui.mine.myFarm.farmer.management.add.AddFarmActivity;
import com.example.greenfarm.utils.HttpUtil;
import com.example.greenfarm.utils.ImageUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class AddProductActivity extends AppCompatActivity {

    private final static int NETWORK_ERR = -1;
    private final static int GET_CATEGORY_FAIL = 0;
    private final static int GET_CATEGORY_SUCCEED = 1;
    private final static int ADD_PRODUCT_FAIL = 2;
    private final static int ADD_PRODUCT_SUCCEED = 3;

    private EditText etProductName;

    private EditText etProductDescription;

    private EditText etProductPrice;

    private ImageView ivProductPicture;

    private AppCompatSpinner spinner;

    private ArrayAdapter<String> categoryAdapter;

    private List<String> categoryNameList = new ArrayList<>();

    private List<Category> categoryList = new ArrayList<>();

    private Category category;//新增产品的类别

    private Uri imageUri;

    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case NETWORK_ERR:
                    showToast("网络连接错误");
                    break;
                case GET_CATEGORY_SUCCEED:
                    showToast("获取类别数据成功");
                    for (int i = 0; i < categoryList.size(); i++) {
                        categoryNameList.add(categoryList.get(i).getCname());
                    }
                    categoryAdapter = new ArrayAdapter<String>(AddProductActivity.this,R.layout.support_simple_spinner_dropdown_item,categoryNameList);
                    spinner.setAdapter(categoryAdapter);
                    spinner.setSelection(0);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            category = categoryList.get(position);
                            Log.d("选择类别",category.toString());
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    break;
                case GET_CATEGORY_FAIL:
                    showToast("获取类别数据失败");
                    break;
                case ADD_PRODUCT_FAIL:
                    showToast("添加失败");
                    break;
                case ADD_PRODUCT_SUCCEED:
                    showToast("添加成功");
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initView();

        initCategoryList();
    }

    private void initView() {
        etProductName = findViewById(R.id.et_add_product_name);
        etProductDescription = findViewById(R.id.et_add_product_description);
        etProductPrice = findViewById(R.id.et_add_product_price);
        ivProductPicture = findViewById(R.id.iv_add_product_picture_to_upload);
        spinner = findViewById(R.id.spinner_product_category);
    }


    public void choosePicture(View view) {
        //申请权限部分
        String write= Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String read=Manifest.permission.READ_EXTERNAL_STORAGE;

        final String[] WriteReadPermission = new String[] {write, read};

        int checkWrite= ContextCompat.checkSelfPermission(AddProductActivity.this,write);
        int checkRead= ContextCompat.checkSelfPermission(AddProductActivity.this,read);
        int ok= PackageManager.PERMISSION_GRANTED;

        if (checkWrite!= ok && checkRead!=ok){
            //申请权限，读和写都申请一下，不然容易出问题
            ActivityCompat.requestPermissions(AddProductActivity.this,WriteReadPermission,1);
        }else {
            openAlbum();
        }
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
                    ivProductPicture.setImageURI(Uri.withAppendedPath(baseUri, id));
                    imageUri = Uri.withAppendedPath(baseUri, id);
                    //直接传入Uri地址，该地址为content://media/external/images/media/26

                }
            }
        }
    }

    /**
     * 按钮点击事件
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addProduct(View view) {
        if (etProductName.getText().toString().isEmpty() || etProductDescription.getText().toString().isEmpty() || etProductPrice.getText().toString().isEmpty()) {
            showToast("输入信息不全");
        } else {
            ivProductPicture.setDrawingCacheEnabled(true);
            Bitmap btp = null;
            if (imageUri == null) {
                showToast("请选择图片");
                return;
            }
            try {
                btp = BitmapFactory.decodeStream(AddProductActivity.this.getContentResolver().openInputStream(imageUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Bitmap bitmap = ImageUtil.zoomBitmap(btp,100,100);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            btp.compress(Bitmap.CompressFormat.JPEG,90,baos);
            byte[] bytes = baos.toByteArray();
            final Base64.Encoder encoder = Base64.getEncoder();
            String encodePictureText = encoder.encodeToString(bytes);//图片编码成字符串
            ivProductPicture.setDrawingCacheEnabled(false);
            HashMap<String, String> dataToServer = new HashMap<>();
            dataToServer.put("name",etProductName.getText().toString());
            dataToServer.put("description",etProductDescription.getText().toString());
            dataToServer.put("price",etProductPrice.getText().toString());
            dataToServer.put("categoryId",String.valueOf(category.getId()));
            dataToServer.put("farmerId", String.valueOf(UserManager.currentUser.getId()));
            dataToServer.put("picture",encodePictureText);

            String jsonToServer = new Gson().toJson(dataToServer);

            try {
                HttpUtil.postWithOkHttp(HttpUtil.getUrl("/addProduct"), jsonToServer, new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message msg = Message.obtain();
                        msg.what = NETWORK_ERR;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String body = response.body().string();
                        Log.d("addProductResult",body);
                        HashMap<String,String> res = new Gson().fromJson(body, new TypeToken<HashMap<String,String>>(){}.getType());
                        Message msg = Message.obtain();
                        if (res.get("result").equals("success")) {
                            msg.what = ADD_PRODUCT_SUCCEED;
                        } else {
                            msg.what = ADD_PRODUCT_FAIL;
                        }
                        mHandler.sendMessage(msg);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 完成类别数据的初始化
     */
    private void initCategoryList() {
        try {
            HttpUtil.sendOkHttpRequest(HttpUtil.getUrl("/getAllCategory"),new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Message msg = Message.obtain();
                    msg.what = NETWORK_ERR;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
                    categoryList = new Gson().fromJson(body,new TypeToken<List<Category>>(){}.getType());
                    Message msg = Message.obtain();
                    if (categoryList == null) {
                        msg.what = GET_CATEGORY_FAIL;
                    } else {
                        msg.what = GET_CATEGORY_SUCCEED;
                    }
                    mHandler.sendMessage(msg);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

}