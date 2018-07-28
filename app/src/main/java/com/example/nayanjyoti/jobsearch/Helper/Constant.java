package com.example.nayanjyoti.jobsearch.Helper;

public class Constant {

    private static final String BASE_URL = "http://192.168.225.132/mscit/controllers/";

    public static final String LOGIN_URL = BASE_URL+"login.php";
    public static final String REGISTER_URL = BASE_URL+"register.php";
    public static final String GET_EDUCATION_DATA = BASE_URL+"education_data.php";
    public static final String ADD_CADIDATE_EDUCATION = BASE_URL+"candidate/add_education.php";
    public static final String GET_CANDIDATE_DATA = BASE_URL+"candidate/get_candidate_eduaction.php?user_id=";
    public static final String ADD_POST = BASE_URL+"recruiter/create_post.php";
    public static final String MY_POSTS = BASE_URL+"recruiter/my_posts.php?user_id=";
    public static final String CREATE_COMPANY = BASE_URL+"recruiter/create_company.php";
    public static final String ADD_POST_EDUCATION = BASE_URL+"recruiter/add_post_education.php";
    public static final String GET_POST_EDUCATION = BASE_URL+"recruiter/get_post_education.php?post_id=";
    public static final String RECOMMENDED_POST = BASE_URL+"candidate/recommended_post.php?user_id=";
    public static final String APPLY_POST = BASE_URL+"candidate/apply_post.php";
    public static final String APPLY_POST_LIST = BASE_URL+"candidate/apply_post_list.php?user_id=";
    public static final String APPLICANT_LIST = BASE_URL+"recruiter/applicant_list.php?post_id=";
    public static final String GET_USER = BASE_URL+"user.php?user_id=";
    public static final String UPDATE_USER = BASE_URL+"profile_update.php";
    public static final String DELETE_CANDIDATE_EDUCATION = BASE_URL+"candidate/delete_education.php";

    public static final String COMPANY_DETAILS = BASE_URL+"company_details.php?user_id=";

    public static final String RECRUITER = "recruiter";
    public static final String CANDIDATE = "candidate";

    public static final String MALE = "male";



}
