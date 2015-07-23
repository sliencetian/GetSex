package com.xy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Utils {

	public static final String URL = "http://demo.wudilabs.org/lab/gender_guesser/";

	public static String getSex(String name) {
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("name_list", name);
		String context = http(URL, rawParams);
		return parsContext(context);
	}

	private static String parsContext(String context) {
		Document doc = Jsoup.parse(context);
		Elements links = doc.getElementsByTag("span");
		for (Element link : links) {
			  String text = link.text();
			  return text.split(" ")[0];
		} 
		return null;
	}

	/**
	 * 直接请求 不适用cookie的请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String http(String url, Map<String, String> params) {
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Entry<String, String> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
				sb.append("&");
			}
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		// 尝试发送请求
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			con.setRequestProperty("Referer", "http://cet.99sushe.com/");
			OutputStreamWriter osw = new OutputStreamWriter(
					con.getOutputStream(), "utf-8");
			osw.write(sb.toString());
			osw.flush();
			if (con.getResponseCode() == 200) {
				osw.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} /*
		 * finally { if (con != null) { con.disconnect(); } }
		 */

		// 读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "utf-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		con.disconnect();
		return buffer.toString();
	}
}
