package com.android.chalkboard.school.service;

import com.android.chalkboard.school.model.AddSchoolResponse;
import com.android.chalkboard.school.model.EditSchoolRequest;
import com.android.chalkboard.school.model.EditSchoolResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EditSchoolServiceInterface {

    @PUT("/admin/institution")
    Call<EditSchoolResponse> editInstitute( @Body EditSchoolRequest editSchoolRequest);
}
