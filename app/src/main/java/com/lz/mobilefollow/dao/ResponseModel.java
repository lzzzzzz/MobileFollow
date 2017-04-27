package com.lz.mobilefollow.dao;

/**返回数据模型*/
public class ResponseModel {

	/**校验信息*/
	private Valiate valiate;
	/**返回数据*/
	private Object data;
	
	/**设置返回码*/
	public void setResponseCode(int code){
		initValiate(code);
	}
	/**初始化校验信息*/
	private void initValiate(int code){
		if(null==this.valiate){
			this.valiate=new Valiate();
		}		
		this.valiate.setRsponseCode(code);
	}
	public Valiate getValiate() {
		return valiate;
	}

	public void setValiate(Valiate valiate) {
		this.valiate = valiate;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**校验返回信息*/
	public class Valiate{
		/**返回码*/
		private int rsponseCode;
		/**返回结果描述*/
		private String resume;
		
		public int getRsponseCode() {
			return rsponseCode;
		}
		public void setRsponseCode(int rsponseCode) {
			this.rsponseCode = rsponseCode;
			this.resume=ResponseMessage.getResponseMessage(rsponseCode);
		}
		public String getResume() {
			return resume;
		}
		public void setResume(String resume) {
			this.resume = resume;
		}
		@Override
		public String toString() {
			return "Valiate [resume=" + resume + ", rsponseCode=" + rsponseCode
					+ "]";
		}	
		
	}
}
