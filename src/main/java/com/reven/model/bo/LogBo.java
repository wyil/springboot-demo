package com.reven.model.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class LogBo implements Serializable {
	private static final long serialVersionUID = -1230182677210934707L;
	private String logType;
	private String operatorId;
	private String operatorName;
	private String clientIp;
	private String businessId;
	private String businessName;
	private String operationResult;
	private String businessType;
	private String operationDomain;
	private Map<String, String> otherInfoMap;
	private String description;
	private Date operationDate;
	private String operationDateStart;
	private String operationDateEnd;

	public String getLogType() {
		return this.logType;
	}

	public String getOperatorId() {
		return this.operatorId;
	}

	public String getOperatorName() {
		return this.operatorName;
	}

	public String getClientIp() {
		return this.clientIp;
	}

	public String getBusinessId() {
		return this.businessId;
	}

	public String getBusinessName() {
		return this.businessName;
	}

	public String getOperationResult() {
		return this.operationResult;
	}

	public String getBusinessType() {
		return this.businessType;
	}

	public String getOperationDomain() {
		return this.operationDomain;
	}

	public Map<String, String> getOtherInfoMap() {
		return this.otherInfoMap;
	}

	public String getDescription() {
		return this.description;
	}

	public Date getOperationDate() {
		return this.operationDate;
	}

	public String getOperationDateStart() {
		return this.operationDateStart;
	}

	public String getOperationDateEnd() {
		return this.operationDateEnd;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public void setOperationResult(String operationResult) {
		this.operationResult = operationResult;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public void setOperationDomain(String operationDomain) {
		this.operationDomain = operationDomain;
	}

	public void setOtherInfoMap(Map<String, String> otherInfoMap) {
		this.otherInfoMap = otherInfoMap;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public void setOperationDateStart(String operationDateStart) {
		this.operationDateStart = operationDateStart;
	}

	public void setOperationDateEnd(String operationDateEnd) {
		this.operationDateEnd = operationDateEnd;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof LogBo)) {
			return false;
		} else {
			LogBo other = (LogBo) o;
			if (!other.canEqual(this)) {
				return false;
			} else {
				Object this$logType = this.getLogType();
				Object other$logType = other.getLogType();
				if (this$logType == null) {
					if (other$logType != null) {
						return false;
					}
				} else if (!this$logType.equals(other$logType)) {
					return false;
				}

				Object this$operatorId = this.getOperatorId();
				Object other$operatorId = other.getOperatorId();
				if (this$operatorId == null) {
					if (other$operatorId != null) {
						return false;
					}
				} else if (!this$operatorId.equals(other$operatorId)) {
					return false;
				}

				Object this$operatorName = this.getOperatorName();
				Object other$operatorName = other.getOperatorName();
				if (this$operatorName == null) {
					if (other$operatorName != null) {
						return false;
					}
				} else if (!this$operatorName.equals(other$operatorName)) {
					return false;
				}

				label158 : {
					Object this$clientIp = this.getClientIp();
					Object other$clientIp = other.getClientIp();
					if (this$clientIp == null) {
						if (other$clientIp == null) {
							break label158;
						}
					} else if (this$clientIp.equals(other$clientIp)) {
						break label158;
					}

					return false;
				}

				label151 : {
					Object this$businessId = this.getBusinessId();
					Object other$businessId = other.getBusinessId();
					if (this$businessId == null) {
						if (other$businessId == null) {
							break label151;
						}
					} else if (this$businessId.equals(other$businessId)) {
						break label151;
					}

					return false;
				}

				Object this$businessName = this.getBusinessName();
				Object other$businessName = other.getBusinessName();
				if (this$businessName == null) {
					if (other$businessName != null) {
						return false;
					}
				} else if (!this$businessName.equals(other$businessName)) {
					return false;
				}

				label137 : {
					Object this$operationResult = this.getOperationResult();
					Object other$operationResult = other.getOperationResult();
					if (this$operationResult == null) {
						if (other$operationResult == null) {
							break label137;
						}
					} else if (this$operationResult.equals(other$operationResult)) {
						break label137;
					}

					return false;
				}

				label130 : {
					Object this$businessType = this.getBusinessType();
					Object other$businessType = other.getBusinessType();
					if (this$businessType == null) {
						if (other$businessType == null) {
							break label130;
						}
					} else if (this$businessType.equals(other$businessType)) {
						break label130;
					}

					return false;
				}

				Object this$operationDomain = this.getOperationDomain();
				Object other$operationDomain = other.getOperationDomain();
				if (this$operationDomain == null) {
					if (other$operationDomain != null) {
						return false;
					}
				} else if (!this$operationDomain.equals(other$operationDomain)) {
					return false;
				}

				Object this$otherInfoMap = this.getOtherInfoMap();
				Object other$otherInfoMap = other.getOtherInfoMap();
				if (this$otherInfoMap == null) {
					if (other$otherInfoMap != null) {
						return false;
					}
				} else if (!this$otherInfoMap.equals(other$otherInfoMap)) {
					return false;
				}

				label109 : {
					Object this$description = this.getDescription();
					Object other$description = other.getDescription();
					if (this$description == null) {
						if (other$description == null) {
							break label109;
						}
					} else if (this$description.equals(other$description)) {
						break label109;
					}

					return false;
				}

				label102 : {
					Object this$operationDate = this.getOperationDate();
					Object other$operationDate = other.getOperationDate();
					if (this$operationDate == null) {
						if (other$operationDate == null) {
							break label102;
						}
					} else if (this$operationDate.equals(other$operationDate)) {
						break label102;
					}

					return false;
				}

				Object this$operationDateStart = this.getOperationDateStart();
				Object other$operationDateStart = other.getOperationDateStart();
				if (this$operationDateStart == null) {
					if (other$operationDateStart != null) {
						return false;
					}
				} else if (!this$operationDateStart.equals(other$operationDateStart)) {
					return false;
				}

				Object this$operationDateEnd = this.getOperationDateEnd();
				Object other$operationDateEnd = other.getOperationDateEnd();
				if (this$operationDateEnd == null) {
					if (other$operationDateEnd != null) {
						return false;
					}
				} else if (!this$operationDateEnd.equals(other$operationDateEnd)) {
					return false;
				}

				return true;
			}
		}
	}

	protected boolean canEqual(Object other) {
		return other instanceof LogBo;
	}

	public int hashCode() {
		int result = 1;
		Object $logType = this.getLogType();
		result = result * 59 + ($logType == null ? 43 : $logType.hashCode());
		Object $operatorId = this.getOperatorId();
		result = result * 59 + ($operatorId == null ? 43 : $operatorId.hashCode());
		Object $operatorName = this.getOperatorName();
		result = result * 59 + ($operatorName == null ? 43 : $operatorName.hashCode());
		Object $clientIp = this.getClientIp();
		result = result * 59 + ($clientIp == null ? 43 : $clientIp.hashCode());
		Object $businessId = this.getBusinessId();
		result = result * 59 + ($businessId == null ? 43 : $businessId.hashCode());
		Object $businessName = this.getBusinessName();
		result = result * 59 + ($businessName == null ? 43 : $businessName.hashCode());
		Object $operationResult = this.getOperationResult();
		result = result * 59 + ($operationResult == null ? 43 : $operationResult.hashCode());
		Object $businessType = this.getBusinessType();
		result = result * 59 + ($businessType == null ? 43 : $businessType.hashCode());
		Object $operationDomain = this.getOperationDomain();
		result = result * 59 + ($operationDomain == null ? 43 : $operationDomain.hashCode());
		Object $otherInfoMap = this.getOtherInfoMap();
		result = result * 59 + ($otherInfoMap == null ? 43 : $otherInfoMap.hashCode());
		Object $description = this.getDescription();
		result = result * 59 + ($description == null ? 43 : $description.hashCode());
		Object $operationDate = this.getOperationDate();
		result = result * 59 + ($operationDate == null ? 43 : $operationDate.hashCode());
		Object $operationDateStart = this.getOperationDateStart();
		result = result * 59 + ($operationDateStart == null ? 43 : $operationDateStart.hashCode());
		Object $operationDateEnd = this.getOperationDateEnd();
		result = result * 59 + ($operationDateEnd == null ? 43 : $operationDateEnd.hashCode());
		return result;
	}

	public String toString() {
		return "LogBo(logType=" + this.getLogType() + ", operatorId=" + this.getOperatorId() + ", operatorName="
				+ this.getOperatorName() + ", clientIp=" + this.getClientIp() + ", businessId=" + this.getBusinessId()
				+ ", businessName=" + this.getBusinessName() + ", operationResult=" + this.getOperationResult()
				+ ", businessType=" + this.getBusinessType() + ", operationDomain=" + this.getOperationDomain()
				+ ", otherInfoMap=" + this.getOtherInfoMap() + ", description=" + this.getDescription()
				+ ", operationDate=" + this.getOperationDate() + ", operationDateStart=" + this.getOperationDateStart()
				+ ", operationDateEnd=" + this.getOperationDateEnd() + ")";
	}

	public LogBo() {
	}

	public LogBo(String logType, String operatorId, String operatorName, String clientIp, String businessId,
			String businessName, String operationResult, String businessType, String operationDomain,
			Map<String, String> otherInfoMap, String description, Date operationDate, String operationDateStart,
			String operationDateEnd) {
		this.logType = logType;
		this.operatorId = operatorId;
		this.operatorName = operatorName;
		this.clientIp = clientIp;
		this.businessId = businessId;
		this.businessName = businessName;
		this.operationResult = operationResult;
		this.businessType = businessType;
		this.operationDomain = operationDomain;
		this.otherInfoMap = otherInfoMap;
		this.description = description;
		this.operationDate = operationDate;
		this.operationDateStart = operationDateStart;
		this.operationDateEnd = operationDateEnd;
	}
}