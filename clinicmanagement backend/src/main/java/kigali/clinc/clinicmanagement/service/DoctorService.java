package kigali.clinc.clinicmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import kigali.clinc.clinicmanagement.domain.Doctor;
import kigali.clinc.clinicmanagement.repository.DoctorRepository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Service
public class DoctorService {
    
    @Autowired
    private DoctorRepository doctorRepository;

    public String saveDoctor(Doctor doctor){

        if(doctorRepository.existsByFirstNameAndLastName(doctor.getFirstName(),doctor.getLastName())){
            return "Doctor " + doctor.getFirstName() + " " + doctor.getLastName() + " already exists";

        }
        
        doctorRepository.save(doctor);

        return "Doctor  is saved successfully";
        
    }

    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(UUID id){
            Optional<Doctor> doctorOptional = doctorRepository.findById(id);
            if(doctorOptional.isPresent()){
            return doctorOptional.get();
        }else{
                return null;
        }
    }

    public String deleteDoctorById(UUID id){
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if(doctorOptional.isPresent()){
            doctorRepository.deleteById(id);
            return "Doctor deleted successfully";
        }else{
                return "Doctor not found";
        }
    }


    public String updateDoctor(UUID id, Doctor newDoctorData){
        Optional<Doctor> foundDoctor = doctorRepository.findById(id);

        if(foundDoctor.isEmpty()){
            return "Doctor not found";
        }

        Doctor existingDoctor = foundDoctor.get();
        existingDoctor.setFirstName(newDoctorData.getFirstName());
        existingDoctor.setLastName(newDoctorData.getLastName());
        existingDoctor.setDateOfBirth(newDoctorData.getDateOfBirth());

        doctorRepository.save(existingDoctor);

        return "Doctor updated successfully";
    }

    // B1: Doctors by specialization name (case-insensitive)
    public List<Doctor> getDoctorsBySpecialization(String name) {
        return doctorRepository.findBySpecializationName(name);
    }

    // B2: Doctors without an office, sorted by last name
    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorRepository.findDoctorsWithoutOffice();
    }

}
