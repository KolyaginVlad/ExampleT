package com.example.examplet.data

import com.example.examplet.data.models.TokenDataExportDto
import com.example.examplet.data.models.TokenDataImportDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/refresh")
    suspend fun refreshBearer(@Body body: TokenDataExportDto): Result<TokenDataImportDto>
}