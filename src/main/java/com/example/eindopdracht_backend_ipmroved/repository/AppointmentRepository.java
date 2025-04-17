package com.example.eindopdracht_backend_ipmroved.repository;

import com.example.eindopdracht_backend_ipmroved.models.Appointment;
import com.example.eindopdracht_backend_ipmroved.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Optional<Appointment> findByIdAndUser(int id, User user);
    List<Appointment> findByUser(User user);
}
