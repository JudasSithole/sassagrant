package com.sassa.scamshield.domain.usecase

import com.sassa.scamshield.domain.repository.ScamRepository
import com.sassa.scamshield.domain.repository.SecurityStats
import kotlinx.coroutines.flow.Flow

class GetSecurityStatsUseCase(
    private val scamRepo: ScamRepository
) {
    operator fun invoke(): Flow<SecurityStats> {
        return scamRepo.getSecurityStatsFlow()
    }
}
