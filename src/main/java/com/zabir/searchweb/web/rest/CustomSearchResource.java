package com.zabir.searchweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.zabir.searchweb.config.ImageProcessing.FeatureDescriptionImage;
import com.zabir.searchweb.config.ImageProcessing.FeatureExtractionImage;
import com.zabir.searchweb.config.ImageProcessing.FeatureExtractionImage2;
import com.zabir.searchweb.config.ImageProcessing.MyKeyPoint;
import com.zabir.searchweb.domain.Herb;
import com.zabir.searchweb.domain.ImageSearchDTO;
import com.zabir.searchweb.repository.HerbRepository;
import io.github.jhipster.web.util.ResponseUtil;
import org.opencv.features2d.KeyPoint;
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

    private final HerbRepository herbRepository;

    public CustomSearchResource(HerbRepository herbRepository) {
        this.herbRepository = herbRepository;
    }

    @PostMapping("/by-image")
    @Timed
    public ResponseEntity<ImageSearchDTO> searchByImage (
        @RequestParam(value = "imageFile") MultipartFile imageFile,
        @RequestParam(value = "filename") String filename,
        @RequestParam(value = "metadata", required = false) String metada) {
        log.debug("REST request search by image");

        Map<String, Integer> goodMatchAchieve = new HashMap<>();

        List<File> allFiles = null;
        Herb herb = null;
        try {
            File searchImage = multipartToFile(imageFile);

            allFiles = getAllFilesInDir("/images");

            FeatureExtractionImage firstExtraction = new FeatureExtractionImage(searchImage.getAbsolutePath());
            double acceptableAmount = firstExtraction.getKeyPoints().toArray().length * 0.5;
            for (File file : allFiles) {
                if (!goodMatchAchieve.containsKey(file.getParentFile().getName().toLowerCase())) {
                    goodMatchAchieve.put(file.getParentFile().getName().toLowerCase(), 0);
                }
                FeatureExtractionImage secondExtraction = new FeatureExtractionImage(file.getAbsolutePath());

                FeatureDescriptionImage compute = new FeatureDescriptionImage(firstExtraction, secondExtraction);

                if (compute.getGoodMatches().toArray().length > acceptableAmount) {
                    goodMatchAchieve.put(file.getParentFile().getName().toLowerCase(), goodMatchAchieve.get(file.getParentFile().getName().toLowerCase()) + 1);
                }
            }

            String herbSelected = "";
            int max = 0;
            for (Map.Entry<String, Integer> entry : goodMatchAchieve.entrySet()) {
                if (max == 0 || max < entry.getValue()) {
                    max = entry.getValue();
                    herbSelected = entry.getKey();
                }
            }

            herb = herbRepository.findByDirectory("/images/" + herbSelected);

        } catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        List<String> directories = new ArrayList<>();
        for (File file : allFiles) {
            if (file.getParentFile().getName().equalsIgnoreCase(herb.getName())) {
                directories.add(file.getAbsolutePath());
            }
        }

        ImageSearchDTO imageSearchDTO = new ImageSearchDTO();
        imageSearchDTO.setDirectories(directories);
        imageSearchDTO.setHerb(herb);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(imageSearchDTO));
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

    @GetMapping("/by-text/{search}")
    @Timed
    public ResponseEntity<ImageSearchDTO> searchByText(@PathVariable("search") String search) {
        List<String> directories = new ArrayList<>();
        Herb herb = null;
        try {
            List<Path> paths = getAllFoldersInDir("/images");
            List<String> names = new ArrayList<>();
            for (Path path : paths) {
                String name = path.toFile().getName();
                if (name.toLowerCase().contains(search.toLowerCase())) {
                    names.add(name);
                }
            }
            if (names.isEmpty() || names.size() > 1) {
                return ResponseEntity.noContent().build();
            }
            List<File> truePaths = new ArrayList<>();
            for (String name : names) {
                truePaths.addAll(getAllFilesInDir("/images/" + name));
                herb = herbRepository.findByDirectory("/images/" + name);
            }

            for (File file: truePaths) {
                directories.add(file.getAbsolutePath());
            }

        } catch(IOException e) {
            e.printStackTrace();;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        ImageSearchDTO imageSearchDTO = new ImageSearchDTO();
        imageSearchDTO.setDirectories(directories);
        imageSearchDTO.setHerb(herb);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(imageSearchDTO));
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

    private List<Path> getAllFoldersInDir(String dir) throws IOException {
        List<File> files = new ArrayList<>();
        try (Stream<Path> allPaths = Files.walk(Paths.get(System.getProperty("user.dir") + dir))) {
            List<Path> filePaths = allPaths.filter(path -> !Files.isRegularFile(path)).collect(Collectors.toList());
            return filePaths;
        }
    }
}
