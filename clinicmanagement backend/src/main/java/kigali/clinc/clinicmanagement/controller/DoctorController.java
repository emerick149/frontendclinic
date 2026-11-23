package kigali.clinc.clinicmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import kigali.clinc.clinicmanagement.service.DoctorService;
import kigali.clinc.clinicmanagement.domain.Doctor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping (value = "/api/doctors")
public class DoctorController {
    
    @Autowired
    private DoctorService doctorService;

    @PostMapping  (value = "/save",
consumes = MediaType.APPLICATION_JSON_VALUE,
produces  = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<?>  saveDoctor(@RequestBody  Doctor doctor){
        String returnMessage = doctorService.saveDoctor(doctor);

        if(returnMessage.equalsIgnoreCase("Doctor  is saved successfully")){
            return new ResponseEntity<>(returnMessage, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(returnMessage, HttpStatus.CONFLICT);
        }
    }

    @GetMapping (value = "/all",
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAllDoctors(){

        List<Doctor> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }



    @GetMapping (value = "{id}",
        produces  = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<?> getDoctorById(@PathVariable UUID id){
        
        Doctor doctor = doctorService.getDoctorById(id);

        if(doctor != null){
            return new ResponseEntity<>(doctor, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping (value = "{id}/updateDoctor",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity <?> updateDoctor(@PathVariable UUID id, @RequestBody Doctor newDoctorData){
    

        String returnMessage = doctorService.updateDoctor(id, newDoctorData);

        if(returnMessage.equalsIgnoreCase("Doctor updated successfully")){
            return new ResponseEntity<>(returnMessage, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(returnMessage, HttpStatus.NOT_FOUND);
        }
    }


@DeleteMapping(value = "{id}/deleteDoctor")
    public ResponseEntity<?> deleteDoctorById(@PathVariable UUID id){
        String returnMessage = doctorService.deleteDoctorById(id);

        if(returnMessage.equalsIgnoreCase("Doctor deleted successfully")){
            return new ResponseEntity<>(returnMessage, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(returnMessage, HttpStatus.NOT_FOUND);
        }
    }

    // B1: GET /api/doctors/by-specialization?name=cardiology
    @GetMapping(value = "/by-specialization",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorsBySpecialization(@RequestParam String name) {
        List<Doctor> doctors = doctorService.getDoctorsBySpecialization(name);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    // B2: GET /api/doctors/without-office
    @GetMapping(value = "/without-office",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorsWithoutOffice() {
        List<Doctor> doctors = doctorService.getDoctorsWithoutOffice();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

}
