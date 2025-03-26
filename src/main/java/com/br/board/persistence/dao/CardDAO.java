package com.br.board.persistence.dao;

import com.br.board.dto.CardDetailsDTO;
import com.br.board.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static com.br.board.persistence.converter.OffsetDateTimeConverter.toOffsetDateTime;
import static java.util.Objects.nonNull;

@AllArgsConstructor
public class CardDAO {
    private Connection connection;

    public CardEntity insert(final CardEntity entity) throws SQLException {
        var sql = "INSERT INTO CARDS (title, description, board_column_id) VALUES (?, ?, ?) RETURNING id;";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDescription());
            statement.setLong(3, entity.getBoardColumn().getId());

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    entity.setId(resultSet.getLong(1));
                }
            }
        }
        return entity;
    }

    public void moveToColumn(final Long columnId, final Long cardId) throws SQLException{
        var sql = "UPDATE CARDS SET board_column_id = ? WHERE id = ?;";

        try(var statement = connection.prepareStatement(sql)){
            statement.setLong(1, columnId);
            statement.setLong(2, cardId);
            statement.executeUpdate();
        }
    }

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var sql =
                """
                SELECT c.id,
                       c.title,
                       c.description,
                       b.blocked_at,
                       b.block_reason,
                       c.board_column_id,
                       bc.name,
                       (SELECT COUNT(sub_b.id)
                           FROM BLOCKS sub_b
                          WHERE sub_b.card_id = c.id) AS blocks_amount
                  FROM CARDS c
                  LEFT JOIN BLOCKS b
                    ON c.id = b.card_id AND b.unblocked_at IS NULL
                  INNER JOIN BOARDS_COLUMNS bc
                    ON bc.id = c.board_column_id
                  WHERE c.id = ?;
                """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                var dto = new CardDetailsDTO(
                        resultSet.getLong("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        nonNull(resultSet.getString("block_reason")),
                        toOffsetDateTime(resultSet.getTimestamp("blocked_at")),
                        resultSet.getString("block_reason"),
                        resultSet.getInt("blocks_amount"),
                        resultSet.getLong("board_column_id"),
                        resultSet.getString("name")
                );
                return Optional.of(dto);
            }
        }
        return Optional.empty();
    }
}
