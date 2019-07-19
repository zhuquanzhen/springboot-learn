/**
 * 
 */
package com.huixdou.api.bean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author jinxin 2019年4月25日上午9:38:21
 *
 */
public class AppMessageNew implements Serializable{

	    private static final long serialVersionUID = 1L;
	
	    private String id;

	    private String label;

	    private String labelName;

	    @NotBlank(message="标题不能为空")
	    private String title;

	    @NotBlank(message="内容不能为空")
	    @Size(max = 200,message="内容不能超过200个字符")
	    private String content;

	    @Size(max = 60,message="链接地址不能超过60个字符")
	    private String url;

	    private Integer status = 2;

	    private String sendDate;

	    private String createDate;

	    private Integer groupFlag = 0;

	    private String typeCode;

	    private String typeName;

	    private Integer messageType = 1;

	    private String messageTypeName;
	    
	    private Integer sendNum = 0;

	    private String statusName;

	   /**
	    * 接受人（为了方便接受数据）
	    */
	    @NotBlank(message="请选择接收人")
     	private String member;

	    /*
	     * 判断app端未读消息数量是否加一
	     * 
	     * 0就不加， 1就加一 （默认为0）
	     */
	    private Integer addOne = 0;
	    
	    
	    
	    public Integer getAddOne() {
			return addOne;
		}

		public void setAddOne(Integer addOne) {
			this.addOne = addOne;
		}

		public String getMessageTypeName() {
		   return messageTypeName;
      	}

	    public void setMessageTypeName(String messageTypeName) {
	 	  this.messageTypeName = messageTypeName;
	    }

		public String getStatusName() {
	     	return statusName;
	    }

	    public void setStatusName(String statusName) {
		   this.statusName = statusName;
	    }

	    public String getLabelName() {
	    	return labelName;
	    }

	    public void setLabelName(String labelName) {
		    this.labelName = labelName;
	    }

	    public String getMember() {
		   return member;
	    }

     	public void setMember(String member) {
		   this.member = member;
	    }

	    public String getId() {
			return id;
		}

		public String getLabel() {
			return label;
		}

		public String getTitle() {
			return title;
		}

		public String getContent() {
			return content;
		}

		public String getUrl() {
			return url;
		}

		public Integer getStatus() {
			return status;
		}

		public String getSendDate() {
			return sendDate;
		}

		public String getCreateDate() {
			return createDate;
		}

		public Integer getGroupFlag() {
			return groupFlag;
		}

		public String getTypeCode() {
			return typeCode;
		}

		public String getTypeName() {
			return typeName;
		}

		public Integer getMessageType() {
			return messageType;
		}

		public Integer getSendNum() {
			return sendNum;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public void setSendDate(String sendDate) {
			this.sendDate = sendDate;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public void setGroupFlag(Integer groupFlag) {
			this.groupFlag = groupFlag;
		}

		public void setTypeCode(String typeCode) {
			this.typeCode = typeCode;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public void setMessageType(Integer messageType) {
			this.messageType = messageType;
		}

		public void setSendNum(Integer sendNum) {
			this.sendNum = sendNum;
		}

		
	    
	    
}
