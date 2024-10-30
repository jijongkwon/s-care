package com.scare.api.solution.walk.repository;

import com.scare.api.solution.walk.domain.WalkingDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WalkingDetailRepository extends MongoRepository<WalkingDetail, Long> {
}
