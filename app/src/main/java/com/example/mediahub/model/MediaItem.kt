package com.example.mediahub.model

// enum class : static values that should not change
enum class UserRole {STUDENT, TEACHER}
data class UserProfile(
    val uid: String = "",
    val fullname: String = "",
    val email: String = "",
    val role: String = "student" // default student
){
    //tomap will reference live values from firebase
    // for credential checkup
    fun toMap(): Map<String,Any> = mapOf(
        "fullName" to fullname ,
        "email" to email,
        "role" to role
    )
    // will return appropriate user role
    fun userRole(): UserRole =
        if(role == "teacher") UserRole.TEACHER else
            UserRole.STUDENT
}