package com.example.ratefilm.model

data class CreditResponse(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)