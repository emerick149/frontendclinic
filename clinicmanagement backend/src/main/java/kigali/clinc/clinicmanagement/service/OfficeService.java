package kigali.clinc.clinicmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Office;
import kigali.clinc.clinicmanagement.repository.OfficeRepository;


@Service 
public class OfficeService {
    

    @Autowired 
    private OfficeRepository officeRepo;


    public String saveOffice(Office office){

        if(officeRepo.findByOfficeNumber(office.getOfficeNumber()) != null){
            return "Office with that number "+office.getOfficeNumber()+ " already exists";
        }

        officeRepo.save(office);

        return "Office with " + office.getOfficeNumber() + " created successfully";
    }


    public List<Office> getAllOffice(){
        return officeRepo.findAll();
    }

    public Office getOfficeById(UUID id){

        Optional<Office>  foundOffice = officeRepo.findById(id);

        if(foundOffice.isPresent()){
            return foundOffice.get();

        }else{
            return null;
        }
    }

    public String updateOffice(UUID id , Office newOfficeData){

        Optional<Office> foundOffice = officeRepo.findById(id);

        if(foundOffice.isEmpty()){
            return "Office with id "+id+" not found";
        }

        Office existingOffice =  foundOffice.get();

        existingOffice.setName(newOfficeData.getName());
        existingOffice.setOfficeNumber(newOfficeData.getOfficeNumber());

        officeRepo.save(existingOffice);

        return "Office with id "+id+" updated successfully";



    }

    public String deleteOffice(UUID id){

      if(!officeRepo.existsById(id)){
        return "Office with id "+id+" not found";
      }

      officeRepo.deleteById(id);
      return "Office with id "+id+" deleted successfully";
    }

    // C3: Returns [name, officeNumber, count] of the busiest office (top row only)
    public Object[] getBusiestOffice() {
        List<Object[]> results = officeRepo.findBusiestOffice();
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }
}

