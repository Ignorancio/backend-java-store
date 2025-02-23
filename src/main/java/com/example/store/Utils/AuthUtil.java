package com.example.store.Utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUtil {

    public static String getAuthenticatedUserEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails){
            return userDetails.getUsername();
        }
        throw new IllegalStateException("Usuario no autenticado");
    }

    public static boolean hasRole(String role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails){
            return userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_"+role));
        }
        return false;
    }
}
