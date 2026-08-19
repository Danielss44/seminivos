package com.Danielss44.seminovos.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final UsuarioRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.login}")
    private String adminLogin;

    @Value("${app.admin.senha}")
    private String adminSenha;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (repository.findByLogin(adminLogin).isEmpty()){
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setLogin(adminLogin);
            admin.setSenha(passwordEncoder.encode(adminSenha));
            admin.setPerfil(PerfilUsuario.ADMIN);
            repository.save(admin);
        }

    }
}
