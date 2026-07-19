package dev.aj.security_oauth2_0.conrollers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${resource_server_uri}")
public class RSVerifier {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "You are always welcome to view public pages";
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_fdx:accounts.read', 'SCOPE_fdx:accounts.write')")
    @GetMapping("/secure")
    public String secureEndpoint() {
        return "You, in particular, having accounts read/write access, is welcome even in secure areas.";
    }

    @PreAuthorize("hasAuthority('SCOPE_fdx:accounts.write')")
    @GetMapping("/pii_secure")
    public String piiSecureEndpoint() {
        return "You are welcome because you are authorised to update accounts, hence can access secure areas.";
    }

}
