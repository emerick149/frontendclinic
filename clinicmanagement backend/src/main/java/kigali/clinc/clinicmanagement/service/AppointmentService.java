package kigali.clinc.clinicmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // A2: Appointments by status, ordered by date ascending
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatusOrderByAppointmentDateAsc(status);
    }

    // A3: Appointments between two dates inclusive, ordered by date
    public List<Appointment> getAppointmentsBetween(LocalDate start, LocalDate end) {
        return appointmentRepository.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    // A4: Save appointment with no-double-booking check
    public String saveAppointment(Appointment appointment) {
        LocalDate date = appointment.getAppointmentDate();
        UUID doctorId = appointment.getDoctor().getId();

        boolean alreadyBooked = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndStatusNot(doctorId, date, AppointmentStatus.CANCELLED);

        if (alreadyBooked) {
            return "Doctor is already booked on that date";
        }

        appointmentRepository.save(appointment);
        return "Appointment saved successfully";
    }

    // C1: Count of appointments grouped by status — List<Object[]> [status, count]
    public List<Object[]> getAppointmentsPerStatus() {
        return appointmentRepository.countByStatus();
    }

    // C2: Patients with at least min appointments, most appointments first
    public List<Object[]> getFrequentPatients(long min) {
        return appointmentRepository.findFrequentPatients(min);
    }

    // C4: Bulk cancel all non-COMPLETED appointments of a doctor on a date
    @Transactional
    public String cancelDoctorDay(UUID doctorId, LocalDate date) {
        int count = appointmentRepository.cancelDoctorDay(doctorId, date);
        return count + " appointments cancelled";
    }

    // BONUS +5: Paginated appointments sorted by appointmentDate desc
    public Page<Appointment> getAppointmentsPage(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));
        return appointmentRepository.findAll(pageable);
    }

    // BONUS +5: Delete all CANCELLED appointments before a given date
    @Transactional
    public String deleteCancelledBefore(LocalDate date) {
        int count = appointmentRepository.deleteCancelledBefore(date);
        return count + " appointments deleted";
    }
}
