package domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Planet extends SolarSystemBody {
    private String planet;
}
