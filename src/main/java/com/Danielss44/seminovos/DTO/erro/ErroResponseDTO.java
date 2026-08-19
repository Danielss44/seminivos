package com.Danielss44.seminovos.DTO.erro;

import java.time.LocalDateTime;

public record ErroResponseDTO(int status, String mensagem, LocalDateTime timestamp) {
}
