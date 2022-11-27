package com.csv.csv.service;

import com.csv.csv.model.ModelExample;
import com.csv.csv.repository.ModelExampleRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ModelExampleService {

    private final ModelExampleRepository modelExampleRepository;

    public ModelExampleService(ModelExampleRepository modelExampleRepository) {
        this.modelExampleRepository = modelExampleRepository;
    }

    public ModelExample insertExample(ModelExample object) {
        return modelExampleRepository.save(object);
    }
}
