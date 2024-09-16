package com.teamsantos.easybarber.controllers.resources;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/icons")
public class IconsController {

    private final ResourceLoader resourceLoader;

    public IconsController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/{filename:.+\\.svg}")
    public ResponseEntity<Resource> serveSvg(@PathVariable String filename) {
        String svgFile = "classpath:/static/icons/" + filename;
        Resource resource = resourceLoader.getResource(svgFile);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
