package edu.ntnu.idatt2106.smartmat.unit.filtering;

import static org.junit.Assert.assertEquals;

import edu.ntnu.idatt2106.smartmat.filtering.SearchRequest;
import edu.ntnu.idatt2106.smartmat.filtering.SearchSpecification;
import org.junit.Test;

public class SearchSpecificationTest {

  SearchRequest searchRequest;

  @Test
  public void testPageableOfPageSizeLargerThanZero() {
    searchRequest = new SearchRequest();
    searchRequest.setPage(1);
    searchRequest.setSize(1);

    assertEquals(1, SearchSpecification.getPageable(searchRequest).getPageSize());
  }

  @Test
  public void testPageableOfPageLargerThanZero() {
    searchRequest = new SearchRequest();
    searchRequest.setPage(1);
    searchRequest.setSize(1);

    assertEquals(1, SearchSpecification.getPageable(searchRequest).getPageNumber());
  }
}
