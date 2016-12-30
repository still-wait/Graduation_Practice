package transfer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class AnswerListTransfer {

    @SerializedName("totalCount")
    @Expose
    public String totalCount;
    @SerializedName("totalPage")
    @Expose
    public Long totalPage;
    @SerializedName("answers")
    @Expose
    public List<AnswerTransfer> answers = new ArrayList<AnswerTransfer>();
    @SerializedName("curPage")
    @Expose
    public String curPage;

}
