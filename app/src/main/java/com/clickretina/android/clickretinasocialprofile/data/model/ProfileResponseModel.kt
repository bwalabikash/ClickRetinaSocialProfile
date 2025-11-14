package com.clickretina.android.clickretinasocialprofile.data.model

import com.google.gson.annotations.SerializedName

data class ProfileResponseModel(

	@field:SerializedName("user")
	val user: User? = null
)

data class ProfileStatistics(

	@field:SerializedName("followers")
	val followers: Int? = null,

	@field:SerializedName("activity")
	val activity: ProfileActivity? = null,

	@field:SerializedName("following")
	val following: Int? = null
)

data class ProfileLocation(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("city")
	val city: String? = null
)

data class ProfileProfilesItem(

	@field:SerializedName("platform")
	val platform: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class User(

	@field:SerializedName("social")
	val social: ProfileSocial? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("location")
	val location: ProfileLocation? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("statistics")
	val statistics: ProfileStatistics? = null
)

data class ProfileActivity(

	@field:SerializedName("collections")
	val collections: Int? = null,

	@field:SerializedName("shots")
	val shots: Int? = null
)

data class ProfileSocial(

	@field:SerializedName("website")
	val website: String? = null,

	@field:SerializedName("profiles")
	val profiles: List<ProfileProfilesItem?>? = null
)
