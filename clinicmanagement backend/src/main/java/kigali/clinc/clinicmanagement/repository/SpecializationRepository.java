package kigali.clinc.clinicmanagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Specialization;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, UUID> {

    // B3: Specializations that no doctor holds (doctors collection is empty)
    @Query("SELECT s FROM Specialization s WHERE s.doctors IS EMPTY")
    List<Specialization> findUnusedSpecializations();
}
