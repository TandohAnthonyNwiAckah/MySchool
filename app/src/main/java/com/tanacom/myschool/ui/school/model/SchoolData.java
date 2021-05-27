package com.tanacom.myschool.ui.school.model;

public class SchoolData {

    public static final String TABLE_NAME = "school";

    public static final String COLUMN_SCHOOL_ID = "school_id";
    public static final String COLUMN_SCHOOL_NAME = "school_name";
    public static final String COLUMN_SCHOOL_REGION = "school_region";
    public static final String COLUMN_SCHOOL_COUNTRY = "school_country";
    public static final String COLUMN_DATE_FOUNDED = "date_founded";
    public static final String COLUMN_SCHOOL_STATUS = "school_status";
    public static final String COLUMN_LONGITUDE = "school_longitude";
    public static final String COLUMN_LATITUDE = "school_latitude";
    public static final String COLUMN_SCHOOL_IMAGE = "school_image";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_SCHOOL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SCHOOL_NAME + " TEXT,"
                    + COLUMN_SCHOOL_REGION + " TEXT,"
                    + COLUMN_SCHOOL_COUNTRY + " TEXT,"
                    + COLUMN_DATE_FOUNDED + " DATETIME,"
                    + COLUMN_SCHOOL_STATUS + " TEXT(1),"
                    + COLUMN_LONGITUDE + " DOUBLE,"
                    + COLUMN_LATITUDE + " DOUBLE,"
                    + COLUMN_SCHOOL_IMAGE + " TEXT"
                    + ")";


    int schoolId;
    String schoolName;
    String schoolRegion;
    String schoolCountry;
    String dateFounded;
    String schoolStatus;
    Double schoolLatitude;
    Double schoolLongitude;
    String schoolImage;


    public SchoolData() {
    }


    public SchoolData(int schoolId, String schoolName, String schoolRegion, String schoolCountry, String dateFounded, String schoolStatus, Double schoolLatitude, Double schoolLongitude, String schoolImage) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.schoolRegion = schoolRegion;
        this.schoolCountry = schoolCountry;
        this.dateFounded = dateFounded;
        this.schoolStatus = schoolStatus;
        this.schoolLatitude = schoolLatitude;
        this.schoolLongitude = schoolLongitude;
        this.schoolImage = schoolImage;
    }


    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolRegion() {
        return schoolRegion;
    }

    public void setSchoolRegion(String schoolRegion) {
        this.schoolRegion = schoolRegion;
    }

    public String getSchoolCountry() {
        return schoolCountry;
    }

    public void setSchoolCountry(String schoolCountry) {
        this.schoolCountry = schoolCountry;
    }

    public String getDateFounded() {
        return dateFounded;
    }

    public void setDateFounded(String dateFounded) {
        this.dateFounded = dateFounded;
    }

    public String getSchoolStatus() {
        return schoolStatus;
    }

    public void setSchoolStatus(String schoolStatus) {
        this.schoolStatus = schoolStatus;
    }

    public Double getSchoolLatitude() {
        return schoolLatitude;
    }

    public void setSchoolLatitude(Double schoolLatitude) {
        this.schoolLatitude = schoolLatitude;
    }

    public Double getSchoolLongitude() {
        return schoolLongitude;
    }

    public void setSchoolLongitude(Double schoolLongitude) {
        this.schoolLongitude = schoolLongitude;
    }

    public String getSchoolImage() {
        return schoolImage;
    }

    public void setSchoolImage(String schoolImage) {
        this.schoolImage = schoolImage;
    }
}
