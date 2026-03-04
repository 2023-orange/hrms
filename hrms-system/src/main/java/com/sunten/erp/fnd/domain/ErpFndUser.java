package com.sunten.erp.fnd.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ErpFndUser implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Long userId;
	private String userName;
	private String encryptedUserPassword;
	private Timestamp startDate;
	private Timestamp endDate;
	private String description;
	private Long employeeId;
	private String emailAddress;
	private String fax;
	private Long customerId;
	private Long supplierId;
	private String webPassword;
	private String userGuid;
	private Long gcnCodeCombinationId;
	private BigDecimal personPartyId;

	// Constructors

	/** default constructor */
	public ErpFndUser() {
	}

	/** minimal constructor */
	public ErpFndUser(String userName, String encryptedUserPassword,
					  Timestamp startDate) {
		this.userName = userName;
		this.encryptedUserPassword = encryptedUserPassword;
		this.startDate = startDate;
	}

	public ErpFndUser(String userName, String encryptedUserPassword,
					  Timestamp startDate, Timestamp endDate, String description,
					  Long employeeId, String emailAddress, String fax, Long customerId,
					  Long supplierId, String webPassword, String userGuid,
					  Long gcnCodeCombinationId, BigDecimal personPartyId) {
		this.userName = userName;
		this.encryptedUserPassword = encryptedUserPassword;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.employeeId = employeeId;
		this.emailAddress = emailAddress;
		this.fax = fax;
		this.customerId = customerId;
		this.supplierId = supplierId;
		this.webPassword = webPassword;
		this.userGuid = userGuid;
		this.gcnCodeCombinationId = gcnCodeCombinationId;
		this.personPartyId = personPartyId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEncryptedUserPassword() {
		return encryptedUserPassword;
	}

	public void setEncryptedUserPassword(String encryptedUserPassword) {
		this.encryptedUserPassword = encryptedUserPassword;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getWebPassword() {
		return webPassword;
	}

	public void setWebPassword(String webPassword) {
		this.webPassword = webPassword;
	}

	public String getUserGuid() {
		return userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

	public Long getGcnCodeCombinationId() {
		return gcnCodeCombinationId;
	}

	public void setGcnCodeCombinationId(Long gcnCodeCombinationId) {
		this.gcnCodeCombinationId = gcnCodeCombinationId;
	}

	public BigDecimal getPersonPartyId() {
		return personPartyId;
	}

	public void setPersonPartyId(BigDecimal personPartyId) {
		this.personPartyId = personPartyId;
	}

	@Override
	public String toString() {
		return "FndUser [userId=" + userId + ", userName=" + userName
				+ ", encryptedUserPassword=" + encryptedUserPassword
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", description=" + description + ", employeeId=" + employeeId
				+ ", emailAddress=" + emailAddress + ", fax=" + fax
				+ ", customerId=" + customerId + ", supplierId=" + supplierId
				+ ", webPassword=" + webPassword + ", userGuid=" + userGuid
				+ ", gcnCodeCombinationId=" + gcnCodeCombinationId
				+ ", personPartyId=" + personPartyId + "]";
	}
	
	
}