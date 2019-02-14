package cn.com.my.demo.net;

import com.client.data.protobuf.Persons;
import com.http.tools.net.common.BasicResponse;

import java.util.List;
import java.util.Map;

import cn.com.my.demo.bean.weather_info;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public interface ApiService {

    /**
     * 数据提交配置更新接口
     * @param options
     * @return
     */
    @POST("WeatherApi")
    Observable<weather_info> getWeatherData(@QueryMap Map<String, String> options);

    /**
     * 算法数据提交接口
     * @param module
     * @param bytes     @path参数用于替换url中{}的部分，
     * @return
     */
    @POST ("upload/{module}")
    Observable<String> upload_Data(@Path("module") String module, @Body RequestBody bytes);


    /**
     *  protobuf
     * @param url  @path参数用于替换url中{}的部分，
     * @param bytes
     * @return
     */
    @POST("servlet/{name}")
    Observable<Persons.Response> common_config(@Path("name") String url, @Body RequestBody bytes);

    /**
     * 单文件上传 方法一
     * @param partList
     * @return
     */
    @Multipart
    @POST("upload/uploadFile.do")
    Observable<BasicResponse> uploadFiles(@Part List<MultipartBody.Part> partList);

}
