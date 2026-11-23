package kigali.clinc.clinicmanagement.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Office;

@Repository
public interface OfficeRepository extends JpaRepository<Office, UUID> {

    Office findByOfficeNumber(int officeNumber);

    Optional<Office> findByNameAndOfficeNumber(String name, int officeNumber);

    Boolean existsByOfficeNumber(int officeNumber);

    // C3: Office name, office number and appointment count of the busiest office (top 1)
    @Query("SELECT o.name, o.officeNumber, COUNT(a) AS cnt FROM Appointment a JOIN a.doctor d JOIN d.office o GROUP BY o.id, o.name, o.officeNumber ORDER BY cnt DESC")
    List<Object[]> findBusiestOffice();
}
