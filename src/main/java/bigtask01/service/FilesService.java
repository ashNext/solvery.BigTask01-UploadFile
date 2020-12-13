package bigtask01.service;

import bigtask01.model.FileInfo;
import bigtask01.repository.FilesRepository;
import bigtask01.util.exceptions.IllegalRequestDataException;
import bigtask01.util.exceptions.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class FilesService {

  private final FilesRepository repository;

  public FilesService(FilesRepository repository) {
    this.repository = repository;
  }

  public List<FileInfo> getAll() {
    return repository.findAll(Sort.by(Direction.ASC, "name"));
  }

  public FileInfo get(int id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException(id));
  }

  public void delete(int id) {
    repository.delete(get(id));
  }

  @Transactional
  public FileInfo create(FileInfo files) {
    if (files.getId() != null) {
      throw new IllegalRequestDataException(files + " must be new (id=null)");
    }

    return repository.save(files);
  }

  @Transactional
  public void update(FileInfo files, int id) {
    if (files.getId() != id) {
      throw new IllegalRequestDataException(files + " must be with id=" + id);
    }

    get(files.getId());
    repository.save(files);
  }
}