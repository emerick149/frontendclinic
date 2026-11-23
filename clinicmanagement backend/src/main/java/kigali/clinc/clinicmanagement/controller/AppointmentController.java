package kigali.clinc.clinicmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.service.AppointmentService;

@RestController
@RequestMapping(value = "/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // A4: POST /api/appointments/save — save with no-double-booking check
    @PostMapping(value = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveAppointment(@RequestBody Appointment appointment) {
        String result = appointmentService.saveAppointment(appointment);

        if (result.equals("Doctor is already booked on that date")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // A2: GET /api/appointments/by-status?status=SCHEDULED
    @GetMapping(value = "/by-status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByStatus(@RequestParam AppointmentStatus status) {
        List<Appointment> appointments = appointmentService.getAppointmentsByStatus(status);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    // A3: GET /api/appointments/between?start=2026-10-01&end=2026-10-31
    @GetMapping(value = "/between",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<Appointment> appointments = appointmentService.getAppointmentsBetween(startDate, endDate);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    // C1: GET /api/appointments/stats/by-status
    @GetMapping(value = "/stats/by-status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStatsPerStatus() {
        List<Object[]> stats = appointmentService.getAppointmentsPerStatus();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    // C4: PATCH /api/appointments/cancel-day?doctorId=...&date=2026-10-20
    @PatchMapping(value = "/cancel-day",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelDoctorDay(@RequestParam UUID doctorId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        String result = appointmentService.cancelDoctorDay(doctorId, localDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // BONUS +5: GET /api/appointments/page?page=0&size=5&sort=appointmentDate,desc
    @GetMapping(value = "/page",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "appointmentDate") String sort) {
        Page<Appointment> result = appointmentService.getAppointmentsPage(page, size, sort);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // BONUS +5: DELETE /api/appointments/cancelled-before?date=2026-10-15
    @DeleteMapping(value = "/cancelled-before",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCancelledBefore(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        String result = appointmentService.deleteCancelledBefore(localDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
