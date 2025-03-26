package com.br.board.persistence.dao;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BlockDAO {
    private final Connection connection;

    public void block(final String reason, final Long cardId) throws SQLException {
        var sql = "INSERT INTO BLOCKS (blocked_at, block_reason, card_id) VALUES (CURRENT_TIMESTAMP, ?, ?);";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, reason);
            statement.setLong(2, cardId);
            statement.executeUpdate();
        }
    }

    public void unblock(final String reason, final Long cardId) throws SQLException {
        var sql = """
              UPDATE BLOCKS
                 SET unblocked_at = CURRENT_TIMESTAMP,
                     unblock_reason = ?
               WHERE card_id = ?
                 AND unblock_reason IS NULL;
              """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, reason);
            statement.setLong(2, cardId);
            statement.executeUpdate();
        }
    }
}
