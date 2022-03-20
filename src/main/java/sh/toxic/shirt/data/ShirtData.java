package sh.toxic.shirt.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShirtData {

    private int back = 172; // DEF=172
    private int width = 242; // DEF=242
    private int length = 142; // DEF=142
    private int hip = 138; // DEF=132

}
