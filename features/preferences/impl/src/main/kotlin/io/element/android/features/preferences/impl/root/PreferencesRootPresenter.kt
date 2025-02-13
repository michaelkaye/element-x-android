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

package io.element.android.features.preferences.impl.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import io.element.android.features.logout.api.LogoutPreferencePresenter
import io.element.android.libraries.architecture.Presenter
import io.element.android.libraries.core.meta.BuildType
import io.element.android.libraries.matrix.api.MatrixClient
import io.element.android.libraries.matrix.api.user.MatrixUser
import io.element.android.libraries.matrix.api.user.getCurrentUser
import io.element.android.libraries.matrix.api.verification.SessionVerificationService
import io.element.android.libraries.matrix.api.verification.SessionVerifiedStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class PreferencesRootPresenter @Inject constructor(
    private val logoutPresenter: LogoutPreferencePresenter,
    private val matrixClient: MatrixClient,
    private val sessionVerificationService: SessionVerificationService,
    private val buildType: BuildType,
    private val versionFormatter: VersionFormatter,
) : Presenter<PreferencesRootState> {

    @Composable
    override fun present(): PreferencesRootState {
        val matrixUser: MutableState<MatrixUser?> = rememberSaveable {
            mutableStateOf(null)
        }
        LaunchedEffect(Unit) {
            initialLoad(matrixUser)
        }

        // Session verification status (unknown, not verified, verified)
        val sessionVerifiedStatus by sessionVerificationService.sessionVerifiedStatus.collectAsState()
        val sessionIsNotVerified by remember {
            derivedStateOf { sessionVerifiedStatus == SessionVerifiedStatus.NotVerified }
        }

        val logoutState = logoutPresenter.present()
        val showDeveloperSettings = buildType != BuildType.RELEASE
        return PreferencesRootState(
            logoutState = logoutState,
            myUser = matrixUser.value,
            version = versionFormatter.get(),
            showCompleteVerification = sessionIsNotVerified,
            showDeveloperSettings = showDeveloperSettings
        )
    }

    private fun CoroutineScope.initialLoad(matrixUser: MutableState<MatrixUser?>) = launch {
        matrixUser.value = matrixClient.getCurrentUser()
    }
}
