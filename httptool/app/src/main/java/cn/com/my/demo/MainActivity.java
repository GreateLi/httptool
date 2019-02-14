package cn.com.my.demo;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.View;


import com.client.data.protobuf.Persons;
import com.hdl.elog.ELog;
import com.http.tools.net.common.BasicResponse;
import com.http.tools.net.common.DefaultObserver;
import com.http.tools.net.common.ProgressUtils;
import com.http.tools.utils.FileUtils;
import com.http.tools.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.my.demo.bean.weather_info;
import cn.com.my.demo.net.HttpSend;
import cn.com.my.demo.net.RetrofitHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class MainActivity extends BaseActivity {
    Context context;
    private final static String TAG="MainActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        context = this;
    }

    public void Weather_info(View view) {
        Map<String, String> options = new HashMap<>();
        options.put("citykey", "101010100");
        HttpSend.toSubscribe(RetrofitHelper.getApiService().getWeatherData(options))
                .subscribe(new DefaultObserver<weather_info>() {
                    @Override
                    public void onSuccess(weather_info response) {
                       Log.e("weather","weather:"+ response.toString());
                    }

                    @Override
                    public void onFail(String message) {
                        Log.e("weather","weather failed:"+ message);
                    }


                });
    }

    public void upload_persion(View view)
    {

        Persons.Person.Builder pb = Persons.Person.newBuilder();
        pb.setEmail("aa");
        pb.setId(0);
        pb.setName("aa");

        Persons.Data.Builder builder = Persons.Data.newBuilder();
        builder.addPerson(pb.getDefaultInstanceForType());
        Persons.Data data = builder.build();
        RequestBody parms = RequestBody.create(MediaType.parse("gzip"), data.toByteArray() );
        String url = "config";
        HttpSend.toSubscribe(RetrofitHelper.getApiService().common_config(url,parms))
                .subscribe(new DefaultObserver<Persons.Response>() {
                    @Override
                    public void onSuccess(Persons.Response response) {
                        ELog.e("DefaultObserver upload_config value = " + response.toString() );
                    }
                    @Override
                    public void onFail(String message) {
                        ELog.e("DefaultObserver onFail   " + message);
                    }
                });
    }

    /**
     * 单文件上传 方法一
     */
    public void uploadFile1(View view) {
        //文件路径
        File file = getFile();
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", "12345678901")
                .addFormDataPart("password", "123123")
                .addFormDataPart("uploadFile", file.getName(), fileBody);
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitHelper.getApiService()
                .uploadFiles(parts)
                .subscribeOn(Schedulers.io())
                .compose(this.<BasicResponse>bindToLifecycle())
                .compose(ProgressUtils.<BasicResponse>applyProgressBar(this, "上传文件..."))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    public void onSuccess(BasicResponse response) {
                        ToastUtils.show("文件上传成功");
                    }

                    @Override
                    public void onFail(String message) {
                        ToastUtils.show("文件上传失败"+message);
                    }
                });
    }


    private File getFile() {
        String fileStoreDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filePath = fileStoreDir + "/myfile.txt";
        FileUtils.createOrExistsFile(filePath);
        //文件路径
        return new File(filePath);
    }


}