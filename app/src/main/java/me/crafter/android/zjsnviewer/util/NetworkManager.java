package me.crafter.android.zjsnviewer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.InflaterInputStream;

import me.crafter.android.zjsnviewer.config.Storage;

public class NetworkManager {

    // TODO this class needs cleanup
    public static String url_init = "api/initGame";
    public static String url_passport_p7 = "http://login.alpha.p7game.com/index/passportLogin/";// +username/password
    //hm change the login in url as http://login.jianniang.com/index/passportLogin/
    //hm change the login in url as http://login.jr.moefantasy.com/index/passportLogin/ in 6/4/2016
    public static String url_passport_hm = "http://login.jr.moefantasy.com/index/passportLogin/";// +username/password
    public static String url_passport_hm_ios = "http://loginios.jianniang.com/index/passportLogin/";// +username/password
    public static String url_login = "index/login/";//+uid
    public static String url_Explore = "explore/start/"; // +fleetID/+exploreID/
    public static String url_getExploreResult = "explore/getResult/"; // +exploreID/
    public static String[] url_server_p7 = {
            "http://zj.alpha.p7game.com/",
            "http://s2.zj.p7game.com/",
            "http://s3.zj.p7game.com/",
            "http://s4.zj.p7game.com/",
            "http://s5.zj.p7game.com/",
            "http://s6.zj.p7game.com/",
            "http://s7.zj.p7game.com/",
            "http://s8.zj.p7game.com/",
            "http://s9.zj.p7game.com/",
            "http://s10.zj.p7game.com/",
            "http://s11.zj.p7game.com/",
    };

    public static String[] url_server_hm = {
            "http://zj.alpha.jr.moefantasy.com/",
            "http://s2.jr.moefantasy.com/",
            "http://s3.jr.moefantasy.com/",
            "http://s4.jr.moefantasy.com/",
            "http://s5.jr.moefantasy.com/",
            "http://s6.jr.moefantasy.com/",
            "http://s7.jr.moefantasy.com/",
            "http://s8.jr.moefantasy.com/",
            "http://s9.jr.moefantasy.com/",
            "http://s10.jr.moefantasy.com/",
            "http://s11.jr.moefantasy.com/",
            "http://s12.jr.moefantasy.com/"
    };

    public static String[] url_server_hm_ios = {
        "http://s101.jr.moefantasy.com/",
        "http://s102.jr.moefantasy.com/",
        "http://s103.jr.moefantasy.com/",
        "http://s104.jr.moefantasy.com/",
        "http://s105.jr.moefantasy.com/",
        "http://s106.jr.moefantasy.com/"
    };
    public static String getCurrentUnixTime() {
        long unixTime = System.currentTimeMillis();
        return String.valueOf(unixTime);
    }
    public static int currentUnix(){
        return ((int)(System.currentTimeMillis() / 1000L));
    }

