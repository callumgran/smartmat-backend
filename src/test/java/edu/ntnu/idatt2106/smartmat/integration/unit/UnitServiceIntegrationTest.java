package edu.ntnu.idatt2106.smartmat.integration.unit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.model.unit.UnitTypeEnum;
import edu.ntnu.idatt2106.smartmat.repository.unit.UnitRepository;
import edu.ntnu.idatt2106.smartmat.service.unit.UnitService;
import edu.ntnu.idatt2106.smartmat.service.unit.UnitServiceImpl;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UnitServiceIntegrationTest {

  @TestConfiguration
  static class UnitServiceIntegrationTestConfiguration {

    @Bean
    public UnitService unitService() {
      return new UnitServiceImpl();
    }
  }

  @Autowired
  private UnitService unitService;

  @MockBean
  private UnitRepository unitRepository;

  private Unit kg;

  private Unit g;

  private Unit l;

  @Before
  public void setUp() {
    kg = new Unit("kilogram", "kg", new HashSet<>(), 1, UnitTypeEnum.SOLID);
    g = new Unit("gram", "g", new HashSet<>(), 0.001, UnitTypeEnum.SOLID);
    l = new Unit("liter", "l", new HashSet<>(), 1, UnitTypeEnum.LIQUID);
  }

  @Test
  public void testGetAllUnits() {
    when(unitRepository.findAll()).thenReturn(List.of(kg, g, l));
    List<Unit> units = unitService.getAllUnits().stream().toList();
    assertEquals(3, units.size());
    assertEquals(kg, units.get(0));
    assertEquals(g, units.get(1));
    assertEquals(l, units.get(2));
  }
}
