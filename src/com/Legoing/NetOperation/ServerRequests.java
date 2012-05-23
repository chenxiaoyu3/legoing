package com.Legoing.NetOperation;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

class DefaultNameValuePair implements NameValuePair
{

	String name,value;
	public DefaultNameValuePair(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

}

class SearchRequest {
	public String type = "Any";
	public int year = 0;
	public String itemNo = "";
	public String name = "";
	public List<NameValuePair> toParam()
	{
		List<NameValuePair> retList = new ArrayList<NameValuePair>(4);
		retList.add(new DefaultNameValuePair("type", type));
		retList.add(new DefaultNameValuePair("year", String.valueOf(year)));
		retList.add(new DefaultNameValuePair("no", itemNo));
		retList.add(new DefaultNameValuePair("name", name));
		return retList;
	}

}
abstract class ServerRequest
{
	public abstract List<NameValuePair> toParam();
	public String URLPATH;
}

class Request_ItemInfo extends ServerRequest
{
	public static final String URLPATH = "request_item_detail.php";
	public String type;
	public String no;
	public List<NameValuePair> toParam()
	{
		List<NameValuePair> retList = new ArrayList<NameValuePair>(2);
		retList.add(new DefaultNameValuePair("type", type));
		retList.add(new DefaultNameValuePair("no", no));
		return retList;
	}
}


class Request_Components
{
	public static final int NOT_FOUND = -3;
	public String type;
	public String no;
	public List<NameValuePair> toParam()
	{
		List<NameValuePair> retList = new ArrayList<NameValuePair>(2);
		retList.add(new DefaultNameValuePair("type", type));
		retList.add(new DefaultNameValuePair("no", no));
		return retList;
	}
}

class Request_UserVerify
{
	public static final int VERIFY_FAILED = -3;
	public String name;
	public String psd;
	public List<NameValuePair> toParam()
	{
		List<NameValuePair> retList = new ArrayList<NameValuePair>(2);
		retList.add(new DefaultNameValuePair("name", name));
		retList.add(new DefaultNameValuePair("pass", psd));
		return retList;
	}
}

class Request_UserItems
{
	public static final String URLPATH = "request_user_item_get.php";
	public int userIndex;
	public String type = "Any";
	public List<NameValuePair> toParam()
	{
		List<NameValuePair> retList = new ArrayList<NameValuePair>(2);
		retList.add(new DefaultNameValuePair("index", String.valueOf(userIndex)));
		retList.add(new DefaultNameValuePair("type", type));
		return retList;
	}
}

class Request_AddUserItem
{
	public final static String URLPATH = "request_user_item_add.php";
	public final static int RET_EXIST = -3;
	int uIndex;
	String type;
	String no;
	int quantity;
	int colorIndex;
	

	public Request_AddUserItem(int uIndex, String type, String no,
			int quantity, int color) {
		super();
		this.uIndex = uIndex;
		this.type = type;
		this.no = no;
		this.quantity = quantity;
		this.colorIndex = color;
	}


	public List<NameValuePair> toParam()
	{
		List<NameValuePair> retList = new ArrayList<NameValuePair>(5);
		retList.add(new DefaultNameValuePair("index", String.valueOf(uIndex)));
		retList.add(new DefaultNameValuePair("type", type));
		retList.add(new DefaultNameValuePair("no", no));
		retList.add(new DefaultNameValuePair("quantity", String.valueOf(quantity)));
		retList.add(new DefaultNameValuePair("colorIndex", String.valueOf(colorIndex)));
		return retList;
		
	}
	
}

class Request_Import extends ServerRequest
{
	public static final String URLPATH = "request_user_item_import.php";
	public static final int RET_AUTHENTICATION_FAILED = -3;
	int index;
	String uName, uPsd;
	public Request_Import(int userIndex, String name, String psd) {
		// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 10, 2012 11:42:00 AM
		super();
		this.index = userIndex;
		this.uName = name;
		this.uPsd = psd;
		
	}
	@Override
	public List<NameValuePair> toParam() {
		// _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 10, 2012 11:42:36 AM
		List<NameValuePair> retList = new ArrayList<NameValuePair>(3);
		retList.add(new DefaultNameValuePair("index", String.valueOf(index)));
		retList.add(new DefaultNameValuePair("name", uName));
		retList.add(new DefaultNameValuePair("pass", uPsd));
		return retList;
	}
}
class Request_Evaluation extends ServerRequest{

    public static final String URLPATH = "request_item_comment_get.php";
    public static final int RET_404 = -1;
    String no;
    public Request_Evaluation(String no)
    {
        super();
        this.no = no;
    }
    @Override
    public List<NameValuePair> toParam() {
        // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 26, 2012 2:07:52 PM
        List<NameValuePair> retList = new ArrayList<NameValuePair>(1);
        retList.add(new DefaultNameValuePair("no", no));
        return retList;
    }
    
}

class Request_AddComment extends ServerRequest{

    public static final String URLPATH = "request_item_comment_add.php";
    String no;
    int score;
    String desc;
    int index;
    public Request_AddComment(String no, int score, String desc, int index)
    {
        this.no = no;
        this.score = score;
        this.desc = desc;
        this.index = index;
    }
    @Override
    public List<NameValuePair> toParam() {
        // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, Apr 28, 2012 4:26:19 PM
        List<NameValuePair> retList = new ArrayList<NameValuePair>(4);
        retList.add(new DefaultNameValuePair("no", no));
        retList.add(new DefaultNameValuePair("score", String.valueOf(score)));
        retList.add(new DefaultNameValuePair("des", desc));
        retList.add(new DefaultNameValuePair("index", String.valueOf(index)));
        return retList;
    }
    
}
class Request_TopSuggest extends ServerRequest{
    public static final String URLPATH = "request_item_suggest.php";

    @Override
    public List<NameValuePair> toParam() {
        // _TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 8, 2012 4:02:26 PM
        return new ArrayList<NameValuePair>(0);
    }
    
}

class Request_BarcodeSearch extends ServerRequest{
    public static final String URLPATH = "request_item_search_barcode.php";
    public static final int RET_FAILED = -2;
    String code;
    public Request_BarcodeSearch(String code)
    {
        this.code = code;
    }
    @Override
    public List<NameValuePair> toParam() {
        // TODO , Xiaoyu Chen<chenxiaoyu3@gmail.com>, May 8, 2012 8:04:23 PM
        List<NameValuePair> retList = new ArrayList<NameValuePair>(4);
        retList.add(new DefaultNameValuePair("bar", code));
        return retList;
    }
}