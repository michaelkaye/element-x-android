/*
 * Copyright (c) 2023 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.element.android.libraries.designsystem.components.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.element.android.libraries.designsystem.components.preferences.components.PreferenceIcon
import io.element.android.libraries.designsystem.preview.ElementThemedPreview
import io.element.android.libraries.designsystem.preview.PreviewGroup
import io.element.android.libraries.designsystem.theme.components.Checkbox
import io.element.android.libraries.designsystem.theme.components.Text
import io.element.android.libraries.designsystem.toEnabledColor
import io.element.android.libraries.theme.ElementTheme

@Composable
fun PreferenceCheckbox(
    title: String,
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    showIconAreaIfNoIcon: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = preferenceMinHeight)
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 4.dp, horizontal = preferencePaddingHorizontal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreferenceIcon(
            icon = icon,
            enabled = enabled,
            isVisible = showIconAreaIfNoIcon
        )
        Text(
            modifier = Modifier
                .weight(1f),
            style = ElementTheme.typography.fontBodyLgRegular,
            text = title,
            color = enabled.toEnabledColor(),
        )
        Checkbox(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            checked = isChecked,
            enabled = enabled,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(group = PreviewGroup.Preferences)
@Composable
internal fun PreferenceCheckboxPreview() = ElementThemedPreview { ContentToPreview() }

@Composable
private fun ContentToPreview() {
    PreferenceCheckbox(
        title = "Checkbox",
        icon = Icons.Default.Announcement,
        enabled = true,
        isChecked = true
    )
}
