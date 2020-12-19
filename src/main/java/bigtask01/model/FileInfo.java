package bigtask01.model;

import java.util.Arrays;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Files")
public class FileInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank
  @Size(min = 1, max = 255)
  @Column(name = "name", nullable = false)
  private String name;

  @Lob
  @NotNull
  @Column(name = "file", length = 1024)
//  @JsonIgnore
  private byte[] bytes;

  public FileInfo() {
  }

  public FileInfo(
      @NotBlank @Size(min = 1, max = 255) String name,
      @NotNull byte[] bytes) {
    this(null, name, bytes);
  }

  public FileInfo(Integer id,
      @NotBlank @Size(min = 1, max = 255) String name,
      @NotNull byte[] bytes) {
    this.id = id;
    this.name = name;
    this.bytes = bytes;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    this.bytes = bytes;
  }

  @Override
  public String toString() {
    return "FileInfo{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", bytes=" + Arrays.toString(bytes) +
        '}';
  }
}
