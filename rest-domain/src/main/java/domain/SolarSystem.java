package domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SolarSystem {
    private List<SolarSystemBody> bodies;
}
