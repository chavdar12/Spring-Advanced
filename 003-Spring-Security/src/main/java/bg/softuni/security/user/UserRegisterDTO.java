package bg.softuni.security.user;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterDTO(@NotBlank @Size(min = 3, max = 20) String username,
                              @NotBlank @Size(min = 5) String password,
                              String confirmPassword) {

    @AssertTrue(message = "Passwords do not match.")
    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }
}
