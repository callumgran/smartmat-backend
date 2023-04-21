package edu.ntnu.idatt2106.smartmat.filtering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Search request for filtering and sorting.
 * Based on the search request class from the blog post below and IDATT2105 project.
 * @see https://blog.piinalpin.com/2022/04/searching-and-filtering-using-jpa-specification/
 * @author Callum G.
 * @version 1.0 - 21.04.2023
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest implements Serializable {

  private static final long serialVersionUID = 543216789L;

  private List<FilterRequest> filterRequests;

  private List<SortRequest> sortRequests;

  private Integer page;

  private Integer size;

  public List<FilterRequest> getFilterRequests() {
    if (filterRequests == null) {
      filterRequests = new ArrayList<>();
    }
    return filterRequests;
  }

  public List<SortRequest> getSortRequests() {
    if (sortRequests == null) {
      sortRequests = new ArrayList<>();
    }
    return sortRequests;
  }
}
