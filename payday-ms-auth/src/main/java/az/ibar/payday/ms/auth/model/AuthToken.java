package az.ibar.payday.ms.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthToken {
    private String token;
    private String username;
}
