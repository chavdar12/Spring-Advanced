package bg.softuni.testing.message;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @PreAuthorize("isAuthenticated()")
    public String getMessage() {
        return "Hello, " + SecurityContextHolder.getContext().getAuthentication().getName() + "!";
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminMessage() {
        return "Hello, admin!";
    }
}
