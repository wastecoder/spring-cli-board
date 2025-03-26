package com.br.board.service;

import com.br.board.persistence.dao.CardDAO;
import com.br.board.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class CardService {
    private final Connection connection;

    public CardEntity create(final CardEntity entity) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            dao.insert(entity);
            connection.commit();
            return entity;
        } catch (SQLException ex){
            connection.rollback();
            throw ex;
        }
    }
}
