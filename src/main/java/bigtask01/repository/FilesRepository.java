package bigtask01.repository;

import bigtask01.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface FilesRepository extends JpaRepository<FileInfo, Integer> {

}
