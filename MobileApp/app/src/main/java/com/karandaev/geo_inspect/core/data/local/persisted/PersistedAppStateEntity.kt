package com.karandaev.geo_inspect.core.data.local.persisted

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Universal key-value storage for app runtime state persisted between sessions.
 *
 * Values are stored as strings. Complex values can be encoded as JSON by repository mappers.
 */
@Entity(tableName = "persisted_app_state")
data class PersistedAppStateEntity(
  @PrimaryKey
  val key: String,
  val value: String,
  val updatedAt: Long
)