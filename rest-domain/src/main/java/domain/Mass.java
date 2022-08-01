package domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mass extends SolarSystemBody {
    private float massValue;
    private int massExponent;
}
