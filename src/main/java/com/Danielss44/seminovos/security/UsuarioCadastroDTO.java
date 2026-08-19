package com.Danielss44.seminovos.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioCadastroDTO(
         @NotBlank String nome,
         @NotBlank String login,
         @NotBlank String senha,
         @NotNull PerfilUsuario perfil) {}
