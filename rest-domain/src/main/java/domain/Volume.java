package domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Volume {
    private float volValue;
    private int volExponent;
}
