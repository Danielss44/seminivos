package com.Danielss44.seminovos.exception;

import com.Danielss44.seminovos.DTO.erro.ErroResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handleValidacao(MethodArgumentNotValidException exception){
        String mensagem = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(400)
                .body(new ErroResponseDTO(400, mensagem, LocalDateTime.now()));
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<ErroResponseDTO> handleNaoEncontrada(EntidadeNaoEncontradaException exception){
        return ResponseEntity.status(404).body(new ErroResponseDTO(404, exception.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResponseDTO> handleRegraInvalida(RegraDeNegocioException exception){
        return ResponseEntity.status(422).body(new ErroResponseDTO(422, exception.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<ErroResponseDTO> handleValidacao(ValidacaoException exception){
        return ResponseEntity.status(400).body(new ErroResponseDTO(400, exception.getMessage(), LocalDateTime.now()));
    }
}
