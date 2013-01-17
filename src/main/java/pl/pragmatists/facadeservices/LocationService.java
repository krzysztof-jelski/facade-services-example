package pl.pragmatists.facadeservices;

import java.util.List;

public interface LocationService {
    List<Warehouse> sortByProximity(Location location, List<Warehouse> warehouses);
}
