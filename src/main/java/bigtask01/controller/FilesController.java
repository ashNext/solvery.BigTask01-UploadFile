package bigtask01.controller;

import bigtask01.model.FileInfo;
import bigtask01.service.FilesService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = FilesController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class FilesController {

  static final String REST_URL = "rest/files";

  private final FilesService service;

  public FilesController(FilesService service) {
    this.service = service;
  }

  @GetMapping
  public List<FileInfo> getAll() {
    return service.getAll();
  }

  @GetMapping("/{id}")
  public FileInfo get(@PathVariable int id) {
    return service.get(id);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable int id) {
    service.delete(id);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<FileInfo> create(@RequestParam("attach") MultipartFile file) {
    try {
      return new ResponseEntity<>(
          service.create(new FileInfo(file.getOriginalFilename(), file.getBytes())),
          HttpStatus.CREATED);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/{id}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<Resource> download(@PathVariable("id") int id) {
    FileInfo fileInfo = service.get(id);

    Path path = Paths.get(System.getenv("TEMP"), Integer.toString(fileInfo.getId()));
    Resource resource = null;
    try {
      Files.write(path, fileInfo.getBytes());
      resource = new UrlResource(path.toUri());
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=" + fileInfo.getName())
        .body(resource);
  }
}
