import com.fasterxml.jackson.databind.ObjectMapper;
import io.avrios.Application;
import io.avrios.controller.CurrencyController;
import io.avrios.services.CurrencyService;
import io.avrios.services.entities.Converted;
import io.avrios.services.entities.Cube;
import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * @author igorzg on 2019-05-12.
 * @since 1.0
 */

@RunWith(SpringRunner.class)
@WebMvcTest(CurrencyController.class)
@ContextConfiguration(classes = {Application.class})
public class CurrencyControllerTest {


  private static Logger logger = LoggerFactory.getLogger(CurrencyControllerTest.class);

  @Autowired
  private MockMvc mvc;

  @MockBean
  CurrencyService currencyService;

  @Autowired
  ObjectMapper objectMapper;

  private List<Node> toListOfCubeNode(NodeList nodeList) {
    return toListOfNode(nodeList).stream().filter(i -> "Cube".equals(i.getNodeName())).collect(Collectors.toList());
  }

  private List<Node> toListOfNode(NodeList nodeList) {
    return IntStream.range(0, nodeList.getLength()).mapToObj(nodeList::item).collect(Collectors.toList());
  }

  /**
   * Mock node list, fetch local one
   *
   * @return
   * @throws Exception
   */
  private List<Node> fetchNodeList() throws Exception {
    var stream = this.getClass().getResourceAsStream("local.xml");

    var document = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().parse(stream);
    // Envelope
    var envelopeNode = document.getDocumentElement();

    var childNodes = envelopeNode.getLastChild().getChildNodes();
    // Cube -> Cube
    return toListOfCubeNode(childNodes);
  }

  /**
   * Parse date
   *
   * @param date String
   * @return LocalDate
   */
  public LocalDate parseDate(String date) {
    return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

  @Test
  public void getDateList() throws Exception {
    var nodeList = fetchNodeList();
    given(currencyService.fetchNodeList()).willReturn(nodeList);
    given(currencyService.fetchCubes()).willCallRealMethod();


    var uri = "/currency/dates";
    MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
    int status = mvcResult.getResponse().getStatus();
    var content = mvcResult.getResponse().getContentAsString();

    logger.debug("Status {}", status);
    logger.debug("Content {}", content);
    assertEquals(status, HttpStatus.OK.value());

    var cubeList = currencyService.fetchCubes().stream().map(i -> i.getDate().toString()).distinct().collect(Collectors.toList());
    assertEquals(objectMapper.writeValueAsString(cubeList), content);
  }



  @Test
  public void getEurToChf() throws Exception {
    var nodeList = fetchNodeList();
    given(currencyService.fetchNodeList()).willReturn(nodeList);
    given(currencyService.fetchCubes()).willCallRealMethod();
    var localDate = parseDate(LocalDate.now().toString());
    given(currencyService.parseDate(LocalDate.now().toString())).willReturn(localDate);
    var cubeList = currencyService.fetchCubes();
    given(currencyService.getCubes()).willReturn(cubeList);
    given(currencyService.convert("eur", "chf", 20D, Cube.dateToEpoch(localDate))).willCallRealMethod();


    var uri = "/currency/eur/chf/20";
    MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
    int status = mvcResult.getResponse().getStatus();
    var content = mvcResult.getResponse().getContentAsString();

    logger.debug("Status {}", status);
    logger.debug("Content {}", content);
    assertEquals(status, HttpStatus.OK.value());

    var res1 = new Converted("EUR", "CHF", 22.756, LocalDate.parse("2019-05-10"));
    var res2 = objectMapper.readValue(content, Converted.class);
    assertEquals(res1, res2);
  }


}
