package com.csv.csv.controller;

import com.csv.csv.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import java.io.IOException;

@RestController
@RequestMapping(value ="/api/file")
public class FileController {

    private final FileService fileService;


    @Autowired
    public FileController(FileService fileService, ServletContext servletContext) {
        this.fileService = fileService;
    }


    @GetMapping("/readFile/{id}")
    public void readFile(@PathVariable Long id) throws IOException {
        fileService.readByte(id);
    }


    @PostMapping("/upload")
    public String ingestDataFile(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) throws IOException {
        return fileService.upload(file, redirectAttributes);
    }


}
