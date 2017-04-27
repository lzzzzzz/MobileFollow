package com.lz.mobilefollow.dao;

import com.lz.mobilefollow.lzcore.constant.MessageConfig;

import java.util.HashMap;
import java.util.Map;

public class ResponseMessage {

	private static Map<Integer,String> res=new HashMap<Integer,String>(){
		{
			put(MessageConfig.RESPONSE_SUCCESS,MessageConfig.RESPONSE_SUCCESS_MESSAGE);
			put(MessageConfig.RESPONSE_FAILED,MessageConfig.RESPONSE_FAILED_MESSAGE);
		}
	};
	
	public static String getResponseMessage(int code){
		String message=res.get(code);
		return message;
	}
}
