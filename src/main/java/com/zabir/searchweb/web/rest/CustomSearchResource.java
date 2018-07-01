package com.zabir.searchweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.zabir.searchweb.config.ImageProcessing.FeatureDescriptionImage;
import com.zabir.searchweb.config.ImageProcessing.FeatureExtractionImage;
import com.zabir.searchweb.config.ImageProcessing.FeatureExtractionImage2;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/search")
public class CustomSearchResource {
    private final Logger log = LoggerFactory.getLogger(CustomSearchResource.class);

    public CustomSearchResource() {

    }

    @PostMapping("/by-image")
    @Timed
    public ResponseEntity<List<String>> searchByImage (
        @RequestParam(value = "imageFile") MultipartFile imageFile,
        @RequestParam(value = "filename") String filename,
        @RequestParam(value = "metadata", required = false) String metada) {
        log.debug("REST request search by image");

        Map<String, Integer> goodMatchAchieve = new HashMap<>();

        List<File> allFiles = null;
        try {
            File searchImage = multipartToFile(imageFile);

            allFiles = getAllFilesInDir("/images");

            FeatureExtractionImage firstExtraction = new FeatureExtractionImage(searchImage.getAbsolutePath());
            for (File file : allFiles) {
                FeatureExtractionImage secondExtraction = new FeatureExtractionImage(file.getAbsolutePath());

                FeatureDescriptionImage compute = new FeatureDescriptionImage(firstExtraction, secondExtraction);
                    if (compute.getGoodMatches().toArray().length > 150) {
                        goodMatchAchieve.put(file.getAbsolutePath(), compute.getGoodMatches().toArray().length);
                }
            }
        } catch (IOException e){
            e.printStackTrace();;
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Map<String, Integer> goodMatchAchieveSorted = goodMatchAchieve.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<String> directories = goodMatchAchieveSorted.keySet().stream().collect(Collectors.toList());


        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(directories));
    }

    @PostMapping("/get-image")
    @Timed
    public ResponseEntity<Resource> downloadFile(@RequestParam("directory") String directory) {
        log.debug("REST request get image : {}", directory);
        Path path = Paths.get(directory);
        if (!Files.exists(path)) {
            return ResponseEntity.noContent().build();
        }

        Resource resource = new FileSystemResource(path.toFile());
        return ResponseEntity.ok().body(resource);
    }

    private File multipartToFile(MultipartFile imageFile) throws IOException {
        File file = new File(imageFile.getOriginalFilename());
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(imageFile.getBytes());
        fos.close();
        return file;
    }

    private List<File> getAllFilesInDir(String dir) throws IOException {
        List<File> files = new ArrayList<>();
        try (Stream<Path> allPaths = Files.walk(Paths.get(System.getProperty("user.dir") + dir))) {
            List<Path> filePaths = allPaths.filter(Files::isRegularFile).collect(Collectors.toList());
            for (Path path : filePaths) {
                files.add(path.toFile());
            }
        }

        return files;
    }
}
