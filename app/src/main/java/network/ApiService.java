package network;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import transfer.AnswerListTransfer;
import transfer.InformationTransfer;
import transfer.QuestionListTransfer;
import transfer.UploadInformationTransfer;
import transfer.UserTransfer;

public interface ApiService {

    @FormUrlEncoded
    @POST("register.php")
    Call<InformationTransfer> register(@Field("name") String name, @Field("password") String password);

    @FormUrlEncoded
    @POST("login.php")
    Call<UserTransfer> login(@Field("name") String name, @Field("password") String password);

    @FormUrlEncoded
    @POST("loginWithOldToken.php")
    Call<UserTransfer> loginWithOldToken( @Field("token") String token);

    @FormUrlEncoded
    @POST("getQuestionList.php")
    Call<QuestionListTransfer> getQuestionList( @Field("page") String page);

    @FormUrlEncoded
    @POST("getAnswerList.php")
    Call<AnswerListTransfer> getAnswerList( @Field("questionId") String questionId, @Field("page") String page, @Field("desc") String desc);

    @Multipart
    @POST("uploadImage.php")
    Call<UploadInformationTransfer> uploadImage(@PartMap Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST("modifyFace.php")
    Call<InformationTransfer> modifyFace( @Field("token") String token, @Field("face") String face);

    @FormUrlEncoded
    @POST("question.php")
    Call<InformationTransfer> question(@Field("token") String token, @Field("title") String title, @Field("content") String content);

    @FormUrlEncoded
    @POST("answer.php")
    Call<InformationTransfer> answer( @Field("token") String token, @Field("questionId") String questionId, @Field("content") String content);

}
