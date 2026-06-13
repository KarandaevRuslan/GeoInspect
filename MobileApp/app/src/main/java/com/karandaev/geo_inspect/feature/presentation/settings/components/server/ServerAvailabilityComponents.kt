package com.karandaev.geo_inspect.feature.presentation.settings.components.server

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.presentation.settings.ServerAvailabilityState

/**
 * Button that displays current server availability state and starts availability check.
 *
 * @param availabilityState Current server availability state.
 * @param enabled Whether the button is enabled.
 * @param onClick Called when the button is clicked.
 */
@Composable
internal fun ServerAvailabilityMarkerButton(
  availabilityState: ServerAvailabilityState,
  enabled: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val contentColor = serverAvailabilityContentColor(
    availabilityState = availabilityState
  )

  Surface(
    modifier = modifier.size(40.dp),
    shape = CircleShape,
    color = serverAvailabilityContainerColor(
      availabilityState = availabilityState
    )
  ) {
    IconButton(
      modifier = Modifier.size(40.dp),
      enabled = enabled,
      onClick = onClick
    ) {
      Box(
        modifier = Modifier.size(24.dp),
        contentAlignment = Alignment.Center
      ) {
        if (availabilityState == ServerAvailabilityState.Checking) {
          CircularProgressIndicator(
            modifier = Modifier.size(22.dp),
            strokeWidth = 2.dp,
            color = contentColor
          )
        } else {
          Icon(
            imageVector = serverAvailabilityIcon(
              availabilityState = availabilityState
            ),
            contentDescription = stringResource(
              R.string.settings_server_availability_check_content_description
            ),
            tint = contentColor,
            modifier = Modifier.size(22.dp)
          )
        }
      }
    }
  }
}

/**
 * Text that describes current server availability state.
 *
 * @param availabilityState Current server availability state.
 */
@Composable
internal fun ServerAvailabilityText(
  availabilityState: ServerAvailabilityState
) {
  Text(
    text = serverAvailabilityText(
      availabilityState = availabilityState
    ),
    style = MaterialTheme.typography.bodySmall,
    color = serverAvailabilityTextColor(
      availabilityState = availabilityState
    )
  )
}

@Composable
private fun serverAvailabilityText(
  availabilityState: ServerAvailabilityState
): String {
  return when (availabilityState) {
    ServerAvailabilityState.Unknown -> {
      stringResource(R.string.settings_server_availability_unknown)
    }

    ServerAvailabilityState.Checking -> {
      stringResource(R.string.settings_server_availability_checking)
    }

    ServerAvailabilityState.Available -> {
      stringResource(R.string.settings_server_availability_available)
    }

    ServerAvailabilityState.Unavailable -> {
      stringResource(R.string.settings_server_availability_unavailable)
    }
  }
}

private fun serverAvailabilityIcon(
  availabilityState: ServerAvailabilityState
): ImageVector {
  return when (availabilityState) {
    ServerAvailabilityState.Unknown -> Icons.Default.RadioButtonUnchecked
    ServerAvailabilityState.Checking -> Icons.Default.Refresh
    ServerAvailabilityState.Available -> Icons.Default.CheckCircle
    ServerAvailabilityState.Unavailable -> Icons.Default.Warning
  }
}

@Composable
private fun serverAvailabilityContainerColor(
  availabilityState: ServerAvailabilityState
): Color {
  return when (availabilityState) {
    ServerAvailabilityState.Unknown -> {
      MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
    }

    ServerAvailabilityState.Checking -> {
      MaterialTheme.colorScheme.primaryContainer
    }

    ServerAvailabilityState.Available -> {
      MaterialTheme.colorScheme.primaryContainer
    }

    ServerAvailabilityState.Unavailable -> {
      MaterialTheme.colorScheme.errorContainer
    }
  }
}

@Composable
private fun serverAvailabilityContentColor(
  availabilityState: ServerAvailabilityState
): Color {
  return when (availabilityState) {
    ServerAvailabilityState.Unknown -> {
      MaterialTheme.colorScheme.onSurfaceVariant
    }

    ServerAvailabilityState.Checking -> {
      MaterialTheme.colorScheme.onPrimaryContainer
    }

    ServerAvailabilityState.Available -> {
      MaterialTheme.colorScheme.onPrimaryContainer
    }

    ServerAvailabilityState.Unavailable -> {
      MaterialTheme.colorScheme.onErrorContainer
    }
  }
}

@Composable
private fun serverAvailabilityTextColor(
  availabilityState: ServerAvailabilityState
): Color {
  return when (availabilityState) {
    ServerAvailabilityState.Available -> MaterialTheme.colorScheme.primary
    ServerAvailabilityState.Unavailable -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.onSurfaceVariant
  }
}