    //将gzip压缩的字符串解压
    //加入gz=1返回的应该不是gzip？
    //java.io.IOException: unknown format (magic number da78)
    //问题解决，不要使用GZIPInputStream， 78da - data compressed by "zlib.zlibConst.Z_BEST_COMPRESSION" (or int=9) marker
    //使用InflaterInputStream 代替 GZIPInputStream
    public static InputStream decompress(InputStream in) {
        return new InflaterInputStream(in);
        /*
        try {
        GZIPInputStream gis = new GZIPInputStream(in);
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        String outStr = "";
        String line;
        while ((line=bf.readLine())!=null) {
            outStr += line;
        }
        return outStr;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "ERR1";
    */
}

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString().substring(0, 16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *
     * @param urString
     * @return FinalUrl
     */
    public static String getFinalUrl(String urString) {
        String FinalUrl = urString + "/&t=" + getCurrentUnixTime();
        String market = "&gz=1&market=2&channel=0&version=2.4.0";
        //返回的是一个16位散列，暂时猜测是md5
        String FinalMd5 = md5(FinalUrl);
        FinalUrl += "&e=" + FinalMd5 + market;
        return FinalUrl;
    }

    public static class OperateSession{
        public String urlString;
        public String cookie;
        public URLConnection connection;
        private String response;
        OperateSession(String urString, String loginCookie){
            urlString = urString;
            cookie = loginCookie;
        }

        OperateSession(String urString){
            urlString = urString;
            cookie = "";
        }
        public String open(){
            response = "";
            try {
                urlString = getFinalUrl(urlString);
                URL url = new URL(urlString);
                Log.i("Session", url.toString());
                connection = url.openConnection();
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.setDoOutput(false);
                if (!cookie.equals("")) connection.setRequestProperty("cookie", cookie);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                decompress(connection.getInputStream()), "UTF-8"));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response += inputLine;
                }
                in.close();
                if (response.contains("\"eid\"")){
                    Log.i("Session", "get eid when get reward.");
                }

            }catch (Exception ex) {
                Log.e("Session", ex.toString());
            }
            // force sleep 3s after every network operation
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e){

            }
            return response;
        }


        public String getCookie(){
            try{
                List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
                return TextUtils.join(";",cookies);
            } catch (Exception ex) {
                Log.e("Session", "can not get cookie");
            }
            return "";
        }
    }
    /**
     *
     * @param context
     * @param server 服务器地址
     * @param loginCookie 登陆返回的cookie
     * @param exploreId 远征区域ID
     * @param fleetId 舰队编号
     * @return true or false
     */
    //// TODO: 6/8/2016    This should be out of NetworkManager, be apart. New Class Needed.
    //// TODO: 6/12/2016 这个类中的context是否会产生内存泄漏？ 找时间检查下，看看使用的是Application Context还是 act的context。
    /** master不提供该功能**/
    public static boolean autoExplore(Context context, String server ,String loginCookie  ,int exploreId, int fleetId) {
        Log.d("NetworkManager", "autoExplore");
        Log.d("NetworkManager", "check time");
        //// TODO: 6/12/2016  Use isNoAutoExploreNow instead  isNoDisturbNow.
        if (Storage.isNoDisturbNow(context)) return false;

        String urString = server + url_getExploreResult + exploreId;
        OperateSession exploreSession = new OperateSession(urString,loginCookie);
        String response;
        response = exploreSession.open();

        if (response.contains("\"eid\"")){
            Log.i("autoExplore()", "get eid when get reward.");
            return false;
        }
        // Auto Explore
        urString = server + url_Explore + fleetId + "/" + exploreId;
        exploreSession.urlString = urString;

        response = exploreSession.open();
        if (response.contains("\"eid\"")){
            Log.i("autoExplore()", "get eid when auto explore.");
            return false;
        }
        return true;
    }

    public static void updateDockInfo(Context context){
        Log.i("NetworkManager", "updateDockInfo()");
        Log.i("NetworkManager", "Unix: " + getCurrentUnixTime());
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String username = prefs.getString("username", "none");
        String password = prefs.getString("password", "none");
        String server = prefs.getString("server", "-1");
        if (server.equals("-1")) return;
        int serverId = Integer.parseInt(server);

        if (serverId < 100){
            server = url_server_p7[serverId];
        }
        else if (serverId < 200) {
            server = url_server_hm[serverId-100];
        }
        else {
            server = url_server_hm_ios[serverId-201];
        }

        // Black: Alt Server
        boolean altserver = prefs.getBoolean("altserver", false);
        if (altserver){
            server = prefs.getString("alt_url_server", "");
        }
        // the login alt server is changed in step 1

        Boolean on = prefs.getBoolean("on", false);
        if (!on){
            Storage.str_tiduName = Storage.str_notOn[Storage.language];
            DockInfo.updateInterval = 15;
            return;
        }


        if (ZjsnState.getZjsnState() == 0 && prefs.getBoolean("auto_run", true)){
            DockInfo.updateInterval = 15;
            Storage.str_tiduName = Storage.str_gameRunning[Storage.language];
            return;
        }

        String error = "";

        try {
            // STEP 1 PASSPORT LOGIN
            String url;
            if (serverId < 100){
                url = url_passport_p7 +username+"/"+password;
            }else if (serverId < 200){
                url = url_passport_hm +username+"/"+password;
            }else {
                url = url_passport_hm_ios +username+"/"+password;
            }
            if (altserver){
                url = prefs.getString("alt_url_login", "") +username+"/"+password;
            }
            OperateSession workingSession = new OperateSession(url);
            String response = workingSession.open();
            String loginCookie = workingSession.getCookie();

            JSONObject obj = new JSONObject(response);
            int uid = obj.getInt("userId");

            // STEP 2 UID SERVER LOGIN
            url = server + url_login + uid;
            workingSession.urlString = url;
            workingSession.cookie = loginCookie;

            response = workingSession.open();
//            loginCookie = workingSession.getCookie();


            // STEP 3 GET USER DATA
            initGame(context, server, loginCookie);

//            if (WeatherGuard.yes){
//                DockInfo.updateInterval = Math.min(DockInfo.updateInterval, 305);
//                WeatherGuard.dash(data, server, loginCookie);
//            }

        } catch (Exception ex) {
            Log.e("UpdateDockInfo()", "ERR1");
            ex.printStackTrace();
            if (error.equals("")) {
                Storage.str_tiduName = Storage.str_badConnection[Storage.language];
            } else {
                Storage.str_tiduName = error;
            }
        }

    }
    private static void initGame(Context context ,String server, String loginCookie){
        String urString;
        urString = server + url_init;
        String error = "";
        try {
            OperateSession workingSession = new OperateSession(urString, loginCookie);
            String response = workingSession.open();
    //          Log.i("NetworkManager", response.toString());
            JSONObject data = new JSONObject(response);

            if (!data.has("userVo")){
                error = Storage.str_noUserData[Storage.language];
            }

            Storage.str_tiduName = data.getJSONObject("userVo").getString("username");
            DockInfo.exp = data.getJSONObject("userVo").getString("exp");
            DockInfo.nextExp = data.getJSONObject("userVo").getString("nextExp");
            DockInfo.level = data.getJSONObject("userVo").getString("level");
            JSONObject pveExploreVo = data.getJSONObject("pveExploreVo");
            JSONArray levels = pveExploreVo.getJSONArray("levels");
            boolean shouldExplore = false;
            for (int i = 0; i < levels.length(); i++){
                JSONObject level = levels.getJSONObject(i);
                int endTime = level.getInt("endTime");
                DockInfo.dockTravelTime[level.getInt("fleetId")-1] = endTime;
                if(endTime < currentUnix()) {
//master不提供该功能
//                    autoExplore(context, server, loginCookie, level.getInt("exploreId"), level.getInt("fleetId"));
                    shouldExplore = true;
                }
            }
////            TODO 如果远征了立刻刷新一遍状态，但是太暴力，需要有更好的方法
            if (shouldExplore) initGame(context, server, loginCookie);

            JSONArray dockVo = data.getJSONArray("dockVo");
            JSONArray repairDockVo = data.getJSONArray("repairDockVo");
            JSONArray equipmentDockVo = data.getJSONArray("equipmentDockVo");
            for (int i = 0; i < 4; i++){
                JSONObject o = dockVo.getJSONObject(i);
                if (o.getInt("locked") == 1){
                    DockInfo.dockBuildTime[i] = -1;
                } else if (o.has("endTime")){
                    DockInfo.dockBuildTime[i] = o.getInt("endTime");
                } else {
                    DockInfo.dockBuildTime[i] = 0;
                }
                o = repairDockVo.getJSONObject(i);
                if (o.getInt("locked") == 1){
                    DockInfo.dockRepairTime[i] = -1;
                } else if (o.has("endTime")){
                    DockInfo.dockRepairTime[i] = o.getInt("endTime");
                } else {
                    DockInfo.dockRepairTime[i] = 0;
                }
                o = equipmentDockVo.getJSONObject(i);
                if (o.getInt("locked") == 1){
                    DockInfo.dockMakeTime[i] = -1;
                } else if (o.has("endTime")){
                    DockInfo.dockMakeTime[i] = o.getInt("endTime");
                } else {
                    DockInfo.dockMakeTime[i] = 0;
                }
            }
            Log.i("NetworkManager", "Update successful");
            DockInfo.updateInterval += 75;
            DockInfo.updateInterval = Math.min(DockInfo.updateInterval, 1210);
        } catch (Exception ex) {
            Log.e("initGame", "ERR1");
            ex.printStackTrace();
            if (ex.equals("")){
                Storage.str_tiduName = Storage.str_badConnection[Storage.language];
            } else {
                Storage.str_tiduName = error;
            }
        }
    }
}
