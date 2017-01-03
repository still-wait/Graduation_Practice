package network;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Y-GH on 2017/1/3.
 */

public class volley {

    public static String url="http://192.168.191.1";
    private String httpurl1 = url+":8080/Graduation_practic/joinmatch.action";
    private static String httpurl2 = url+":8080/Graduation_practic/reqallmatch.action";
    private String httpurl3 = url+"http://192.168.191.1:8080/Graduation_practic/login.action";
    private String httpurl4 = url+"http://192.168.191.1:8080/Graduation_practic/login.action";
    private String httpurl5 = url+"http://192.168.191.1:8080/Graduation_practic/login.action";
    private static ProgressDialog dialog;

    /**
     * 加入比赛
     *
     * @param username
     * @param matchid
     * @param context
     */
    public void onJoinMatch(final String username, String matchid, final Context context) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("matchid", matchid);

        /**
         * loading...
         */

        if (dialog == null) {
            dialog = new ProgressDialog(context);
        }
        dialog.setMessage("加入中...");
        dialog.setCancelable(false);
        dialog.show();
        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        Request<JSONObject> request = new NormalPostRequest(httpurl1,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String result = null;
                        try {
                            result = response.getString("result");
                            if (result.equals("success")) {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
//                                position.setText(response.getString("poision"));
                                Toast.makeText(context, "加入成功", Toast.LENGTH_SHORT).show();
//                                userTransfer = response;
                            } else {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
                                Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(Old_LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                JUtils.ToastLong(getResources().getString(R.string.other_server_error));
                Toast.makeText(context, "连接服务器错误", Toast.LENGTH_SHORT).show();

                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
            }
        }, params);
        requestQueue.add(request);
    }

    /**
     * 获取所有比赛列表
     *
     * @param page
     * @param context
     */
    public static void onreqAllMatch(int page, final Context context) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", page + "");

        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        Request<JSONArray> request = new NormalPostRequest2(httpurl2,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.e("---2从服务器取得12-----", "----" + response.toString());
                            List<MatchBean> list = new ArrayList<MatchBean>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject json2 = response.getJSONObject(i);
                                MatchBean bean = new MatchBean();
                                bean.setfname(json2.getString("username"));
                                bean.settime(json2.getString("time"));
                                bean.setlocation(json2.getString("location"));
                                bean.setnumber(Integer.parseInt(json2.getString("number")));
                                bean.settotalmin(Integer.parseInt(json2.getString("totalmin")));
                                bean.settotalmax(Integer.parseInt(json2.getString("totalmax")));
                                bean.setremark(json2.getString("remark"));
                                bean.setmatchid(json2.getString("matchid"));
                                bean.settitle(json2.getString("title"));
                                bean.setftime(json2.getString("ftime"));
                                list.add(bean);
                                Log.e("-----从服务器取得------", "----" + list);
                            }
//                            return list;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                JUtils.ToastLong(getResources().getString(R.string.other_server_error));
                Toast.makeText(context, "连接服务器错误", Toast.LENGTH_SHORT).show();

                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
            }
        }, params);
        requestQueue.add(request);
    }

    /**
     * 查询我的比赛
     *
     * @param username
     */
    private void onselectMymatch(final String username, final Context context) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);

        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        Request<JSONObject> request = new NormalPostRequest(httpurl1,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String result = null;
                        try {
                            result = response.getString("result");
                            if (result.equals("success")) {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
//                                position.setText(response.getString("poision"));
                                Toast.makeText(context, "加入成功", Toast.LENGTH_SHORT).show();
//                                userTransfer = response;
                            } else {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
                                Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(Old_LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                JUtils.ToastLong(getResources().getString(R.string.other_server_error));
                Toast.makeText(context, "连接服务器错误", Toast.LENGTH_SHORT).show();

                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
            }
        }, params);
        requestQueue.add(request);
    }


    /**
     * 查询我的信息
     *
     * @param username
     */
    private void onselectMymsg(final String username, final Context context) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);

        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        Request<JSONObject> request = new NormalPostRequest(httpurl1,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String result = null;
                        try {
                            result = response.getString("result");
                            if (result.equals("success")) {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
//                                position.setText(response.getString("poision"));
                                Toast.makeText(context, "加入成功", Toast.LENGTH_SHORT).show();
//                                userTransfer = response;
                            } else {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
                                Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(Old_LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                JUtils.ToastLong(getResources().getString(R.string.other_server_error));
                Toast.makeText(context, "连接服务器错误", Toast.LENGTH_SHORT).show();

                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
            }
        }, params);
        requestQueue.add(request);
    }

}
