package com.example.melodate.data.remote.response

import com.google.gson.annotations.SerializedName

data class DeleteAccountResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
