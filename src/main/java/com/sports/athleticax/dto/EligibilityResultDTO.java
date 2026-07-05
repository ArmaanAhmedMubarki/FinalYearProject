package com.sports.athleticax.dto;

import com.sports.athleticax.entity.EligibilityStatus;

public class EligibilityResultDTO
{
	private EligibilityStatus eligibilityStatus;
	private String rejectionReason;
	private Long athleteId;
	private Long registrationId;
	private String athleteName;
	private Integer age;
	private Long weight;
	private Long height;
	private String gender;
	private String approvalStatus;

	public EligibilityResultDTO()
	{
	}

	public EligibilityResultDTO(EligibilityStatus eligibilityStatus, String rejectionReason)
	{
		this.eligibilityStatus = eligibilityStatus;
		this.rejectionReason = rejectionReason;
	}

	public EligibilityStatus getEligibilityStatus()
	{
		return eligibilityStatus;
	}

	public void setEligibilityStatus(EligibilityStatus eligibilityStatus)
	{
		this.eligibilityStatus = eligibilityStatus;
	}

	public String getRejectionReason()
	{
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason)
	{
		this.rejectionReason = rejectionReason;
	}

	public Long getAthleteId()
	{
		return athleteId;
	}

	public void setAthleteId(Long athleteId)
	{
		this.athleteId = athleteId;
	}

	public Long getRegistrationId()
	{
		return registrationId;
	}

	public void setRegistrationId(Long registrationId)
	{
		this.registrationId = registrationId;
	}

	public String getAthleteName()
	{
		return athleteName;
	}

	public void setAthleteName(String athleteName)
	{
		this.athleteName = athleteName;
	}

	public Integer getAge()
	{
		return age;
	}

	public void setAge(Integer age)
	{
		this.age = age;
	}

	public Long getWeight()
	{
		return weight;
	}

	public void setWeight(Long weight)
	{
		this.weight = weight;
	}

	public Long getHeight()
	{
		return height;
	}

	public void setHeight(Long height)
	{
		this.height = height;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public String getApprovalStatus()
	{
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus)
	{
		this.approvalStatus = approvalStatus;
	}
}
