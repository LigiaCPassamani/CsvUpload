package com.csv.csv.service;

import com.csv.csv.model.File;
import com.csv.csv.model.ModelExample;
import com.csv.csv.repository.FileRepository;
import com.csv.csv.service.exception.ObjectNotFoundException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private FileRepository fileRepository;

    private ModelExampleService modelExampleService;

    @Autowired
    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    @Autowired
    public void setModelExampleService(ModelExampleService modelExampleService) {
        this.modelExampleService = modelExampleService;
    }

    public String upload(MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        if (file.isEmpty()) {
            return "No File is Present!";
        }
            if(file.getContentType().equals("text/csv")) {
                try {
                    // Get the file and save it somewhere
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get("file/" + file.getOriginalFilename());
                    Files.write(path, bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                csvInserts(file.getName());
                insertFile(file.getName());
                return "File upload successful";
            }
        return "File incorrect";
    }

    public File findById(Long id) {
        Optional<File> object = fileRepository.findById(id);
        if (object.isPresent()) {
            return object.get();
        } else {
            throw new ObjectNotFoundException("Object Not Found! Id: " + id + "  " + File.class.getSimpleName());
        }
    }

    public void insertFile(String name) throws IOException {
        File file = new File();
        file.setId(null);
        Path path = Paths.get("file/" + name + ".csv");
        try {
            byte[] encoded = Files.readAllBytes(path);
            file.setBytes(encoded);
        } catch (IOException e) {

        }
        file.setName(name);
        file.setContentType(".csv");
        fileRepository.save(file);
        cleanFiles(file.getName());
    }

    public void readByte( Long id) throws IOException {
        File file = findById(id);
        byte[] bytes = file.getBytes();
        Path path = Paths.get("file/newFile.csv");
        Files.write(path, bytes);
    }

    public void cleanFiles(String name) throws IOException {
        Path path = Paths.get("file/" + name + ".csv");
        Files.delete(path);
    }
    public List<ModelExample> csvReader(String name) throws IOException {
        List<ModelExample> listExamplesFinal = new ArrayList<>();
        String path = "file/" + name + ".csv" ;

        Reader reader = Files.newBufferedReader(Paths.get(path));

        CsvToBean<ModelExample> csvToBean = new CsvToBeanBuilder(reader)
                .withType(ModelExample.class)
                .build();


        List<ModelExample> listExamples = csvToBean.parse();

        for (ModelExample example : listExamples) {
            listExamplesFinal.add(example);
        }
        return listExamplesFinal;
    }

    public void csvInserts(String name) throws IOException {

        String path = "file/" + name + ".csv";

        Reader reader = Files.newBufferedReader(Paths.get(path));

        CsvToBean<ModelExample> csvToBean = new CsvToBeanBuilder(reader)
                .withType(ModelExample.class)
                .build();

        List<ModelExample> listExamples = csvToBean.parse();

        for (ModelExample example : listExamples) {
            modelExampleService.insertExample(example);
        }
    }

}


