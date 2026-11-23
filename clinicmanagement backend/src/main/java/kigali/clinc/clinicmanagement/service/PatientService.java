package kigali.clinc.clinicmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Patient;
import kigali.clinc.clinicmanagement.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public String savePatient(Patient patient) {
        if (patientRepository.existsByFirstNameAndLastName(patient.getFirstName(), patient.getLastName())) {
            return "Patient already exists";
        } else {
            patientRepository.save(patient);
            return "Patient saved successfully";
        }
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(UUID id) {
        Optional<Patient> foundPatient = patientRepository.findById(id);
        if (foundPatient.isPresent()) {
            return foundPatient.get();
        } else {
            return null;
        }
    }

    public String updatePatient(UUID id, Patient newPatientData) {
        Optional<Patient> foundPatient = patientRepository.findById(id);

        if (foundPatient.isEmpty()) {
            return "Patient not found";
        }

        Patient existingPatient = foundPatient.get();
        existingPatient.setFirstName(newPatientData.getFirstName());
        existingPatient.setLastName(newPatientData.getLastName());
        existingPatient.setDateOfBirth(newPatientData.getDateOfBirth());
        
        // --- NEW: Added ssNumber update for Quiz 2 ---
        existingPatient.setSsNumber(newPatientData.getSsNumber());

        patientRepository.save(existingPatient);
        return "Patient updated successfully";
    }

    public String deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            return "Patient not found";
        }

        patientRepository.deleteById(id);
        return "Patient deleted successfully";
    }

    // B4: Distinct patients who have at least one appointment with the given doctor
    public List<Patient> getPatientsByDoctorId(UUID doctorId) {
        return patientRepository.findPatientsByDoctorId(doctorId);
    }

    // A1: Find patients by last name, case-insensitive, sorted by first name A-Z
    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }
}