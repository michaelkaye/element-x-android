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

package io.element.android.libraries.featureflag.impl

import com.google.common.truth.Truth.assertThat
import io.element.android.libraries.featureflag.api.FeatureFlags
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DefaultFeatureFlagServiceTest {

    @Test
    fun `given service without provider when feature is checked then it returns the default value`() = runTest {
        val featureFlagService = DefaultFeatureFlagService(emptySet())
        val isFeatureEnabled = featureFlagService.isFeatureEnabled(FeatureFlags.ShowStartChatFlow)
        assertThat(isFeatureEnabled).isEqualTo(FeatureFlags.ShowStartChatFlow.defaultValue)
    }

    @Test
    fun `given service without provider when set enabled feature is called then it returns false`() = runTest {
        val featureFlagService = DefaultFeatureFlagService(emptySet())
        val result = featureFlagService.setFeatureEnabled(FeatureFlags.ShowStartChatFlow, true)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `given service with a runtime provider when set enabled feature is called then it returns true`() = runTest {
        val featureFlagProvider = FakeRuntimeFeatureFlagProvider(0)
        val featureFlagService = DefaultFeatureFlagService(setOf(featureFlagProvider))
        val result = featureFlagService.setFeatureEnabled(FeatureFlags.ShowStartChatFlow, true)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `given service with a runtime provider and feature enabled when feature is checked then it returns the correct value`() = runTest {
        val featureFlagProvider = FakeRuntimeFeatureFlagProvider(0)
        val featureFlagService = DefaultFeatureFlagService(setOf(featureFlagProvider))
        featureFlagService.setFeatureEnabled(FeatureFlags.ShowStartChatFlow, true)
        assertThat(featureFlagService.isFeatureEnabled(FeatureFlags.ShowStartChatFlow)).isEqualTo(true)
        featureFlagService.setFeatureEnabled(FeatureFlags.ShowStartChatFlow, false)
        assertThat(featureFlagService.isFeatureEnabled(FeatureFlags.ShowStartChatFlow)).isEqualTo(false)
    }

    @Test
    fun `given service with 2 runtime providers when feature is checked then it uses the priority correctly`() = runTest {
        val lowPriorityfeatureFlagProvider = FakeRuntimeFeatureFlagProvider(LOW_PRIORITY)
        val highPriorityfeatureFlagProvider = FakeRuntimeFeatureFlagProvider(HIGH_PRIORITY)
        val featureFlagService = DefaultFeatureFlagService(setOf(lowPriorityfeatureFlagProvider, highPriorityfeatureFlagProvider))
        lowPriorityfeatureFlagProvider.setFeatureEnabled(FeatureFlags.ShowStartChatFlow, false)
        highPriorityfeatureFlagProvider.setFeatureEnabled(FeatureFlags.ShowStartChatFlow, true)
        assertThat(featureFlagService.isFeatureEnabled(FeatureFlags.ShowStartChatFlow)).isEqualTo(true)
    }
}
