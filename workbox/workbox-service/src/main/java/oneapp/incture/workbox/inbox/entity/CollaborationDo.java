package oneapp.incture.workbox.inbox.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import oneapp.incture.workbox.poadapter.entity.BaseDo;

@Entity
@Table(name = "COLLABORATION")

public class CollaborationDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MESSAGE_ID", length = 70)
	private String messageId=UUID.randomUUID().toString().replaceAll("-", "");

	@Column(name = "PROCESS_ID", length = 70)
	private String processId;

	@Column(name = "EVENT_ID", length = 70)
	private String eventId;

	@Column(name = "USER_ID", length = 70)
	private String userId;

	@Column(name = "USER_DISPLAY_NAME", length = 255)
	private String userDisplayName;

	@Column(name = "CREATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "MESSAGE", length = 2000)
	private String message;

	@Column(name = "CHAT_ID", length = 70)
	private String chatId;

	@Column(name = "CHAT_DISPLAY_NAME", length = 70)
	private String chatDisplayName;
	
	@Column(name = "TAGGED_USER_ID", length = 70)
	private String taggedUserId;

	public String getMessageId() {
		return messageId;
	}

	public String getTaggedUserId() {
		return taggedUserId;
	}

	public void setTaggedUserId(String taggedUserId) {
		this.taggedUserId = taggedUserId;
	}

	/**
	 * @param messageId
	 *            the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the processId
	 */
	public String getProcessId() {
		return processId;
	}

	/**
	 * @param processId
	 *            the processId to set
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
	 * @return the eventId
	 */
	public String getEventId() {
		return eventId;
	}

	/**
	 * @param eventId
	 *            the eventId to set
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userDisplayName
	 */
	public String getUserDisplayName() {
		return userDisplayName;
	}

	/**
	 * @param userDisplayName
	 *            the userDisplayName to set
	 */
	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the chatId
	 */
	public String getChatId() {
		return chatId;
	}

	/**
	 * @param chatId
	 *            the chatId to set
	 */
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	/**
	 * @return the chatDisplayName
	 */
	public String getChatDisplayName() {
		return chatDisplayName;
	}

	/**
	 * @param chatDisplayName
	 *            the chatDisplayName to set
	 */
	public void setChatDisplayName(String chatDisplayName) {
		this.chatDisplayName = chatDisplayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CollaborationDo [messageId=" + messageId + ", processId=" + processId + ", eventId=" + eventId
				+ ", userId=" + userId + ", userDisplayName=" + userDisplayName + ", createdAt=" + createdAt
				+ ", message=" + message + ", chatId=" + chatId + ", chatDisplayName=" + chatDisplayName + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return messageId;
	}

}