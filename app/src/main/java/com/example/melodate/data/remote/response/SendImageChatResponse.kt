package com.example.melodate.data.remote.response

import com.google.gson.annotations.SerializedName

data class SendImageChatResponse(

	@field:SerializedName("file")
	val file: File? = null,

	@field:SerializedName("roomId")
	val roomId: String? = null,

	@field:SerializedName("predictions")
	val predictions: List<PredictionsItem?>? = null
)

data class PredictionsItem(

	@field:SerializedName("probability")
	val probability: Any? = null,

	@field:SerializedName("className")
	val className: String? = null
)

data class File(

	@field:SerializedName("imageLink")
	val imageLink: String? = null,

	@field:SerializedName("sender")
	val sender: String? = null
)
