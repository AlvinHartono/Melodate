package com.example.melodate.data.remote.response

import com.google.gson.annotations.SerializedName

data class CheckEmailResponse(

	@field:SerializedName("available")
	val available: Boolean? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
