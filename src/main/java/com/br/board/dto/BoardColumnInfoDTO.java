package com.br.board.dto;

import com.br.board.persistence.entity.BoardColumnKindEnum;

public record BoardColumnInfoDTO(
        Long id,
        int order,
        BoardColumnKindEnum kind) {
}
