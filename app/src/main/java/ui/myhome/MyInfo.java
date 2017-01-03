package ui.myhome;


public class MyInfo {

//        private Drawable Image;

    private String time;

    private String location;

    private String count;

    private String remark;

    public  MyInfo(String time,String location,String count,String remark){

//        this.Image=Image;
        this.time=time;
        this.location=location;
        this.count=count;
        this.remark=remark;
    }
//    public Drawable getImage(){
//
//        return Image;
//    }
    public String getTime(){

        return time;
    }
    public String getLocation(){

        return location;
    }
    public String getCount(){

        return count;
    }
    public String getRemark(){
        return remark;
    }
}
