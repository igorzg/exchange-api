package io.avrios.services;

import io.avrios.services.entities.Converted;
import io.avrios.services.entities.Cube;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author igorzg on 2019-05-12.
 * @since 1.0
 */
@Service
public class CurrencyService {

  private static Logger logger = LoggerFactory.getLogger(CurrencyService.class);

  @Value("${app.exchange.d90}")
  private String exchangeApi;


  @Value("${app.exchange.dateformat}")
  private String dateFormat;

  private final HttpClient httpClient;

  private final DocumentBuilder documentBuilder;

  private DateTimeFormatter format;

  private List<Cube> cubeList;

  @Autowired
  public CurrencyService(HttpClient httpClient, DocumentBuilder documentBuilder) {
    this.httpClient = httpClient;
    this.documentBuilder = documentBuilder;
  }


  @PostConstruct
  public void afterInit() {
    logger.debug("Fetch xml document: {}", this.exchangeApi);
    format = DateTimeFormatter.ofPattern(dateFormat);
    updateCubes();
  }

  /**
   * Returns Cube map
   *
   * @return get cubemap
   */
  public List<Cube> getCubes() {
    return cubeList;
  }

  /**
   * Set cubes map
   */
  public void updateCubes() {
    try {
      var cubeMap = fetchCubes();
      if (!cubeMap.isEmpty()) {
        this.cubeList = cubeMap;
      }
    } catch (Exception e) {
      logger.debug("Fetch CubeMap Error", e);
    }
  }

  /**
   * Fetches cubes map
   *
   * @return List<Cube>
   * @throws Exception
   */
  public List<Cube> fetchCubes() throws Exception {
    var nodeList = fetchNodeList();
    return toCubeList(nodeList);
  }

  /**
   * Conversion
   * @param from String currency eg. eur
   * @param into String currency eg. usd
   * @param value Double, eg. 20.0
   * @param timestamp Long
   * @return Double
   */
  public Converted convert(String from, String into, double value, Long timestamp) {
    final var closestTimestamp = getClosestTimestamp(timestamp);
    var closestCubesList = cubeList.stream().filter(i -> i.getTimestamp().equals(closestTimestamp)).collect(Collectors.toList());
    var intoCurrency = closestCubesList.stream().filter(i -> i.getCurrency().equals(into.toUpperCase())).findFirst();
    var fromCurrency = closestCubesList.stream().filter(i -> i.getCurrency().equals(from.toUpperCase())).findFirst();
    var result = value;
    if (fromCurrency.isEmpty() && intoCurrency.isPresent()) {
      result = intoCurrency.get().getRate() * value;
    } else if (intoCurrency.isPresent() && fromCurrency.isPresent()) {
      result = (intoCurrency.get().getRate() * (1 / fromCurrency.get().getRate())) * value;
    } else if (fromCurrency.isPresent() && intoCurrency.isEmpty()) {
      result = value / fromCurrency.get().getRate();
    }

    return new Converted(
      from.toUpperCase(),
      into.toUpperCase(),
      result,
      LocalDate.ofInstant(Instant.ofEpochMilli(closestTimestamp), ZoneId.systemDefault())
    );
  }

  /**
   * Parse date
   * @param date String
   * @return LocalDate
   */
  public LocalDate parseDate(String date) {
    return LocalDate.parse(date, format);
  }
  /**
   * Returns closest timestamp
   *
   * @param timestamp Long
   * @return Long
   */
  public Long getClosestTimestamp(Long timestamp) {
    return cubeList.stream().filter(i -> i.getTimestamp() <= timestamp).map(Cube::getTimestamp).unordered().collect(Collectors.toList()).get(0);
  }

  /**
   * Get attribute value
   *
   * @param node Node
   * @param name String
   * @return String
   */
  private String getAttributeValue(Node node, String name) {
    return node.getAttributes().getNamedItem(name).getNodeValue();
  }

  /**
   * Convert List<Node> to Cube map by date
   *
   * @param nodeList List<Node>
   * @return HashMap<String, List < Cube>>
   */
  private List<Cube> toCubeList(List<Node> nodeList) throws Exception {
    var list = new ArrayList<Cube>();
    for (Node node : nodeList) {
      var cubeList = toListOfCubeNode(node.getChildNodes());
      var date = parseDate(getAttributeValue(node, "time"));
      list.addAll(
        cubeList.stream().map(
          i -> new Cube(
            getAttributeValue(i, "currency"),
            Double.parseDouble(getAttributeValue(i, "rate")),
            date
          )
        ).collect(Collectors.toList())
      );
    }
    return list;
  }

  /**
   * Convert NodeList to list of Cube Nodes
   *
   * @param nodeList NodeList
   * @return List<Node>
   */
  private List<Node> toListOfCubeNode(NodeList nodeList) {
    return toListOfNode(nodeList).stream().filter(i -> "Cube".equals(i.getNodeName())).collect(Collectors.toList());
  }

  /**
   * Convert NodeList to list of Nodes
   *
   * @param nodeList NodeList
   * @return List<Node>
   */
  private List<Node> toListOfNode(NodeList nodeList) {
    return IntStream.range(0, nodeList.getLength()).mapToObj(nodeList::item).collect(Collectors.toList());
  }

  /**
   * Fetch Node list
   *
   * @return list of cube nodes
   * @throws Exception if xml is invalid
   */
  private List<Node> fetchNodeList() throws Exception {
    var httpResponse = httpClient.execute(new HttpGet(this.exchangeApi));
    var document = documentBuilder.parse(httpResponse.getEntity().getContent());
    // Envelope
    var envelopeNode = document.getDocumentElement();
    // Cube -> Cube
    return toListOfCubeNode(envelopeNode.getLastChild().getChildNodes());
  }
}
