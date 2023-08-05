package com.jisoo.mymovie.response

import com.google.gson.annotations.SerializedName

data class ResponseGuest(

	@field:SerializedName("expires_at")
	val expiresAt: String,

	@field:SerializedName("guest_session_id")
	val guestSessionId: String,

	@field:SerializedName("success")
	val success: Boolean
)
