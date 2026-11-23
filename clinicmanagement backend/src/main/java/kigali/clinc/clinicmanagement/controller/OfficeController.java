package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Office;
import kigali.clinc.clinicmanagement.service.OfficeService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping (value = "/api/offices")
public class OfficeController {
    

    @Autowired
    private OfficeService officeServes;


    @PostMapping (value = "/save",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces  = MediaType.APPLICATION_JSON_VALUE

    )

    public ResponseEntity<?> saveOffice(@RequestBody Office office){

        String returnMessage = officeServes.saveOffice(office);

        if(returnMessage.equalsIgnoreCase("Office saved Successfully.")){
            return new ResponseEntity<>(returnMessage, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(returnMessage, HttpStatus.CONFLICT);
        }

    }

    @GetMapping (value = "/all",
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAllOffice()
    {
        List<Office> offices = officeServes.getAllOffice();
        return new ResponseEntity<>(offices, HttpStatus.OK);
    }


    @GetMapping (value = "{id}",
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    
    public ResponseEntity<?> getOfficeById(@PathVariable  UUID id){

        Office office =  officeServes.getOfficeById(id);

        if(office != null){
            return new ResponseEntity<>(office, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Office with id "+id+" not found", HttpStatus.NOT_FOUND);
        }

    }


    @PutMapping (value = "{id}/updateOffice",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<?> updateOffice(@PathVariable UUID id, @RequestBody Office newOfficeData){

        String returMessage = officeServes.updateOffice(id , newOfficeData);

        if(returMessage.equalsIgnoreCase("Office with id "+id+" updated successfully")){
            return new ResponseEntity<>(returMessage, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(returMessage, HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping (value = "/delete/{id}",
        produces  = MediaType.APPLICATION_JSON_VALUE
        )
    
    public ResponseEntity<?> deleteOffice(@PathVariable UUID id)
    {

        String returnMessage = officeServes.deleteOffice(id);

        if(returnMessage.equalsIgnoreCase("Office with id "+id+" deleted successfully")){
            return new ResponseEntity<>(returnMessage, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(returnMessage, HttpStatus.NOT_FOUND);
        }
    }

    // C3: GET /api/offices/busiest
    @GetMapping(value = "/busiest",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBusiestOffice() {
        Object[] result = officeServes.getBusiestOffice();
        if (result == null) {
            return new ResponseEntity<>("No appointments yet", HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}