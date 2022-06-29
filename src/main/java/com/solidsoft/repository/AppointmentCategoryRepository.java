package com.solidsoft.repository;

import com.solidsoft.domain.AppointmentCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AppointmentCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentCategoryRepository extends MongoRepository<AppointmentCategory, String> {}
