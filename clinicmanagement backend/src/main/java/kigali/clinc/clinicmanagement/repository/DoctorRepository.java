package kigali.clinc.clinicmanagement.repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import kigali.clinc.clinicmanagement.domain.Doctor;

import org.springframework.stereotype.Repository;

@Repository 

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    List <Doctor> findByFirstName(String firstName);
    List <Doctor> findByLastName(String lastName);
    
    Optional <Doctor> findByFirstNameAndLastName(String firstName, String lastName);

    Boolean existsByFirstNameAndLastName(String firstName, String lastName);

    // B1: Doctors by specialization name (case-insensitive, JOIN through many-to-many)
    @Query("SELECT d FROM Doctor d JOIN d.specializations s WHERE LOWER(s.name) = LOWER(:name)")
    List<Doctor> findBySpecializationName(@Param("name") String name);

    // B2: Doctors with no office assigned, sorted by last name
    @Query("SELECT d FROM Doctor d WHERE d.office IS NULL ORDER BY d.lastName ASC")
    List<Doctor> findDoctorsWithoutOffice();

}


