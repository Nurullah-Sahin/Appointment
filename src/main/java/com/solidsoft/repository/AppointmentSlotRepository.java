package com.solidsoft.repository;

import com.solidsoft.domain.AppointmentSlot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AppointmentSlot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentSlotRepository extends MongoRepository<AppointmentSlot, String> {}
