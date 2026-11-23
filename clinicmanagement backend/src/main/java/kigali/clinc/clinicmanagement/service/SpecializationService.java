package kigali.clinc.clinicmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Specialization;
import kigali.clinc.clinicmanagement.repository.SpecializationRepository;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepository;

    // B3: Specializations that no doctor holds
    public List<Specialization> getUnusedSpecializations() {
        return specializationRepository.findUnusedSpecializations();
    }
}
