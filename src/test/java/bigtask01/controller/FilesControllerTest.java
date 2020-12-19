package bigtask01.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bigtask01.model.FileInfo;
import bigtask01.service.FilesService;
import bigtask01.util.exceptions.NotFoundException;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringJUnitWebConfig(locations = {
    "classpath:spring/spring-app.xml",
    "classpath:spring/spring-mvc.xml"
})
@Sql(value = {"/db/populateDB.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilesControllerTest {

  private static final String REST_URL = FilesController.REST_URL + '/';

  public MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private FilesService service;

  @PostConstruct
  private void postConstruct() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .build();
  }

  @Test
  void getAll() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content()
            .json("[{\"id\":0,\"name\":\"fileName1\"},{\"id\":1,\"name\":\"fileName2\"}]"));
  }

  @Test
  void get() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "0"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().json("{\"id\":0,\"name\":\"fileName1\"}"));
  }

  @Test
  void getNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "100"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void delete() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "1"))
        .andDo(print())
        .andExpect(status().isNoContent());
    assertThrows(NotFoundException.class, () -> service.get(1));
  }

  @Test
  void deleteNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "100"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void upload() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile("attach", "test.txt", "text/plain",
            "text".getBytes());

    mockMvc.perform(multipart(REST_URL).file(file))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().json("{\"id\":2,\"name\":\"test.txt\",\"bytes\":\"dGV4dA==\"}"));

    FileInfo created = service.get(2);
    assertEquals("test.txt", created.getName());
    assertEquals(Arrays.toString("text".getBytes()), Arrays.toString(created.getBytes()));
  }

  @Test
  void uploadEmpty() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile("attach", "", "text/plain",
            "".getBytes());

    mockMvc.perform(multipart(REST_URL).file(file))
        .andDo(print())
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  void download() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile("attach", "test.txt", "text/plain",
            "text".getBytes());

    mockMvc.perform(multipart(REST_URL).file(file));

    mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "2/download"))
        .andDo(print())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM_VALUE))
        .andExpect(content().bytes("text".getBytes()));
  }

  @Test
  void downloadNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "2/download"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }
}