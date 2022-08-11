package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTOStudent {
    private int id;
    private String fio;
    private String sex;
    private DTOGroup idGroup;

    public static class DTOStudentBuilder {
        private String sex;

        public DTOStudentBuilder sex(String sex) {
            this.sex = sex;
            validateSex();
            return this;
        }

        private void validateSex() {
            if (!sex.equals("M") && !sex.equals("W")) {
                throw new IllegalStateException("Пол должен быть либо M(Man), либо W(Woman)");
            }
        }
    }
}
