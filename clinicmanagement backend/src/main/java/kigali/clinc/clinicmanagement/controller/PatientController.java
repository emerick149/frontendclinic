package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Patient;
import kigali.clinc.clinicmanagement.repository.DoctorRepository;
import kigali.clinc.clinicmanagement.service.AppointmentService;
import kigali.clinc.clinicmanagement.service.PatientService;


@RestController
@RequestMapping(value = "/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentService appointmentService;


    @PostMapping(value = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savePatient(@RequestBody Patient patient){

        String returnMessage = patientService.savePatient(patient);

        if(returnMessage.equalsIgnoreCase("Patient saved successfully")){
            return new ResponseEntity<>(returnMessage, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(returnMessage, HttpStatus.CONFLICT);
        }
    }


    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPatients(){

        List<Patient> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }


    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPatientById(@PathVariable UUID id){

        Patient patient = patientService.getPatientById(id);

        if(patient != null){
            return new ResponseEntity<>(patient, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
    }



    @PutMapping(value = "{id}/updateDoctor",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePatient(@PathVariable UUID id, @RequestBody Patient newPatientData){

        String returnMessage = patientService.updatePatient(id, newPatientData);


        if(returnMessage.equalsIgnoreCase("Patient updated successfully")){
            return new ResponseEntity<>(returnMessage, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(returnMessage, HttpStatus.NOT_FOUND);
        }
    }



    @DeleteMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePatient(@PathVariable UUID id){

        String returnMessage = patientService.deletePatient(id);

        
        if(returnMessage.equalsIgnoreCase("Patient deleted successfully")){
            return new ResponseEntity<>(returnMessage, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(returnMessage, HttpStatus.NOT_FOUND);
        }
    }

    // B4: GET /api/patients/of-doctor/{doctorId}
    @GetMapping(value = "/of-doctor/{doctorId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPatientsByDoctor(@PathVariable UUID doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            return new ResponseEntity<>("The doctor with that id does not exist", HttpStatus.NOT_FOUND);
        }
        List<Patient> patients = patientService.getPatientsByDoctorId(doctorId);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    // A1: GET /api/patients/by-last-name?lastName=uwase
    @GetMapping(value = "/by-last-name",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPatientsByLastName(@RequestParam String lastName) {
        List<Patient> patients = patientService.getPatientsByLastName(lastName);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    // C2: GET /api/patients/frequent?min=3
    @GetMapping(value = "/frequent",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFrequentPatients(@RequestParam long min) {
        List<Object[]> results = appointmentService.getFrequentPatients(min);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}