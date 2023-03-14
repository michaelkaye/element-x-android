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

package io.element.android.features.createroom.impl.addpeople

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.element.android.libraries.designsystem.components.avatar.AvatarData
import io.element.android.libraries.matrix.api.core.UserId
import io.element.android.libraries.matrix.ui.model.MatrixUser
import kotlinx.collections.immutable.persistentListOf

open class AddPeopleStateProvider : PreviewParameterProvider<AddPeopleState> {
    override val values: Sequence<AddPeopleState>
        get() = sequenceOf(
            aAddPeopleState(),
            aAddPeopleState().copy(
                selectedUsers = persistentListOf(
                    aMatrixUser(userName = ""),
                    aMatrixUser(userName = "User"),
                    aMatrixUser(userName = "User with long name"),
                )
            )
        )
}

fun aAddPeopleState() = AddPeopleState(
    selectedUsers = persistentListOf(),
    eventSink = {}
)

fun aMatrixUser(userName: String): MatrixUser {
    return MatrixUser(id = UserId("@id"), username = userName, avatarData = AvatarData("@id", "U"))
}
