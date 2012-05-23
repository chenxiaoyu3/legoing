package com.Legoing.NetOperation;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.*;

import com.Legoing.CommuObj;
import com.Legoing.DefaultParamObj;
import com.Legoing.ParamObj;
import com.Legoing.R;
import com.Legoing.StaticOverall;
import com.Legoing.ItemDef.ComponentList;
import com.Legoing.ItemDef.ImportResult;
import com.Legoing.ItemDef.LegoItem;
import com.Legoing.ItemDef.LegoItemEvaluation;
import com.Legoing.ItemDef.LegoingUser;
import com.Legoing.ItemDef.LegoingUserEvaluation;
import com.Legoing.ItemDef.Minifig;
import com.Legoing.ItemDef.Part;
import com.Legoing.ItemDef.Set;
import com.Legoing.ItemDef.UserItems;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetOperation {
	public enum NetErrorCode {
		CONNECT_FAILED, TIME_OUT
	};

	public NetErrorCode errorCode;
	ConnectivityManager connectivityManager;

	static String URL_SERVER_HOME = "http://legosys.sinaapp.com/";
	static String URLPATH_DATABASE_OPERATION = "test/Database/DBOpera.php";

	static String URLPATH_GET_COMPONENTS = "request_item_inv.php";
	static String URLPATH_SEARCH = "request_item_search.php";
	static String URLPATH_USER_VERIFY = "request_user_login.php";

	public static final int NET_MODE_NONE = 0x1000;
	public static final int NET_MODE_MOBILE = 0x1001;
	public static final int NET_MODE_WIFI = 0x1002;
	public int netMode = NET_MODE_NONE;

	static int CONNECTION_TIMEOUT = 15000;
	static int SO_TIMEOUT = 30000;

	public NetOperation() {
		connectivityManager = (ConnectivityManager) StaticOverall.getMainActivity().getSystemService(
				Context.CONNECTIVITY_SERVICE);
		checkNetMode();
		if (StaticOverall.DebugLocal) {
			URL_SERVER_HOME = "http://192.168.1.103/Lego/1/";
		}

	}

	public boolean IsNetworkAvailable() {
		NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
		for (NetworkInfo networkInfo : infos) {
			if (networkInfo.isAvailable())  
				return true;
			}
		
		return false;
	}

	void checkNetMode() {
		NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (info.isConnected()) {
			netMode = NET_MODE_MOBILE;
			CONNECTION_TIMEOUT = 30000;
			SO_TIMEOUT = 30000;
		}
		info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (info.isConnected())
		{
			netMode = NET_MODE_WIFI;
			CONNECTION_TIMEOUT = 15000;
		}
		
	}

	public boolean IsServerConnectable() {
		ParamObj<String> result = new DefaultParamObj<String>();
		int ret = sendPost2(URL_SERVER_HOME, new ArrayList<NameValuePair>(), result);
		return ret == 0;

	}

	// public int sendPost(String url, String param, String result) {
	// int ret = -1;
	// PrintWriter out = null;
	// BufferedReader in = null;
	// try {
	//
	// URL realUrl = new URL(url);
	// HttpURLConnection connection = (HttpURLConnection) realUrl
	// .openConnection();
	// connection.setRequestProperty("accept", "*/*");
	// connection.setRequestProperty("connection", "Keep-Alive");
	// connection.setRequestProperty("User-agent",
	// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
	// connection.setRequestProperty("Content-type", "application/xxx");
	//
	// connection.setDoOutput(true);
	// connection.setDoInput(true);
	// connection.setRequestMethod("post");
	//
	// out = new PrintWriter(connection.getOutputStream());
	// out.print(param);
	// out.flush();
	//
	// in = new BufferedReader(new InputStreamReader(
	// connection.getInputStream()));
	// String line = "";
	//
	// HttpGet post = new HttpGet(url);
	// post.addHeader("func", "getset");
	// post.addHeader("argvs[]","100");
	// HttpClient client = new DefaultHttpClient();
	// HttpResponse response = client.execute(post);
	// line = EntityUtils.toString(response.getEntity());
	//
	//
	// while ((line = in.readLine()) != null) {
	// ret = 0;
	// result += "\n" + line;
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// if(e instanceof UnknownHostException)
	// {
	// errorCode = NetErrorCode.CONNECT_FAILED;
	// }
	//
	// } finally {
	// try {
	// if (out != null)
	// out.close();
	// if (in != null)
	// in.close();
	// } catch (Exception e2) {
	// // TODO: handle exception
	// }
	// }
	// return ret;
	// }

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	// public String sendGet(String url, String param) {
	// String result = "";
	// BufferedReader in = null;
	// try {
	// String urlName = url + "?" + param;
	// URL realUrl = new URL(urlName);
	// // 打开和URL之间的连接
	// URLConnection conn = realUrl.openConnection();
	// // 设置通用的请求属性
	// conn.setRequestProperty("accept", "*/*");
	// conn.setRequestProperty("connection", "Keep-Alive");
	// conn.setRequestProperty("user-agent",
	// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
	// //建立实际的连接
	// conn.connect();
	// // 获取所有响应头字段
	// // Map<String, List<String>> map = conn.getHeaderFields();
	// // // 遍历所有的响应头字段
	// // for (String key : map.keySet()) {
	// // System.out.println(key + "--->" + map.get(key));
	// // }
	// // 定义BufferedReader输入流来读取URL的响应
	// in = new BufferedReader(
	// new InputStreamReader(conn.getInputStream()));
	// String line;
	// while ((line = in.readLine()) != null) {
	// result += "n" + line;
	// }
	// } catch (Exception e) {
	// System.out.println("发送GET请求出现异常！" + e);
	// e.printStackTrace();
	// }
	// // 使用finally块来关闭输入流
	// finally {
	// try {
	// if (in != null) {
	// in.close();
	// }
	// } catch (IOException ex) {
	// ex.printStackTrace();
	// }
	// }
	// return result;
	// }
	//

	public static final int CODE_NET_FAILED = 0x2000;
	public static final int CODE_INNER_ERROR = 0x2005; // 内部错误，比如返回的json格式无法被解析
	public static final int CODE_UNKOWN_RET = 0x2006;

	public static final int NET_CODE_TIME_OUT = 0x2001;
	public static final int NET_CODE_CONNECT_FAILED = 0x2002;

	/**
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @param result
	 * @return
	 */
	public int sendPost2(String url, List<NameValuePair> nameValuePairs, ParamObj<String> result) {

		int ret = -1;

		BasicHttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);
		HttpClient httpclient = new DefaultHttpClient(params);

		HttpPost httppost = new HttpPost(url);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			// 客户端收到响应的信息流
			InputStream inputStream = response.getEntity().getContent();
			// byte[] bb = new byte[2048];
			//
			// int len = inputStream.read(bb);
			// ret = 0;
			// result.value = new String(bb,0,len, "UTF-8");
			// result.value = result.value.trim();
			// inputStream.read(bb);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			result.value = new String();
			String line = null;
			while ((line = reader.readLine()) != null) {
				result.value += line;
				ret = 0;
			}
			result.value = result.value.substring(result.value.indexOf('{'));
			// result.value = new String(result.value.getBytes("ASCII"),
			// "ASCII");
			Log.i("Return:", result.value);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	String jsonTestStr = "{\"ret\":0,\"value\":{\"id\":2,\"setID\":100,\"setIDSecond\":1,\"setName\":\"TestSet\",\"partsNum\":0,\"minifigs\":0,\"yearReleased\":\"0000\",\"weight\":0,\"length\":0,\"width\":0,\"height\":0,\"imageLinks\":\"\",\"partsList_InnerID\":null},\"info\":\"\"}";

	public int getSetInfo(int setInnerID, ParamObj<Set> result) {
		if (StaticOverall.DebugLocal) {
			try {
				CommuObj ss = JSON.parseObject(jsonTestStr, CommuObj.class);
				Set set = JSON.parseObject(((JSONObject) ss.value).toJSONString(), Set.class);
				result.value = set;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}

		int ret = -1;
		String urlString = URL_SERVER_HOME + URLPATH_DATABASE_OPERATION;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("func", "getset"));
		nameValuePairs.add(new BasicNameValuePair("argv[]", String.valueOf(setInnerID)));
		ParamObj<String> webString = new DefaultParamObj<String>();
		ret = sendPost2(urlString, nameValuePairs, webString);
		if (ret == 0) {
			CommuObj ss = JSON.parseObject(webString.value, CommuObj.class);
			result.value = JSON.parseObject(((JSONObject) ss.value).toJSONString(), Set.class);
		}
		return 0;
	}

	static final String testGetURL = "http://1.wanderaxe.sinaapp.com/commTest/test.php";

	public int testSearch() {
		ParamObj<CommuObj> result = new DefaultParamObj<CommuObj>();
		ParamObj<String> paramObj = new DefaultParamObj<String>();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		int ret = sendPost2(testGetURL, nameValuePairs, paramObj);
		CommuObj obj = JSON.parseObject(paramObj.value, CommuObj.class);
		ret = obj.ret;
		List<Object> searchResult = JSON.parseArray(((JSONArray) obj.value).toJSONString());

		List<Set> result0 = JSON.parseArray(((JSONArray) searchResult.get(0)).toJSONString(), Set.class);
		List<Set> result1 = JSON.parseArray(((JSONArray) searchResult.get(1)).toJSONString(), Set.class);
		List<Part> result2 = JSON.parseArray(((JSONArray) searchResult.get(2)).toJSONString(), Part.class);

		return 0;
	}

	String test_getSearchResult = "{\"ret\":0,\"value\":[[{\"setQuantity\":\"0\",\"minifigQuantity\":\"2\",\"partQuantity\":\"367\",\"no\":\"8257-1\",\"name\":\"cyber strikers\",\"category\":\"technic\",\"type\":\"Set\",\"yearBegin\":\"1998\",\"yearEnd\":\"1998\",\"imgLink\":\"http:\\/\\/img.bricklink.com\\/s\\/8257-1.gif\",\"comment\":\"\"}],[],[]],\"info\":\"search result in database\"}";

	/**
	 * 
	 * @param type
	 * @param year
	 *            , 0 if any year
	 * @param itemNo
	 * @param name
	 * @param result
	 * @return 0 if success( 0 results included ), > 0 NET fail, < 0 server fail
	 */
	public int getSearchResult(String type, int year, String itemNo, String name, ParamObj<List<List>> result) {
		SearchRequest request = new SearchRequest();
		request.type = type;
		request.year = year;
		request.itemNo = itemNo;
		request.name = name;

		ParamObj<String> paramObj = new DefaultParamObj<String>();
		int retValue = sendPost2(URL_SERVER_HOME + URLPATH_SEARCH, request.toParam(), paramObj);

		result.value = new ArrayList<List>(3);
		if (retValue == 0) {
			CommuObj obj = null;
			try {
				obj = JSON.parseObject(paramObj.value, CommuObj.class);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return CODE_INNER_ERROR;
			}

			if (obj.ret == CommuObj.RET_SUCCESS) {
				List<Object> searchResult = JSON.parseArray(((JSONArray) obj.value).toJSONString());

				List<Set> result0;
				List<Part> result1;
				List<Minifig> result2;

				try {
					result0 = JSON.parseArray(((JSONArray) searchResult.get(0)).toJSONString(), Set.class);
					result.value.add(result0);

					result2 = JSON.parseArray(((JSONArray) searchResult.get(1)).toJSONString(), Minifig.class);
					result.value.add(result2);

					result1 = JSON.parseArray(((JSONArray) searchResult.get(2)).toJSONString(), Part.class);
					result.value.add(result1);
				} catch (Exception e) {
					// TODO: handle exception
					return CODE_INNER_ERROR;
				}

			} else {

			}
			return obj.ret;
		} else {
			return CODE_NET_FAILED;
		}

	}

	public int getItemInfo(String type, String no, ParamObj<LegoItem> result) {
		Request_ItemInfo request_ItemInfo = new Request_ItemInfo();
		request_ItemInfo.no = no;
		request_ItemInfo.type = type;
		int ret = 0;
		ParamObj<String> paramObj = new DefaultParamObj<String>();
		int retValue = sendPost2(URL_SERVER_HOME + Request_ItemInfo.URLPATH, request_ItemInfo.toParam(), paramObj);

		if (retValue == 0) {
			
			try {
				CommuObj obj = JSON.parseObject(paramObj.value, CommuObj.class);

				switch (obj.ret) {
				case CommuObj.RET_SUCCESS:

					String va = ((JSONObject) obj.value).toJSONString();
					LegoItem item = JSON.parseObject(va, LegoItem.class);
					switch (item.getTYPE()) {
					case LegoItem.ITEM_TYPE_SET:
						item = JSON.parseObject(va, Set.class);
						break;

					default:
						break;
					}

					result.value = item;

					break;
				default:
					ret = CODE_UNKOWN_RET;
					break;

				}
			} catch (Exception e) {
				// TODO: handle exception
			    e.printStackTrace();
				ret = CODE_INNER_ERROR;
			}
		} else {
			ret = CODE_NET_FAILED;
		}
		return ret;
	}

	String test_getComponentLiString = "{\"pList\":[],\"Sno\":\"8257-1\"}";

	/**
	 * 
	 * @param itemNo
	 * @param type
	 * @param result
	 * @return 0:success -1:not found
	 */
	public int getComponentList(String itemNo, String type, ParamObj<ComponentList> result) {
		Request_Components request_Comonents = new Request_Components();
		request_Comonents.no = itemNo;
		request_Comonents.type = type;
		int ret = 0;
		ParamObj<String> paramObj = new DefaultParamObj<String>();
		int retValue = sendPost2(URL_SERVER_HOME + URLPATH_GET_COMPONENTS, request_Comonents.toParam(), paramObj);

		if (retValue == 0) {
		    try {
    			Log.i("Return:", paramObj.value);
    			CommuObj obj = JSON.parseObject(paramObj.value, CommuObj.class);
    
    			switch (obj.ret) {
    			case CommuObj.RET_SUCCESS:
    				
    					ComponentList componentList = JSON.parseObject(((JSONObject) obj.value).toJSONString(),
    							ComponentList.class);
    					result.value = componentList;
    				break;
    			case Request_Components.NOT_FOUND:
    				ret = -1;
    				break;
    			default:
    				ret = CODE_UNKOWN_RET;
    				break;
    			}
		    } catch (Exception e) {
                // TODO: handle exception
		        e.printStackTrace();
                ret = CODE_INNER_ERROR;
            }
		} else {
			ret = CODE_NET_FAILED;
		}
		return ret;
	}
	/**
	 * 
	 * @param name
	 * @param psd
	 * @param uIndex
	 * @param result
	 * @return 0 : success , -1:authentication failed
	 */
	public int requestImport(String name, String psd, int uIndex, ParamObj<ImportResult> result)
	{
		int ret = 0;
		ServerRequest request = new Request_Import(uIndex, name, psd);
		ParamObj<String> paramObj = new DefaultParamObj<String>();
		int retValue = sendPost2(URL_SERVER_HOME + Request_Import.URLPATH, request.toParam(), paramObj);
		if (retValue == 0) {
			Log.i("Return:", paramObj.value);
			CommuObj obj = JSON.parseObject(paramObj.value, CommuObj.class);

			switch (obj.ret) {
			case CommuObj.RET_SUCCESS:
				try {
					ImportResult importResult = JSON.parseObject(((JSONObject) obj.value).toJSONString(),
							ImportResult.class);
					result.value = importResult;
				} catch (Exception e) {
					// TODO: handle exception
					ret = CODE_INNER_ERROR;
				}
				break;
			case Request_Import.RET_AUTHENTICATION_FAILED:
				ret = -1;
				break;
			default:
				ret = CODE_UNKOWN_RET;
				break;

			}
		} else {
			ret = CODE_NET_FAILED;
		}
		return ret;
	}
	
	
	

	// ///////////// 图片下载 ///////////////
	/**
	 * Chen Xiaoyu
	 * 
	 * @param path
	 * @return null if fail
	 * @throws MalformedURLException
	 */
	public byte[] downloadBytes(String path) throws MalformedURLException {
		URL url;
		url = new URL(path);
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000);
			// conn.setRequestMethod("GET");
			InputStream inStream = conn.getInputStream();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return readStream(inStream);
			}
		} catch (Exception e) {
			// TODO , Cxy, Chen Xiaoyu, 2011-11-24 下午3:57:51
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get data from stream
	 * 
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 
	 * @param uName
	 * @param uPsd
	 * @param retObj
	 * @return 0 success, -1:wrong psd or name
	 */
	public int userVerify(String uName, String uPsd, ParamObj<LegoingUser> retObj) {
		int ret = 0;
		Request_UserVerify request_UserVerify = new Request_UserVerify();
		request_UserVerify.name = uName;
		request_UserVerify.psd = uPsd;
		ParamObj<String> paramObj = new DefaultParamObj<String>();
		int retValue = sendPost2(URL_SERVER_HOME + URLPATH_USER_VERIFY, request_UserVerify.toParam(), paramObj);

		if (retValue == 0) {
			Log.i("Return:", paramObj.value);
			try {
				CommuObj obj = JSON.parseObject(paramObj.value, CommuObj.class);
				switch (obj.ret) {
				case CommuObj.RET_SUCCESS:

					LegoingUser user = JSON.parseObject(((JSONObject) obj.value).toJSONString(), LegoingUser.class);
					retObj.value = user;

					break;
				case Request_UserVerify.VERIFY_FAILED:
					ret = -1;
					break;
				default:
					ret = CODE_UNKOWN_RET;
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				ret = CODE_INNER_ERROR;
			}

		} else {
			ret = CODE_NET_FAILED;
		}
		return ret;
	}

	/**
	 * 12/31 17:35 未测试
	 * 
	 * @param uIndex
	 * @return
	 */
	public int getUserItems(int uIndex, ParamObj<UserItems> result) {
		int ret = 0;
		Request_UserItems request_UserItems = new Request_UserItems();
		request_UserItems.userIndex = uIndex;
		ParamObj<String> paramObj = new DefaultParamObj<String>();
		int retValue = sendPost2(URL_SERVER_HOME + Request_UserItems.URLPATH, request_UserItems.toParam(), paramObj);

		if (retValue == 0) {
			Log.i("Return:", paramObj.value);
			CommuObj obj;
			try {
				obj = JSON.parseObject(paramObj.value, CommuObj.class);
			} catch (Exception e) {
				// TODO: handle exception
				return CODE_INNER_ERROR;
			}

			switch (obj.ret) {
			case CommuObj.RET_SUCCESS:
				try {
					UserItems items = JSON.parseObject(((JSONObject) obj.value).toJSONString(), UserItems.class);
					result.value = items;
					// System.out.print(items.getUserIndex());

				} catch (Exception e) {
					// TODO: handle exception
					ret = CODE_INNER_ERROR;
				}
				break;
			default:
				ret = CODE_UNKOWN_RET;
				break;
			}

		} else {
			ret = CODE_NET_FAILED;
		}
		return ret;
	}

	/**
	 * 
	 * @param uIndex
	 * @param type
	 * @param no
	 * @param quantity
	 * @param color
	 * @return 0 success, -1:exist
	 */
	public int addUserItem(int uIndex, String type, String no, int quantity, int color) {
		int ret = 0;
		Request_AddUserItem request_AddUserItem = new Request_AddUserItem(uIndex, type, no, quantity, color);
		ParamObj<String> paramObj = new DefaultParamObj<String>();
		int retValue = sendPost2(URL_SERVER_HOME + Request_AddUserItem.URLPATH, request_AddUserItem.toParam(), paramObj);

		if (retValue == 0) {
			Log.i("Return:", paramObj.value);
			CommuObj obj;
			try {
				obj = JSON.parseObject(paramObj.value, CommuObj.class);
			} catch (Exception e) {
				return CODE_INNER_ERROR;
			}

			switch (obj.ret) {
			case CommuObj.RET_SUCCESS:
				ret = 0;
				break;
			case Request_AddUserItem.RET_EXIST:
				ret = -1;
				break;

			default:
				ret = CODE_UNKOWN_RET;
				break;
			}

		} else {
			ret = CODE_NET_FAILED;
		}
		return ret;
	}
	
	public int requestEvalutation(String setNo, ParamObj<LegoItemEvaluation> result )
	{
	    int ret = 0;
	    Request_Evaluation request_Evaluation = new Request_Evaluation(setNo);
        ParamObj<String> paramObj = new DefaultParamObj<String>();
        int retValue = sendPost2(URL_SERVER_HOME + Request_Evaluation.URLPATH, request_Evaluation.toParam(), paramObj);

        if (retValue == 0) {
            try {
                CommuObj obj;
                obj = JSON.parseObject(paramObj.value, CommuObj.class);
                switch (obj.ret) {
                case CommuObj.RET_SUCCESS:
                    LegoItemEvaluation evaluation = JSON.parseObject(((JSONObject) obj.value).toJSONString(),LegoItemEvaluation.class);
                    result.value = evaluation;
                    ret = 0;
                    break;
                case Request_Evaluation.RET_404:
                    ret = -1;
                    break;
    
                default:
                    ret = CODE_UNKOWN_RET;
                    break;
                }
            } catch (Exception e) {
                // _TODO: handle exception
                return CODE_INNER_ERROR;
            }
        } else {
            ret = CODE_NET_FAILED;
        }
        return ret;
	}
	
	public int requestAddComment(String setNo, int userIndex, int score, String desc)
	{
	    // 4/28 _TODO
	    int ret = -1;
	    Request_AddComment request_AddComment = new Request_AddComment(setNo, score, desc, userIndex);
	    
	    ParamObj<String> paramObj = new DefaultParamObj<String>();
        int retValue = sendPost2(URL_SERVER_HOME + Request_AddComment.URLPATH, request_AddComment.toParam(), paramObj);

        if (retValue == 0) {
            try {
                CommuObj obj;
                obj = JSON.parseObject(paramObj.value, CommuObj.class);
                switch (obj.ret) {
                case CommuObj.RET_SUCCESS:
                    ret = 0;
                    break;
    
                default:
                    ret = CODE_UNKOWN_RET;
                    break;
                }
            } catch (Exception e) {
                // _TODO: handle exception
                return CODE_INNER_ERROR;
            }
        } else {
            ret = CODE_NET_FAILED;
        }
        return ret;
	}
	
	public int requestTopSuggest(ParamObj<List<Set>> result){
	    int ret = -1;
	    Request_TopSuggest request_TopSuggest = new Request_TopSuggest();

	    ParamObj<String> paramObj = new DefaultParamObj<String>();
	    int retValue = sendPost2(URL_SERVER_HOME + Request_TopSuggest.URLPATH, request_TopSuggest.toParam(), paramObj);

	    if (retValue == 0) {
	        try {
	            CommuObj obj;
	            obj = JSON.parseObject(paramObj.value, CommuObj.class);
	            switch (obj.ret) {
	                case CommuObj.RET_SUCCESS:
	                    List<Set> sets = JSON.parseArray( ((JSONArray)obj.value).toJSONString(),Set.class);
	                    result.value = sets;
	                    ret = 0;
	                    break;
	                    

	                default:
	                    ret = CODE_UNKOWN_RET;
	                    break;
	            }
	        } catch (Exception e) {
	            // _TODO: handle exception
	            return CODE_INNER_ERROR;
	        }
	    } else {
	        ret = CODE_NET_FAILED;
	    }
	    return ret;
	    
	}
	
	public int requestBarcodeSearch(String code, ParamObj<List<List>> result) {

	    Request_BarcodeSearch request = new Request_BarcodeSearch(code);

        ParamObj<String> paramObj = new DefaultParamObj<String>();
        int retValue = sendPost2(URL_SERVER_HOME + Request_BarcodeSearch.URLPATH, request.toParam(), paramObj);

        result.value = new ArrayList<List>(3);
        if (retValue == 0) {
            CommuObj obj = null;
            try {
                obj = JSON.parseObject(paramObj.value, CommuObj.class);

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return CODE_INNER_ERROR;
            }

            if (obj.ret == CommuObj.RET_SUCCESS) {
                List<Object> searchResult = JSON.parseArray(((JSONArray) obj.value).toJSONString());

                List<Set> result0;
                List<Part> result1;
                List<Minifig> result2;

                try {
                    result0 = JSON.parseArray(((JSONArray) searchResult.get(0)).toJSONString(), Set.class);
                    result.value.add(result0);

                    result2 = JSON.parseArray(((JSONArray) searchResult.get(1)).toJSONString(), Minifig.class);
                    result.value.add(result2);

                    result1 = JSON.parseArray(((JSONArray) searchResult.get(2)).toJSONString(), Part.class);
                    result.value.add(result1);
                } catch (Exception e) {
                    // _TODO: handle exception
                    return CODE_INNER_ERROR;
                }

            } else if(obj.ret == Request_BarcodeSearch.RET_FAILED){
                return -1;
            } else {
                return CODE_UNKOWN_RET;
            }
            return obj.ret;
        } else {
            return CODE_NET_FAILED;
        }

    }
	public void handleReturnValue(int ret)
	{
		switch (ret) {
		case CODE_NET_FAILED:
			Toast.makeText(StaticOverall.getMainActivity(), R.string.msg_Error_ServerConnectFail, Toast.LENGTH_SHORT).show();
			break;
		case CODE_INNER_ERROR:
			Toast.makeText(StaticOverall.getMainActivity(), R.string.msg_Error_INNER, Toast.LENGTH_SHORT).show();
			break;
		case CODE_UNKOWN_RET:
			Toast.makeText(StaticOverall.getMainActivity(), R.string.msg_Error_Unkown, Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}
