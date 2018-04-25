/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be;

/**
 *
 * @author alexl
 */
public class Profile {
    
    private int profileId;
    private String siteName = "";
    private String assetSerialNumber;
    private String type;
    private String externalWorkOrderId;
    private String systemStatus;
    private String userStatus;
    private String createdOn;
    private String createdBy = "SAP";
    private String name;
    private String priority;
    private String status = "NEW";
    private String latestFinishDate;
    private String earliestStartDate;
    private String latestStartDate;
    private String estimatedTime;
    private String profileDescription;
    
    
    /**
     * Creates a profile for use when converting.
     */
    public Profile (int profileId,
                    String assetSerialNumber,
                    String type,
                    String externalWorkOrderId,
                    String systemStatus,
                    String userStatus,
                    String createdOn,
                    String name,
                    String priority,
                    String latestFinishDate,
                    String earliestStartDate,
                    String latestStartDate,
                    String estimatedTime,
                    String profileDesription) {
        this.profileId = profileId;
        this.assetSerialNumber = assetSerialNumber;
        this.type = type;
        this.externalWorkOrderId = externalWorkOrderId;
        this.systemStatus = systemStatus;
        this.userStatus = userStatus;
        this.createdOn = createdOn;
        this.name = name;
        this.priority = priority;
        this.latestFinishDate = latestFinishDate;
        this.earliestStartDate = earliestStartDate;
        this.latestStartDate = latestStartDate;
        this.estimatedTime = estimatedTime;
        this.profileDescription = profileDesription;
    }

    public int getProfileId() {
        return profileId;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getAssetSerialNumber() {
        return assetSerialNumber;
    }

    public String getType() {
        return type;
    }

    public String getExternalWorkOrderId() {
        return externalWorkOrderId;
    }

    public String getSystemStatus() {
        return systemStatus;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getName() {
        return name;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getLatestFinishDate() {
        return latestFinishDate;
    }

    public String getEarliestStartDate() {
        return earliestStartDate;
    }

    public String getLatestStartDate() {
        return latestStartDate;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }
    
    public String getProfileDescription() {
        return profileDescription;
    }
    
    
}
