package io.github.yamacraft.app.sampra.api

import io.github.yamacraft.app.sampra.api.entity.FunctionVerifyTwitchResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthFunctionApi {
    @FormUrlEncoded
    @POST("verifyTwitch")
    fun verify(@Field("token") token: String):
        Call<FunctionVerifyTwitchResponse>
}
