package com.example.greenfarm.ui.mine.myFarm.farmer.management.add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.greenfarm.R;
import com.example.greenfarm.ui.main.MainActivity;

public class AddFarmActivity extends AppCompatActivity {

    RelativeLayout rlUploadPicture;

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
                }else openAlbum();
            }
        });

        imageView = findViewById(R.id.iv_add_farm_picture_to_upload);
        btnAddFarm = findViewById(R.id.btn_add_farm);
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
                    //直接传入Uri地址，该地址为content://media/external/images/media/26

                }
            }

        }
    }

}