package com.example.examplet.domain.models

import android.util.Base64
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

class JwtToken(
    val raw: String
) {
    val expiresAt: LocalDateTime

    init {
        val json = String(
            Base64.decode(raw.split(".")[1], Base64.DEFAULT),
            Charsets.UTF_8
        )
        val expiresAtTimestamp = JSONObject(json).getLong(EXPIRES_AT_FIELD)
        expiresAt = LocalDateTime.ofEpochSecond(expiresAtTimestamp, 0, ZoneOffset.UTC)
    }

    fun isNotExpired(): Boolean {
        val now = ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime()
        return now.isBefore(expiresAt)
    }

    private companion object {

        const val EXPIRES_AT_FIELD = "exp"
    }
}
