package kigali.clinc.clinicmanagement.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Patient;


@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    List<Patient> findByFirstName(String firstName);

    List<Patient> findByLastName(String lastName);

    Optional<Patient> findByFirstNameAndLastName(String firstName, String lastName);

    Boolean existsByFirstNameAndLastName(String firstName, String lastName);

    // A1: Find patients by last name, case-insensitive, sorted by first name A-Z
    List<Patient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    // B4: Distinct patients who have at least one appointment with a given doctor
    @Query("SELECT DISTINCT p FROM Patient p JOIN p.appointments a WHERE a.doctor.id = :doctorId")
    List<Patient> findPatientsByDoctorId(@Param("doctorId") UUID doctorId);
}