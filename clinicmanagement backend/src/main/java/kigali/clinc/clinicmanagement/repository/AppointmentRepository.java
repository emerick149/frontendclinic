package kigali.clinc.clinicmanagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    // A2: All appointments with a given status, ordered by appointment date ascending
    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);

    // A3: Appointments between two dates (inclusive), ordered by appointment date
    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(LocalDate start, LocalDate end);

    // A4: Check if doctor already has a non-CANCELLED appointment on a given date
    boolean existsByDoctorIdAndAppointmentDateAndStatusNot(UUID doctorId, LocalDate appointmentDate, AppointmentStatus status);

    // C1: Count of appointments grouped by status — returns List<Object[]> where [0]=status, [1]=count
    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countByStatus();

    // C2: Patients with at least :min appointments, most appointments first
    @Query("SELECT a.patient, COUNT(a) AS cnt FROM Appointment a GROUP BY a.patient HAVING COUNT(a) >= :min ORDER BY cnt DESC")
    List<Object[]> findFrequentPatients(@Param("min") long min);

    // C4: Bulk cancel — set all non-COMPLETED appointments of a doctor on a date to CANCELLED
    @Modifying
    @Query("UPDATE Appointment a SET a.status = kigali.clinc.clinicmanagement.domain.AppointmentStatus.CANCELLED WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date AND a.status <> kigali.clinc.clinicmanagement.domain.AppointmentStatus.COMPLETED")
    int cancelDoctorDay(@Param("doctorId") UUID doctorId, @Param("date") LocalDate date);

    // BONUS +5: Delete all CANCELLED appointments before a given date
    @Modifying
    @Query("DELETE FROM Appointment a WHERE a.status = kigali.clinc.clinicmanagement.domain.AppointmentStatus.CANCELLED AND a.appointmentDate < :date")
    int deleteCancelledBefore(@Param("date") LocalDate date);
}
